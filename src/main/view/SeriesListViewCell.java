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
    private Label title;

    @FXML
    private Label description;

    @FXML
    private ImageView image;

    @FXML
    private HBox container;

    private FXMLLoader fxmlLoader;

    private ImageHelper imageHelper = new ImageHelper();

    @Override
    public void updateItem(Series series, boolean empty) {
	super.updateItem(series, empty);

	if(empty || series == null) {
	    setText(null);
	    setGraphic(null);
	} else {
	    if (fxmlLoader == null) {
		fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/series_cell.fxml"));
		fxmlLoader.setController(this);
		try {
		    fxmlLoader.load();
		} catch (IOException e) {
		    Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
		}
	    }

	    title.setText(series.getTitle());
	    description.setText(series.getDescription());

	    String imageUrl = series.getPosterPath();

	    //We are utilizing caching to minimize network calls
	    if(imageHelper.isImageCached(imageUrl)) {
		image.setImage(imageHelper.getCachedImage(imageUrl));
	    } else {
		imageHelper.downloadAndSetImage(imageUrl, image);
	    }

	    setText(null);
	    setGraphic(container);
	}

    }

}
