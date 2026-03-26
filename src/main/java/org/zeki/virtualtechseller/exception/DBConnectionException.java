package org.zeki.virtualtechseller.exception;

public class DBConnectionException extends Exception {
    public DBConnectionException(String message, Throwable cause) {
        super(message,cause);
    }
}
