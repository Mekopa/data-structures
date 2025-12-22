package com.sovereign.algo;

import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;
import java.util.*;

public class BFS {

    // A container for the "Discovery" results
    public static class DiscoveryResult {
        public Node center;
        public List<Node> neighbors = new ArrayList<>();
        // Map<BridgeNodeName, List<DeepNode>>
        public Map<String, List<Node>> deepNeighbors = new HashMap<>(); 
    }

    // The Algorithm: Depth-Limited Breadth-First Search (Depth = 2)
    public static DiscoveryResult discover(AdjacencyListGraph graph, String startID) {
        DiscoveryResult result = new DiscoveryResult();
        
        // 1. Center Node
        result.center = graph.getNode(startID);
        if (result.center == null) return result;

        // The "Visited Notebook" (Cycle Detection)
        Set<String> visited = new HashSet<>();
        visited.add(startID);

        // 2. Level 1: Neighbors
        Map<String, Integer> level1 = graph.getNeighbors(startID);
        for (String id1 : level1.keySet()) {
            Node n1 = graph.getNode(id1);
            if (n1 != null) {
                result.neighbors.add(n1);
                visited.add(id1); // Mark as visited
            }
        }

        // 3. Level 2: Deep Neighbors
        for (Node n1 : result.neighbors) {
            Map<String, Integer> level2 = graph.getNeighbors(n1.id);
            List<Node> subList = new ArrayList<>();
            
            for (String id2 : level2.keySet()) {
                // CYCLE CHECK: Only add if NOT visited
                if (!visited.contains(id2)) {
                    Node n2 = graph.getNode(id2);
                    if (n2 != null) {
                        subList.add(n2);
                        // We do NOT mark visited here to allow multiple bridges to point to the same deep node
                        // (e.g. "Linked via A" AND "Linked via B")
                    }
                }
            }
            if (!subList.isEmpty()) {
                result.deepNeighbors.put(n1.name, subList);
            }
        }

        return result;
    }
}
