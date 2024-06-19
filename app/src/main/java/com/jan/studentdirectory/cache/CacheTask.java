package com.jan.studentdirectory.cache;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.jan.studentdirectory.Logger;
import com.jan.studentdirectory.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

public class CacheTask extends TimerTask {

    private final SQLiteManager sqlManager;
    private final List<Student> students;
    private final Logger logger;
    public CacheTask(SQLiteManager sqlManager, List<Student> students) {
        this.sqlManager = sqlManager;
        this.students = students;
        this.logger = Logger.getLogger();
    }
    @Override
    public void run() {
        logger.logInfoMessage("Caching user data...");
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        for (Student student : students) {
            ContentValues values = getContentValues(student);
            long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
            logger.logInfoMessage("New row created. ID: " + newRowId);
        }
    }

    private ContentValues getContentValues(Student student) {
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME, student.getName());
        values.put(UserContract.UserEntry.COLUMN_NAME_STUDENT_ID, student.getStudentId());
        values.put(UserContract.UserEntry.COLUMN_NAME_ADDRESS, student.getAddress());
        values.put(UserContract.UserEntry.COLUMN_NAME_LATITUDE, student.getLatitude());
        values.put(UserContract.UserEntry.COLUMN_NAME_LONGITUDE, student.getLongitude());
        values.put(UserContract.UserEntry.COLUMN_NAME_PHONE, student.getPhone());
        values.put(UserContract.UserEntry.COLUMN_NAME_TIMESTAMP, getCurrentTimestamp());
        return values;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
