package com.jan.studentdirectory.cache;

import com.jan.studentdirectory.cache.sqlite.SQLiteManager;
import com.jan.studentdirectory.cache.tasks.CacheTask;
import com.jan.studentdirectory.cache.tasks.ClearTask;
import com.jan.studentdirectory.model.Student;
import com.jan.studentdirectory.exceptions.InvalidTimeException;

import java.util.List;
import java.util.Timer;

public class CacheManager {

    private final SQLiteManager sqlManager;
    private final List<Student> students;
    private final Timer cacheTimer;
    private final Timer clearTimer;

    public CacheManager(SQLiteManager sqlManager, List<Student> students) {
        this.sqlManager = sqlManager;
        this.students = students;
        this.cacheTimer = new Timer();
        this.clearTimer = new Timer();
    }

    public void startCachingInterval(long seconds) throws InvalidTimeException {
        if (seconds < 0) {
            throw new InvalidTimeException("Caching interval must be a non-negative integer.");
        }
        long milliseconds = seconds * 1000;
        cacheTimer.schedule(new CacheTask(sqlManager, students), 0, milliseconds);
    }

    public void startClearingInterval(long seconds) throws InvalidTimeException {
        if (seconds < 0) {
            throw new InvalidTimeException("Clearing interval must be a non-negative integer.");
        }
        long milliseconds = seconds * 1000;
        clearTimer.schedule(new ClearTask(sqlManager), 1000, milliseconds);
    }

    public void stopCachingInterval() {
        cacheTimer.cancel();
    }

    public void stopClearingInterval() {
        clearTimer.cancel();
    }
}
