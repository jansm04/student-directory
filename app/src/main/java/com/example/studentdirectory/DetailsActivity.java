package com.example.studentdirectory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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

    public void handleMapButton(View view) {
        Intent currentIntent = getIntent();
        Intent nextIntent = new Intent(this, MapActivity.class);
        nextIntent.putExtra("latitude", currentIntent.getDoubleExtra("latitude", 0));
        nextIntent.putExtra("longitude", currentIntent.getDoubleExtra("longitude", 0));
        startActivity(nextIntent);
    }
}
