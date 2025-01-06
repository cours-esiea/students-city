package com.example.studentscity.data.converter;

import androidx.room.TypeConverter;

import com.example.studentscity.model.PlaceType;

public class PlaceTypeConverter {
    @TypeConverter
    public static PlaceType toPlaceType(String value) {
        return value == null ? null : PlaceType.valueOf(value);
    }

    @TypeConverter
    public static String fromPlaceType(PlaceType type) {
        return type == null ? null : type.name();
    }
} 