package com.sovereign.model;

import java.util.*;

// Implementation A: Adjacency List (Map of Maps)
// Space: O(V + E)
public class AdjacencyListGraph {
    private Map<String, Node> nodesMap;
    private Map<String, Map<String, Integer>> edgesMap;

    public AdjacencyListGraph() {
        this.nodesMap = new HashMap<>();
        this.edgesMap = new HashMap<>();
    }

    public void addNode(Node n) {
        nodesMap.put(n.id, n);
    }

    public void addEdge(String source, String target, int weight) {
        // Ensure both exist (Validation)
        if (!nodesMap.containsKey(source) || !nodesMap.containsKey(target)) {
            return;
        }

        // Add edge: source -> target
        edgesMap.computeIfAbsent(source, k -> new HashMap<>()).put(target, weight);
    }

    public Node getNode(String id) {
        return nodesMap.get(id);
    }

    // O(1) Lookup
    public Map<String, Integer> getNeighbors(String id) {
        return edgesMap.getOrDefault(id, Collections.emptyMap());
    }

    public Collection<String> getAllNodeIDs() {
        return nodesMap.keySet();
    }
}
