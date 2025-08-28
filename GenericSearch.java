import java.util.*;

public abstract class GenericSearch {

    // Local definition of Node class inside GenericSearch
    public class Node {
        String state;
        Node parent;
        String action;
        int pathCost;
        int depth;

        // Constructor
        public Node(String state, Node parent, String action, int pathCost, int depth) {
            this.state = state;
            this.parent = parent;
            this.action = action;
            this.pathCost = pathCost;
            this.depth = depth;
        }

        // Method to get the path from the root to the current node
        public String getPath() {
            if (parent == null) {
                return "";
            }
            return parent.getPath() + action + ",";
        }
    }

    // Main search function that applies the specified search strategy
    public String search(String initialState, String strategy, boolean visualize) {
        Node root = new Node(initialState, null, "", 0, 0); // Create the root node
        Queue<Node> frontier = createFrontier(strategy); // Frontier for BFS (for DFS, we'll use a stack)
        Set<String> explored = new HashSet<>(); // Set to track explored states

        frontier.add(root); // Add the root node to the frontier

        while (!frontier.isEmpty()) {
            Node node = frontier.poll(); // Get the node from the frontier

            // Visualize the current state if required
            if (visualize) {
                System.out.println("Expanding node with state: " + node.state);
            }

            // Check if the current node is a goal state
            if (goalTest(node.state)) {
                return node.getPath() + ";" + node.pathCost + ";" + explored.size(); // Return path, cost, and explored
                                                                                     // nodes
            }

            // Mark the node's state as explored
            explored.add(node.state);

            // Expand the node (get all possible actions and resulting states)
            List<Node> successors = expand(node);

            // Add successors to the frontier
            for (Node child : successors) {
                if (!explored.contains(child.state)) {
                    frontier.add(child); // Add unvisited nodes to the frontier
                }
            }
        }

        return "NOSOLUTION"; // If no solution is found
    }

    // Create a frontier (queue for BFS, stack for DFS, etc.)
    private Queue<Node> createFrontier(String strategy) {
        switch (strategy) {
            case "BF": // Breadth-first search
                return new LinkedList<>(); // BFS uses a queue
            case "DF": // Depth-first search
                return new ArrayDeque<>(); // DFS uses a stack (Deque)
            // Add other search strategies like UCS, A*, etc.
            default:
                throw new IllegalArgumentException("Unknown search strategy: " + strategy);
        }
    }

    // Abstract methods for problem-specific logic
    public abstract List<Node> expand(Node node); // Expand node to generate successors

    public abstract boolean goalTest(String state); // Test if the state is a goal state
}
