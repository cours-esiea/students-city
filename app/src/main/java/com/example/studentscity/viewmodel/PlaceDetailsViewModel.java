package com.example.studentscity.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.Review;
import com.example.studentscity.repository.PlacesRepository;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class PlaceDetailsViewModel extends AndroidViewModel {
    private final PlacesRepository repository;
    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private String placeId;

    public PlaceDetailsViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PlacesRepository(application);
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
        Review review = new Review(reviewId, placeId, "current_user", content, rating, true);
        
        // Add the review to the list immediately
        List<Review> currentReviews = reviews.getValue();
        if (currentReviews != null) {
            List<Review> updatedReviews = new ArrayList<>(currentReviews);
            updatedReviews.add(0, review); // Add at the beginning of the list
            reviews.setValue(updatedReviews);
        }
        
        // Submit to repository
        repository.submitReview(review)
                .thenAccept(success -> {
                    if (!success) {
                        // If submission fails, reload reviews to remove the pending one
                        loadReviews();
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