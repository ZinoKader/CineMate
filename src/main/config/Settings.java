package main.config;

import main.exceptions.PropertyAccessException;

/**
 *
 */
public interface Settings {
    public String getApiKey() throws PropertyAccessException;
    public void setApiKey(String apiKey);
}

