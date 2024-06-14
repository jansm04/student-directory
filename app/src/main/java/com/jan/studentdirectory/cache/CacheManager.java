package com.jan.studentdirectory.cache;

import com.jan.studentdirectory.Student;

import java.util.List;
import java.util.Timer;

public class CacheManager {

    private final SQLManager sqlManager;
    private final List<Student> students;

    public CacheManager(SQLManager sqlManager, List<Student> students) {
        this.sqlManager = sqlManager;
        this.students = students;
    }

    public void startCachingInterval(int seconds) {
        Timer timer = new Timer();
        timer.schedule(new CacheTask(sqlManager, students), 0, seconds * 1000L);
    }

    public void startClearingInterval(int seconds) {
        Timer timer = new Timer();
        timer.schedule(new PostTask(sqlManager), 1000, seconds * 1000L);
    }
}
