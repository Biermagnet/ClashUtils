package de.michi.clashutils.exceptions;

public class InvalidClanException extends Exception {

    public InvalidClanException() {
        super("This clan does not exist.");
    }
}
