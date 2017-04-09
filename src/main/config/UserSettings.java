package main.config;

import main.exceptions.EmptyValueException;
import main.exceptions.PropertyLoadException;

import java.io.IOException;

/**
 * UserSettings expose simplified methods that it inherits from PropertyLoader.
 * When UserSettings is initialized, it also initializes its superclass, propertyloader,
 * and throws an exception if the properties could not be loaded.
 * This lets the user define an instance of UserSettings before initializing it,
 * and also gives some assurance that the getters and setters are going to work.
 */

public class UserSettings extends PropertyLoader implements Settings {

    public UserSettings() throws PropertyLoadException {
        initializeProperties();
    }

    @Override
    public void setApiKey(String apiKey) throws IOException {
        put(SettingsKey.API_KEY, apiKey);
    }

    @Override
    public String getApiKey() throws EmptyValueException {
        return get(SettingsKey.API_KEY);
    }


}
