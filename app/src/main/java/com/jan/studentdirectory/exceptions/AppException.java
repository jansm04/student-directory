package com.jan.studentdirectory.exceptions;

import com.jan.studentdirectory.Logger;

public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public void logErrorMessage() {
        Logger.getLogger().logErrorMessage(getMessage());
    }
}
