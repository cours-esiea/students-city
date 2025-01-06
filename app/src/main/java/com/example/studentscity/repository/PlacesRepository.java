package com.example.studentscity.repository;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.PlaceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlacesRepository {
    
    public CompletableFuture<List<Place>> getPlaces() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulated data - in real app, this would come from an API or database
            List<Place> places = new ArrayList<>();
            places.add(new Place("1", "Le Grand Café de la Fontaine", 
                    "Popular student café with outdoor seating", 
                    43.7102, -1.0536, PlaceType.CAFE));
            
            places.add(new Place("2", "La Brasserie du Commerce", 
                    "Student-friendly restaurant with budget meals", 
                    43.7120, -1.0520, PlaceType.RESTAURANT));
            
            places.add(new Place("3", "Le QG", 
                    "Trendy bar with student nights", 
                    43.7090, -1.0550, PlaceType.BAR));
            
            places.add(new Place("4", "Bibliothèque Municipale", 
                    "Quiet study spot with free WiFi", 
                    43.7115, -1.0545, PlaceType.LIBRARY));
            
            return places;
        });
    }
} 