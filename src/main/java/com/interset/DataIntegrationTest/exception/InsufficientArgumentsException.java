package com.interset.DataIntegrationTest.exception;

public class InsufficientArgumentsException extends Exception {

    private static final long serialVersionUID = -2249168204604177837L;

    public InsufficientArgumentsException(String message) {
        super(message);
    }

    public InsufficientArgumentsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}