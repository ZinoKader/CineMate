package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageCache;
import main.model.AccreditedMovie;
import main.model.AccreditedSeries;
import main.model.MotionPicture;

import java.io.IOException;

/**
 * Cell view for movies and series (MotionPicture). Has to extend ListCell.
 */
public class MotionPictureListViewCell extends JFXListCell<MotionPicture> {

	@FXML
	private Label mpTitle;

	@FXML
	private Label mpCharacter;

	@FXML
	private ImageView mpImage;

	@FXML
	private HBox mpContainer;

	private FXMLLoader mpFxmlLoader;

	private ImageCache imageCache = new ImageCache();

	@Override
	public void updateItem(MotionPicture motionPicture, boolean empty) {
		super.updateItem(motionPicture, empty);

		if(empty || motionPicture == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (mpFxmlLoader == null) {
				mpFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/motion_picture_cell.fxml"));
				mpFxmlLoader.setController(this);
				try {
					mpFxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}



			switch(motionPicture.getMediaType()) {
				//We can't keep a common superclass between accredited types
                //since these fields are set automatically and have different serialized names
				case ACCREDITED_MOVIE:
					AccreditedMovie movie = (AccreditedMovie) motionPicture;
					mpTitle.setText(movie.getTitle());
					mpCharacter.setText(movie.getCharacter());
					break;
				case ACCREDITED_SERIES:
					AccreditedSeries series = (AccreditedSeries) motionPicture;
					mpTitle.setText(series.getTitle());
					mpCharacter.setText(series.getCharacter());
					break;
			}

			String imageUrl = motionPicture.getPosterPath();


            if(imageCache.isImageCached(imageUrl)) {
                mpImage.setImage(imageCache.getCachedImage(imageUrl));
            } else {
                imageCache.downloadAndSetImage(imageUrl, mpImage, false);
            }

			setText(null);
			setGraphic(mpContainer);
		}

	}
}
