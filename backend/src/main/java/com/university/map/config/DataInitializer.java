package com.university.map.config;

import com.university.map.model.Building;
import com.university.map.model.Path;
import com.university.map.repository.BuildingRepository;
import com.university.map.repository.PathRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class DataInitializer {

        @Bean
        public CommandLineRunner initData(BuildingRepository bRepo, PathRepository pRepo) {
                return args -> {
                        System.out.println("DEBUG: Initializing University Campus Data...");
                        Map<String, Building> buildings = new HashMap<>();

                        // 1. Hostels - Spaced 60m apart (0.00054 deg lat)
                        for (int i = 1; i <= 10; i++) {
                                String name = "BH" + i;
                                buildings.put(name, bRepo.save(new Building(name, "HOSTEL", 23.812 + (i * 0.0006),
                                                90.410, "Boys Hostel " + i, false)));
                        }
                        for (int i = 1; i <= 8; i++) {
                                String name = "GH" + i;
                                buildings.put(name, bRepo.save(new Building(name, "HOSTEL", 23.805 - (i * 0.0006),
                                                90.410, "Girls Hostel " + i, false)));
                        }

                        // 2. Important Places
                        buildings.put("Main Gate", bRepo.save(new Building("Main Gate", "GATE", 23.810, 90.405,
                                        "Entrance to university", false)));
                        buildings.put("Hospital", bRepo.save(new Building("Hospital", "SERVICE", 23.811, 90.415,
                                        "University Hospital", true)));
                        buildings.put("Library", bRepo.save(
                                        new Building("Library", "SERVICE", 23.810, 90.412, "Central Library", false)));
                        buildings.put("Admin Block", bRepo.save(new Building("Admin Block", "SERVICE", 23.809, 90.4125,
                                        "Administration", false)));
                        buildings.put("Cafeteria", bRepo.save(new Building("Cafeteria", "SERVICE", 23.8105, 90.411,
                                        "Main dining area", false)));

                        // New Points
                        buildings.put("Playground", bRepo.save(new Building("Playground", "SERVICE", 23.819, 90.409,
                                        "Backside Playground", false)));
                        buildings.put("Box Cricket", bRepo.save(new Building("Box Cricket", "SERVICE", 23.8115, 90.4155,
                                        "Hospital Side Cricket Ground", false)));

                        // Stalls for connectivity
                        for (int i = 1; i <= 5; i++) {
                                String name = "Stall " + i;
                                buildings.put(name, bRepo.save(new Building(name, "SERVICE", 23.811 + (i * 0.001),
                                                90.412 - (i * 0.0005), "Food Stall " + i, false)));
                        }

                        // 3. Academic Blocks (1 to 40)
                        for (int i = 1; i <= 40; i++) {
                                String name = "Block " + i;
                                double lat = 23.815 + (i * 0.0003);
                                double lon = 90.414 + (i * 0.0001);
                                if (i >= 25 && i <= 38) {
                                        lat = 23.820 + (i - 25) * 0.0001;
                                        lon = 90.420 + (i - 25) * 0.00005;
                                }
                                buildings.put(name, bRepo.save(new Building(name, "ACADEMIC", lat, lon,
                                                "Departmental Block " + i, false)));
                        }

                        // 4. Robust Connection Network
                        // Connect BHs to Playground and Stall 1
                        for (int i = 1; i <= 10; i++) {
                                Building bh = buildings.get("BH" + i);
                                pRepo.save(new Path(bh, buildings.get("Playground"), 100.0, 1.0, 1, false, true, true));
                                pRepo.save(new Path(bh, buildings.get("Stall 1"), 200.0, 2.0, 1, false, true, true));
                        }

                        // Connect GHs to Admin and Stall 2
                        for (int i = 1; i <= 8; i++) {
                                Building gh = buildings.get("GH" + i);
                                pRepo.save(new Path(gh, buildings.get("Admin Block"), 150.0, 1.5, 1, false, true,
                                                true));
                                pRepo.save(new Path(gh, buildings.get("Stall 2"), 250.0, 3.0, 1, false, true, true));
                        }

                        // Path Chain: Gate -> Stall 1 -> Stall 2 -> Stall 3 -> Stall 4 -> Stall 5 ->
                        // Hospital
                        pRepo.save(new Path(buildings.get("Main Gate"), buildings.get("Stall 1"), 200.0, 2.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Stall 1"), buildings.get("Stall 2"), 300.0, 3.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Stall 2"), buildings.get("Library"), 150.0, 1.5, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Library"), buildings.get("Stall 3"), 200.0, 2.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Stall 3"), buildings.get("Stall 4"), 250.0, 2.5, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Stall 4"), buildings.get("Block 1"), 300.0, 3.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Block 1"), buildings.get("Stall 5"), 400.0, 4.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Stall 5"), buildings.get("Hospital"), 200.0, 2.0, 1, false,
                                        true, true));
                        pRepo.save(new Path(buildings.get("Hospital"), buildings.get("Box Cricket"), 50.0, 0.5, 1,
                                        false, true, true));

                        // Connect Academic chain to Hospital link
                        pRepo.save(new Path(buildings.get("Block 40"), buildings.get("Stall 5"), 500.0, 5.0, 1, false,
                                        true, true));

                        for (int i = 1; i < 40; i++) {
                                Building b1 = buildings.get("Block " + i);
                                Building b2 = buildings.get("Block " + (i + 1));
                                pRepo.save(new Path(b1, b2, 100.0, 1.0, 1, false, true, true));
                        }

                        System.out.println("DEBUG: All paths initialized. Graph is fully connected via Stalls.");
                };
        }
}
