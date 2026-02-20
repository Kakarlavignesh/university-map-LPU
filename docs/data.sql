-- Sample Data for a 40-Block University Campus (Example)
-- Coordinate system: Latitude, Longitude (around a central Dhaka area for demonstration)

INSERT INTO buildings (name, latitude, longitude, description, is_emergency_hub) VALUES
('Administrative Building', 23.8103, 90.4125, 'Main administration and registrar office', TRUE),
('Faculty of Engineering', 23.8115, 90.4135, 'CSE, EEE, and Civil Engineering departments', FALSE),
('Central Library', 23.8095, 90.4115, 'Largest resource center on campus', FALSE),
('Auditorium', 23.8085, 90.4120, '1000-seat multi-purpose hall', FALSE),
('Student Center', 23.8110, 90.4110, 'Cafeteria, Clubs, and Shopping', FALSE),
('North Plaza', 23.8125, 90.4125, 'Open space for gatherings', FALSE),
('South Plaza', 23.8075, 90.4125, 'Outdoor study area', FALSE),
('Science Lab A', 23.8120, 90.4140, 'Chemistry and Physics labs', FALSE),
('Medical Center', 23.8098, 90.4138, '24/7 health services', TRUE),
('Gymnasium', 23.8130, 90.4118, 'Sports and fitness facilities', FALSE);

-- Roads/Paths (Connecting nodes)
-- Path: Admin -> Faculty (Distance approx 150m, 2 min walk)
INSERT INTO paths (source_id, destination_id, distance, walking_time_minutes, is_blocked) VALUES
(1, 2, 150, 2, FALSE),
(1, 3, 120, 1.5, FALSE),
(2, 8, 80, 1, FALSE),
(3, 4, 100, 1.2, FALSE),
(3, 5, 90, 1, FALSE),
(5, 6, 200, 2.5, FALSE),
(4, 7, 130, 1.8, TRUE), -- Blocked for construction
(2, 9, 110, 1.4, FALSE),
(9, 10, 300, 4, FALSE);

-- Add some wheelchair friendly / night safe attributes
UPDATE paths SET is_wheelchair_accessible = TRUE, is_night_safe = TRUE;
UPDATE paths SET is_wheelchair_accessible = FALSE WHERE id = 4; -- Hypothetical stairs
UPDATE paths SET is_night_safe = FALSE WHERE id = 6; -- Dimly lit path
