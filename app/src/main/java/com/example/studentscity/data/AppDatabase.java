package com.example.studentscity.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.studentscity.data.dao.PlaceDao;
import com.example.studentscity.data.dao.ReviewDao;
import com.example.studentscity.data.entity.PlaceEntity;
import com.example.studentscity.data.entity.ReviewEntity;
import com.example.studentscity.data.converter.PlaceTypeConverter;

@Database(entities = {PlaceEntity.class, ReviewEntity.class}, version = 1)
@TypeConverters({PlaceTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract PlaceDao placeDao();
    public abstract ReviewDao reviewDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "students_city_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 