package com.example.studentscity.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.studentscity.model.PlaceType;

@Entity(tableName = "places")
public class PlaceEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private PlaceType type;

    public PlaceEntity(@NonNull String id, String name, String description, 
                      double latitude, double longitude, PlaceType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    // Getters
    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public PlaceType getType() { return type; }

    // Setters
    public void setId(@NonNull String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setType(PlaceType type) { this.type = type; }
} 