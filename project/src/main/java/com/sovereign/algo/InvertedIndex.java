package com.sovereign.algo;

import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;
import java.util.*;

// The Search Engine
public class InvertedIndex {
    // Map<Word, List<NodeID>>
    private Map<String, List<String>> store;

    public InvertedIndex() {
        this.store = new HashMap<>();
    }

    public void build(AdjacencyListGraph graph) {
        for (String id : graph.getAllNodeIDs()) {
            Node node = graph.getNode(id);
            if (node == null) continue;

            String fullText = (node.name + " " + node.description).toLowerCase();
            String[] tokens = fullText.split("\\s+");

            Set<String> seenWords = new HashSet<>();
            for (String word : tokens) {
                // Basic cleanup
                word = word.replaceAll("[^a-zA-Z0-9]", ""); 
                if (word.isEmpty()) continue;

                if (!seenWords.contains(word)) {
                    store.computeIfAbsent(word, k -> new ArrayList<>()).add(id);
                    seenWords.add(word);
                }
            }
        }
    }

    // O(1) Average Search with Set Intersection (AND logic)
    public List<String> search(String query) {
        String[] queryWords = query.toLowerCase().trim().split("\\s+");
        if (queryWords.length == 0) return Collections.emptyList();

        // 1. Get results for the first word
        Set<String> resSet = new HashSet<>();
        String firstWord = queryWords[0].replaceAll("[^a-zA-Z0-9]", "");
        if (store.containsKey(firstWord)) {
            resSet.addAll(store.get(firstWord));
        } else {
            return Collections.emptyList();
        }

        // 2. Intersect with remaining words
        for (int i = 1; i < queryWords.length; i++) {
            String word = queryWords[i].replaceAll("[^a-zA-Z0-9]", "");
            if (word.isEmpty()) continue;
            
            if (store.containsKey(word)) {
                resSet.retainAll(store.get(word)); // The Intersection (AND)
            } else {
                return Collections.emptyList(); // One word missing = no results
            }
        }

        return new ArrayList<>(resSet);
    }
}
