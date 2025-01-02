
# Android Java Project Guidelines

This document outlines the guidelines for developing the **Students City** Android application in **Java**, using the **MVVM** (Model-View-ViewModel) architectural pattern, **CompletableFuture** for asynchronous calls, and **OpenStreetMap** for displaying maps.

---

## 1. Project Overview

- **Language**: Java  
- **Architecture**: MVVM (Model-View-ViewModel)  
- **Async Library**: `CompletableFuture`  
- **Map Library**: OpenStreetMap (via the [OSMDroid](https://github.com/osmdroid/osmdroid) or similar library)  
- **Database**: SQL-based (local or remote, depending on setup)  
- **Networking**: REST API for backend communication  

The overall goal is to provide a clear, maintainable structure that separates concerns, ensures responsive UI through asynchronous operations, and cleanly integrates mapping functionalities.

---

## 2. Architecture: MVVM

The **MVVM** pattern separates the codebase into three main layers:

1. **Model**  
   - Responsible for the data (e.g., entities, DTOs) and business logic.  
   - Interacts with data sources (e.g., database, network).  

2. **View**  
   - The UI layer (e.g., `Activity`/`Fragment`).  
   - Displays data to the user via `LiveData` from the `ViewModel`.  
   - Delegates user actions (clicks, input) to the `ViewModel`.  

3. **ViewModel**  
   - Acts as a communication center between View and Model.  
   - Exposes data as `LiveData` (or `MutableLiveData`) to the View.  
   - Uses `Repository` classes to fetch/update data asynchronously.  

### 2.1 Typical Flow
1. The user performs an action in the **View** (e.g., taps a button to fetch data).  
2. The **ViewModel** calls a method from the **Repository** to retrieve data.  
3. The **Repository** performs the task (network call, DB query) using `CompletableFuture`.  
4. Upon completion, the **ViewModel** updates its `LiveData`.  
5. The **View** observes changes in the `LiveData` and updates the UI accordingly.

---

## 3. Project Structure

A suggested package structure is:

`
com.example.studentscity ┣ model ┃ ┗ (entities, DTOs, etc.) ┣ repository ┃ ┗ (classes handling data sources, e.g., network, DB) ┣ view ┃ ┣ activity ┃ ┣ fragment ┃ ┗ (UI components for each screen) ┣ viewmodel ┃ ┗ (ViewModel classes providing data to the Views) ┗ utils ┗ (helpers, constants, utilities)



## 4. Asynchronous Operations with CompletableFuture

### 4.1 Why CompletableFuture?

- It allows non-blocking, asynchronous operations in a more modern style than older Java concurrency approaches.  
- It can easily chain multiple async operations and handle success/failure callbacks.  
- Works well when bridging to Android’s `LiveData` in a ViewModel.

### 4.2 Repository Example

Below is a simplified example of a **Repository** class that fetches data via a REST call (pseudo-code):

```java
public class PlacesRepository {

    public CompletableFuture<List<Place>> fetchPlacesFromApi() {
        // Return a CompletableFuture to ensure it's async
        return CompletableFuture.supplyAsync(() -> {
            // 1. Make network request (synchronously in a background thread)
            // 2. Parse response into a List<Place>
            // 3. Return the List<Place>
            return NetworkClient.getInstance().fetchPlaces();
        });
    }

}
````

### 4.3 ViewModel Example

The **ViewModel** exposes a `LiveData` that is updated when the Repository call completes:

```java
public class PlacesViewModel extends ViewModel {
    
    private MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
    private PlacesRepository placesRepository;

    public PlacesViewModel() {
        placesRepository = new PlacesRepository();
    }

    public LiveData<List<Place>> getPlacesLiveData() {
        return placesLiveData;
    }

    public void loadPlaces() {
        placesRepository.fetchPlacesFromApi()
                .thenAccept(places -> {
                    // Update LiveData on the main thread
                    // Use postValue if you're off the main thread
                    placesLiveData.postValue(places);
                })
                .exceptionally(throwable -> {
                    // Handle error state (e.g., show error message)
                    placesLiveData.postValue(Collections.emptyList());
                    return null;
                });
    }
}
```

### 4.4 View/Activity Example

Inside an `Activity` or `Fragment`, observe the `LiveData` to update the UI:

```java
public class PlacesActivity extends AppCompatActivity {

    private PlacesViewModel placesViewModel;
    private RecyclerView placesRecyclerView;
    private PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesRecyclerView = findViewById(R.id.places_recycler_view);
        placesAdapter = new PlacesAdapter();
        placesRecyclerView.setAdapter(placesAdapter);

        // Observe changes in placesLiveData
        placesViewModel.getPlacesLiveData().observe(this, places -> {
            // Update UI when data arrives
            placesAdapter.setPlaces(places);
        });

        // Trigger the data load
        placesViewModel.loadPlaces();
    }
}
```

---

## 5. Integrating OpenStreetMap

To display **OpenStreetMap** in an Android app, you can use the [OSMDroid](https://github.com/osmdroid/osmdroid) library. Basic steps:

1. **Add Dependencies**  
    In your `build.gradle` (Module: app):
    
    ```groovy
    implementation 'org.osmdroid:osmdroid-android:6.1.10' // or latest version
    ```
    
2. **Set Up Permissions**  
    Make sure you have permissions for internet and, if needed, location:
    
    ```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    ```
    
3. **Include a Map View in Layout**
    
    ```xml
    <org.osmdroid.views.MapView
        android:id="@+id/mapview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    ```
    
4. **Configure the Map in Activity/Fragment**
    
    ```java
    public class MapActivity extends AppCompatActivity {
        private MapView mapView;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
    
            // Configure user agent value and other settings
            Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    
            mapView = findViewById(R.id.mapview);
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setBuiltInZoomControls(true);
            mapView.setMultiTouchControls(true);
    
            // Center the map on Dax, France, with a zoom level
            IMapController mapController = mapView.getController();
            mapController.setZoom(14.0);
            GeoPoint startPoint = new GeoPoint(43.7102, -1.0536); // Approx. coordinates for Dax
            mapController.setCenter(startPoint);
        }
    }
    ```
    
5. **Optional: Add Markers**
    
    ```java
    Marker marker = new Marker(mapView);
    marker.setPosition(new GeoPoint(43.7102, -1.0536));
    marker.setTitle("My Marker");
    mapView.getOverlays().add(marker);
    ```
    

---

## 6. Best Practices

1. **Keep `ViewModel` Logic Light**
    - Use the `Repository` layer for data fetching and business logic.
2. **Use `LiveData` or `StateFlow`**
    - Ensure the UI reacts to changes automatically.
3. **Handle Errors Gracefully**
    - Use `CompletableFuture.exceptionally` or a similar mechanism to handle errors without blocking the UI.
4. **Test Offline Behaviors**
    - Plan for network failures or limited connectivity.
5. **Map Performance**
    - Cache map tiles and manage memory usage carefully with OSMDroid.
6. **User Permissions**
    - Request location permissions responsibly and explain why you need them.

---

## 7. Conclusion

By following these guidelines—**structuring the project with MVVM**, employing **CompletableFuture** for async operations, and leveraging **OpenStreetMap**—you will create a well-organized and responsive Android Java application. This approach simplifies maintenance, ensures scalability, and provides a robust foundation for future enhancements.