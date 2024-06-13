package com.jan.studentdirectory;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Data.db";

    public SQLManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQLHelper.SQL_CREATE_ENTRIES);
            System.out.println("Successfully created new table.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL(SQLHelper.SQL_DELETE_ENTRIES);
                onCreate(db);
                System.out.println("Successfully upgraded database.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
