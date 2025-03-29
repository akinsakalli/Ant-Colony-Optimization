import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * Program finds the shortest route of a delivery car.
 * An input text file is given by the user. The program reads this input file,
 * calculates the shortest route using the brute-force or ant colony optimization approach,
 * and plots the shortest route graph to the user, using StdDraw library.
 *
 * @author Akin Tuna Sakalli
 * @since Date: 12.05.2024
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        int chosenMethod = 1; // Select 1 for brute force, 2 for ant colony optimization

        int displayMode = 1; // Select 1 for shortest path, 2 for pheromone intensity map

        String fileName = "InputData/input01.txt"; // Select the name of the input text file to be processed

        // Hyperparameters
        int iterationCount = 160;
        int countPerIteration = 80;
        double degradationFactor = 0.8;
        double alpha = 1.1;
        double beta = 1.6;
        double initialPheromoneIntensity = 0.01;
        double q = 0.0001;

        // File is opened, and a scanner is created in order to process it
        File file = new File(fileName);
        Scanner inputFile = new Scanner(file);
        // An arraylist is created in order to store all the node objects
        ArrayList<Node> nodeList = new ArrayList<>();
        int nodeNumber = 1;

        // All node objects are created with increasing node numbers and the coordinates in the input file
        // And all the nodes are appended to the arraylist after their instantiation
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            String[] lineSplit = line.split(",");
            double x = Double.parseDouble(lineSplit[0]);
            double y = Double.parseDouble(lineSplit[1]);
            nodeList.add(new Node(x,y, nodeNumber));
            nodeNumber++;
        }
        nodeNumber--;
        int numberOfNodes = nodeNumber;
        inputFile.close(); // File is closed after the reading process is finished

        // All edges of each node are initialised
        for (Node node: nodeList) {
            node.initialiseEdgeList(numberOfNodes, nodeList, initialPheromoneIntensity, alpha, beta);
        }

        // If the user has picked brute-force approach, its method is called
        if (chosenMethod == 1) {
            bruteForce(numberOfNodes, nodeList);
        }
        // If the user has picked ant colony optimization approach, its method is called with the required parameters
        else if (chosenMethod == 2) {
            // Ant colony optimization method returns a node array
            Node[] shortestPath = antColonyOptimization(nodeList, iterationCount, countPerIteration, degradationFactor, q, alpha, beta);
            // Shortest path is displayed to the user
            display(shortestPath, nodeList, displayMode);
        }
    }

    /**
     * This method calls the permutation method, prints the information about the program and the shortest path,
     * and calls the display method which prints the map to screen.
     *
     * @param numberOfNodes is the number of nodes in the given input node list
     * @param nodeList is the array list of nodes, which are objects with coordinates and node number values
     */
    public static void bruteForce(int numberOfNodes, ArrayList<Node> nodeList) {
        // An array is created to be used in the permutation method later
        Node[] nodeArray = new Node[numberOfNodes-1];
        int index = 0;
        // Each node except migros is added to the array
        for (int i=1; i<numberOfNodes; i++) {
            nodeArray[index] = nodeList.get(i);
            index++;
        }
        Node[] shortestRoute = nodeArray;
        // Initial time is recorded to calculate the running time of the permutation method
        double initialTime = System.currentTimeMillis()/1000.0;
        // Permutation method is called with the initial, sorted array of nodes
        shortestRoute = permute(nodeList, shortestRoute, 0);
        // Running time of the permutation method is calculated
        double finalTime = System.currentTimeMillis()/1000.0;
        double deltaTime = finalTime - initialTime;
        // Distance of the shortest route is calculated
        double shortestRouteDistance = calculateRouteDistance(shortestRoute, nodeList);

        // Information about the method and the shortest path is printed out
        System.out.println("Method: Brute-Force Method");
        System.out.printf("Shortest Distance: %.5f\n",shortestRouteDistance);
        System.out.print("Shortest Path: ");
        System.out.print("[1, ");
        for (Node node: shortestRoute) {
            System.out.print(node.getNodeNumber() + ", ");
        }
        System.out.println("1]");
        System.out.printf("Time it takes to find the shortest path: %.5f seconds.", deltaTime);

        Node[] shortestPath = new Node[numberOfNodes+1];
        // Migros is appended to the starting of the list.
        shortestPath[0] = nodeList.get(0);
        // All the nodes are appended to the list
        for (int i = 1; i < numberOfNodes; i++) {
            shortestPath[i] = shortestRoute[i-1];
        }
        // Migros is appended to the end of the list.
        shortestPath[numberOfNodes] = nodeList.get(0);
        // Shortest path is drawn to the canvas and displayed to the user
        display(shortestPath, nodeList, 1);
    }

    /**
     * This method calculates all the possible permutations of a given list with recursion and returns the shortest possible path.
     * @param nodes is the list that contains all the information about the nodes.
     * @param route is the current path that is changed in each iteration.
     * @param k is the current depth of the recursion, and when its value reaches the length of the list, current route is returned to the previous iteration.
     * @return A node array with the shortest possible path in order is returned.
     */
    public static Node[] permute(ArrayList<Node> nodes, Node[] route, int k) {
        Node[] shortestRoute = route.clone(); // Current shortest route is stored in this variable in each iteration.
        // While the recursion depth is smaller than the length of the route, function keeps iterating through itself.
        // When the recursion depth is equal to the length of the route, current shortest route is returned to the previous recursive iteration.
        if (k != route.length) {
            // For each node after the current node, change that node with the current node, and find all possible permutations of this newly created route.
            // After this recursive call is done until the end of the route, backtrack with changing that current node with its original position.
            // While doing all of these iterations, calculate the distance of every route and change it with the shortest route if its distance is shorter.
            // With doing this, we will have encountered all possible permutations of the given list and find the shortest one of them.
            for (int i = k; i < route.length; i++) {
                Node temp = route[i];
                route[i] = route[k];
                route[k] = temp;
                Node[] candidateRoute = permute(nodes, route,k+1);
                double distance = calculateRouteDistance(candidateRoute, nodes);
                double shortestDistance = calculateRouteDistance(shortestRoute, nodes);
                if (distance < shortestDistance) {
                    System.arraycopy(candidateRoute, 0, shortestRoute, 0, route.length);
                }
                temp = route[i];
                route[i] = route[k];
                route[k] = temp;
            }
        }
        return shortestRoute;
    }

    /**
     * This method calculates the distance of the given route.
     * @param route is the route whose distance we want to calculate.
     * @param nodeList is the list of all the nodes.
     * @return the distance, which is the magnitude of the path from the first node to the last node when we traverse the given route.
     */
    public static double calculateRouteDistance(Node[] route, ArrayList<Node> nodeList) {
        // Initialise a total distance variable which will be returned eventually.
        double totalDistance = 0;
        Node migros = nodeList.get(0);
        Node lastNode = migros;
        // For each node starting from the first one (migros), calculate the distance between the next node and that node,
        // and add that distance to the total distance variable.
        for (int i=0; i< route.length; i++) {
            Node nextNode= route[i];
            double distance = Math.sqrt(Math.pow(lastNode.getX()-nextNode.getX(),2) + Math.pow(lastNode.getY()-nextNode.getY(),2));
            totalDistance += distance;
            lastNode = nextNode;
        }
        // After adding all the distances between adjacent nodes, add the distance between the last node and the migros in order to complete the cycle.
        totalDistance += Math.sqrt(Math.pow(lastNode.getX()-migros.getX(), 2) + Math.pow(lastNode.getY()- migros.getY(), 2));
        return totalDistance;
    }

    /**
     * Applies the Ant Colony Optimization algorithm to find the shortest path through a graph.
     * @param nodeList is the list of nodes in the graph.
     * @param iterationCount is the number of iterations for the optimization process.
     * @param countPerIteration is the number of ants to traverse the graph per iteration.
     * @param degradationFactor is the degradation factor for pheromone intensity on edges.
     * @param q is the constant pheromone quantity to deposit.
     * @param alpha is the weight for the pheromone level.
     * @param beta is the weight for the heuristic information.
     * @return an array representing the shortest path found by the algorithm.
     */
    public static Node[] antColonyOptimization(ArrayList<Node> nodeList, int iterationCount, int countPerIteration, double degradationFactor, double q, double alpha, double beta) {
        double initialTime = System.currentTimeMillis() / 1000.0;
        ArrayList<Node> bestAntTraversal = new ArrayList<>();
        double bestTraversalDistance = Double.MAX_VALUE;
        Random randomGenerator = new Random();

        // ArrayList<Double> bestTraversalList = new ArrayList<>();
        // ArrayList<Integer> bestTraversalIterationCountList = new ArrayList<>();

        for (int i=0; i<iterationCount; i++) {
            for (int j=0; j<countPerIteration; j++) {
                int startingNodeIndex = randomGenerator.nextInt(nodeList.size());
                Node startingNode = nodeList.get(startingNodeIndex);
                Ant ant = new Ant(nodeList, startingNode);
                ant.traverse();
                ant.updatePheromoneIntensity(q, alpha, beta);
                if (ant.getTotalDistance() < bestTraversalDistance) {
                    bestTraversalDistance = ant.getTotalDistance();
                    bestAntTraversal = (ArrayList<Node>)ant.getVisitedNodes().clone();
                }
            }
            for (Node node: nodeList) {
                node.degradeAllEdges(degradationFactor, alpha, beta);
            }
            // bestTraversalList.add(bestTraversalDistance);
            // bestTraversalIterationCountList.add(i+ 1);
        }

        /*
         // In order to plot the graph in the report,
         // a new file is created and the distance of the shortest path until that iteration is written in that file.
        try {
            File myFile = new File("output.txt");
            myFile.createNewFile();
            FileWriter myWriter = new FileWriter("output.txt");
            for (Double distance: bestTraversalList) {
                myWriter.write(distance.toString() +"\n");
            }
            for (Integer count: bestTraversalIterationCountList) {
                myWriter.write(count.toString() + "\n");
            }
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("error file could not be written");
        }
         */

        Node migros = nodeList.get(0);
        while (bestAntTraversal.get(0).getNodeNumber() != migros.getNodeNumber()) {
            bestAntTraversal.add(bestAntTraversal.get(0));
            bestAntTraversal.remove(0);
        }
        bestAntTraversal.remove(0);
        Node[] shortestPath = new Node[bestAntTraversal.size()];
        for (int i=0; i < bestAntTraversal.size(); i++) {
            shortestPath[i] = bestAntTraversal.get(i);
        }
        double shortestPathDistance = calculateRouteDistance(shortestPath, nodeList);

        double finalTime = System.currentTimeMillis() / 1000.0;
        double deltaTime = finalTime - initialTime;

        System.out.println("Method: Ant Colony Optimization Method");
        System.out.printf("Shortest Distance: %.5f\n",shortestPathDistance);
        System.out.print("Shortest Path: ");
        System.out.print("[1, ");
        for (int i=0; i < shortestPath.length-1; i++) {
            System.out.print(shortestPath[i].getNodeNumber() + ", ");
        }
        System.out.println("1]");
        System.out.printf("Time it takes to find the shortest path: %.5f seconds.", deltaTime);

        Node[] shortestRoute = new Node[bestAntTraversal.size()+2];
        shortestRoute[0] = migros;
        for (int i=1; i < shortestRoute.length-1; i++) {
            shortestRoute[i] = shortestPath[i-1];
        }
        shortestRoute[shortestRoute.length-1] = migros;
        return shortestRoute;
    }

    /**
     * This method displays the given node array to the screen.
     * @param shortestPath is the array of nodes whose pictorial representation will be printed out to the canvas.
     * @param nodeList is the array list of all the nodes.
     * @param mode is the display mode, shortest path is drawn for 1, and all edge values in the map is drawn for 2.
     */
    public static void display(Node[] shortestPath, ArrayList<Node> nodeList, int mode) {
        // Canvas is prepared
        StdDraw.enableDoubleBuffering();
        int canvas_width = 800;
        int canvas_height = 800;
        StdDraw.setCanvasSize(canvas_width, canvas_height);
        StdDraw.setXscale(0, 1.0);
        StdDraw.setYscale(0, 1.0);
        double radius = 0.025;
        double penRadius = 0.003;

        Node migros = nodeList.get(0);

        // For mode 1, all the lines between nodes are drawn.
        if (mode == 1) {
            for (int i = 0; i < shortestPath.length - 1; i++) {
                Node node1 = shortestPath[i];
                Node node2 = shortestPath[i + 1];
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(penRadius);
                StdDraw.line(node1.getX(), node1.getY(), node2.getX(), node2.getY());
            }
        }
        // For mode 2, all edges of all nodes are drawn.
        else if (mode == 2) {
            StdDraw.setPenColor(StdDraw.BLACK);
            for (Node node : nodeList) {
                for (Edge edge : node.getEdgeList()) {
                    Node node1 = edge.getNode1();
                    Node node2 = edge.getNode2();
                    StdDraw.setPenRadius(edge.getPheromoneIntensity() * 2);
                    StdDraw.line(node1.getX(), node1.getY(), node2.getX(), node2.getY());
                }
            }
        }
        // Migros is drawn.
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.filledCircle(migros.getX(),migros.getY(), radius);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(migros.getX(), migros.getY(), Integer.toString(migros.getNodeNumber()));

        // All nodes are drawn.
        for (int i=1; i < shortestPath.length-1; i++) {
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            Node node = shortestPath[i];
            StdDraw.filledCircle(node.getX(), node.getY(), radius);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(node.getX(), node.getY(), Integer.toString(node.getNodeNumber()));
        }
        // Canvas is displayed to the screen.
        StdDraw.show();
    }
}