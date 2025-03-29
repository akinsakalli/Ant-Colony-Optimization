import java.util.ArrayList;

/**
 * The Node class represents a node in a graph.
 * It stores information about its coordinates, node number, and the edges connected to it.
 */
public class Node {
    private int nodeNumber;
    private double x;
    private double y;
    private ArrayList<Edge> edgeList;
    /**
     * Constructs a Node object with coordinates and a node number.
     * @param x is the x-coordinate of the node.
     * @param y is the y-coordinate of the node.
     * @param nodeNumber the unique identifier of the node.
     */
    Node(double x, double y, int nodeNumber) {
        this.x = x;
        this.y = y;
        this.nodeNumber = nodeNumber;
    }

    /**
     * Gets the node number.
     * @return The node number.
     */
    public int getNodeNumber() {
        return this.nodeNumber;
    }

    /**
     * Gets the x-coordinate of the node.
     * @return The x-coordinate.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the node.
     * @return The y-coordinate.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets the list of edges connected to the node.
     *
     * @return The list of connected edges.
     */
    public ArrayList<Edge> getEdgeList() {
        return this.edgeList;
    }

    /**
     * This method creates an array list and assigns it to the edgeList data field of the object,
     * then creates edges between this node and each of all the other nodes.
     * @param numberOfNodes is the number of nodes in the given nodeList.
     * @param nodeList is an array list of all the nodes that are present in the map.
     * @param initialPheromoneIntensity is the value of every edge's initial pheromone level.
     * @param alpha is the value that determines how much the pheromone level is important.
     * @param beta is the value that determines how much the distance is imporant.
     */
    public void initialiseEdgeList(int numberOfNodes, ArrayList<Node> nodeList, double initialPheromoneIntensity, double alpha, double beta) {
        this.edgeList = new ArrayList<>();
        for (int i=0; i<numberOfNodes; i++) {
            if (i != this.nodeNumber-1)
                this.edgeList.add(new Edge(this, nodeList.get(i), initialPheromoneIntensity, alpha, beta));
        }
    }
    /**
     * This method degrades the pheromone levels of all the edges of the node object.
     * @param degradationConstant is the constant that determines how much the pheromone levels are decreased.
     * @param alpha is the value that determines how much the pheromone level is important.
     * @param beta is the value that determines how much the distance is important.
     */
    public void degradeAllEdges(double degradationConstant, double alpha, double beta) {
        for (Edge edge: this.getEdgeList()) {
            edge.setPheromoneIntensity(edge.getPheromoneIntensity()*degradationConstant, alpha, beta);
        }
    }
}
