package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.helpers.ImageHelper;
import main.helpers.Log;
import main.model.Series;

import java.io.IOException;

/**
 * Cell view for series for the main screen listview. Has to extend ListCell.
 */
public class SeriesListViewCell extends ListCell<Series> {

    @FXML
    private Text title;

    @FXML
    private Text description;

    @FXML
    private ImageView image;

    @FXML
    private HBox container;

    private FXMLLoader fxmlLoader;

    private ImageHelper imageHelper = new ImageHelper();

    @Override
    protected void updateItem(Series series, boolean empty) {
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
