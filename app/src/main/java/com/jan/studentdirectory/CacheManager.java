package com.jan.studentdirectory;

import java.util.List;
import java.util.Timer;

public class CacheManager {

    private final SQLManager sqlManager;
    private final List<Student> students;

    public CacheManager(SQLManager sqlManager, List<Student> students) {
        this.sqlManager = sqlManager;
        this.students = students;
    }

    Timer timer = new Timer();
    public void startInterval(int seconds) {
        timer.schedule(new CacheTask(sqlManager, students), 0, seconds * 1000L);
    }
}
