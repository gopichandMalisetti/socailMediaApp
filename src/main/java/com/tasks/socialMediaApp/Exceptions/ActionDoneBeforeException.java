package com.tasks.socialMediaApp.Exceptions;

public class ActionDoneBeforeException extends RuntimeException {
    public ActionDoneBeforeException(String message) {
        super(message);
    }
}
