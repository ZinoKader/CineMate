package main.exceptions;

/**
 * This exception is thrown when the requested property could not be accessed.
 */

public class PropertyAccessException extends Exception {

    public PropertyAccessException() {
        super("Could not access user data. The properties were not loaded correctly.");
    }

}

