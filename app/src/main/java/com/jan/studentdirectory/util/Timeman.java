package com.jan.studentdirectory.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timeman {

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
