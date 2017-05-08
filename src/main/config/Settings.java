package main.config;

import main.exceptions.EmptyValueException;

import java.io.IOException;

/**
 * Settings interface generalized for all settings types
 */
@SuppressWarnings("unused")
public interface Settings {
    String getApiKey() throws EmptyValueException;
    void setApiKey(String apiKey) throws IOException;
}

