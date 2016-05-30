package fr.bsimo.dijsktra;

/**
 * Created by Ben on 25/05/16.
 */
public class Edge {

    private String id;
    private Vertex source;
    private Vertex destination;
    private double weight;

    public Edge(String id, Vertex source, Vertex destination, double weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getId() {
        return this.id;
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getDestination() {
        return this.destination;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "[Edge] {" + this.id + "} : " + this.source + " -> " + this.destination + " (" + this.weight + ")";
    }
}
