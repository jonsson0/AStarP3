package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Stack;

public class Controller {

    AStarGraph AStarGraph = CreateGraph();

    public AStarGraph CreateGraph() {

        AStarGraph MyMaze = new AStarGraph();

        Vertex A = new Vertex("A", 0, 4);
        Vertex B = new Vertex("B", 1, 7);
        Vertex C = new Vertex("C", 4, 0);
        Vertex D = new Vertex("D", 3, 7);
        Vertex G = new Vertex("G", 7, 2);
        Vertex I = new Vertex("I", 9, 2);
        Vertex E = new Vertex("E", 3, 3);
        Vertex F = new Vertex("F", 6, 6);
        Vertex H = new Vertex("H", 8, 7);
        Vertex J = new Vertex("J", 11, 5);

        MyMaze.addVertex(A);
        MyMaze.addVertex(B);
        MyMaze.addVertex(C);
        MyMaze.addVertex(D);
        MyMaze.addVertex(G);
        MyMaze.addVertex(I);
        MyMaze.addVertex(E);
        MyMaze.addVertex(F);
        MyMaze.addVertex(H);
        MyMaze.addVertex(J);

        MyMaze.newConnection(A, B, 3.41);
        MyMaze.newConnection(A, C, 6.82);
        MyMaze.newConnection(B, D, 2.0);
        MyMaze.newConnection(C, G, 4.41);
        MyMaze.newConnection(C, I, 4.82);
        MyMaze.newConnection(D, E, 4.0);
        MyMaze.newConnection(E, F, 6.23);
        MyMaze.newConnection(F, G, 4.41);
        MyMaze.newConnection(F, H, 3.82);
        MyMaze.newConnection(G, H, 5.41);
        MyMaze.newConnection(G, I, 2.82);
        MyMaze.newConnection(H, J, 4.41);
        MyMaze.newConnection(I, J, 3.82);

        return MyMaze;
    }

    // Smart print graph
    public void printShortestPath(Vertex destination) {
        Vertex pVertex = destination;
        printArea.appendText("You are going to: " + destination.getId() + " | Distance: " + destination.getF() + "\n");
        Stack<Vertex> Path = new Stack<>();
        int limit = 0;

        while (pVertex != null) {
            Path.push(pVertex);
            pVertex = pVertex.getPrev();
        }
        if (!Path.isEmpty()) {
            limit = Path.size();
        }
        for (int i = 0; i < limit - 1; i++) {
            printArea.appendText(Path.pop().getId() + " to ");
        }

        if (limit > 0) {

            printArea.appendText(Path.pop().getId() + "\n");
        }
    }

    public void initialize(String selA, String selB, String selMethod) {
        System.out.println();

        // set comboboxes to the values of the vertices hashmap
        comboStart.setItems(FXCollections.observableList(new ArrayList(AStarGraph.getVertices().values())));
        comboDestination.setItems(FXCollections.observableList(new ArrayList(AStarGraph.getVertices().values())));

        // select the previous selections
        comboStart.getSelectionModel().select(AStarGraph.getVertices().get(selA));
        comboDestination.getSelectionModel().select(AStarGraph.getVertices().get(selB));

        // adds "Manhattan" og "Euclidean" to the list and used in the choose method button (comboEstimation)
        ObservableList<String> estimationMethod = FXCollections.observableArrayList("Manhattan", "Euclidean");
        comboEstimation.setItems(estimationMethod);
        comboEstimation.getSelectionModel().select(selMethod);
    }

    // just an overloading for the start, it selects the start to A, end to A and estimation to Manhattan
    public void initialize() {
        initialize("A", "A", "Manhattan");
    }

    @FXML
    ComboBox<Vertex> comboStart;

    @FXML
    ComboBox<Vertex> comboDestination;

    @FXML
    ComboBox<String> comboEstimation;

    @FXML
    TextArea printArea;

    @FXML
        // choose start vertex button
    void startVertexChoice(ActionEvent event) {
        //  System.out.println("You are starting from: " + comboStart.getValue());
    }

    @FXML
        // choose end vertex button
    void destinationVertexChoice(ActionEvent event) {
        // System.out.println("You are going to: " + comboDestination.getValue());
    }

    @FXML
        // Method button
    void estimationChoice(ActionEvent event) {
      //  System.out.println("You are using the " + comboEstimation.getValue() + " method");
    }

    @FXML
        // Go button
    void startPathfinder(ActionEvent event) {
        /*
         Calls the distance calculation and the printing.
         It also resets the comboboxes
         */
        Vertex start = comboStart.getValue();
        AStarGraph.A_Star(start, comboDestination.getValue(), comboEstimation.getValue());
        printShortestPath(comboDestination.getValue());

        AStarGraph = CreateGraph();
        initialize(comboStart.getValue().getId(), comboDestination.getValue().getId(), comboEstimation.getValue());
    }

    @FXML
        // Exit Button
    void exitButton(ActionEvent event) {
       // System.out.println("EXITING THE PROGRAM NOW");
        Platform.exit();
    }
}