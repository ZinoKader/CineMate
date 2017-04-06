package main.config;

import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;

import java.io.IOException;
import java.util.Properties;

/**
 * Methods in this class are made protected to only allow usage from its subclasses.
 */

public abstract class PropertyLoader {

    public static final String PROPERTIES_PATH = "config/settings.properties";
    private Properties properties = new Properties();

    protected PropertyLoader() {
    }

    protected void initializeProperties() throws PropertyLoadException {
        try {
            properties.load((getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH)));
        } catch (IOException exception) {
            throw new PropertyLoadException(exception);
        }
    }


    protected void store(SettingsKey key, String value) {
        properties.put(key, value);
    }

    protected String get(SettingsKey key) throws PropertyAccessException {
        String value = String.valueOf(properties.get(key));
        if(value != null) {
            return String.valueOf(properties.get(key));
        } else {
            throw new PropertyAccessException();
        }
    }


}
