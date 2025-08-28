import java.util.List;
import java.util.ArrayList;

public class WaterSortSearch extends GenericSearch {

    // Expand the current node by generating all valid successor states
    @Override
    public List<Node> expand(Node node) {
        List<Node> successors = new ArrayList<>();
        String[] bottles = node.state.split(";");

        int numberOfBottles = Integer.parseInt(bottles[0]);

        // For each pair of bottles, try pouring from one to another
        for (int i = 0; i < numberOfBottles; i++) {
            for (int j = 0; j < numberOfBottles; j++) {
                if (i != j && canPour(i, j, bottles)) {
                    String newState = pour(i, j, bottles);
                    successors.add(new Node(newState, node, "pour_" + i + "_" + j, node.pathCost + 1, node.depth + 1));
                }
            }
        }
        return successors;
    }

    // Check if the current state is a goal (all bottles are sorted by color)
    @Override
    public boolean goalTest(String state) {
        String[] bottles = state.split(";");

        // Iterate over each bottle (skip first two elements as they are numberOfBottles
        // and bottleCapacity)
        for (int i = 2; i < bottles.length; i++) {
            String[] layers = bottles[i].split(",");
            String topColor = null;

            // Find the first non-empty color in the bottle
            for (String layer : layers) {
                if (!layer.equals("e")) {
                    topColor = layer; // Set the topColor to the first non-empty layer
                    break;
                }
            }

            // If the bottle is not empty, check that all non-empty layers are the same
            // color
            if (topColor != null) {
                for (String layer : layers) {
                    if (!layer.equals("e") && !layer.equals(topColor)) {
                        return false; // If a different color is found, this is not a valid goal state
                    }
                }
            }
        }

        // If all bottles pass the test (either empty or filled with one color), return
        // true
        return true;
    }

    // Implement pouring logic
    private boolean canPour(int source, int destination, String[] bottles) {
        String[] sourceBottle = bottles[source].split(",");
        String[] destinationBottle = bottles[destination].split(",");

        // Find the top non-empty color in the source bottle
        String topSourceColor = null;
        for (String color : sourceBottle) {
            if (!color.equals("e")) {
                topSourceColor = color;
                break;
            }
        }

        // If the source bottle is completely empty, we can't pour
        if (topSourceColor == null) {
            return false;
        }

        // Find the number of empty spaces and the top color in the destination bottle
        int emptySpaces = 0;
        String topDestinationColor = null;
        for (String color : destinationBottle) {
            if (color.equals("e")) {
                emptySpaces++; // Count the number of empty spaces
            } else {
                topDestinationColor = color; // The top non-empty color in the destination
            }
        }

        // Pouring is allowed if:
        // 1. There is at least one empty layer in the destination bottle
        // 2. The destination is empty or has the same topmost color as the source
        return (emptySpaces > 0 && (topDestinationColor == null || topDestinationColor.equals(topSourceColor)));
    }

    private String pour(int source, int destination, String[] bottles) {
        String[] sourceBottle = bottles[source].split(",");
        String[] destinationBottle = bottles[destination].split(",");

        // Find the top non-empty color in the source bottle
        String topSourceColor = null;
        int pourCount = 0;
        for (int i = 0; i < sourceBottle.length; i++) {
            if (!sourceBottle[i].equals("e")) {
                topSourceColor = sourceBottle[i];
                pourCount++;
            } else if (topSourceColor != null) {
                break; // Stop counting once we encounter an empty layer
            }
        }

        // Count the empty spaces in the destination bottle
        int emptySpaces = 0;
        for (String color : destinationBottle) {
            if (color.equals("e")) {
                emptySpaces++;
            }
        }

        // Determine how much liquid can be poured
        int pourAmount = Math.min(pourCount, emptySpaces);

        // Pour the liquid from source to destination
        for (int i = sourceBottle.length - 1; i >= 0 && pourAmount > 0; i--) {
            if (!sourceBottle[i].equals("e")) {
                sourceBottle[i] = "e"; // Remove liquid from the source bottle
                pourAmount--;
            }
        }

        for (int i = destinationBottle.length - 1; i >= 0 && pourCount > 0; i--) {
            if (destinationBottle[i].equals("e")) {
                destinationBottle[i] = topSourceColor; // Add liquid to the destination bottle
                pourCount--;
            }
        }

        // Update the state of bottles after pouring
        bottles[source] = String.join(",", sourceBottle);
        bottles[destination] = String.join(",", destinationBottle);

        // Return the updated state of all bottles
        StringBuilder newState = new StringBuilder();
        newState.append(bottles[0]).append(";");
        for (int i = 1; i < bottles.length; i++) {
            newState.append(bottles[i]).append(";");
        }
        return newState.toString();
    }

    public boolean validateColors(String state) {
        // Define the allowed characters (valid colors and 'e' for empty)
        String validColors = "r,g,b,y,o,e";

        // Split the input state into individual bottles
        String[] bottles = state.split(";");

        // Iterate through each bottle (starting from index 2 to skip numberOfBottles
        // and bottleCapacity)
        for (int i = 2; i < bottles.length; i++) {
            // Split the bottle into individual layers
            String[] layers = bottles[i].split(",");

            // Check each layer
            for (String layer : layers) {
                // If the color is not in the valid set of colors, return false
                if (!validColors.contains(layer)) {
                    System.out.println("Invalid color found: " + layer);
                    return false; // Invalid color found
                }
            }
        }

        // If all colors are valid, return true
        return true;
    }

    // Solve the Water Sort puzzle using the selected strategy
    public String solve(String initialState, String strategy, boolean visualize) {
        // Validate the input colors before proceeding
        if (!validateColors(initialState)) {
            return "Invalid colors in input.";
        }

        // Call the search method inherited from GenericSearch
        String result = search(initialState, strategy, visualize);

        // Return the result of the search (either the plan, path cost, nodes expanded
        // or NOSOLUTION)
        return result;
    }

    public static void main(String[] args) {
        // Example input with 5 bottles, 4 layers, and mixed colors (requiring sorting)
        String initialState = "3;2;y,y;y,y;e,e;";

        WaterSortSearch waterSort = new WaterSortSearch();

        // Solve using BFS (Breadth-First Search), with visualization enabled
        String result = waterSort.solve(initialState, "BF", true);

        // Output the result
        System.out.println("Result: " + result);
    }
}
