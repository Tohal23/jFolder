package com.app.jFolder.exception.user;

public class UserEmailAlreadyExistsException extends UserException {

    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
