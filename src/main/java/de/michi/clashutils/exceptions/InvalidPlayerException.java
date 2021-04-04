package de.michi.clashutils.exceptions;

public class InvalidPlayerException extends Exception{

    public InvalidPlayerException() {
        super("This player does not exist");
    }
}
