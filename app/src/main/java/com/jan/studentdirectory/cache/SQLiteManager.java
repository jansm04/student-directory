package com.jan.studentdirectory.cache;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jan.studentdirectory.Logger;

public class SQLiteManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Data.db";
    private final Logger logger;

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.logger = Logger.getLogger();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQLiteHelper.SQL_CREATE_ENTRIES);
            logger.logSuccessfulTableCreation();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL(SQLiteHelper.SQL_DELETE_ENTRIES);
                onCreate(db);
                logger.logSuccessfulDatabaseUpgrade();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
