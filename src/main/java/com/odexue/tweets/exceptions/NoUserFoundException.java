package com.odexue.tweets.exceptions;

public class NoUserFoundException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "The requested user is not found in the database.";

    public NoUserFoundException(String message) {
        super(message);
    }

}
