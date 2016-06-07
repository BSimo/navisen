package fr.bsimo.websocket;

import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import fr.bsimo.server.Server;
import fr.bsimo.server.ServerClient;
import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Created by Ben on 21/04/16.
 */
public class WSServerClient extends ServerClient {

    protected String forwarded_ip = null;
    protected Thread heartbeat_thread = null;
    protected byte[] heartbeat_waiting = null;
    protected ByteArrayOutputStream req_buf = null;
    protected long req_buf_remainlen = 0;
    protected ByteArrayOutputStream payload_buf = null;

    public boolean handshake_done = false;

    public WSServerClient(Server server, int id, Socket socket) {
        super(server, id, socket);
        this.req_buf = new ByteArrayOutputStream();
        this.payload_buf = new ByteArrayOutputStream();

        /*this.heartbeat_thread = new Thread(() -> {
            while(this.isRunning()) {
                try {
                    this.heartbeat();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    public void disconnect() {
        if(!this.isRunning()) return;

        this.send(this.buildCloseReq("".getBytes()));
        this.stop();
    }

    protected boolean handshake(byte[] headerBin) {
        String header = new String(headerBin);
        String[] headerElmt = header.split("\r\n");
        String[] req = headerElmt[0].split(" ");

        if(req.length != 3 || !req[0].equalsIgnoreCase("GET"))
            return false;


        String[] http = req[2].split("/");

        if(!http[0].equalsIgnoreCase("http"))
            return false;

        String[] httpver = http[1].split("\\.");
        int httpver_major = Integer.parseInt(httpver[0]);
        int httpver_minor = Integer.parseInt(httpver[1]);

        if(httpver_major < 1 || (httpver_major == 1 && httpver_minor < 1))
            return false;

        boolean hdr_upgrade = false;
        boolean hdr_connection = false;
        String hdr_ws_key = null;
        boolean hdr_ws_version = false;

        for(int i = 0; i < headerElmt.length; i++) {
            String[] entry = headerElmt[i].split(":");

            if(entry.length != 2)
                continue;

            String entryKey = entry[0].trim();
            String entryVal = entry[1].trim();

            if(entryKey.equalsIgnoreCase("Upgrade")) {
                String[] param = entryVal.toLowerCase().split(", ");
                if(Arrays.asList(param).contains("websocket"))
                    hdr_upgrade = true;
            } else if(entryKey.equalsIgnoreCase("Connection")) {
                String[] param = entryVal.toLowerCase().split(", ");
                if(Arrays.asList(param).contains("upgrade"))
                    hdr_connection = true;
            } else if(entryKey.equalsIgnoreCase("Sec-WebSocket-Key")) {
                hdr_ws_key = entryVal;
            } else if(entryKey.equalsIgnoreCase("Sec-WebSocket-Version")) {
                if(Integer.parseInt(entryVal) == 13)
                    hdr_ws_version = true;
            } else if(entryKey.equalsIgnoreCase("X-Forwarded-For")) {
                String[] param = entryVal.toLowerCase().split(", ");
                if(InetAddressValidator.getInstance().isValid(param[0]))
                    this.forwarded_ip = param[0];
            }
        }

        if(!hdr_upgrade || !hdr_connection || hdr_ws_key == null || !hdr_ws_version)
            return false;


        String ans = "HTTP/1.1 101 Switching Protocols\r\n";
        ans += "Upgrade: websocket\r\n";
        ans += "Connection: Upgrade\r\n";
        ans += "Sec-WebSocket-Accept: " + Base64.encodeBase64String(DigestUtils.sha1(hdr_ws_key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")) + "\r\n";
        ans += "\r\n";

        this.send(ans);
        return true;
    }

    public void onData(byte[] data) {
        if(!this.handshake_done) {
            if (!this.handshake(data)) {
                System.out.println("WebSocket: Handshake failed !");
                this.stop();
                return;
            }
            this.handshake_done = true;
            //this.heartbeat_thread.start();
            this.sendClientHandshakeDoneEvent();
            return;
        }

        if(this.req_buf.size() > 0) {
            this.req_buf.write(data, 0, data.length);

            this.req_buf_remainlen -= data.length;
            if(this.req_buf_remainlen > 0)
                return;

            data = this.req_buf.toByteArray();
            this.req_buf.reset();
            this.req_buf_remainlen = 0;
        }

        while(true) {
            if(data.length < 2)
                return;

            int idx = 0;
            int c = data[idx] & 0xFF;
            int opCode = c & 0x0F;
            int fin = c >> 7;
            idx++;

            int d = data[idx] & 0xFF;
            long payload_len = d & 0x7F;
            int mask = (d & 0x80) >> 7;
            idx++;

            if(mask == 0) {
                this.disconnect();
                return;
            }

            if(payload_len == 126) {
                byte[] n = new byte[Short.SIZE];
                for(int i = 0; i < Short.SIZE; i++)
                    n[i] = data[idx + i];
                idx += Short.SIZE;
                payload_len = this.binToShort(n);
            } else if(payload_len == 127) {
                byte[] n = new byte[Long.SIZE];
                for(int i = 0; i < Long.SIZE; i++)
                    n[i] = data[idx + i];
                idx += Long.SIZE;
                payload_len = this.binToLong(n);
            }

            byte[] mask_key = new byte[4];
            for(int i = 0; i < 4; i++) {
                mask_key[i] = data[idx + i];
            }
            idx += 4;

            int max_idx = (int) (idx + payload_len);
            if(data.length < max_idx) {
                max_idx = data.length;
            }

            int payload_maxlen = data.length - idx;
            if(payload_maxlen < payload_len) {
                this.req_buf.write(data, 0, data.length);
                this.req_buf_remainlen = payload_len - payload_maxlen;
                return;
            }

            byte[] payload = new byte[(int) payload_len];
            for(int i = 0; i < payload.length; i++) {
                payload[i] = (byte) (data[i + idx] ^ mask_key[i % 4]);
            }

            switch(opCode) {
                case 0: /* Continuation */
                    this.payload_buf.write(payload, 0, payload.length);

                    if(fin > 0)
                        payload = this.payload_buf.toByteArray();
                    else
                        payload = null;

                    break;

                case 1: /* Text */
                case 2: /* Binary */
                    this.payload_buf.reset();
                    this.payload_buf.write(payload, 0, payload.length);

                    if(fin == 0)
                        payload = null;

                    break;

                case 8: /* Close */
                    this.disconnect();
                    return;

                case 9: /* Ping */
                    this.send(this.buildPingReq(payload));
                    payload = null;

                    break;

                case 10: /* Pong */
                    if(this.heartbeat_waiting == payload)
                        this.heartbeat_waiting = null;

                    payload = null;

                    break;
            }

