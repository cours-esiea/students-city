package com.example.studentscity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.studentscity.data.entity.ReviewEntity;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE placeId = :placeId ORDER BY timestamp DESC")
    List<ReviewEntity> getForPlace(String placeId);

    @Query("SELECT * FROM reviews WHERE isPending = 1")
    List<ReviewEntity> getPendingReviews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReviewEntity review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ReviewEntity> reviews);
} 