package com.jan.studentdirectory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostTask extends TimerTask {

    private final SQLManager sqlManager;

    public PostTask(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }
    @Override
    public void run() {
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        String[] projection = {
                UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME,
                UserContract.UserEntry.COLUMN_NAME_STUDENT_ID,
                UserContract.UserEntry.COLUMN_NAME_ADDRESS,
                UserContract.UserEntry.COLUMN_NAME_LATITUDE,
                UserContract.UserEntry.COLUMN_NAME_LONGITUDE,
                UserContract.UserEntry.COLUMN_NAME_PHONE
            };
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, projection, null, null, null, null, null);
        List<Student> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            students.add(new Student(
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_ADDRESS)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PHONE)),
                    null
            ));
        }
        cursor.close();

        ApiService apiService = ApiClient.createService(Properties.USERNAME, Properties.PASSWORD);
        Call<Void> call = apiService.postData(students);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                System.out.println("Successfully posted " + students.size() + " records to API endpoint.");

                // clear cache if data is successfully posted
                int deletedRows = db.delete(UserContract.UserEntry.TABLE_NAME, null, null);
                System.out.println("Successfully deleted " + deletedRows + " rows.");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                System.out.println("An error occurred trying to post the data. As a result, the cache was not yet cleared.");
            }
        });
    }
}
