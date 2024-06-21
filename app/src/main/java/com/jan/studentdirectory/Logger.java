package com.jan.studentdirectory;

import timber.log.Timber;

public class Logger {

    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private Logger() {}

    public void logInfoMessage(String infoMessage) {
        Timber.tag("Info").i(infoMessage);
    }

    public void logErrorMessage(String errorMessage) {
        Timber.tag("Error").e(errorMessage);
    }
}
