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
        sample.Vertex CurrentV;                                                 // the active vertex
        ArrayList<sample.Vertex> CurrentNeighbors;
        sample.Vertex Neighbor;

        // for each values of the hashmap
        getVertices().values().forEach(new Consumer<sample.Vertex>() {
            @Override
            public void accept(sample.Vertex vertex) {

                if (choice.equals("Manhattan")) {

                    vertex.setH(manhattanHeuristic(vertex, destination));
                }
                if (choice.equals("Euclidean")) {

                    vertex.setH(euclideanHeuristic(vertex, destination));
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

            CurrentV = Openlist.remove();

            // adds the current to the closed list
            Closedlist.add(CurrentV);
            if (CurrentV == destination) {

                return true;
            }

            for (int i = 0; i < CurrentV.getNeighbours().size(); i++) {

                double weight = CurrentV.getNeighbourDistance().get(i);

                double tempGofV = CurrentV.getG() + weight;


                if (tempGofV <= CurrentV.getNeighbours().get(i).getG()) {

                    CurrentV.getNeighbours().get(i).setPrev(CurrentV);

                    CurrentV.getNeighbours().get(i).setg(tempGofV);

                    CurrentV.getNeighbours().get(i).calculateF();

                    if ((!Closedlist.contains(CurrentV.getNeighbours().get(i))) && (!Openlist.contains(CurrentV.getNeighbours().get(i)))) {

                        Openlist.offer(CurrentV.getNeighbours().get(i));

                        //if openlist has neighbour vertex(i), remove then add it again
                    } else if (Openlist.contains(CurrentV.getNeighbours().get(i))) {

                        Openlist.remove(CurrentV.getNeighbours().get(i));
                        Openlist.offer(CurrentV.getNeighbours().get(i));
                    }
                }
            }
        }
        return false;
    }

    //  horizontal & vertical distance calculated
    public Double manhattanHeuristic(sample.Vertex from, sample.Vertex to) {
        double dist = Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY());
        return dist;
    }

    // the direct distance
    public Double euclideanHeuristic(sample.Vertex from, sample.Vertex to) {
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double dist = Math.sqrt((x * x) + (y * y));
        return dist;
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