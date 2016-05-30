package fr.bsimo.server;

import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;
import fr.bsimo.server.event.ServerEvent;

/**
 * Created by BSimo on 30/04/16.
 */
public interface ServerListener {
    default void onServerStart(ServerEvent e) {

    }
    default void onServerStop(ServerEvent e) {

    }

    default void onClientConnect(ClientEvent e) {

    }
    default void onClientDisconnect(ClientEvent e) {

    }
    default void onClientData(ClientDataEvent e) {

    }
}
