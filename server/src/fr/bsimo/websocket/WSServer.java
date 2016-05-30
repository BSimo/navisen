package fr.bsimo.websocket;

import fr.bsimo.server.Server;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Ben on 21/04/16.
 */
public class WSServer extends Server {

    private ArrayList<WSServerListener> ws_listeners;
    public WSServer(String host, int port) {
        super(host, port);
        this.addListener(new WSServerInternalListener());

        this.ws_listeners = new ArrayList<>();
    }

    @Override
    protected WSServerClient createServerClient(int id, Socket sock) {
        return new WSServerClient(this, id, sock);
    }

    public void addWSListener(WSServerListener listener) {
        this.ws_listeners.add(listener);
    }

    public ArrayList<WSServerListener> getWSListeners() {
        return this.ws_listeners;
    }
}
