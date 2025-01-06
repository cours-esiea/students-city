package com.example.studentscity.model;

public class PendingPlace extends Place {
    private String submittedBy; // User who submitted the place
    private long submissionDate;
    private boolean isPending;

    public PendingPlace(String id, String name, String description, 
                       double latitude, double longitude, PlaceType type,
                       String submittedBy) {
        super(id, name, description, latitude, longitude, type);
        this.submittedBy = submittedBy;
        this.submissionDate = System.currentTimeMillis();
        this.isPending = true;
    }

    public String getSubmittedBy() { return submittedBy; }
    public long getSubmissionDate() { return submissionDate; }
    public boolean isPending() { return isPending; }
} 