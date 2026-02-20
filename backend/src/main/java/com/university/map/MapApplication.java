package com.university.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.university.map")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.university.map.repository")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.university.map.model")
public class MapApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapApplication.class, args);
    }
}
