package com.jan.studentdirectory.exceptions;

import com.jan.studentdirectory.Logman;

public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public void logErrorMessage() {
        Logman.getLogman().logErrorMessage(getMessage());
    }
}
