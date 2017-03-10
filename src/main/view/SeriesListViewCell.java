package main.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.model.Series;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SeriesListViewCell extends ListCell<Series> {

    @FXML
    private Text title;

    @FXML
    private Text description;

    @FXML
    private ImageView image;

    @FXML
    private HBox container;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Series series, boolean empty) {
	super.updateItem(series, empty);

	if(empty || series == null) {
	    setText(null);
	    setGraphic(null);
	} else {
	    if (mLLoader == null) {
		mLLoader = new FXMLLoader(getClass().getResource("/fxml/series_cell.fxml"));
		mLLoader.setController(this);

		try {
		    mLLoader.load();
		} catch (IOException e) {
		    e.printStackTrace();
		}

	    }

	    title.setText(series.getTitle());
	    description.setText(series.getDescription());

	    String imageUrl = series.getPosterPath();

	    //This task will ensure we run the downloading of the image in a background thread
	    Task<Void> setImageTask = new Task<Void>() {
		@Override protected Void call() throws Exception {
		    try(InputStream in = new URL(imageUrl).openStream()) {
			image.setImage(new Image(in));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    return null;
		}};

	    //We run each task like this in a new thread to ensure maximum performance
	    new Thread(setImageTask).start();

	    setText(null);
	    setGraphic(container);
	}

    }

}
