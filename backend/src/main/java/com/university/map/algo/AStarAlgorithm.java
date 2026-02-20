package com.university.map.algo;

import java.util.*;

public class AStarAlgorithm {

    public static List<Node> findShortestPath(Map<Node, List<Edge>> adjacencyList, Node source, Node destination,
            String criteria) {
        PriorityQueue<NodeScore> openSet = new PriorityQueue<>(Comparator.comparingDouble(NodeScore::getFScore));
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Node> parentMap = new HashMap<>();

        for (Node node : adjacencyList.keySet()) {
            gScore.put(node, Double.MAX_VALUE);
        }

        gScore.put(source, 0.0);
        openSet.add(new NodeScore(source, calculateHeuristic(source, destination)));

        while (!openSet.isEmpty()) {
            NodeScore current = openSet.poll();
            Node currentNode = current.getNode();

            if (currentNode.equals(destination)) {
                return reconstructPath(parentMap, destination);
            }

            for (Edge edge : adjacencyList.getOrDefault(currentNode, new ArrayList<>())) {
                if (edge.isBlocked())
                    continue;
                if (criteria.equals("wheelchair") && !edge.isWheelchairAccessible())
                    continue;
                if (criteria.equals("nightsafe") && !edge.isNightSafe())
                    continue;

                double tentativeGScore = gScore.get(currentNode) + edge.getWeight();
                if (tentativeGScore < gScore.get(edge.getTarget())) {
                    parentMap.put(edge.getTarget(), currentNode);
                    gScore.put(edge.getTarget(), tentativeGScore);
                    double fScore = tentativeGScore + calculateHeuristic(edge.getTarget(), destination);

                    NodeScore nextNodeScore = new NodeScore(edge.getTarget(), fScore);
                    openSet.add(nextNodeScore);
                }
            }
        }

        return Collections.emptyList();
    }

    private static double calculateHeuristic(Node a, Node b) {
        double R = 6371e3; // metres
        double phi1 = Math.toRadians(a.getLatitude());
        double phi2 = Math.toRadians(b.getLatitude());
        double deltaPhi = Math.toRadians(b.getLatitude() - a.getLatitude());
        double deltaLambda = Math.toRadians(b.getLongitude() - a.getLongitude());

        double x = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));

        return R * y;
    }

    private static List<Node> reconstructPath(Map<Node, Node> parentMap, Node destination) {
        List<Node> path = new LinkedList<>();
        for (Node at = destination; at != null; at = parentMap.get(at)) {
            path.add(0, at);
        }
        return path;
    }

    private static class NodeScore {
        private Node node;
        private double fScore;

        public NodeScore(Node node, double fScore) {
            this.node = node;
            this.fScore = fScore;
        }

        public Node getNode() {
            return node;
        }

        public double getFScore() {
            return fScore;
        }
    }
}
