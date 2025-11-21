/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 * Custom exception, utilsed for parameters entered in dialogue structures checking.
 */
public class ValidationException extends RuntimeException {

    // Exception code for handling the exception
    private final int code;

    public ValidationException(String text) {
        // (-1) - let it be a default value
        this(text, -1);
    }

    public ValidationException(String message, int code) {
        super(message);
        if (code < -1) {
            throw new IllegalArgumentException("Illegal code in Validation Exception: " + code);
        }
        this.code = code;
    }

    public ValidationException(String message, Throwable throwable, int code) {
        super(message, throwable);
        if (code < -1) {
            throw new IllegalArgumentException("Illegal code in MyException: " + code);
        }
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
