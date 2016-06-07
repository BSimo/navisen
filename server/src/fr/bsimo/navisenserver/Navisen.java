package fr.bsimo.navisenserver;

import fr.bsimo.websocket.WSServer;

/**
 * Created by Ben on 25/05/16.
 */
public class Navisen {

    private WSServer server;
    private NavisenListener navisenListener;

    public Navisen(int port) {
        this.server = new WSServer("0.0.0.0", port);
        this.navisenListener = new NavisenListener(this);
        this.server.addWSListener(this.navisenListener);
        this.server.start();
    }
}
