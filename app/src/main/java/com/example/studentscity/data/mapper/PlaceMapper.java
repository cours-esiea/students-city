package com.example.studentscity.data.mapper;

import com.example.studentscity.data.entity.PlaceEntity;
import com.example.studentscity.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapper {
    public static Place toDomainModel(PlaceEntity entity) {
        if (entity == null) return null;
        return new Place(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getType()
        );
    }

    public static PlaceEntity toEntity(Place place) {
        if (place == null) return null;
        return new PlaceEntity(
                place.getId(),
                place.getName(),
                place.getDescription(),
                place.getLatitude(),
                place.getLongitude(),
                place.getType()
        );
    }

    public static List<Place> toDomainList(List<PlaceEntity> entities) {
        List<Place> places = new ArrayList<>();
        for (PlaceEntity entity : entities) {
            Place place = toDomainModel(entity);
            if (place != null) {
                places.add(place);
            }
        }
        return places;
    }

    public static List<PlaceEntity> toEntityList(List<Place> places) {
        List<PlaceEntity> entities = new ArrayList<>();
        for (Place place : places) {
            PlaceEntity entity = toEntity(place);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
} 