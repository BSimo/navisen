package fr.bsimo.navisenserver;

import fr.bsimo.dijsktra.DijkstraAlgorithm;
import fr.bsimo.dijsktra.Edge;
import fr.bsimo.dijsktra.Graph;
import fr.bsimo.dijsktra.Vertex;
import fr.bsimo.websocket.WSServer;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ben on 25/05/16.
 */
public class Navisen {

    private WSServer server;
    private NavisenListener navisenListener;

    public Navisen() {
        this.server = new WSServer("0.0.0.0", 8888);
        this.navisenListener = new NavisenListener(this);
        this.server.addWSListener(this.navisenListener);
        this.server.start();
    }
}
