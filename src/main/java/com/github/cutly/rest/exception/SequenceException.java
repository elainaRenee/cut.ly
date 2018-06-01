package com.github.cutly.rest.exception;

public class SequenceException extends RuntimeException {

    private String errorMessage;

    public SequenceException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
