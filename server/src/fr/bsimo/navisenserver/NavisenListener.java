package fr.bsimo.navisenserver;

import fr.bsimo.dijsktra.DijkstraAlgorithm;
import fr.bsimo.dijsktra.Graph;
import fr.bsimo.dijsktra.Vertex;
import fr.bsimo.server.event.ClientDataEvent;
import fr.bsimo.server.event.ClientEvent;
import fr.bsimo.websocket.WSServerClient;
import fr.bsimo.websocket.WSServerListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Ben on 25/05/16.
 */
public class NavisenListener implements WSServerListener {

    private Navisen server;

    public NavisenListener(Navisen server) {
        this.server = server;
    }

    @Override
    public void onClientConnect(ClientEvent e) {
        System.out.println("Client Connected: " + e.getClient().getClientInetAddress().getHostName());
    }

    @Override
    public void onClientData(ClientDataEvent e) {
        JSONObject data = e.getDataJSON();
        System.out.println(data.toString());

        if(!data.has("from") || !data.has("to")) return;

        Graph graph = BDD.createGraphFromBDD();
        Vertex from = BDD.getVertexById(graph.getVertexes(), data.getInt("from") + "");
        Vertex to = BDD.getVertexById(graph.getVertexes(), data.getInt("to") + "");

        if(from == null || to == null) return;

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(from);
        LinkedList<Vertex> path = dijkstra.getPath(to);
        double distance = dijkstra.getShortestDistance(to);

        JSONObject ret = new JSONObject();
        if(distance >= Double.MAX_VALUE) {
            ret.put("success", false);
        } else {
            ret.put("success", true);

            ArrayList<Integer> pathId = new ArrayList<>();
            for (Vertex vertex : path)
                pathId.add(Integer.parseInt(vertex.getId()));

            ret.put("path", new JSONArray(pathId));
            ret.put("distance", dijkstra.getShortestDistance(to));
        }

        System.out.println(ret.toString());
        WSServerClient client = (WSServerClient) e.getClient();
        client.sendTxt(ret.toString());
    }
}
