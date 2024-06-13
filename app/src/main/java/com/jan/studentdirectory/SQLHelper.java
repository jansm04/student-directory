package com.jan.studentdirectory;

public class SQLHelper {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserContract.UserEntry.COLUMN_NAME_STUDENT_NAME + " TEXT," +
                    UserContract.UserEntry.COLUMN_NAME_STUDENT_ID + " INTEGER," +
                    UserContract.UserEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    UserContract.UserEntry.COLUMN_NAME_LATITUDE + " REAL," +
                    UserContract.UserEntry.COLUMN_NAME_LONGITUDE + " REAL," +
                    UserContract.UserEntry.COLUMN_NAME_PHONE + " TEXT," +
                    UserContract.UserEntry.COLUMN_NAME_TIMESTAMP + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;

 }
