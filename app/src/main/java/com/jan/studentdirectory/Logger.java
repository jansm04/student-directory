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

    public void logSuccessfulTableCreation() {
        Log.i("Info", "Successfully created new table.");
    }

    public void logSuccessfulDatabaseUpgrade() {
        Log.i("Info", "Successfully upgraded database.");
    }

    public void logCacheProcessBeginning() {
        Log.i("Info", "Caching user data...");
    }

    public void logRecordAddedToCache(long newRowId) {
        Log.i("Info", "New row created. ID: " + newRowId);
    }

    public void logPostProcessBeginning(int size, String currentTimestamp) {
        Log.i("Info", "Posting " + size + " records to API endpoint at " + currentTimestamp);
    }

    public void logSuccessfulPost(int size, String currentTimestamp) {
        Log.i("Info", "Successfully posted " + size + " records to API endpoint at " + currentTimestamp);
    }

    public void logSuccessfulCacheClear(int deletedRows) {
        Log.i("Info", "Successfully deleted " + deletedRows + " rows.");
    }

    public void logUnsuccessfulPost() {
        Log.e("Error", "An error occurred trying to post the data. As a result, the cache was not yet cleared.");
    }

    public void logUnsuccessfulFetch(String errorMessage) {
        Log.e("Error", errorMessage);
    }

    public void logSuccessfulFetch() {
        Log.i("Info", "Successfully fetched student data.");
    }

    public void logUnsuccessfulImageLoad() {
        Log.e("Error", "Failed to fetch image from URL.");
    }

    public void logNullLocation() {
        Log.e("Error", "Null location.");
    }

    public void logSuccessfulLocation(double latitude, double longitude) {
        Log.e("Error", "Location: " + latitude + ", " + longitude);
    }
}
