package com.example.studentscity.model;

import android.location.Location;

public class Place {
    private String id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private PlaceType type;
    private float distanceToUser;

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

    public float getDistanceToUser() {
        return distanceToUser;
    }

    public void calculateDistanceToUser(Location userLocation) {
        if (userLocation == null) {
            this.distanceToUser = Float.MAX_VALUE;
            return;
        }

        Location placeLocation = new Location("");
        placeLocation.setLatitude(latitude);
        placeLocation.setLongitude(longitude);
        
        this.distanceToUser = userLocation.distanceTo(placeLocation);
    }
} 