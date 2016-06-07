package fr.bsimo.server;

import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;
import fr.bsimo.server.event.ServerEvent;

/**
 * Created by BSimo on 30/04/16.
 */
public interface ServerListener {
    void onServerStart(ServerEvent e);
    void onServerStop(ServerEvent e);

    void onClientConnect(ClientEvent e);
    void onClientDisconnect(ClientEvent e);
    void onClientData(ClientDataEvent e);
}
