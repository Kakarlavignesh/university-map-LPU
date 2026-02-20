package com.university.map.service;

import com.university.map.algo.*;
import com.university.map.model.Building;
import com.university.map.model.Path;
import com.university.map.repository.BuildingRepository;
import com.university.map.repository.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private PathRepository pathRepository;

    public List<Node> getShortestPath(Long startId, Long endId, String algorithm, String criteria) {
        List<Building> buildings = buildingRepository.findAll();
        List<Path> paths = pathRepository.findAll();

        Map<Long, Node> nodeMap = buildings.stream()
                .collect(Collectors.toMap(Building::getId,
                        b -> new Node(b.getId(), b.getName(), b.getLatitude(), b.getLongitude())));

        Map<Node, List<Edge>> adjacencyList = new HashMap<>();
        for (Path path : paths) {
            Node source = nodeMap.get(path.getSource().getId());
            Node dest = nodeMap.get(path.getDestination().getId());

            double dist = path.getDistance() != null ? path.getDistance() : 1.0;
            int crowd = path.getCrowdLevel() != null ? path.getCrowdLevel() : 1;
            boolean blocked = path.getIsBlocked() != null ? path.getIsBlocked() : false;
            boolean wheel = path.getIsWheelchairAccessible() != null ? path.getIsWheelchairAccessible() : true;
            boolean night = path.getIsNightSafe() != null ? path.getIsNightSafe() : true;

            adjacencyList.computeIfAbsent(source, k -> new ArrayList<>())
                    .add(new Edge(dest, dist, crowd, blocked, wheel, night));

            // Bidirectional for roads
            adjacencyList.computeIfAbsent(dest, k -> new ArrayList<>())
                    .add(new Edge(source, dist, crowd, blocked, wheel, night));
        }

        Node startNode = nodeMap.get(startId);
        Node endNode = nodeMap.get(endId);

        if (algorithm.equalsIgnoreCase("astar")) {
            return AStarAlgorithm.findShortestPath(adjacencyList, startNode, endNode, criteria);
        } else {
            return DijkstraAlgorithm.findShortestPath(adjacencyList, startNode, endNode, criteria);
        }
    }
}
