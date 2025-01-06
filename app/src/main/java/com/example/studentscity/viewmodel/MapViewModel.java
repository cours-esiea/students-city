package com.example.studentscity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentscity.model.Place;
import com.example.studentscity.repository.PlacesRepository;

import java.util.List;

public class MapViewModel extends ViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

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

    private void loadPlaces() {
        repository.getPlaces()
                .thenAccept(placesList -> places.postValue(placesList))
                .exceptionally(throwable -> {
                    error.postValue("Failed to load places: " + throwable.getMessage());
                    return null;
                });
    }
} 