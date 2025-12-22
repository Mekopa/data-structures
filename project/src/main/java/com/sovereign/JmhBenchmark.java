package com.sovereign;

import com.sovereign.algo.InvertedIndex;
import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.List;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class JmhBenchmark {

    @Param({"100", "1000", "10000"})
    private int nodeCount;

    private AdjacencyListGraph graph;
    private InvertedIndex index;
    private String query;

    @Setup(Level.Trial)
    public void setup() {
        graph = new AdjacencyListGraph();
        Random rand = new Random(42);

        for (int i = 0; i < nodeCount; i++) {
            graph.addNode(new Node("node_" + i, "Node Name " + i, "Some random text content."));
        }
        
        // Ensure the target exists
        query = "content"; // "content" is in every description
        
        // Build Index
        index = new InvertedIndex();
        index.build(graph);
    }

    @Benchmark
    public int linearSearch() {
        int found = 0;
        // Naive scan
        for (String id : graph.getAllNodeIDs()) {
            Node n = graph.getNode(id);
            if (n.description.contains(query)) {
                found++;
            }
        }
        return found;
    }

    @Benchmark
    public List<String> indexedSearch() {
        // Optimized O(1)
        return index.search(query);
    }
}
