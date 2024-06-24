package com.jan.studentdirectory.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.jan.studentdirectory.exceptions.PermissionDeniedException;

public abstract class SDActivity extends AppCompatActivity {

    public void handleHomeButton(MenuItem item) {}

    public void handleMapButton(MenuItem item) {}

    public void handleWebButton(MenuItem item) {}

    protected void startActivityWithSameData(Class<?> targetActivity) {
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(this, targetActivity);
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            newIntent.putExtras(extras);
        }
        startActivity(newIntent);
    }

    protected void moveTaskToBackgroundOnBack() {
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
    }

    protected boolean checkPermissions(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermissions(Activity activity, String permission, int permissionID) {
        ActivityCompat.requestPermissions(activity, new String[]{ permission }, permissionID);
    }

    protected void onRequestPermissionsResult(int requestCode, int[] grantResults, int permissionID, GrantedCallback grantedCallback) throws PermissionDeniedException {
        if (requestCode == permissionID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                grantedCallback.run();
            } else {
                String message = "Permission denied.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                throw new PermissionDeniedException(message);
            }
        }
    }

}
