package com.university.map.controller;

import com.university.map.algo.Node;
import com.university.map.model.Building;
import com.university.map.service.MapService;
import com.university.map.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@CrossOrigin(origins = "*")
public class MapController {

    @Autowired
    private MapService mapService;

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("/buildings")
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @GetMapping("/route")
    public List<Node> getRoute(
            @RequestParam Long startId,
            @RequestParam Long endId,
            @RequestParam(defaultValue = "dijkstra") String algorithm,
            @RequestParam(defaultValue = "fastest") String criteria) {
        return mapService.getShortestPath(startId, endId, algorithm, criteria);
    }
}
