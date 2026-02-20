CREATE DATABASE IF NOT EXISTS university_map;
USE university_map;

CREATE TABLE IF NOT EXISTS buildings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    description TEXT,
    is_emergency_hub BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS paths (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    distance DOUBLE NOT NULL, -- in meters
    walking_time_minutes DOUBLE NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_wheelchair_accessible BOOLEAN DEFAULT TRUE,
    is_night_safe BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (source_id) REFERENCES buildings(id),
    FOREIGN KEY (destination_id) REFERENCES buildings(id)
);

CREATE TABLE IF NOT EXISTS frequent_routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_building_id BIGINT,
    end_building_id BIGINT,
    search_count INT DEFAULT 1,
    FOREIGN KEY (start_building_id) REFERENCES buildings(id),
    FOREIGN KEY (end_building_id) REFERENCES buildings(id)
);
