package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.helpers.ImageHelper;
import main.helpers.Log;
import main.model.Person;

import java.io.IOException;

/**
 * Cell view for people for the main screen listview. Has to extend ListCell.
 */
public class PersonListViewCell extends ListCell<Person> {

    @FXML
    private Text title;

    @FXML
    private Text description;

    @FXML
    private ImageView imageView;

    @FXML
    private HBox container;

    private ImageHelper imageHelper = new ImageHelper();

    public PersonListViewCell() {
	final FXMLLoader mLLoader = new FXMLLoader(getClass().getResource("/fxml/person_cell.fxml"));
	mLLoader.setController(this);

	try {
	    mLLoader.load();
	} catch (IOException e) {
	    Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
	}
    }

    @Override
    protected void updateItem(Person person, boolean empty) {
	super.updateItem(person, empty);

	if(empty || person == null) {
	    setText(null);
	    setGraphic(null);
	} else {

	    title.setText(person.getName());


	    //description.setText(person.getKnownFor());

	    String imageUrl = person.getProfilePath();

	    //We are utilizing caching to minimize network calls
	    if(imageHelper.isImageCached(imageUrl)) {
		imageView.setImage(imageHelper.getCachedImage(imageUrl));
	    } else {
		imageHelper.downloadAndSetImage(imageUrl, imageView);
	    }

	    setText(null);
	    setGraphic(container);
	}

    }

}
