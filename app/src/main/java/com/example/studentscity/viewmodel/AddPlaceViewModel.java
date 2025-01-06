package com.example.studentscity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentscity.model.PendingPlace;
import com.example.studentscity.model.PlaceType;
import com.example.studentscity.repository.PlacesRepository;

import java.util.UUID;

public class AddPlaceViewModel extends ViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<Boolean> submissionResult = new MutableLiveData<>();

    public AddPlaceViewModel() {
        this.repository = new PlacesRepository();
    }

    public void submitPlace(String name, String description, double latitude,
                          double longitude, PlaceType type) {
        String id = UUID.randomUUID().toString();
        PendingPlace place = new PendingPlace(id, name, description, latitude,
                longitude, type, "current_user"); // TODO: Add real user management

        repository.submitPendingPlace(place)
                .thenAccept(success -> submissionResult.postValue(true))
                .exceptionally(throwable -> {
                    submissionResult.postValue(false);
                    return null;
                });
    }

    public LiveData<Boolean> getSubmissionResult() {
        return submissionResult;
    }
} 