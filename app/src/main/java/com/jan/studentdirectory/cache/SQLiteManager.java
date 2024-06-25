package com.jan.studentdirectory.cache;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jan.studentdirectory.Logman;

public class SQLiteManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Data.db";
    private final Logman logman;

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.logman = Logman.getLogman();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQLiteHelper.SQL_CREATE_ENTRIES);
            logman.logInfoMessage("Successfully created new table.");
        } catch (SQLException e) {
            logman.logErrorMessage(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL(SQLiteHelper.SQL_DELETE_ENTRIES);
                onCreate(db);
                logman.logInfoMessage("Successfully upgraded database.");
            } catch (SQLException e) {
                logman.logErrorMessage(e.getMessage());
            }
        }
    }
}
