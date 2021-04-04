package de.michi.clashutils.exceptions;

public class InvalidMessageChannelException extends Exception {

    public InvalidMessageChannelException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidMessageChannelException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
