import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Ant class represents an ant in an ant colony optimization algorithm.
 * An ant traverses a graph, choosing paths based on probabilities calculated from pheromone levels and heuristic information.
 */
public class Ant {
    private Node currentNode;
    private ArrayList<Node> unvisitedNodes;
    private ArrayList<Node> visitedNodes;
    private double totalDistance;

    /**
     * Constructs an Ant object with a list of nodes and a starting node.
     * @param nodeList      The list of nodes in the graph.
     * @param startingNode  The starting node for the ant.
     */
    Ant(ArrayList<Node> nodeList, Node startingNode) {
        this.currentNode = startingNode;
        this.totalDistance = 0;
        this.unvisitedNodes = (ArrayList<Node>)nodeList.clone();
        this.unvisitedNodes.remove(startingNode);
        this.visitedNodes = new ArrayList<>();
        this.visitedNodes.add(startingNode);
    }

    /**
     * Gets the total distance traveled by the ant during traversal.
     * @return The total distance traveled.
     */
    public double getTotalDistance() {
        return this.totalDistance;
    }

    /**
     * Gets the list of nodes visited by the ant during traversal.
     * @return The list of visited nodes.
     */
    public ArrayList<Node> getVisitedNodes() {
        return visitedNodes;
    }

    /**
     * Traverses the graph, selecting edges based on probabilities calculated from pheromone levels and heuristic information.
     */
    public void traverse() {
        Node startingNode = this.currentNode;
        while (!unvisitedNodes.isEmpty()) {
            Edge edge = this.chooseNextEdge();
            Node nextNode;
            if (edge.getNode1() == this.currentNode) {
                nextNode = edge.getNode2();
            }
            else {
                nextNode = edge.getNode1();
            }
            this.totalDistance += edge.getDistance();
            this.visitedNodes.add(nextNode);
            this.unvisitedNodes.remove(nextNode);
            this.currentNode = nextNode;
        }

        Edge edgeToStart = this.currentNode.getEdgeList().get(0);;

        for (Edge edge: this.currentNode.getEdgeList()) {
            if (edge.getNode2().getNodeNumber() == startingNode.getNodeNumber()) {
                edgeToStart = edge;
            }
        }
        this.totalDistance += edgeToStart.getDistance();
        this.currentNode = startingNode;
    }

    /**
     * Chooses the next edge for traversal based on probabilities calculated from pheromone levels and heuristic information.
     * @return is the chosen edge.
     */
    public Edge chooseNextEdge() {
        double totalEdgeValue = 0;
        for (Edge edge: this.currentNode.getEdgeList()) {
            if (this.unvisitedNodes.contains(edge.getNode2())) {
                totalEdgeValue += edge.getEdgeValue();
            }
        }
        Random randomGenerator = new Random();
        double nextEdgeValue = randomGenerator.nextDouble();
        double accumulatedEdgeValue = 0;

        Edge nextEdge = this.currentNode.getEdgeList().get(0);

        for (Edge edge : this.currentNode.getEdgeList()) {
            if (!this.unvisitedNodes.contains(edge.getNode2()))
                continue;
            if (accumulatedEdgeValue <= nextEdgeValue && nextEdgeValue <= accumulatedEdgeValue + (double)edge.getEdgeValue() / totalEdgeValue) {
                return edge;
            } else {
                accumulatedEdgeValue += (double)edge.getEdgeValue() / totalEdgeValue;
            }
        }
        return nextEdge;
    }
    /**
     * Updates the pheromone intensity on edges based on the total distance traveled by the ant.
     *
     * @param q is the constant pheromone quantity to deposit.
     * @param alpha is the weight for the pheromone level.
     * @param beta is the weight for the heuristic information.
     */
    public void updatePheromoneIntensity(double q, double alpha, double beta) {
        Node startingNode = this.currentNode;
        Node lastNode = this.currentNode;
        double delta = q / this.totalDistance;
        for (int i=0; i<visitedNodes.size()-1; i++) {
            Node node1 = visitedNodes.get(i);
            Node node2 = visitedNodes.get(i+1);
            for (Edge edge: node1.getEdgeList()) {
                if (edge.getNode2().getNodeNumber() == node2.getNodeNumber()) {
                    edge.setPheromoneIntensity(edge.getPheromoneIntensity() + delta, alpha, beta);
                }
            }
            for (Edge edge: node2.getEdgeList()) {
                if (edge.getNode2().getNodeNumber() == node1.getNodeNumber()) {
                    edge.setPheromoneIntensity(edge.getPheromoneIntensity() + delta, alpha, beta);
                }
            }
            lastNode = node2;
        }
        // go to starting position and complete the cycle, update that last edge
        for (Edge edge: lastNode.getEdgeList()) {
            if (edge.getNode2().getNodeNumber() == startingNode.getNodeNumber()) {
                edge.setPheromoneIntensity(edge.getPheromoneIntensity() + delta, alpha, beta);
            }
        }
        for (Edge edge: startingNode.getEdgeList()) {
            if (edge.getNode2().getNodeNumber() == lastNode.getNodeNumber()) {
                edge.setPheromoneIntensity(edge.getPheromoneIntensity() + delta, alpha, beta);
            }
        }
    }

}
