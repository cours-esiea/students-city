package com.example.studentscity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.studentscity.data.entity.PlaceEntity;
import com.example.studentscity.model.PlaceType;

import java.util.List;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM places")
    List<PlaceEntity> getAll();

    @Query("SELECT * FROM places WHERE type = :type")
    List<PlaceEntity> getByType(PlaceType type);

    @Query("SELECT * FROM places WHERE id = :id")
    PlaceEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaceEntity place);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PlaceEntity> places);
} 