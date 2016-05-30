package fr.bsimo.dijsktra;

import java.util.List;

/**
 * Created by Ben on 25/05/16.
 */
public class Graph {

    private List<Vertex> vertexes;
    private List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return this.vertexes;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }
}
