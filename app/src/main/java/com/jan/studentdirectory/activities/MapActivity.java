package com.jan.studentdirectory.activities;

import android.Manifest;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jan.studentdirectory.util.Logman;
import com.jan.studentdirectory.R;
import com.jan.studentdirectory.model.Student;
import com.jan.studentdirectory.exceptions.PermissionDeniedException;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends SDActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    private GoogleMap map;
    private Map<Marker, Student> markerStudentMap;
    private Map<Marker, ImageView> markerImageMap;
    private final int REQUEST_FINE_LOCATION = 44;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Logman logman = Logman.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        moveTaskToBackgroundOnBack();

        markerStudentMap = new HashMap<>();
        markerImageMap = new HashMap<>();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        logman.logErrorMessage("Null location.");
                    } else {
                        logman.logInfoMessage("Location: " + location.getLatitude() + ", " + location.getLongitude());
                        setMarker(location);
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            onRequestPermissionsResult(requestCode, grantResults, REQUEST_FINE_LOCATION, this::getLastLocation);
        } catch (PermissionDeniedException e) {
            e.logErrorMessage();
        }
    }

    private void setMarker(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng coordinates = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private void getStudentLocations() {
        Intent intent = getIntent();
        String[] names = intent.getStringArrayExtra("names");
        int[] ids = intent.getIntArrayExtra("ids");
        String[] addresses = intent.getStringArrayExtra("addresses");
        double[] latitudes = intent.getDoubleArrayExtra("latitudes");
        double[] longitudes = intent.getDoubleArrayExtra("longitudes");
        String[] phones = intent.getStringArrayExtra("phones");
        String[] images = intent.getStringArrayExtra("images");

        if (names != null && ids != null & addresses != null && latitudes != null && longitudes != null && phones != null && images != null) {
            for (int i = 0; i < names.length; i++) {
                LatLng coordinates = new LatLng(latitudes[i], longitudes[i]);
                Marker marker = map.addMarker(new MarkerOptions().position(coordinates));
                markerStudentMap.put(marker, new Student(names[i], ids[i], addresses[i], latitudes[i], longitudes[i], phones[i], images[i], null));
                ImageView imageView = new ImageView(this);

                // initialize picasso
                Picasso picasso = getPicassoBuild();
                picasso.setIndicatorsEnabled(true);
                picasso.setLoggingEnabled(true);

                String imageUrl = images[i];
                picasso.load(imageUrl).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        markerImageMap.put(marker, imageView);
                    }

                    @Override
                    public void onError(Exception e) {
                        logman.logErrorMessage("Failed to fetch image from URL.");
                    }
                });
            }
        }
    }

    private void createInfoWindowAdapter() {
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                // Use the default info window frame
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // Custom view for info window content
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_info_window, null);

                TextView title = view.findViewById(R.id.title);
                TextView id = view.findViewById(R.id.id);
                TextView address = view.findViewById(R.id.address);
                TextView phone = view.findViewById(R.id.phone);
                TextView coordinates = view.findViewById(R.id.coordinates);

                ImageView image = markerImageMap.get(marker);
                Student student = markerStudentMap.get(marker);

                if (student != null && image != null) {
                    title.setText(student.getName());

                    String idText = "ID: " + student.getStudentId();
                    id.setText(idText);
                    address.setText(student.getAddress());
                    phone.setText(student.getPhone());

                    String coordinatesText = student.getLatitude() + ", " + student.getLongitude();
                    coordinates.setText(coordinatesText);
                    if (image.getParent() != null) {
                        ViewGroup parent = (ViewGroup) image.getParent();
                        parent.removeView(image);
                    }
                    view.addView(image);
                }
                return view;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;
        getLastLocation();
        getStudentLocations();
        createInfoWindowAdapter();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void handleHomeButton(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleWebButton(MenuItem item) {
        startActivityWithSameData(WebActivity.class);
    }
}
