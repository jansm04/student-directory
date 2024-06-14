package com.jan.studentdirectory.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jan.studentdirectory.Properties;
import com.jan.studentdirectory.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DetailsActivity extends TabHandler {

    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    public void init() {
        Intent intent = getIntent();

        // view name
        String name = intent.getStringExtra("name");
        TextView nameText = findViewById(R.id.name_var);
        nameText.setText(name);

        // view student ID
        int studentID = intent.getIntExtra("id", 0);
        TextView idText = findViewById(R.id.id_var);
        idText.setText(String.valueOf(studentID));

        // view student address
        String address = intent.getStringExtra("address");
        TextView addressText = findViewById(R.id.address_var);
        addressText.setText(address);

        // view student longitude
        double longitude = intent.getDoubleExtra("longitude", 0);
        TextView longitudeText = findViewById(R.id.long_val);
        longitudeText.setText(String.valueOf(longitude));

        // view student latitude
        double latitude = intent.getDoubleExtra("latitude", 0);
        TextView latitudeText = findViewById(R.id.lat_val);
        latitudeText.setText(String.valueOf(latitude));

        // view student phone number
        String phone = intent.getStringExtra("phone");
        TextView phoneText = findViewById(R.id.phone_val);
        phoneText.setText(phone);
        phoneText.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailsActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                startCall();
            }
        });

        // view image
        String imageUrl = intent.getStringExtra("image");
        ImageView imageView = findViewById(R.id.imageView);
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

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        System.out.println(imageUrl);
        picasso.load(imageUrl).into(imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startCall() {
        TextView phoneText = findViewById(R.id.phone_val);
        String phone = phoneText.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    @Override
    public void handleHomeButton(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleMapButton(MenuItem item) {
        startActivityWithSameData(MapActivity.class);
    }

    @Override
    public void handleWebButton(MenuItem item) {
        startActivityWithSameData(WebActivity.class);
    }
}
