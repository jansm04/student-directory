package com.jan.studentdirectory.exceptions;

import com.jan.studentdirectory.util.Logman;

public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public void logErrorMessage() {
        Logman.getInstance().logErrorMessage(getMessage());
    }
}
