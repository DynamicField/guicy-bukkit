package com.github.jeuxjeux20.guicybukkit.command;

/**
 * This exception is thrown when a command has not been found.
 */
public class CommandNotFoundException extends RuntimeException {

    public CommandNotFoundException() {
    }

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }
}
