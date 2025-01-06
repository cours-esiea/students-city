package com.example.studentscity.data.mapper;

import com.example.studentscity.data.entity.ReviewEntity;
import com.example.studentscity.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {
    public static Review toDomainModel(ReviewEntity entity) {
        if (entity == null) return null;
        return new Review(
                entity.getId(),
                entity.getPlaceId(),
                entity.getAuthorId(),
                entity.getContent(),
                entity.getRating(),
                entity.isPending()
        );
    }

    public static ReviewEntity toEntity(Review review) {
        if (review == null) return null;
        return new ReviewEntity(
                review.getId(),
                review.getPlaceId(),
                review.getAuthorId(),
                review.getContent(),
                review.getRating(),
                review.isPending()
        );
    }

    public static List<Review> toDomainList(List<ReviewEntity> entities) {
        List<Review> reviews = new ArrayList<>();
        for (ReviewEntity entity : entities) {
            Review review = toDomainModel(entity);
            if (review != null) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public static List<ReviewEntity> toEntityList(List<Review> reviews) {
        List<ReviewEntity> entities = new ArrayList<>();
        for (Review review : reviews) {
            ReviewEntity entity = toEntity(review);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
} 