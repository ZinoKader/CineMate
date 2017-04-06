package main.exceptions;


/**
 * This exception is thrown when the properties file could not be loaded at all.
 */
public class PropertyLoadException extends Exception {

    public PropertyLoadException(Throwable cause) {
        super("The properties file was not loaded correctly.", cause);
    }

}