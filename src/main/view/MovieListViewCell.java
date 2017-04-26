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
    public void updateItem(Movie movie, boolean empty) {
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
            if(movie.getDescription().isEmpty()) {
                description.setText("Movie description not available yet.");
            } else {
                description.setText(movie.getDescription());
            }

            String imageUrl = movie.getPosterPath();



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
