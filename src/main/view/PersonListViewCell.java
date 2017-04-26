package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageHelper;
import main.model.Person;

import java.io.IOException;

/**
 * Cell view for people for the main screen listview. Has to extend ListCell.
 */
public class PersonListViewCell extends JFXListCell<Person> {

	@FXML
	private Label personTitle;

	@FXML
	private Label personDescription;

	@FXML
	private ImageView personImage;

	@FXML
	private HBox personContainer;

	private FXMLLoader personFxmlLoader;

	private ImageHelper imageHelper = new ImageHelper();


	@Override
	public void updateItem(Person person, boolean empty) {
		super.updateItem(person, empty);

		if(empty || person == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (personFxmlLoader == null) {
				personFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/person_cell.fxml"));
				personFxmlLoader.setController(this);
				try {
					personFxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			personTitle.setText(person.getName());

			String imageUrl = person.getProfilePath();

            if(imageHelper.isImageCached(imageUrl)) {
                personImage.setImage(imageHelper.getCachedImage(imageUrl));
            } else {
                imageHelper.downloadAndSetImage(imageUrl, personImage, false);
            }

			setText(null);
			setGraphic(personContainer);
		}

	}

}
