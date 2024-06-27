package com.jan.studentdirectory.util;

import org.tinylog.Logger;

public class Logman {

    private static Logman logman;

    public static Logman getInstance() {
        if (logman == null) {
            logman = new Logman();
        }
        return logman;
    }

    private Logman() {}

    public void logInfoMessage(String infoMessage) {
        Logger.info(infoMessage);
    }

    public void logInfoMessage(String infoMessage, Object... arguments) {
        Logger.info(infoMessage, arguments);
    }

    public void logErrorMessage(String errorMessage) {
        Logger.error(errorMessage);
    }
}
