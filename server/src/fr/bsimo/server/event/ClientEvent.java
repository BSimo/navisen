package fr.bsimo.server.event;

import fr.bsimo.server.Server;
import fr.bsimo.server.ServerClient;

/**
 * Created by BSimo on 30/04/16.
 */
public class ClientEvent extends Event {
    private ServerClient client;

    public ClientEvent(ServerClient client) {
        this.client = client;
    }

    public final ServerClient getClient() {
        return this.client;
    }
    public final Server getServer() { return this.client.getServer(); }
}
