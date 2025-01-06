package com.example.studentscity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentscity.adapter.ReviewAdapter;
import com.example.studentscity.model.Place;
import com.example.studentscity.viewmodel.PlaceDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Locale;

public class PlaceDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_PLACE_ID = "place_id";
    
    private PlaceDetailsViewModel viewModel;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        String placeId = getIntent().getStringExtra(EXTRA_PLACE_ID);
        if (placeId == null) {
            finish();
            return;
        }

        setupToolbar();
        setupRecyclerView();
        setupViewModel(placeId);
        setupFab();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        RecyclerView reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupViewModel(String placeId) {
        viewModel = new ViewModelProvider(this).get(PlaceDetailsViewModel.class);
        viewModel.loadPlace(placeId);
        
        viewModel.getPlace().observe(this, this::updateUI);
        viewModel.getReviews().observe(this, reviews -> {
            reviewAdapter.setReviews(reviews);
        });
    }

    private void updateUI(Place place) {
        TextView nameText = findViewById(R.id.placeName);
        TextView descriptionText = findViewById(R.id.placeDescription);
        TextView distanceText = findViewById(R.id.placeDistance);

        nameText.setText(place.getName());
        descriptionText.setText(place.getDescription());

        float distance = place.getDistanceToUser();
        if (distance != Float.MAX_VALUE) {
            String distanceStr = distance < 1000 
                ? String.format(Locale.getDefault(), "%.0f m away", distance)
                : String.format(Locale.getDefault(), "%.1f km away", distance / 1000);
            distanceText.setText(distanceStr);
            distanceText.setVisibility(View.VISIBLE);
        } else {
            distanceText.setVisibility(View.GONE);
        }
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.addReviewFab);
        fab.setOnClickListener(v -> {
            Place place = viewModel.getPlace().getValue();
            if (place != null) {
                showAddReviewDialog(place);
            }
        });
    }

    private void showAddReviewDialog(Place place) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_review, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputEditText reviewInput = dialogView.findViewById(R.id.reviewInput);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.write_review))
                .setView(dialogView)
                .setPositiveButton(R.string.submit, (dialog, which) -> {
                    String content = reviewInput.getText().toString().trim();
                    if (content.isEmpty()) {
                        Toast.makeText(this, R.string.error_empty_review, 
                            Toast.LENGTH_SHORT).show();
                        return;
                    }
                    viewModel.submitReview(content, ratingBar.getRating());
                    Toast.makeText(this, R.string.review_submitted, 
                        Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 