package com.odexue.tweets.exceptions;

public class InvalidUserException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Invalid requested user";

    public InvalidUserException(String message) {
        super(message);
    }
}
