package com.example.studentscity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.widget.Toolbar;

import com.example.studentscity.model.Place;
import com.example.studentscity.model.PlaceType;
import com.example.studentscity.viewmodel.MapViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentscity.adapter.PlacesAdapter;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.widget.RatingBar;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Button;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MapViewModel viewModel;
    
    // Center of Dax, France
    private static final GeoPoint DAX_CENTER = new GeoPoint(43.7102, -1.0536);
    private static final double DEFAULT_ZOOM = 15.0;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private boolean locationPermissionGranted = false;

    private ViewFlipper viewFlipper;
    private RecyclerView placesRecyclerView;
    private PlacesAdapter placesAdapter;
    private boolean isMapView = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize OSMDroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        setContentView(R.layout.activity_map);
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        initializeMap();
        initializeViewModel();
        
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupLocationCallback();
        getLocationPermission();

        viewFlipper = findViewById(R.id.viewFlipper);
        setupRecyclerView();

        setupFab();
    }

    private void initializeMap() {
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        
        // Set initial position and zoom
        map.getController().setZoom(DEFAULT_ZOOM);
        map.getController().setCenter(DAX_CENTER);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this, 
            ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
            .get(MapViewModel.class);
        
        // Observe places LiveData - updates map markers and list when data changes
        viewModel.getPlaces().observe(this, places -> {
            if (isMapView) {
                updateMapMarkers(places);
            }
            placesAdapter.setPlaces(places);
        });
        
        // Observe errors - show toast messages when errors occur
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMapMarkers(List<Place> places) {
        // Clear existing markers
        map.getOverlays().clear();
        
        // Add new markers for each place
        for (Place place : places) {
            addMarker(place);
        }
        
        // Refresh the map
        map.invalidate();
    }

    private void addMarker(Place place) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(place.getLatitude(), place.getLongitude()));
        marker.setTitle(place.getName());
        marker.setSnippet(place.getDescription());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            showPlaceDetailsDialog(place);
            return true;
        });
        
        map.getOverlays().add(marker);
    }

    private void showPlaceDetailsDialog(Place place) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_place_details, null);
        
        TextView nameText = dialogView.findViewById(R.id.placeName);
        TextView descriptionText = dialogView.findViewById(R.id.placeDescription);
        TextView distanceText = dialogView.findViewById(R.id.placeDistance);
        Button viewDetailsButton = dialogView.findViewById(R.id.viewDetailsButton);
        Button leaveReviewButton = dialogView.findViewById(R.id.leaveReviewButton);
        
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

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        viewDetailsButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, PlaceDetailsActivity.class);
            intent.putExtra(PlaceDetailsActivity.EXTRA_PLACE_ID, place.getId());
            startActivity(intent);
        });

        leaveReviewButton.setOnClickListener(v -> {
            dialog.dismiss();
            showAddReviewDialog(place);
        });

        dialog.show();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    viewModel.updateUserLocation(location);
                }
            }
        };
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        if (!locationPermissionGranted) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationPermissionGranted) {
            startLocationUpdates();
        }
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        map.onPause();
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.filter_all) {
            viewModel.setFilter(null);
            return true;
        } else if (id == R.id.filter_bars) {
            viewModel.setFilter(PlaceType.BAR);
            return true;
        } else if (id == R.id.filter_restaurants) {
            viewModel.setFilter(PlaceType.RESTAURANT);
            return true;
        } else if (id == R.id.filter_cafes) {
            viewModel.setFilter(PlaceType.CAFE);
            return true;
        } else if (id == R.id.toggle_view) {
            toggleView();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void toggleView() {
        isMapView = !isMapView;
        viewFlipper.setDisplayedChild(isMapView ? 0 : 1);
    }

    private void setupRecyclerView() {
        placesRecyclerView = findViewById(R.id.placesRecyclerView);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesAdapter = new PlacesAdapter();
        
        // Add click listener
        placesAdapter.setOnPlaceClickListener(place -> {
            Intent intent = new Intent(this, PlaceDetailsActivity.class);
            intent.putExtra(PlaceDetailsActivity.EXTRA_PLACE_ID, place.getId());
            startActivity(intent);
        });
        
        placesRecyclerView.setAdapter(placesAdapter);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.addPlaceFab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPlaceActivity.class);
            startActivity(intent);
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
                    viewModel.submitReview(place.getId(), content, ratingBar.getRating());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}