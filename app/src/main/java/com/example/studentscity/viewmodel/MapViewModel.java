package com.example.studentscity.viewmodel;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.PlaceType;
import com.example.studentscity.repository.PlacesRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MapViewModel extends ViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<PlaceType> currentFilter = new MutableLiveData<>();
    private final MutableLiveData<Location> userLocation = new MutableLiveData<>();
    private List<Place> allPlaces; // Cache all places

    public MapViewModel() {
        this.repository = new PlacesRepository();
        loadPlaces();
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<PlaceType> getCurrentFilter() {
        return currentFilter;
    }

    public void setFilter(PlaceType type) {
        currentFilter.setValue(type);
        if (allPlaces != null) {
            if (type == null) {
                places.setValue(allPlaces);
            } else {
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
} 