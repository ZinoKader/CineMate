package main.controllers;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.controllers.contract.DetailedView;

/**
 * Making this class abstract allows us to "know" that all of our subclasses definitely implement the interfaces in DetailedView.
 * As such, we can call these methods from our abstract class because we're certain that our subclasses have an implementation of these methods.
 */
public abstract class DetailsWindowBase implements DetailedView {

    protected ApiAdapater apiAdapater;
    protected ApiService apiService;
    protected Stage stage;
    protected ScreenController screenController;

    protected void initialize() {
        apiAdapater = new ApiAdapater();
        apiService = apiAdapater.getService();
	/*
	Ensures that we get our data when the window has been initialized and setPassedData() has been called
	Pretty cool Java 8 thing here, we can directly reference the method if it has no parameters instead of creating
	a lambda expression with empty parameters
	 */
	Platform.runLater(this::delegateSetData);

    }

}
