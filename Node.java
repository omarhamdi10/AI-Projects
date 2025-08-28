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