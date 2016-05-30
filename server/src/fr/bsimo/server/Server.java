package fr.bsimo.server;

import fr.bsimo.server.event.ServerEvent;
import fr.bsimo.utils.Seq;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Ben on 31/03/16.
 */
public class Server implements Runnable {
    private String host;
    private int port;

    private ServerSocket socket;
    private Thread thread;
    private boolean is_running = false;
    private Hashtable<Integer, ServerClient> clients; // Hashtable of ServerClient object
    private Seq clients_seq;

    private ArrayList<ServerListener> listeners;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;

        this.clients = new Hashtable<>();
        this.clients_seq = new Seq();
        this.listeners = new ArrayList<>();
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();

        this.sendServerStartEvent();
    }

    public void run() {
        this.initSocket();
        this.is_running = true;

        while (this.is_running) {
            try {
                Socket client_sock = this.socket.accept();
                ServerClient client = this.createServerClient(this.clients_seq.getNext(), client_sock);
                this.clients.put(client.getId(), client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
                this.stop();
                break;
            }
        }

        Enumeration<ServerClient> e = this.clients.elements();
        while(e.hasMoreElements()) {
            ServerClient client = e.nextElement();
            client.stop();
        }

        this.clients = new Hashtable<>();

        this.sendServerStopEvent();
    }

    private void initSocket() {
        try {
            this.socket = new ServerSocket();
            this.socket.setReuseAddress(true);
            this.socket.bind(new InetSocketAddress(this.host, this.port));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected ServerClient createServerClient(int id, Socket sock) {
        return new ServerClient(this, id, sock);
    }

    public void stop() {
        this.is_running = false;
    }

    public void send(int id, byte[] b) {
        ServerClient client = this.clients.get(id);
        client.send(b);
    }

    public void send(int id, String msg) {
        this.send(id, msg.getBytes());
    }

    public void sendAll(byte[] b) {
        Enumeration<ServerClient> e = this.clients.elements();
        while(e.hasMoreElements()) {
            ServerClient client = e.nextElement();
            client.send(b);
        }
    }

    public void sendAll(String msg) {
        this.sendAll(msg.getBytes());
    }

    protected void removeClient(int id) {
        this.clients.remove(id);
    }

    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    public ArrayList<ServerListener> getListeners() {
        return this.listeners;
    }

    protected void sendServerStartEvent() {
        ServerEvent event = new ServerEvent(this);
        for (ServerListener listener : this.listeners)
            listener.onServerStart(event);
    }

    private void sendServerStopEvent() {
        ServerEvent event = new ServerEvent(this);
        for (ServerListener listener : this.listeners) {
            listener.onServerStop(event);
        }
    }
}
