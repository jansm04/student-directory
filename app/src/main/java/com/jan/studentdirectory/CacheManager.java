package com.jan.studentdirectory;

import java.util.Timer;

public class CacheManager {

    Timer timer = new Timer();
    public void startInterval(int seconds) {
        timer.schedule(new CacheTask(), 0, seconds * 1000L);
    }
}
