package fr.bsimo.dijsktra;

/**
 * Created by Ben on 25/05/16.
 */
public class Vertex {

    private String id;

    public Vertex(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "[Vertex] {" + this.id + "}";
    }
}