            if(payload != null && payload.length > 0) {
                this.payload_buf.reset();
                this.sendClientDataEvent(payload);
            }

            break;
        }
    }

    public void heartbeat() {
        if(this.heartbeat_waiting != null) {
            this.disconnect();
            return;
        }

        this.heartbeat_waiting = DigestUtils.sha1("salt.f4cvgr41" + Integer.toBinaryString((int) (154 + Math.random())));
        this.send(this.buildPingReq(this.heartbeat_waiting));
    }

    protected byte[] buildReq(int type, byte[] msg) {
        ByteArrayOutputStream req = new ByteArrayOutputStream();
        req.write(type | 0x80);
        int len = msg.length;

        if (len <= 125) {
            req.write(len);
        } else if (len <= Short.MAX_VALUE) {
            req.write(126);
            req.write(this.shortToBin((short) len), 0, Short.SIZE/8);
        } else {
            req.write(127);
            req.write(this.longToBin(len), 0, Long.SIZE/8);
        }

        req.write(msg, 0, msg.length);
        return req.toByteArray();
    }

    protected byte[] buildContReq(byte[] msg) {
        return this.buildReq(0x0, msg);
    }

    protected byte[] buildTextReq(byte[] msg) {
        return this.buildReq(0x1, msg);
    }

    protected byte[] buildBinReq(byte[] msg) {
        return this.buildReq(0x2, msg);
    }

    protected byte[] buildCloseReq(byte[] msg) {
        return this.buildReq(0x8, msg);
    }

    protected byte[] buildPingReq(byte[] msg) {
        return this.buildReq(0x9, msg);
    }

    protected byte[] buildPongReq(byte[] msg) {
        return this.buildReq(0xA, msg);
    }

    public void sendTxt(String msg) {
        this.send(this.buildTextReq(msg.getBytes()));
    }

    protected short binToShort(byte[] b) {
        return ByteBuffer.wrap(b).getShort();
    }

    protected long binToLong(byte[] b) {
        return ByteBuffer.wrap(b).getLong();
    }

    protected byte[] shortToBin(short s) {
        ByteBuffer buff = ByteBuffer.allocate(Short.SIZE);
        buff.order(ByteOrder.BIG_ENDIAN);
        buff.putShort(s);
        return buff.array();
    }

    protected byte[] longToBin(long l) {
        ByteBuffer buff = ByteBuffer.allocate(Long.SIZE);
        buff.order(ByteOrder.BIG_ENDIAN);
        buff.putLong(l);
        return buff.array();
    }

    private void sendClientHandshakeDoneEvent() {
        WSServer server = (WSServer) this.server;
        ClientEvent handshakeDoneEvent = new ClientEvent(this);
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onClientHandshakeDone(handshakeDoneEvent);
        }
    }

    private void sendClientDataEvent(byte[] data) {
        WSServer server = (WSServer) this.server;
        ClientDataEvent dataEvent = new ClientDataEvent(this, data);
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onClientData(dataEvent);
        }
    }
}
