package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageHelper;
import main.model.*;

import java.io.IOException;

/**
 * Cell view for movies and series (MotionPicture). Has to extend ListCell.
 */
public class MotionPictureListViewCell extends JFXListCell<MotionPicture> {

	@FXML
	private Label title;

	@FXML
	private Label character;

	@FXML
	private ImageView image;

	@FXML
	private HBox container;

	private FXMLLoader fxmlLoader;

	private ImageHelper imageHelper = new ImageHelper();

	@Override
	public void updateItem(MotionPicture motionPicture, boolean empty) {
		super.updateItem(motionPicture, empty);

		if(empty || motionPicture == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (fxmlLoader == null) {
				fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/motion_picture_cell.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}



			switch(motionPicture.getMediaType()) {
				//We can't keep a common title-getter since these fields are set automatically and have different serialized names
				case ACCREDITED_MOVIE:
					AccreditedMovie movie = (AccreditedMovie) motionPicture;
					title.setText(movie.getTitle());
					character.setText(movie.getCharacter());
					break;
				case ACCREDITED_SERIES:
					AccreditedSeries series = (AccreditedSeries) motionPicture;
					title.setText(series.getTitle());
					character.setText(series.getCharacter());
					break;
			}

			String imageUrl = motionPicture.getPosterPath();


			//We are utilizing caching to minimize network calls
			if(imageHelper.isImageCached(imageUrl)) {
				image.setImage(imageHelper.getCachedImage(imageUrl));
			} else {
				imageHelper.downloadAndSetImage(imageUrl, image, false);
			}

			setText(null);
			setGraphic(container);
		}

	}
}
