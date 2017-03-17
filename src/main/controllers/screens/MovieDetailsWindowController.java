package main.controllers.screens;

import javafx.fxml.Initializable;
import main.controllers.ControlledWindow;
import main.model.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovieDetailsWindowController implements Initializable, ControlledWindow {

    private List<Movie> movies = new ArrayList<>();

    @Override public void initialize(final URL location, final ResourceBundle resources) {

    }

    private void getDetailedData() {

    }

    @Override public void setPassedData(final List<?> passedData) {

	for(Object object : passedData) {
	    if(object instanceof Movie) {
		movies.add((Movie) object);
	    }
	}
    }
}
