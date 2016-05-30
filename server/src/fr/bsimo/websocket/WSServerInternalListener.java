package fr.bsimo.websocket;

import fr.bsimo.server.ServerListener;
import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;
import fr.bsimo.server.event.ServerEvent;

/**
 * Created by BSimo on 01/05/16.
 */
public class WSServerInternalListener implements ServerListener {

    @Override
    public void onServerStart(ServerEvent e) {
        WSServer server = (WSServer) e.getServer();
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onServerStart(e);
        }
    }

    @Override
    public void onServerStop(ServerEvent e) {
        WSServer server = (WSServer) e.getServer();
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onServerStop(e);
        }
    }

    @Override
    public void onClientConnect(ClientEvent e) {
        WSServer server = (WSServer) e.getServer();
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onClientConnect(e);
        }
    }

    @Override
    public void onClientDisconnect(ClientEvent e) {
        WSServer server = (WSServer) e.getServer();
        for(WSServerListener listener : server.getWSListeners()) {
            listener.onClientDisconnect(e);
        }
    }

    @Override
    public void onClientData(ClientDataEvent e) {
        WSServerClient client = (WSServerClient) e.getClient();
        client.onData(e.getData());
        //System.out.println("[Client Get ] " + e.getClient().getId() + ": " + e.getDataString());
    }
}
