package com.jan.studentdirectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public abstract class TabHandler extends AppCompatActivity {

    public void handleHomeButton(MenuItem item) {}

    public void handleMapButton(MenuItem item) {}

    public void handleWebButton(MenuItem item) {}

    public void startActivityWithSameData(Class<?> targetActivity) {
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(this, targetActivity);
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            newIntent.putExtras(extras);
        }
        startActivity(newIntent);
    }

    public void moveTaskToBackgroundOnBack() {
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
    }

}
