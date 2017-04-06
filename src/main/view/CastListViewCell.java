package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.helpers.ImageHelper;
import main.model.Cast;

import java.io.IOException;

public class CastListViewCell extends JFXListCell<Cast> {

	@FXML
	private ImageView castImage;

	@FXML
	private Label castName;

	@FXML
	private Label castCharacter;

	@FXML
	private VBox container;

	private FXMLLoader fxmlLoader;

	private ImageHelper imageHelper = new ImageHelper();

	@Override public void updateItem(final Cast cast, final boolean empty) {
		super.updateItem(cast, empty);

		if (empty || cast == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (fxmlLoader == null) {
				fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/cast_cell.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			castName.setText(cast.getName());
			castCharacter.setText("Played: " + cast.getCharacter());

			String imageUrl = cast.getProfilePath();

			//We are utilizing caching to minimize network calls
			if (imageHelper.isImageCached(imageUrl)) {
				castImage.setImage(imageHelper.getCachedImage(imageUrl));
			} else {
				imageHelper.downloadAndSetImage(imageUrl, castImage, true);
			}

			setText(null);
			setGraphic(container);
		}

	}

}
