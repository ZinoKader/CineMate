package main.exceptions;


import main.config.PropertyKey;

/**
 * This exception is thrown when a null value is returned from a property get operation
 */
public class EmptyValueException extends Exception {

    public EmptyValueException(PropertyKey key) {
        super("Could not get value from key: " + key);
    }

}