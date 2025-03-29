/**
 * The Edge class represents a connection between two nodes in a graph.
 * It stores information about the nodes it connects, as well as pheromone intensity, distance, and edge value.
 */
public class Edge {
    private Node node1;
    private Node node2;
    private double pheromoneIntensity;
    private double distance;
    private double edgeValue;
    /**
     * Constructs an Edge object between two nodes with initial pheromone intensity and calculates distance and edge value.
     *
     * @param node1 is the first node connected by the edge.
     * @param node2 is the second node connected by the edge.
     * @param initialPheromoneIntensity is the initial pheromone intensity of the edge.
     * @param alpha is the weight for the pheromone level.
     * @param beta is the weight for the heuristic information.
     */
    Edge(Node node1, Node node2, double initialPheromoneIntensity, double alpha, double beta) {
        this.node1 = node1;
        this.node2 = node2;
        this.pheromoneIntensity = initialPheromoneIntensity;
        this.distance = Math.sqrt(Math.pow(node1.getX() - node2.getX(), 2) + Math.pow(node1.getY() - node2.getY(), 2));
        updateEdgeValue(alpha, beta);
    }

    /**
     * Gets the edge value.
     *
     * @return is the edge value.
     */
    public double getEdgeValue(){
        return this.edgeValue;
    }

    /**
     * Gets the distance between the connected nodes.
     *
     * @return is the distance.
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * Gets the first node connected by the edge.
     *
     * @return is the first node.
     */
    public Node getNode1() {
        return this.node1;
    }

    /**
     * Gets the second node connected by the edge.
     *
     * @return is the second node.
     */
    public Node getNode2() {
        return this.node2;
    }

    /**
     * Gets the current pheromone intensity of the edge.
     *
     * @return is the pheromone intensity.
     */
    public double getPheromoneIntensity() {
        return this.pheromoneIntensity;
    }

    /**
     * Sets the pheromone intensity of the edge and updates the edge value accordingly.
     *
     * @param newPheromoneIntensity is the new pheromone intensity to set.
     * @param alpha is the weight for the pheromone level.
     * @param beta is the weight for the heuristic information.
     */
    public void setPheromoneIntensity(double newPheromoneIntensity, double alpha, double beta) {
        this.pheromoneIntensity = newPheromoneIntensity;
        updateEdgeValue(alpha, beta);
    }
    /**
     * Updates the edge value based on the current pheromone intensity, distance, alpha, and beta values.
     *
     * @param alpha is the weight for the pheromone level.
     * @param beta is the weight for the heuristic information.
     */
    private void updateEdgeValue(double alpha, double beta) {
        this.edgeValue = Math.pow(this.pheromoneIntensity, alpha) / Math.pow(this.distance, beta);
    }

}
