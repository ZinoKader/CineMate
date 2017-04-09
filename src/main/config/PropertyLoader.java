package main.config;

import com.esotericsoftware.minlog.Log;
import main.CineMateApplication;
import main.constants.TmdbConstants;
import main.exceptions.EmptyValueException;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;

import java.io.*;
import java.util.Properties;

/**
 * Methods in this class are made protected to only allow usage from its subclasses.
 */

abstract class PropertyLoader {

    private static final String PROPERTIES_PATH = "settings.properties";
    private Properties properties = new Properties();

    protected PropertyLoader() {
    }

    protected void initializeProperties() throws PropertyLoadException {

        handlePropertiesFileCreation();

        try {
            properties.load(new FileInputStream(new File(PROPERTIES_PATH)));
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new PropertyLoadException(exception);
        }
    }

    private void handlePropertiesFileCreation() {
        if(getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH) == null) {
            try {
                //Create property file if non-existent
                if(new File(PROPERTIES_PATH).createNewFile()) {
                    Log.debug("Created new properties file.");
                    put(SettingsKey.API_KEY, ""); //add empty API KEY property
                } else {
                    Log.debug("Did not create new properties file. File already exists.");
                }
            } catch (IOException e) {
                Log.debug("Could not create new properties file. " + e.getCause());
                e.printStackTrace();
            }
        }
    }


    protected void put(SettingsKey key, String value) throws IOException {
        properties.setProperty(String.valueOf(key), value);
        properties.store(new FileOutputStream(PROPERTIES_PATH), "");
    }

    protected String get(SettingsKey key) throws EmptyValueException {
        String value = properties.getProperty(String.valueOf(key));
        if(value != null) {
            return value;
        } else {
            throw new EmptyValueException(key);
        }
    }


}
