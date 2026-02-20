package com.university.map.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paths")
public class Path {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Building source;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Building destination;

    private Double distance;
    private Double walkingTimeMinutes;
    private Integer crowdLevel; // 1 (low) to 5 (high)
    private Boolean isBlocked;
    private Boolean isWheelchairAccessible;
    private Boolean isNightSafe;

    public Path() {
    }

    public Path(Building source, Building destination, Double distance, Double walkingTimeMinutes, Integer crowdLevel,
            Boolean isBlocked,
            Boolean isWheelchairAccessible, Boolean isNightSafe) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.walkingTimeMinutes = walkingTimeMinutes;
        this.crowdLevel = crowdLevel;
        this.isBlocked = isBlocked;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.isNightSafe = isNightSafe;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getSource() {
        return source;
    }

    public void setSource(Building source) {
        this.source = source;
    }

    public Building getDestination() {
        return destination;
    }

    public void setDestination(Building destination) {
        this.destination = destination;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getWalkingTimeMinutes() {
        return walkingTimeMinutes;
    }

    public void setWalkingTimeMinutes(Double walkingTimeMinutes) {
        this.walkingTimeMinutes = walkingTimeMinutes;
    }

    public Integer getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(Integer crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Boolean getIsWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public void setIsWheelchairAccessible(Boolean isWheelchairAccessible) {
        this.isWheelchairAccessible = isWheelchairAccessible;
    }

    public Boolean getIsNightSafe() {
        return isNightSafe;
    }

    public void setIsNightSafe(Boolean isNightSafe) {
        this.isNightSafe = isNightSafe;
    }
}
