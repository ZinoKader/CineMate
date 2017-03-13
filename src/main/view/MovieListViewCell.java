package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.helpers.ImageHelper;
import main.helpers.Log;
import main.model.Movie;

import java.io.IOException;

/**
 * Cell view for movies for the main screen listview. Has to extend ListCell.
 */
public class MovieListViewCell extends ListCell<Movie> {

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
    protected void updateItem(Movie movie, boolean empty) {
	super.updateItem(movie, empty);

	if(empty || movie == null) {
	    setText(null);
	    setGraphic(null);
	} else {
	    if (fxmlLoader == null) {
		fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/movie_cell.fxml"));
		fxmlLoader.setController(this);
		try {
		    fxmlLoader.load();
		} catch (IOException e) {
		    Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
		}
	    }

	    title.setText(movie.getTitle());
	    description.setText(movie.getDescription());

	    String imageUrl = movie.getPosterPath();

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
