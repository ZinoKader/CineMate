package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.helpers.ImageCache;
import main.model.Cast;

import java.io.IOException;

/**
 * Cell view for cast
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class CastListViewCell extends JFXListCell<Cast> {

	@FXML
	private ImageView castImage;

	@FXML
	private Label castName;

	@FXML
	private Label castCharacter;

	@FXML
	private VBox castContainer;

	private FXMLLoader castFxmlLoader;

	private ImageCache imageCache = new ImageCache();

	@Override
    public void updateItem(final Cast cast, final boolean empty) {
		super.updateItem(cast, empty);

		if (empty || cast == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (castFxmlLoader == null) {
				castFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/cast_cell.fxml"));
				castFxmlLoader.setController(this);
				try {
					castFxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			castName.setText(cast.getName());
			castCharacter.setText(cast.getCharacter());

			String imageUrl = cast.getProfilePath();

            if(imageCache.isImageCached(imageUrl)) {
                castImage.setImage(imageCache.getCachedImage(imageUrl));
            } else {
                imageCache.downloadAndSetImage(imageUrl, castImage, true);
            }

			setText(null);
			setGraphic(castContainer);
		}

	}

}
