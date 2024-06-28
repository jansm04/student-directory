package com.jan.studentdirectory.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jan.studentdirectory.exceptions.AppException;
import com.jan.studentdirectory.exceptions.UncaughtExceptionHandler;
import com.jan.studentdirectory.util.Logman;
import com.jan.studentdirectory.util.Timeman;
import com.jan.studentdirectory.exceptions.InvalidTimeException;
import com.jan.studentdirectory.https.ApiClient;
import com.jan.studentdirectory.https.ApiService;
import com.jan.studentdirectory.cache.CacheManager;
import com.jan.studentdirectory.properties.Properties;
import com.jan.studentdirectory.R;
import com.jan.studentdirectory.cache.sqlite.SQLiteManager;
import com.jan.studentdirectory.model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends SDActivity {

    List<Student> students = new ArrayList<>();
    Logman logman = Logman.getInstance();
    CacheManager cacheManager;
    boolean isTracking = false;

    private void populateStudents() {
        try {
            ApiService apiService = ApiClient.createService(Properties.USERNAME, Properties.PASSWORD);
            Call<List<Student>> call = apiService.getStudents();
            call.enqueue(new Callback<List<Student>>() {
                @Override
                public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        logman.logInfoMessage("Successfully fetched student data.");
                        students.clear();
                        students.addAll(response.body());
                        createTable();
                        createCacheManager();
                    } else {
                        Toast.makeText(MainActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable throwable) {
                    logman.logErrorMessage(throwable.getMessage());
                    Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (AppException e) {
            e.logErrorMessage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        moveTaskToBackgroundOnBack();
        Thread thread = new Thread(() -> {
            initFileLogger();
            populateStudents();
        });
        thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        thread.start();
    }

    private void initFileLogger() {
        File directoryForLogs = getExternalFilesDir(null);
        if (directoryForLogs != null) {
            System.setProperty("tinylog.directory", directoryForLogs.getAbsolutePath());

            logman.logInfoMessage("Process started at {}", Timeman.getCurrentTimestamp());
            logman.logInfoMessage("Logs can be found in \"{}\"", directoryForLogs);
        } else {
            logman.logErrorMessage("Null directory for logs.");
        }

    }

    private void createTable() {
        TableLayout table = findViewById(R.id.table);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams iconParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        TableRow.LayoutParams elementParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 6);

        int size = students.size();
        for (int i = 0; i < size; i++) {
            Student student = students.get(i);
            TableRow row = new TableRow(this);
            int paddingHorizontal = dpToPixels(20);
            int paddingVertical = dpToPixels(16);
            row.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            row.setId(i);
            if (i % 2 == 1) {
                row.setBackgroundColor(Color.argb(200, 244, 244, 244));
            }

            ImageView userIcon = new ImageView(this);
            userIcon.setImageResource(R.drawable.user_icon);
            userIcon.setPadding(0, 0, dpToPixels(8), 0);
            row.addView(userIcon, iconParams);

            createTextView(student.getName(), row, elementParams, View.TEXT_ALIGNMENT_TEXT_START);
            createTextView(String.valueOf(student.getStudentId()), row, elementParams, View.TEXT_ALIGNMENT_TEXT_END);

            row.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                packStudent(intent, student);
                packStudentsAndStart(intent);
            });
            table.addView(row, rowParams);
        }
    }

    private int dpToPixels(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private static void packStudent(Intent intent, Student student) {
        intent.putExtra("name", student.getName());
        intent.putExtra("id", student.getStudentId());
        intent.putExtra("address", student.getAddress());
        intent.putExtra("latitude", student.getLatitude());
        intent.putExtra("longitude", student.getLongitude());
        intent.putExtra("phone", student.getPhone());
        intent.putExtra("image", student.getImage());
    }

    private void createTextView(String text, TableRow row, TableRow.LayoutParams elementParams, int alignment) {
        // add name text view
        TextView name = new TextView(this);
        name.setText(text);
        name.setTextAlignment(alignment);
        row.addView(name, elementParams);
    }

    private void createCacheManager() {
        SQLiteManager sqlManager = new SQLiteManager(getApplicationContext());
        cacheManager = new CacheManager(sqlManager, students);
    }

    private void startCacheIntervals() throws InvalidTimeException {
        cacheManager.startCachingInterval(5);
        cacheManager.startClearingInterval(20);
    }

    private void stopCacheIntervals() {
        cacheManager.stopCachingInterval();
        cacheManager.stopClearingInterval();
    }

    @Override
    public void handleMapButton(MenuItem item) {
        Intent intent = new Intent(this, MapActivity.class);
        packStudentsAndStart(intent);
    }

    @Override
    public void handleWebButton(MenuItem item) {
        Intent intent = new Intent(this, WebActivity.class);
        packStudentsAndStart(intent);
    }

    private void packStudentsAndStart(Intent intent) {
        int n = students.size();
        String[] names = new String[n], addresses = new String[n], phones = new String[n], images = new String[n];
        int[] ids = new int[n];
        double[] latitudes = new double[n], longitudes = new double[n];

        for (int i = 0; i < n; i++) {
            Student student = students.get(i);
            names[i] = student.getName();
            ids[i] = student.getStudentId();
            addresses[i] = student.getAddress();
            latitudes[i] = student.getLatitude();
            longitudes[i] = student.getLongitude();
            phones[i] = student.getPhone();
            images[i] = student.getImage();
        }
        intent.putExtra("names", names);
        intent.putExtra("ids", ids);
        intent.putExtra("addresses", addresses);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        intent.putExtra("phones", phones);
        intent.putExtra("images", images);
        startActivity(intent);
    }

    public void handleTrackButton(View view) {
        Button button = findViewById(R.id.track_button);
        if (!isTracking) {
            try {
                startCacheIntervals();
                button.setText(R.string.stop_tracking);
                isTracking = true;
            } catch (InvalidTimeException e) {
                e.logErrorMessage();
            }
        } else {
            stopCacheIntervals();
            button.setText(R.string.start_tracking);
            isTracking = false;
        }
    }
}
