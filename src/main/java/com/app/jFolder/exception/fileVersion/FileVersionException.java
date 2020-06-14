package com.app.jFolder.exception.fileVersion;

public class FileVersionException extends RuntimeException {

    public FileVersionException(String message) {
        super(message);
    }

    public FileVersionException(String message, Throwable cause) {
        super(message, cause);
    }

}
