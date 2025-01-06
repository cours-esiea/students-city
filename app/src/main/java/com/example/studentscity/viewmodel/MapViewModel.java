package com.example.studentscity.viewmodel;

import android.app.Application;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.PlaceType;
import com.example.studentscity.repository.PlacesRepository;
import com.example.studentscity.model.Review;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

public class MapViewModel extends AndroidViewModel {
    // Repository instance to handle data operations
    private final PlacesRepository repository;
    
    // LiveData objects to communicate with the UI
    // Using MutableLiveData internally but exposing as LiveData to views
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<PlaceType> currentFilter = new MutableLiveData<>();
    private final MutableLiveData<Location> userLocation = new MutableLiveData<>();
    
    // Cache of all places for filtering
    private List<Place> allPlaces;

    // Constructor injects Application context needed for repository
    public MapViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PlacesRepository(application);
        loadPlaces(); // Initial data load
    }

    // Public methods exposed to the View layer
    // Note: We expose LiveData, not MutableLiveData, for unidirectional data flow
    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<PlaceType> getCurrentFilter() {
        return currentFilter;
    }

    // Business logic for filtering places
    public void setFilter(PlaceType type) {
        currentFilter.setValue(type);
        if (allPlaces != null) {
            if (type == null) {
                places.setValue(allPlaces);
            } else {
                // Filter places based on type
                List<Place> filtered = allPlaces.stream()
                        .filter(place -> place.getType() == type)
                        .collect(Collectors.toList());
                places.setValue(filtered);
            }
        }
    }

    public void updateUserLocation(Location location) {
        userLocation.setValue(location);
        updatePlacesWithDistance(location);
    }

    private void updatePlacesWithDistance(Location location) {
        if (allPlaces != null) {
            // Update distances
            allPlaces.forEach(place -> place.calculateDistanceToUser(location));
            
            // Sort by distance
            List<Place> sortedPlaces = allPlaces.stream()
                    .sorted((p1, p2) -> Float.compare(p1.getDistanceToUser(), p2.getDistanceToUser()))
                    .collect(Collectors.toList());
            
            // Apply current filter if exists
            PlaceType filter = currentFilter.getValue();
            if (filter != null) {
                sortedPlaces = sortedPlaces.stream()
                        .filter(place -> place.getType() == filter)
                        .collect(Collectors.toList());
            }
            
            places.setValue(sortedPlaces);
        }
    }

    // Private method to load data from repository
    private void loadPlaces() {
        repository.getPlaces()
                .thenAccept(placesList -> {
                    allPlaces = placesList;
                    // Apply current filter if exists
                    PlaceType filter = currentFilter.getValue();
                    if (filter != null) {
                        setFilter(filter);
                    } else {
                        places.postValue(placesList);
                    }
                })
                .exceptionally(throwable -> {
                    error.postValue("Failed to load places: " + throwable.getMessage());
                    return null;
                });
    }

    public void submitReview(String placeId, String content, float rating) {
        String reviewId = UUID.randomUUID().toString();
        Review review = new Review(reviewId, placeId, "current_user", content, rating, true);
        
        repository.submitReview(review)
                .thenAccept(success -> {
                    // Handle success/failure if needed
                });
    }
} 