package main.controllers.screens;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import main.controllers.ControlledWindow;
import main.model.Person;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PersonDetailsWindowController implements Initializable, ControlledWindow {

    private List<Person> people = new ArrayList<>();

    @Override public void initialize(final URL location, final ResourceBundle resources) {
        for(Person person : people) {
	    Log.debug(person.getName());
	}
    }

    @Override public void setStage(Stage stage) {

    }

    @Override public void setPassedData(final List<?> passedData) {
        //If people were passed on window creation, add person object to the appropriate field
	for(Object object : passedData) {
	    if(object instanceof Person) {
		people.add((Person) object);
	    }
	}
    }
}
