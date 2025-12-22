package com.sovereign;

import com.sovereign.algo.Dijkstra;
import com.sovereign.algo.InvertedIndex;
import com.sovereign.model.AdjacencyListGraph;
import com.sovereign.model.Node;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class CLI {

    private static AdjacencyListGraph graph;
    private static InvertedIndex index;

    public static void main(String[] args) throws IOException {
        System.out.println("🚀 Initializing Sovereign Knowledge Graph (Java Edition)...");
        
        // 1. Setup Data from JSON
        graph = new AdjacencyListGraph();
        loadGraphFromJson("../../knowledge.json");

        // 2. Build Index
        System.out.print("🏗️  Building Search Index... ");
        index = new InvertedIndex();
        index.build(graph);
        System.out.println("Done.");

        // 3. Event Loop
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n✅ System Ready. Commands: 'search <term>', 'info <id>', 'path <from> <to>', 'exit'");

        while (true) {
            System.out.print("\nsovereign-java> ");
            String input = reader.readLine();
            if (input == null) break;
            
            input = input.trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "exit":
                case "quit":
                    System.out.println("Shutting down.");
                    return;

                case "help":
                    System.out.println("\n🆘 SOVEREIGN COMMANDS:");
                    System.out.println("  <anything>    : Smart Search & Discovery (Try: 'goal', 'hebbian', 'report')");
                    System.out.println("  list          : List all available Knowledge Nodes");
                    System.out.println("  path <A> <B>  : Calculate the shortest path between two nodes");
                    System.out.println("  exit          : Close the system");
                    break;

                case "list":
                    System.out.println("\n📂 ALL KNOWLEDGE NODES:");
                    List<String> sortedIds = new ArrayList<>(graph.getAllNodeIDs());
                    Collections.sort(sortedIds);
                    for (String id : sortedIds) {
                        Node node = graph.getNode(id);
                        System.out.printf("  [%s] %s (%s)\n", id, node.name, node.type);
                    }
                    break;

                case "path":
                    if (parts.length < 3) {
                        System.out.println("Usage: path <start_id> <target_id>");
                        continue;
                    }
                    String startID = parts[1];
                    String targetID = parts[2];
                    System.out.printf("Calculating path from %s to %s...\n", startID, targetID);
                    
                    Dijkstra.Result res = Dijkstra.findPath(graph, startID, targetID);
                    List<String> path = Dijkstra.reconstructPath(res.prev, startID, targetID);
                    
                    if (path.isEmpty()) {
                        System.out.println("No path found.");
                    } else {
                        System.out.println("Path found:");
                        System.out.println(String.join(" -> ", path));
                        System.out.printf("Total Cost: %d\n", res.dist.get(targetID));
                    }
                    break;

                default:
                    // SMART OMNIBOX MODE
                    String query = input;
                    String firstWord = parts[0].toLowerCase();
                    
                    // STRIP COMMAND PREFIXES IF PRESENT
                    if (firstWord.equals("search") || firstWord.equals("info")) {
                        if (parts.length > 1) {
                            // Extract everything after the first word
                            query = input.substring(parts[0].length()).trim();
                            firstWord = parts[1].toLowerCase(); // Use the actual ID as the backup check
                        } else {
                            System.out.println("Please provide a term to " + firstWord + ".");
                            continue;
                        }
                    }

                    List<String> results;
                    // A. Check Direct ID
                    if (graph.getNode(firstWord) != null) {
                        results = Collections.singletonList(firstWord);
                    } else {
                        // B. Search
                        results = index.search(query);
                    }

                    if (results.isEmpty()) {
                         System.out.println("No knowledge found.");
                         continue;
                    }
                    
                    System.out.printf("Found %d knowledge entities:\n", results.size());

                    for (int i = 0; i < results.size(); i++) {
                        if (i >= 3) {
                            System.out.printf("\n... and %d more results.\n", results.size() - 3);
                            break;
                        }
                        String id = results.get(i);
                        displayDeepContext(id);
                    }
            }
        }
    }

    private static void loadGraphFromJson(String path) {
        // CANDIDATE PATHS: Try to find the file depending on where the user ran the command from
        String[] candidates = {
            "lab/report_knowledge.json",           // Priority 1
            "report_knowledge.json",
            "../report_knowledge.json",
            "../../report_knowledge.json",
            "../../../report_knowledge.json",
            "../../../../report_knowledge.json",
            "../../../../../report_knowledge.json",
            
            "lab/knowledge.json",                  // Fallback
            "knowledge.json",
            "../knowledge.json",
            "../../knowledge.json",
            "../../../knowledge.json",
            "../../../../knowledge.json",
            "../../../../../knowledge.json"
        };

        java.nio.file.Path filePath = null;
        for (String c : candidates) {
            java.nio.file.Path p = java.nio.file.Paths.get(c);
            if (java.nio.file.Files.exists(p)) {
                filePath = p;
                System.out.println("📂 Loading knowledge from " + c + "...");
                break;
            }
        }

        if (filePath == null) {
            System.out.println("⚠️ Error: Could not find 'knowledge.json' in any standard location.");
            System.out.println("   (Falling back to empty graph)");
            return;
        }

        try {
            String content = new String(java.nio.file.Files.readAllBytes(filePath));
            
            // SUPER SIMPLE MANUAL PARSER (To match Go's dataset without dependencies)
            // 1. Extract Nodes
            String nodesBlock = content.split("\"nodes\": \\[")[1].split("],")[0];
            String[] nodeObjs = nodesBlock.split("\\},");
            
            for (String obj : nodeObjs) {
                String id = extract(obj, "id");
                String name = extract(obj, "name");
                String desc = extract(obj, "description");
                String type = extract(obj, "type");
                
                // Parse coords if needed, simplified here
                double fy = 0;
                try {
                    String fyStr = extract(obj, "fy");
                    if (!fyStr.isEmpty()) fy = Double.parseDouble(fyStr);
                } catch (Exception e) {}

                if (!id.isEmpty()) {
                    Node n = new Node(id, name, desc);
                    n.type = type;
                    n.fy = fy;
                    graph.addNode(n);
                }
            }
            
            // 2. Extract Links
            if (content.contains("\"links\": [")) {
                String linksBlock = content.split("\"links\": \\[")[1].split("]")[0];
                String[] linkObjs = linksBlock.split("\\},");
                
                for (String obj : linkObjs) {
                    String src = extract(obj, "source");
                    String tgt = extract(obj, "target");
                    if (!src.isEmpty() && !tgt.isEmpty()) {
                        graph.addEdge(src, tgt, 1);
                        graph.addEdge(tgt, src, 1);
                    }
                }
            }
            System.out.println("✅ Loaded graph data.");

        } catch (Exception e) {
            System.out.println("⚠️ Error loading JSON: " + e.getMessage());
            System.out.println("   (Falling back to empty graph)");
        }
    }

    private static String extract(String json, String key) {
        try {
            String marker = "\"" + key + "\":";
            if (!json.contains(marker)) return "";
            
            String sub = json.split(marker)[1].trim();
            if (sub.startsWith("\"")) {
                // String value
                return sub.substring(1).split("\"")[0];
            } else {
                // Number value
                return sub.split("[,}]")[0].trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private static void displayDeepContext(String id) {
        // CALL THE ALGORITHM (Separation of Concerns)
        com.sovereign.algo.BFS.DiscoveryResult ctx = com.sovereign.algo.BFS.discover(graph, id);
        
        if (ctx.center == null) {
            System.out.println("Node not found.");
            return;
        }

        Node n = ctx.center;
        
        System.out.println("\n==================================================");
        System.out.printf("   %s\n", n.name.toUpperCase());
        System.out.println("==================================================");
        System.out.printf("ID:       %s\n", n.id);
        System.out.printf("Type:     %s\n", n.type);
        System.out.printf("Layer:    %.0f (Y-Coord)\n", n.fy);
        System.out.printf("\n📝 DESCRIPTION:\n%s\n", n.description);
        
        // DISPLAY LEVEL 1
        System.out.printf("\n🔗 DIRECT CONNECTIONS (1-Hop):\n");
        if (ctx.neighbors.isEmpty()) System.out.println("   (Isolated Node)");
        
        for (Node nb : ctx.neighbors) {
             System.out.printf("   -> [%s] %s (%s)\n", nb.id, nb.name, nb.type);
        }
        
        // DISPLAY LEVEL 2
        System.out.printf("\n🌐 DEEP REACH (2-Hop Context):\n");
        if (ctx.deepNeighbors.isEmpty()) {
            System.out.println("   (No extended context found)");
        } else {
            for (Map.Entry<String, List<Node>> entry : ctx.deepNeighbors.entrySet()) {
                String bridgeName = entry.getKey();
                List<Node> deepNodes = entry.getValue();
                
                System.out.printf("   via '%s':\n", bridgeName);
                for (Node deep : deepNodes) {
                    System.out.printf("      ... connects to [%s] %s\n", deep.id, deep.name);
                }
            }
        }
        System.out.println("==================================================");
    }

    // Hardcoded data setup removed in favor of JSON loader
}
