package com.example.studentscity.repository;

import android.content.Context;

import com.example.studentscity.data.AppDatabase;
import com.example.studentscity.data.dao.PlaceDao;
import com.example.studentscity.data.dao.ReviewDao;
import com.example.studentscity.data.entity.PlaceEntity;
import com.example.studentscity.data.mapper.PlaceMapper;
import com.example.studentscity.data.mapper.ReviewMapper;
import com.example.studentscity.model.Place;
import com.example.studentscity.model.PlaceType;
import com.example.studentscity.model.PendingPlace;
import com.example.studentscity.model.Review;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class PlacesRepository {
    private final PlaceDao placeDao;
    private final ReviewDao reviewDao;

    public PlacesRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        placeDao = db.placeDao();
        reviewDao = db.reviewDao();
        
        // Initialize with sample data if empty
        initializeSampleData();
    }

    private void initializeSampleData() {
        CompletableFuture.runAsync(() -> {
            if (placeDao.getAll().isEmpty()) {
                List<PlaceEntity> samplePlaces = new ArrayList<>();
                
                samplePlaces.add(new PlaceEntity("1", "Le Grand Café de la Fontaine", 
                    "Popular student café with outdoor seating", 
                    43.7102, -1.0536, PlaceType.CAFE));
                
                samplePlaces.add(new PlaceEntity("2", "La Brasserie du Commerce", 
                    "Student-friendly restaurant with budget meals", 
                    43.7120, -1.0520, PlaceType.RESTAURANT));
                
                samplePlaces.add(new PlaceEntity("3", "Le QG", 
                    "Trendy bar with student nights", 
                    43.7090, -1.0550, PlaceType.BAR));

                samplePlaces.add(new PlaceEntity("4", "La plage",
                    "Beer & Sand",
                    43.6090, -1.0550, PlaceType.BAR));
                
                samplePlaces.add(new PlaceEntity("5", "Bibliothèque Municipale", 
                    "Quiet study spot with free WiFi", 
                    43.7115, -1.0545, PlaceType.LIBRARY));

                // Insert all sample places at once
                placeDao.insertAll(samplePlaces);
            }
        }, Executors.newSingleThreadExecutor());
    }

    public CompletableFuture<List<Place>> getPlaces() {
        return CompletableFuture.supplyAsync(() -> 
            PlaceMapper.toDomainList(placeDao.getAll()),
            Executors.newSingleThreadExecutor()
        );
    }

    public CompletableFuture<Place> getPlace(String placeId) {
        return CompletableFuture.supplyAsync(() -> 
            PlaceMapper.toDomainModel(placeDao.getById(placeId)),
            Executors.newSingleThreadExecutor()
        );
    }

    public CompletableFuture<List<Place>> getPlacesByType(PlaceType type) {
        return CompletableFuture.supplyAsync(() -> 
            PlaceMapper.toDomainList(placeDao.getByType(type)),
            Executors.newSingleThreadExecutor()
        );
    }

    public CompletableFuture<List<Review>> getReviews(String placeId) {
        return CompletableFuture.supplyAsync(() -> 
            ReviewMapper.toDomainList(reviewDao.getForPlace(placeId)),
            Executors.newSingleThreadExecutor()
        );
    }

    public CompletableFuture<Boolean> submitReview(Review review) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                reviewDao.insert(ReviewMapper.toEntity(review));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }, Executors.newSingleThreadExecutor());
    }

    public CompletableFuture<Boolean> submitPendingPlace(PendingPlace pendingPlace) {
        // Convert PendingPlace to Place and save it
        Place place = new Place(
            UUID.randomUUID().toString(),
            pendingPlace.getName(),
            pendingPlace.getDescription(),
            pendingPlace.getLatitude(),
            pendingPlace.getLongitude(),
            pendingPlace.getType()
        );

        return CompletableFuture.supplyAsync(() -> {
            try {
                placeDao.insert(PlaceMapper.toEntity(place));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }, Executors.newSingleThreadExecutor());
    }
} 