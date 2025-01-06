package com.example.studentscity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import androidx.annotation.NonNull;

public class MapActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MapViewModel viewModel;
    
    // Center of Dax, France
    private static final GeoPoint DAX_CENTER = new GeoPoint(43.7102, -1.0536);
    private static final double DEFAULT_ZOOM = 15.0;

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
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        
        // Observe places
        viewModel.getPlaces().observe(this, places -> {
            map.getOverlays().clear();
            for (Place place : places) {
                addMarker(place);
            }
            map.invalidate(); // Refresh map
        });
        
        // Observe errors
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMarker(Place place) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(place.getLatitude(), place.getLongitude()));
        marker.setTitle(place.getName());
        marker.setSnippet(place.getDescription());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        }
        
        return super.onOptionsItemSelected(item);
    }
}