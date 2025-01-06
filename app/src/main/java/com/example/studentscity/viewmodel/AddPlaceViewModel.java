package com.example.studentscity.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentscity.model.PendingPlace;
import com.example.studentscity.repository.PlacesRepository;

public class AddPlaceViewModel extends AndroidViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<Boolean> submissionSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public AddPlaceViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PlacesRepository(application);
    }

    public void submitPlace(PendingPlace place) {
        repository.submitPendingPlace(place)
                .thenAccept(success -> {
                    if (success) {
                        submissionSuccess.postValue(true);
                    } else {
                        error.postValue("Failed to submit place");
                    }
                })
                .exceptionally(throwable -> {
                    error.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    public LiveData<Boolean> getSubmissionSuccess() {
        return submissionSuccess;
    }

    public LiveData<String> getError() {
        return error;
    }
} 