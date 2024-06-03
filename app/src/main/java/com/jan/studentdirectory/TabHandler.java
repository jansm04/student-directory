package com.jan.studentdirectory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TabHandler extends AppCompatActivity {

    public void startActivityWithSameData(Class<?> targetActivity) {
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(this, targetActivity);
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            newIntent.putExtras(extras);
        }
        startActivity(newIntent);
    }
}
