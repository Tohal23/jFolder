package com.app.jFolder.exception.fileVersion;

public class FileVersionExistException extends FileVersionException {

    public FileVersionExistException(String message) {
        super(message);
    }

    public FileVersionExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
