package com.example.studentscity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentscity.model.PlaceType;
import com.example.studentscity.viewmodel.AddPlaceViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class AddPlaceActivity extends AppCompatActivity {
    private AddPlaceViewModel viewModel;
    private MapView mapView;
    private TextInputEditText nameInput;
    private TextInputEditText descriptionInput;
    private Spinner typeSpinner;
    private Marker locationMarker;
    private GeoPoint selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        viewModel = new ViewModelProvider(this).get(AddPlaceViewModel.class);
        
        setupViews();
        setupMap();
        setupSubmitButton();
        observeViewModel();
    }

    private void setupViews() {
        nameInput = findViewById(R.id.nameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        typeSpinner = findViewById(R.id.typeSpinner);

        ArrayAdapter<PlaceType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, PlaceType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    private void setupMap() {
        mapView = findViewById(R.id.addPlaceMapView);
        mapView.setMultiTouchControls(true);

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                setLocationMarker(p);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });
        mapView.getOverlays().add(eventsOverlay);
    }

    private void setLocationMarker(GeoPoint point) {
        if (locationMarker == null) {
            locationMarker = new Marker(mapView);
            mapView.getOverlays().add(locationMarker);
        }
        locationMarker.setPosition(point);
        selectedLocation = point;
        mapView.invalidate();
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitPlace());
    }

    private void submitPlace() {
        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        PlaceType type = (PlaceType) typeSpinner.getSelectedItem();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLocation == null) {
            Toast.makeText(this, R.string.error_no_location, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.submitPlace(name, description, selectedLocation.getLatitude(),
                selectedLocation.getLongitude(), type);
    }

    private void observeViewModel() {
        viewModel.getSubmissionResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, R.string.place_submitted, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
} 