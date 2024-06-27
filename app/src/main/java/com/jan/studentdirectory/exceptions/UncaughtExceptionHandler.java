package com.jan.studentdirectory.exceptions;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.jan.studentdirectory.activities.CrashActivity;
import com.jan.studentdirectory.util.Logman;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;

    public UncaughtExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Intent intent = new Intent(context, CrashActivity.class);
        String errorMessage = e.getMessage();
        intent.putExtra("error", errorMessage);

        context.startActivity(intent);
        Logman.getInstance().logErrorMessage(errorMessage);

        // Terminate the process
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
