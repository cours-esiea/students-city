package com.example.studentscity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentscity.R;
import com.example.studentscity.model.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = 
        new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final RatingBar ratingBar;
        private final TextView reviewText;
        private final TextView reviewMetadata;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewText = itemView.findViewById(R.id.reviewText);
            reviewMetadata = itemView.findViewById(R.id.reviewMetadata);
        }

        void bind(Review review) {
            ratingBar.setRating(review.getRating());
            reviewText.setText(review.getContent());
            
            String date = dateFormat.format(new Date(review.getTimestamp()));
            String metadata = itemView.getContext().getString(
                R.string.review_metadata_format, "Anonymous", date);
            reviewMetadata.setText(metadata);
        }
    }
} 