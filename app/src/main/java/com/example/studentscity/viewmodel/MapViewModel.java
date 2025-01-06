package com.example.studentscity.viewmodel;

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