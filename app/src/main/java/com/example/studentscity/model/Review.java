package com.example.studentscity.model;

public class Review {
    private String id;
    private String placeId;
    private String authorId;
    private String content;
    private float rating;
    private long timestamp;
    private boolean isPending;

    public Review(String id, String placeId, String authorId, String content, float rating) {
        this.id = id;
        this.placeId = placeId;
        this.authorId = authorId;
        this.content = content;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
        this.isPending = true;
    }

    // Getters
    public String getId() { return id; }
    public String getPlaceId() { return placeId; }
    public String getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public float getRating() { return rating; }
    public long getTimestamp() { return timestamp; }
    public boolean isPending() { return isPending; }
} 