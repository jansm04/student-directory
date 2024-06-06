package com.jan.studentdirectory;

import java.util.TimerTask;

public class CacheTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("caching user data...");
    }
}
