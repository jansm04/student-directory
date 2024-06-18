package com.jan.studentdirectory.cache;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.jan.studentdirectory.Logger;
import com.jan.studentdirectory.Properties;
import com.jan.studentdirectory.Student;
import com.jan.studentdirectory.http.ApiClient;
import com.jan.studentdirectory.http.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClearTask extends TimerTask {

    private final SQLiteManager sqlManager;
    private final Logger logger;

    public ClearTask(SQLiteManager sqlManager) {
        this.sqlManager = sqlManager;
        this.logger = Logger.getLogger();
    }
    @Override
    public void run() {
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        String currentTimestamp = getCurrentTimestamp();
        String whereClause = UserContract.UserEntry.COLUMN_NAME_TIMESTAMP + " <= ?";
        String[] whereArgs = { currentTimestamp };
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, null, whereClause, whereArgs, null, null, null, String.valueOf(1000));

        List<Student> students = new ArrayList<>();
        StringBuilder rowIds = new StringBuilder("(");
        while (cursor.moveToNext()) {
            students.add(new Student(
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_ADDRESS)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PHONE)),
                    null,
                    currentTimestamp
            ));
            rowIds.append(cursor.getInt(0)).append(",");
        }
        cursor.close();
        rowIds.deleteCharAt(rowIds.length() - 1).append(")");
        String rowsToDelete = rowIds.toString();

        logger.logPostProcessBeginning(students.size(), currentTimestamp);
        ApiService apiService = ApiClient.createService(Properties.USERNAME, Properties.PASSWORD);
        Call<Void> call = apiService.postStudents(students);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                logger.logSuccessfulPost(students.size(), getCurrentTimestamp());

                // clear cache if data is successfully posted
                String whereClause = "_id IN " + rowsToDelete;
                String[] whereArgs = {};
                int deletedRows = db.delete(UserContract.UserEntry.TABLE_NAME, whereClause, whereArgs);
                logger.logSuccessfulCacheClear(deletedRows);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                logger.logUnsuccessfulPost();
            }
        });
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
