package com.sovereign.model;

// The "Atom" of the system
public class Node {
    public String id;
    public String name;
    public String description;
    public String type;
    
    // Coordinates for A*
    public double fx, fy, fz;
    
    public Node(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = "DATA"; // Default
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", id, name);
    }
}
