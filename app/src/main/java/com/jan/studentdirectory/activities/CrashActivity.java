package com.jan.studentdirectory.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jan.studentdirectory.R;
import com.jan.studentdirectory.util.Logman;

public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        TextView errorView = findViewById(R.id.errorText);
        String error = getIntent().getStringExtra("error");
        errorView.setText(error);
    }

    public void onCloseButtonClick(View view) {
        Logman.getLogman().logInfoMessage("Closing app...");
        System.exit(0);
    }
}
