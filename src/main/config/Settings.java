package main.config;

import main.exceptions.EmptyValueException;
import main.exceptions.PropertyAccessException;

import java.io.IOException;

/**
 *
 */
public interface Settings {
    String getApiKey() throws PropertyAccessException, EmptyValueException;
    void setApiKey(String apiKey) throws IOException;
}

