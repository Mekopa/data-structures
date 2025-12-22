package com.sovereign;

import com.sovereign.algo.Dijkstra;
import com.sovereign.algo.InvertedIndex;
import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;

import java.util.Random;

public class ManualBenchmark {

    private static Random rand = new Random();

    // Helper to setup graph
    private static AdjacencyListGraph setupGraph(int n) {
        AdjacencyListGraph g = new AdjacencyListGraph();
        for (int i = 0; i < n; i++) {
            g.addNode(new Node(String.valueOf(i), "Node " + i, "Description"));
        }
        // Random edges
        for (int i = 0; i < n; i++) {
            int target = rand.nextInt(n);
            g.addEdge(String.valueOf(i), String.valueOf(target), 1);
        }
        return g;
    }

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   JAVA BENCHMARK SUITE (Simulation)");
        System.out.println("============================================");

        int[] sizes = {100, 1000, 10000, 50000}; // Sizes to test

        System.out.println("\n--- 1. SEARCH GROWTH TEST ---");
        System.out.printf("%-10s | %-15s | %-15s\n", "N", "Linear (ns)", "Index (ns)");
        System.out.println("----------------------------------------------");

        for (int n : sizes) {
            AdjacencyListGraph g = setupGraph(n);
            InvertedIndex index = new InvertedIndex();
            index.build(g);
            String query = "Node " + (n - 1); // Worst case for linear

            // Warmup
            linearSearch(g, query);
            index.search(query);

            // A. Linear Benchmark
            long startLin = System.nanoTime();
            linearSearch(g, query);
            long durLin = System.nanoTime() - startLin;

            // B. Index Benchmark
            long startIdx = System.nanoTime();
            index.search(query);
            long durIdx = System.nanoTime() - startIdx;

            System.out.printf("%-10d | %-15d | %-15d\n", n, durLin, durIdx);
        }

        System.out.println("\n--- 2. DIJKSTRA PATHFINDING TEST ---");
        System.out.printf("%-10s | %-15s\n", "N", "Time (ns)");
        System.out.println("----------------------------");

        for (int n : sizes) {
            if (n > 10000) break; // Skip huge ones for simple test
            AdjacencyListGraph g = setupGraph(n);
            
            // Ensure path exists for valid test (connect 0 -> N-1 specifically)
            g.addEdge("0", "1", 1);
            for(int i=1; i<n-1; i++) {
                 g.addEdge(String.valueOf(i), String.valueOf(i+1), 1);
            }

            long start = System.nanoTime();
            Dijkstra.findPath(g, "0", String.valueOf(n-1));
            long dur = System.nanoTime() - start;

            System.out.printf("%-10d | %-15d\n", n, dur);
        }
    }

    // Isolated Linear Search Logic
    private static void linearSearch(AdjacencyListGraph g, String query) {
        for (String id : g.getAllNodeIDs()) {
            Node node = g.getNode(id);
            if (node.name.equals(query)) break;
        }
    }
}
