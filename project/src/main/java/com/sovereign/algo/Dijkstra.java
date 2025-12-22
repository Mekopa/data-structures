package com.sovereign.algo;

import com.sovereign.model.AdjacencyListGraph;
import java.util.*;

public class Dijkstra {

    public static class Result {
        public Map<String, Integer> dist = new HashMap<>();
        public Map<String, String> prev = new HashMap<>();
        public int steps = 0;
    }

    // Helper class for Priority Queue
    private static class PQItem implements Comparable<PQItem> {
        String id;
        int priority;

        public PQItem(String id, int priority) {
            this.id = id;
            this.priority = priority;
        }

        @Override
        public int compareTo(PQItem other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public static Result findPath(AdjacencyListGraph graph, String startID, String targetID) {
        Result res = new Result();
        PriorityQueue<PQItem> pq = new PriorityQueue<>();

        // Init
        for (String id : graph.getAllNodeIDs()) {
            res.dist.put(id, Integer.MAX_VALUE);
        }
        res.dist.put(startID, 0);
        pq.add(new PQItem(startID, 0));

        while (!pq.isEmpty()) {
            PQItem current = pq.poll();
            res.steps++;

            if (current.id.equals(targetID)) break; // Found it!

            if (current.priority > res.dist.get(current.id)) continue;

            // Neighbors
            Map<String, Integer> neighbors = graph.getNeighbors(current.id);
            for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                String vID = entry.getKey();
                int weight = entry.getValue();

                int newDist = res.dist.get(current.id) + weight;
                if (newDist < res.dist.get(vID)) {
                    res.dist.put(vID, newDist);
                    res.prev.put(vID, current.id);
                    pq.add(new PQItem(vID, newDist));
                }
            }
        }
        return res;
    }

    public static List<String> reconstructPath(Map<String, String> prev, String startID, String targetID) {
        LinkedList<String> path = new LinkedList<>();
        String curr = targetID;
        
        while (curr != null) {
            path.addFirst(curr);
            if (curr.equals(startID)) break;
            curr = prev.get(curr);
        }
        
        if (path.isEmpty() || !path.getFirst().equals(startID)) return Collections.emptyList();
        return path;
    }
}
