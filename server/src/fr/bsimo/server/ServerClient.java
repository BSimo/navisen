package fr.bsimo.server;

import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Ben on 31/03/16.
 */
public class ServerClient implements Runnable {

    protected Server server;
    private int id;
    private Socket socket;
    private Thread thread;

    private InputStream input;
    private OutputStream output;

    private boolean is_running = false;

    public ServerClient(Server server, int id, Socket socket) {
        this.server = server;
        this.id = id;
        this.socket = socket;

        // Get IO
        try {
            this.input = this.socket.getInputStream();
            this.output = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return this.id;
    }

    public Server getServer() { return this.server; }

    public InetAddress getClientInetAddress() {
        return this.socket.getInetAddress();
    }

    public int getClientPort() {
        return this.socket.getPort();
    }

    public boolean isRunning() {
        return this.is_running;
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void run() {
        this.is_running = true;

        this.sendClientConnectEvent();

        while(this.is_running) {
            byte[] data = new byte[16384];
            try {
                int dataLength = this.input.read(data);
                if(dataLength == -1) {
                    this.is_running = false;
                    break;
                }

                this.sendClientDataEvent(data);
            } catch (IOException e) {
                this.is_running = false;
                break;
            }
        }

        // Close Everything
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sendClientDisconnectEvent();
        this.server.removeClient(this.id);
    }

    public void stop() {
        this.thread.interrupt();
    }

    public void send(byte[] b) {
        if(!this.is_running) return;

        try {
            this.output.write(b);
            this.output.flush();
        } catch(IOException e) {
            if(e.getMessage().equalsIgnoreCase("Socket closed")
                    || e.getMessage().equalsIgnoreCase("Software caused connection abort: socket write error")) {
                this.stop();
            }
            else
                e.printStackTrace();
        }
    }

    public void send(String str) {
        this.send(str.getBytes());
    }

    private void sendClientConnectEvent() {
        ClientEvent connectEvent = new ClientEvent(this);
        for (ServerListener listener : this.server.getListeners())
            listener.onClientConnect(connectEvent);
    }

    private void sendClientDisconnectEvent() {
        ClientEvent disconnectEvent = new ClientEvent(this);
        for (ServerListener listener : this.server.getListeners())
            listener.onClientDisconnect(disconnectEvent);
    }

    private void sendClientDataEvent(byte[] data) {
        ClientDataEvent dataEvent = new ClientDataEvent(this, data);
        for (ServerListener listener : this.server.getListeners())
            listener.onClientData(dataEvent);
    }
}
