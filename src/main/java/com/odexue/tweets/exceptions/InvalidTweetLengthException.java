package com.odexue.tweets.exceptions;

public class InvalidTweetLengthException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Tweet length character must not be more than 160";

    public InvalidTweetLengthException(String message) {
        super(message);
    }
}
