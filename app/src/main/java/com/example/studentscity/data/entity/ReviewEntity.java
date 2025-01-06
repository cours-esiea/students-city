package com.example.studentscity.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews",
        foreignKeys = @ForeignKey(entity = PlaceEntity.class,
                parentColumns = "id",
                childColumns = "placeId",
                onDelete = ForeignKey.CASCADE))
public class ReviewEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String placeId;
    private String authorId;
    private String content;
    private float rating;
    private long timestamp;
    private boolean isPending;

    public ReviewEntity(@NonNull String id, String placeId, String authorId,
                       String content, float rating, boolean isPending) {
        this.id = id;
        this.placeId = placeId;
        this.authorId = authorId;
        this.content = content;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
        this.isPending = isPending;
    }

    // Getters
    @NonNull
    public String getId() { return id; }
    public String getPlaceId() { return placeId; }
    public String getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public float getRating() { return rating; }
    public long getTimestamp() { return timestamp; }
    public boolean isPending() { return isPending; }

    // Setters
    public void setId(@NonNull String id) { this.id = id; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public void setContent(String content) { this.content = content; }
    public void setRating(float rating) { this.rating = rating; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setPending(boolean pending) { isPending = pending; }
} 