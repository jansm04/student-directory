package com.jan.studentdirectory.cache.tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.jan.studentdirectory.cache.sqlite.SQLiteManager;
import com.jan.studentdirectory.cache.sqlite.UserContract;
import com.jan.studentdirectory.util.Logman;
import com.jan.studentdirectory.model.Student;
import com.jan.studentdirectory.util.Timeman;

import java.util.List;
import java.util.TimerTask;

public class CacheTask extends TimerTask {

    private final SQLiteManager sqlManager;
    private final List<Student> students;
    private final Logman logman;
    public CacheTask(SQLiteManager sqlManager, List<Student> students) {
        this.sqlManager = sqlManager;
        this.students = students;
        this.logman = Logman.getLogman();
    }
    @Override
    public void run() {
        logman.logInfoMessage("Caching user data...");
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        for (Student student : students) {
            ContentValues values = getContentValues(student);
            long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
            logman.logInfoMessage("New row created. ID: " + newRowId);
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
        values.put(UserContract.UserEntry.COLUMN_NAME_TIMESTAMP, Timeman.getCurrentTimestamp());
        return values;
    }
}
