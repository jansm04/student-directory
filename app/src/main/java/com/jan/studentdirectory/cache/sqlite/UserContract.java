package com.jan.studentdirectory.cache.sqlite;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "data_v2";
        public static final String COLUMN_NAME_STUDENT_NAME = "name";
        public static final String COLUMN_NAME_STUDENT_ID = "student_id";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
