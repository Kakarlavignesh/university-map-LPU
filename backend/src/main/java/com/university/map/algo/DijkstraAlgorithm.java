package com.university.map.algo;

import java.util.*;

public class DijkstraAlgorithm {

    public static List<Node> findShortestPath(Map<Node, List<Edge>> adjacencyList, Node source, Node destination,
            String criteria) {
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeDistance::getDistance));
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> parentMap = new HashMap<>();

        for (Node node : adjacencyList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }

        distances.put(source, 0.0);
        pq.add(new NodeDistance(source, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.getNode();

            if (currentNode.equals(destination))
                break;

            if (current.getDistance() > distances.get(currentNode))
                continue;

            for (Edge edge : adjacencyList.getOrDefault(currentNode, new ArrayList<>())) {
                if (edge.isBlocked())
                    continue;

                // Apply filters based on criteria
                if (criteria.equals("wheelchair") && !edge.isWheelchairAccessible())
                    continue;
                // The previous "nightsafe" continue filter is replaced by a penalty below.

                double weight = edge.getWeight(); // Default distance

                if ("fastest".equalsIgnoreCase(criteria)) {
                    weight = edge.getWeight(); // In this case distance, but could be time
                } else if ("safest".equalsIgnoreCase(criteria)) {
                    // Safe route: Prefer less crowded paths? Or specifically marked nightsafe
                    weight = edge.getWeight() * (1 + (double) edge.getCrowdLevel() / 10.0);
                } else if ("nightsafe".equalsIgnoreCase(criteria)) {
                    if (!edge.isNightSafe())
                        weight *= 5; // Penalty for unsafe paths
                }

                double newDist = distances.get(currentNode) + weight;
                if (newDist < distances.get(edge.getTarget())) {
                    distances.put(edge.getTarget(), newDist);
                    parentMap.put(edge.getTarget(), currentNode);
                    pq.add(new NodeDistance(edge.getTarget(), newDist));
                }
            }
        }

        return reconstructPath(parentMap, destination);
    }

    private static List<Node> reconstructPath(Map<Node, Node> parentMap, Node destination) {
        List<Node> path = new LinkedList<>();
        for (Node at = destination; at != null; at = parentMap.get(at)) {
            path.add(0, at);
        }
        return path.size() > 1 || (path.size() == 1 && parentMap.isEmpty()) ? path : Collections.emptyList();
    }

    private static class NodeDistance {
        private Node node;
        private double distance;

        public NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        public Node getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }
    }
}
