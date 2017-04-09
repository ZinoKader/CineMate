package main.exceptions;

/**
 * This exception is thrown when the requested property could not be accessed.
 */

public class PropertyAccessException extends Exception {

    public PropertyAccessException() {
        super("Could not access user data from the properties file. Was the data stored correctly?");
    }

}

