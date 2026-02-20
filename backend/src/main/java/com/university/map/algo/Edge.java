package com.university.map.algo;

public class Edge {
    private Node target;
    private double weight;
    private int crowdLevel;
    private boolean isBlocked;
    private boolean isWheelchairAccessible;
    private boolean isNightSafe;

    public Edge(Node target, double weight, int crowdLevel, boolean isBlocked, boolean isWheelchairAccessible,
            boolean isNightSafe) {
        this.target = target;
        this.weight = weight;
        this.crowdLevel = crowdLevel;
        this.isBlocked = isBlocked;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.isNightSafe = isNightSafe;
    }

    // Getters
    public Node getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    public int getCrowdLevel() {
        return crowdLevel;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public boolean isNightSafe() {
        return isNightSafe;
    }
}
