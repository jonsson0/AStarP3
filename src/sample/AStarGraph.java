package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class AStarGraph {

    // hashmap of vertices
    HashMap<String, sample.Vertex> vertices;

    // constructor
    public AStarGraph() {

        setVertices(new HashMap<String, sample.Vertex>());
    }

    public void addVertex(sample.Vertex v) {

        getVertices().put(v.getId(), v);
    }


    public void newConnection(sample.Vertex v1, sample.Vertex v2, Double dist) {

        v1.addOutEdge(v2, dist);
        v2.addOutEdge(v1, dist);
    }


    public boolean A_Star(sample.Vertex start, sample.Vertex destination, String choice) {

        if (start == null || destination == null) {

            return false;
        }
        // Openlist are the uncompleted vertices
        PriorityQueue<sample.Vertex> Openlist = new PriorityQueue<sample.Vertex>();

        // Closedlist is the completed vertices
        ArrayList<sample.Vertex> Closedlist = new ArrayList<sample.Vertex>();
        sample.Vertex Current;                                                 // the active vertex
        ArrayList<sample.Vertex> CurrentNeighbors;
        sample.Vertex Neighbor;

        // for each values of the hashmap
        getVertices().values().forEach(new Consumer<sample.Vertex>() {
            @Override
            public void accept(sample.Vertex vertex) {

                if (choice.equals("Manhattan")) {

                    vertex.setH(Manhattan(vertex, destination));
                }
                if (choice.equals("Euclidean")) {

                    vertex.setH(Euclidean(vertex, destination));
                }
            }
        });

        // Distance to self = 0.0
        start.setg(0.0);

        start.calculateF();

        Openlist.offer(start);

        //System.out.println("The Algorithm is starting!");

        // run as long as its not empty
        while (!Openlist.isEmpty()) {

            // removes current from openlist
            Current = Openlist.remove();

            // adds the current to the closed list
            Closedlist.add(Current);
            if (Current == destination) {

                return true;
            }

            for (int i = 0; i < Current.getNeighbours().size(); i++) {

                double weight = Current.getNeighbourDistance().get(i);

                double tempGofV = Current.getG() + weight;


                if (tempGofV <= Current.getNeighbours().get(i).getG()) {

                    Current.getNeighbours().get(i).setPrev(Current);

                    Current.getNeighbours().get(i).setg(tempGofV);

                    Current.getNeighbours().get(i).calculateF();

                    if ((!Closedlist.contains(Current.getNeighbours().get(i))) && (!Openlist.contains(Current.getNeighbours().get(i)))) {

                        Openlist.offer(Current.getNeighbours().get(i));

                        //if openlist has neighbour vertex(i), remove then add it again
                    } else if (Openlist.contains(Current.getNeighbours().get(i))) {

                        Openlist.remove(Current.getNeighbours().get(i));
                        Openlist.offer(Current.getNeighbours().get(i));
                    }
                }
            }
        }
        return false;
    }

    //  horizontal & vertical distance calculated
    public Double Manhattan(sample.Vertex from, sample.Vertex goal) {
        double distance = Math.abs(goal.getX() - from.getX()) + Math.abs(goal.getY() - from.getY());
        return distance;
    }

    // the direct distance
    public Double Euclidean(sample.Vertex from, sample.Vertex to) {
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double distance = Math.sqrt((x * x) + (y * y));
        return distance;
    }

    public HashMap<String, sample.Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(HashMap<String, sample.Vertex> vertices) {
        this.vertices = vertices;
    }
}

class Vertex implements Comparable<sample.Vertex> {

    private ArrayList<sample.Vertex> Neighbours = new ArrayList<sample.Vertex>();

    private ArrayList<Double> NeighbourDistance = new ArrayList<Double>();

    private String id;
    private Double f;
    private Double g;
    private Double h;
    private Integer x;
    private Integer y;
    private sample.Vertex prev = null;

    // Constructor
    public Vertex(String id, int x_cor, int y_cor) {
        this.id = id;
        this.x = x_cor;
        this.y = y_cor;

        f = Double.POSITIVE_INFINITY;
        g = Double.POSITIVE_INFINITY;
        h = 0.0;
    }

    public void addOutEdge(sample.Vertex toV, Double dist) {
        Neighbours.add(toV);
        NeighbourDistance.add(dist);
    }

    // getters and setters
    public ArrayList<sample.Vertex> getNeighbours() {
        return Neighbours;
    }

    public ArrayList<Double> getNeighbourDistance() {
        return NeighbourDistance;
    }

    public String getId() {
        return id;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Double getF() {
        return f;
    }

    public void calculateF() {
        f = g + h;
    }

    public Double getG() {
        return g;
    }

    public void setg(Double newG) {
        g = newG;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double estimate) {
        h = estimate;
    }

    public sample.Vertex getPrev() {
        return prev;
    }

    public void setPrev(sample.Vertex v) {
        prev = v;
    }

    public void printVertex() {
        System.out.println(id + " g: " + g + ", h: " + h + ", f: " + f);
    }

    //
    @Override
    public int compareTo(sample.Vertex vertex) {
        if (this.getF() > vertex.getF())
            return 1;
        if (this.getF() < vertex.getF())
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return id;
    }
}