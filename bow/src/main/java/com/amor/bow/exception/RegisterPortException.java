package com.amor.bow.exception;

/**
 * Created by dylan on 2017/10/3.
 */
public class RegisterPortException extends Exception {
    public RegisterPortException() {
    }

    public RegisterPortException(String message) {
        super(message);
    }

    public RegisterPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterPortException(Throwable cause) {
        super(cause);
    }

    public RegisterPortException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
