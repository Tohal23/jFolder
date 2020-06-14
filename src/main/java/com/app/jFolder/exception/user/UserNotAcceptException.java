package com.app.jFolder.exception.user;

public class UserNotAcceptException extends UserException {

    public UserNotAcceptException(String message) {
        super(message);
    }

    public UserNotAcceptException(String message, Throwable cause) {
        super(message, cause);
    }

}
