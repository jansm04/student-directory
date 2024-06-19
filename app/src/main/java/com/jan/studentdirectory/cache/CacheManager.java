package com.jan.studentdirectory.cache;

import com.jan.studentdirectory.Student;

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

    public void startCachingInterval(int seconds) {
        cacheTimer.schedule(new CacheTask(sqlManager, students), 0, seconds * 1000L);
    }

    public void startClearingInterval(int seconds) {
        clearTimer.schedule(new ClearTask(sqlManager), 1000, seconds * 1000L);
    }

    public void stopCachingInterval() {
        cacheTimer.cancel();
    }

    public void stopClearingInterval() {
        clearTimer.cancel();
    }
}
