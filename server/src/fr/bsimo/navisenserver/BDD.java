package fr.bsimo.navisenserver;

import fr.bsimo.dijsktra.Edge;
import fr.bsimo.dijsktra.Graph;
import fr.bsimo.dijsktra.Vertex;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Ben on 25/05/16.
 */
public class BDD {

    private static Connection conn = null;
    private static boolean connected = false;

    public static boolean init(String db_name, String db_user, String db_pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.out.println("[ERROR] Can't load the JDBC Driver !");
            return false;
        }

        try {
            BDD.conn = DriverManager.getConnection("jdbc:mysql://localhost/" + db_name + "?user=" + db_user + "&password=" + db_pass);
            BDD.connected = true;
        } catch (SQLException e) {
            System.out.println("[ERROR] Can't etablish connection to the database !");
            BDD.connected = false;
            return false;
        }

        return true;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static Connection getConn() {
        if(!connected) return null;
        return conn;
    }

    public static Graph createGraphFromBDD() {
        Graph graph;
        ArrayList<Vertex> nodes = new ArrayList<Vertex>();
        ArrayList<Edge> edges = new ArrayList<Edge>();

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = BDD.getConn().createStatement();
            rs = stmt.executeQuery("SELECT point_id FROM point");

            while (rs.next()) {
                Vertex vertex = new Vertex(rs.getString("point_id"));
                nodes.add(vertex);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) { }
                rs = null;
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) { }
                stmt = null;
            }
        }

        try {
            stmt = BDD.getConn().createStatement();
            rs = stmt.executeQuery("SELECT P.point_id as P_id, P.floor_id as P_floor, " +
                    "PF.floor_high as P_high, P.point_pos_x as P_x, P.point_pos_y as P_y, " +
                    "N.point_id as N_id, N.floor_id as N_floor, " +
                    "NF.floor_high as N_high, N.point_pos_x as N_x, N.point_pos_y as N_y " +
                    "FROM point_neighbor E " +
                    "LEFT JOIN point P ON P.point_id = E.point_id " +
                    "LEFT JOIN floor PF ON PF.floor_id = P.floor_id " +
                    "LEFT JOIN point N ON N.point_id = E.neighbor_id " +
                    "LEFT JOIN floor NF ON NF.floor_id = N.floor_id");

            while (rs.next()) {
                double distance;
                if (rs.getInt("P_floor") != rs.getInt("N_floor")) {
                    distance = rs.getInt("N_floor") - rs.getInt("P_floor");
                    distance = Math.abs(distance);
                } else {
                    int x = rs.getInt("N_x") - rs.getInt("P_x");
                    int y = rs.getInt("N_y") - rs.getInt("P_y");
                    distance = Math.sqrt(x * x + y * y);
                }

                Vertex P_node = getVertexById(nodes, rs.getString("P_id"));
                Vertex N_node = getVertexById(nodes, rs.getString("N_id"));
                Edge edge = new Edge("Edge_" + rs.getRow(), P_node, N_node, distance);
                edges.add(edge);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) { }
                rs = null;
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) { }
                stmt = null;
            }
        }

        return new Graph(nodes, edges);
    }

    public static Vertex getVertexById(List<Vertex> vertexes, String id) {
        for(Vertex vertex : vertexes)
            if(vertex.getId().equals(id))
                return vertex;
        return null;
    }
}
