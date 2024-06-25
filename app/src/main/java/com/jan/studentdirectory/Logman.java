package com.jan.studentdirectory;

import org.tinylog.Logger;

public class Logman {

    private static Logman logman;

    public static Logman getLogman() {
        if (logman == null) {
            logman = new Logman();
        }
        return logman;
    }

    private Logman() {}

    public void logInfoMessage(String infoMessage) {
        Logger.info(infoMessage);
    }

    public void logErrorMessage(String errorMessage) {
        Logger.error(errorMessage);
    }
}
