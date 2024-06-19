package com.jan.studentdirectory;

import android.util.Log;

public class Logger {

    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private Logger() {};

    public void logInfoMessage(String infoMessage) {
        Log.i("Info", infoMessage);
    }

    public void logErrorMessage(String errorMessage) {
        Log.e("Error", errorMessage);
    }
}
