package com.jan.studentdirectory;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends TabHandler {

    List<Student> students = new ArrayList<>();

    private void populateStudents() {
        ApiService apiService = ApiClient.createService(Properties.USERNAME, Properties.PASSWORD);
        Call<List<Student>> call = apiService.getStudents();

        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    students.clear();
                    students.addAll(response.body());
                    createTable();
                    createCache();
                } else {
                    Toast.makeText(MainActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable throwable) {
                System.out.println(throwable.getMessage());
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        populateStudents();
    }

    private void createTable() {
        TableLayout table = findViewById(R.id.table);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams elementParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            TableRow row = new TableRow(this);
            row.setPadding(8, 8, 8, 8);
            row.setId(i);

            // add name text view
            TextView name = new TextView(this);
            name.setText(student.getName());
            row.addView(name, elementParams);

            // add student ID text view
            TextView id = new TextView(this);
            id.setText(String.valueOf(student.getStudentId()));
            row.addView(id, elementParams);

            // add details button
            Button details = getButton(student);
            row.addView(details, elementParams);

            table.addView(row, rowParams);
        }
    }

    private void createCache() {
        SQLManager sqlManager = new SQLManager(getApplicationContext());
        clearCache(sqlManager);
        CacheManager cacheManager = new CacheManager(sqlManager, students);
        cacheManager.startInterval(5);
    }

    private void clearCache(SQLManager sqlManager) {
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        String selection = UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME + " LIKE ?";
        String[] selectionArgs = { "test_name" };
        int deletedRows = db.delete(UserContract.UserEntry.TABLE_NAME, selection, selectionArgs);
        System.out.println("Successfully deleted " + deletedRows + " rows.");
    }

    @NonNull
    private Button getButton(Student student) {
        Button details = new Button(this);
        details.setText(R.string.details);
        details.setTextSize(12);
        details.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("name", student.getName());
            intent.putExtra("id", student.getStudentId());
            intent.putExtra("address", student.getAddress());
            intent.putExtra("latitude", student.getLatitude());
            intent.putExtra("longitude", student.getLongitude());
            intent.putExtra("phone", student.getPhone());
            intent.putExtra("image", student.getImage());
            packStudentsAndStart(intent);
        });
        return details;
    }

    public void handleHomeButton(MenuItem item) {
        // do nothing
    }

    public void handleMapButton(MenuItem item) {
        Intent intent = new Intent(this, MapActivity.class);
        packStudentsAndStart(intent);
    }

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
}
