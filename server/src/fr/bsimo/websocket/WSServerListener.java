package fr.bsimo.websocket;

import fr.bsimo.server.ServerListener;
import fr.bsimo.server.event.ClientEvent;

/**
 * Created by BSimo on 30/04/16.
 */
public interface WSServerListener extends ServerListener {
    default void onClientHandshakeDone(ClientEvent e) {

    }
}
