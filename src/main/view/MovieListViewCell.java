package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageHelper;
import main.model.Movie;

import java.io.IOException;

/**
 * Cell view for movies for the main screen listview. Has to extend ListCell.
 */
public class MovieListViewCell extends JFXListCell<Movie> {

    @FXML
    private Label movieTitle;

    @FXML
    private Label movieDescription;

    @FXML
    private ImageView movieImage;

    @FXML
    private HBox movieContainer;

    private FXMLLoader movieFxmlLoader;

    private ImageHelper imageHelper = new ImageHelper();

    @Override
    public void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if(empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (movieFxmlLoader == null) {
                movieFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/movie_cell.fxml"));
                movieFxmlLoader.setController(this);
                try {
                    movieFxmlLoader.load();
                } catch (IOException e) {
                    Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
                }
            }

            movieTitle.setText(movie.getTitle());
            if(movie.getDescription().isEmpty()) {
                movieDescription.setText("Movie movieDescription not available yet.");
            } else {
                movieDescription.setText(movie.getDescription());
            }

            String imageUrl = movie.getPosterPath();


            if(imageHelper.isImageCached(imageUrl)) {
                movieImage.setImage(imageHelper.getCachedImage(imageUrl));
            } else {
                imageHelper.downloadAndSetImage(imageUrl, movieImage, false);
            }


            setText(null);
            setGraphic(movieContainer);
        }

    }
}
