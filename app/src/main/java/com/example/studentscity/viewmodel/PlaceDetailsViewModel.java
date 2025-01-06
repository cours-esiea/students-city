package com.example.studentscity.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.Review;
import com.example.studentscity.repository.PlacesRepository;

import java.util.List;
import java.util.UUID;

public class PlaceDetailsViewModel extends ViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private String placeId;

    public PlaceDetailsViewModel() {
        this.repository = new PlacesRepository();
    }

    public void loadPlace(String placeId) {
        this.placeId = placeId;
        repository.getPlace(placeId)
                .thenAccept(place::postValue);
        loadReviews();
    }

    private void loadReviews() {
        repository.getReviews(placeId)
                .thenAccept(reviews::postValue);
    }

    public void submitReview(String content, float rating) {
        String reviewId = UUID.randomUUID().toString();
        Review review = new Review(reviewId, placeId, "current_user", content, rating);
        
        repository.submitReview(review)
                .thenAccept(success -> {
                    if (success) {
                        loadReviews(); // Refresh reviews after submission
                    }
                });
    }

    public LiveData<Place> getPlace() {
        return place;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }
} 