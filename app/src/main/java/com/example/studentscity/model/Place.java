package com.example.studentscity.model;

public class Place {
    private String id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private PlaceType type;

    public Place(String id, String name, String description, double latitude, double longitude, PlaceType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public PlaceType getType() { return type; }
} 