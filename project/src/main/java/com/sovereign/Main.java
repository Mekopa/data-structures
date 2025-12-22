package com.sovereign;

import com.sovereign.algo.Dijkstra;
import com.sovereign.algo.InvertedIndex;
import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;

import java.util.List;
import java.util.Random;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Java Sovereign Lab ===");
        
        int NODE_COUNT = 100000;
        String QUERY = "sovereign";
        
        // 1. Setup
        System.out.printf("Generating %d nodes...\n", NODE_COUNT);
        AdjacencyListGraph graph = new AdjacencyListGraph();
        Random rand = new Random();
        
        for (int i = 0; i < NODE_COUNT; i++) {
            Node n = new Node("node_" + i, "Project Alpha " + i, "Random noise text for database.");
            graph.addNode(n);
        }
        
        // Hide the secret
        int targetIdx = rand.nextInt(NODE_COUNT);
        Node secret = graph.getNode("node_" + targetIdx);
        secret.description = "This is the Sovereign secret location.";
        System.out.println(">> Secret hidden in node_" + targetIdx);

        System.out.println("\n--- BENCHMARK START ---");

        // 2. Linear Scan
        long startLinear = System.nanoTime();
        int found = 0;
        for (String id : graph.getAllNodeIDs()) {
            Node n = graph.getNode(id);
            if (n.name.toLowerCase().contains(QUERY) || n.description.toLowerCase().contains(QUERY)) {
                found++;
            }
        }
        long durLinear = System.nanoTime() - startLinear;
        System.out.printf("🔍 Linear Scan: %d found in %.3f ms\n", found, durLinear / 1_000_000.0);

        // 3. Index Build
        long startBuild = System.nanoTime();
        InvertedIndex index = new InvertedIndex();
        index.build(graph);
        long durBuild = System.nanoTime() - startBuild;
        System.out.printf("🏗️  Index Build: Done in %.3f ms\n", durBuild / 1_000_000.0);

        // 4. Index Search
        long startIndex = System.nanoTime();
        List<String> results = index.search(QUERY);
        long durIndex = System.nanoTime() - startIndex;
        System.out.printf("⚡ Indexed Search: %d found in %.5f ms\n", results.size(), durIndex / 1_000_000.0);

        // 5. Verdict
        double speedup = (double) durLinear / durIndex;
        System.out.printf("\n🚀 Speedup: %.0fx FASTER\n", speedup);
    }
}
