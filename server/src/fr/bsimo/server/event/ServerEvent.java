package fr.bsimo.server.event;

import fr.bsimo.server.Server;

/**
 * Created by BSimo on 30/04/16.
 */
public class ServerEvent extends Event {
    private Server server;

    public ServerEvent(Server server) {
        this.server = server;
    }

    public final Server getServer() {
        return this.server;
    }
}
