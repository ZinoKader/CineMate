package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageHelper;
import main.model.Series;

import java.io.IOException;

/**
 * Cell view for series for the main screen listview. Has to extend ListCell.
 */
public class SeriesListViewCell extends JFXListCell<Series> {

	@FXML
	private Label seriesTitle;

	@FXML
	private Label seriesDescription;

	@FXML
	private ImageView seriesImage;

	@FXML
	private HBox seriesContainer;

	private FXMLLoader seriesFxmlLoader;

	private ImageHelper imageHelper = new ImageHelper();

	@Override
	public void updateItem(Series series, boolean empty) {
		super.updateItem(series, empty);

		if(empty || series == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (seriesFxmlLoader == null) {
				seriesFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/series_cell.fxml"));
				seriesFxmlLoader.setController(this);
				try {
					seriesFxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			seriesTitle.setText(series.getTitle());
			seriesDescription.setText(series.getDescription());

			String imageUrl = series.getPosterPath();

            if(imageHelper.isImageCached(imageUrl)) {
                seriesImage.setImage(imageHelper.getCachedImage(imageUrl));
            } else {
                imageHelper.downloadAndSetImage(imageUrl, seriesImage, false);
            }

			setText(null);
			setGraphic(seriesContainer);
		}

	}

}
