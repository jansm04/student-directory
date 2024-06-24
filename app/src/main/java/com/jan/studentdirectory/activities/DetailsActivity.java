package com.jan.studentdirectory.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jan.studentdirectory.R;
import com.jan.studentdirectory.exceptions.PermissionDeniedException;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends SDActivity {

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

    private void init() {
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        setTextView(R.id.name_var, name);

        int studentID = intent.getIntExtra("id", 0);
        setTextView(R.id.id_var, String.valueOf(studentID));

        String address = intent.getStringExtra("address");
        setTextView(R.id.address_var, address);

        double longitude = intent.getDoubleExtra("longitude", 0);
        setTextView(R.id.long_val, String.valueOf(longitude));

        double latitude = intent.getDoubleExtra("latitude", 0);
        setTextView(R.id.lat_val, String.valueOf(latitude));

        loadPhoneNumber(intent);
        loadImage(intent);
    }

    private void loadImage(Intent intent) {
        // view image
        String imageUrl = intent.getStringExtra("image");
        ImageView imageView = findViewById(R.id.imageView);
        Picasso picasso = getPicassoBuild();
        picasso.load(imageUrl).into(imageView);
    }

    private void loadPhoneNumber(Intent intent) {
        // view student phone number
        String phone = intent.getStringExtra("phone");
        TextView phoneText = setTextView(R.id.phone_val, phone);
        phoneText.setOnClickListener(v -> {
            if (!checkPermissions(this, Manifest.permission.CALL_PHONE)) {
                requestPermissions(this, Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL);
            } else {
                startCall();
            }
        });
    }

    private TextView setTextView(int id, String text) {
        TextView textView = findViewById(id);
        textView.setText(text);
        return textView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            onRequestPermissionsResult(requestCode, grantResults, REQUEST_PHONE_CALL, this::startCall);
        } catch (PermissionDeniedException e) {
            e.logErrorMessage();
        }
    }

    private void startCall() {
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
