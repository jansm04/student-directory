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
import com.jan.studentdirectory.Properties;
import com.jan.studentdirectory.R;
import com.jan.studentdirectory.Student;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends TabHandler implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    private GoogleMap map;
    private Map<Marker, Student> markerStudentMap;
    private Map<Marker, ImageView> markerImageMap;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    int PERMISSION_ID = 44;

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
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        System.out.println("Error: null location.");
                    } else {
                        System.out.println("Location: " + location.getLatitude() + ", " + location.getLongitude());
                        setMarker(location);
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission granted.");
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setMarker(Location location) {
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

    public void getStudentLocations() {
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
                String credential = Credentials.basic(Properties.USERNAME, Properties.PASSWORD);
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(chain -> {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", credential);
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                        .build();

                Picasso picasso = new Picasso.Builder(getApplicationContext())
                        .downloader(new OkHttp3Downloader(okHttpClient))
                        .build();

                String imageUrl = images[i];
                picasso.setIndicatorsEnabled(true);
                picasso.setLoggingEnabled(true);

                picasso.load(imageUrl).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        markerImageMap.put(marker, imageView);
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Failed to fetch image from URL.");
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

            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // Custom view for info window content
                @SuppressLint("InflateParams") LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_info_window, null);

                TextView title = view.findViewById(R.id.title);
                TextView id = view.findViewById(R.id.id);
                TextView address = view.findViewById(R.id.address);
                TextView phone = view.findViewById(R.id.phone);
                TextView coordinates = view.findViewById(R.id.coordinates);

                ImageView image = markerImageMap.get(marker);
                Student student = markerStudentMap.get(marker);

                if (student != null && image != null) {
                    title.setText(student.getName());
                    id.setText("ID: " + student.getStudentId());
                    address.setText(student.getAddress());
                    phone.setText(student.getPhone());
                    coordinates.setText(student.getLatitude() + ", " + student.getLongitude());

                    image.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
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
