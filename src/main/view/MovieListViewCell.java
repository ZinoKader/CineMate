package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MovieListViewCell extends ListCell<Movie> {

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
    protected void updateItem(Movie movie, boolean empty) {
          super.updateItem(movie, empty);

          if(empty || movie == null) {
              setText(null);
              setGraphic(null);
          } else {
              if (mLLoader == null) {
                  mLLoader = new FXMLLoader(getClass().getResource("/fxml/movie_cell.fxml"));
                  mLLoader.setController(this);

                  try {
                      mLLoader.load();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }

              }

              title.setText(movie.getTitle());
              description.setText(movie.getDescription());

              String imageUrl = movie.getPosterPath();
	      try(InputStream in = new URL(imageUrl).openStream()) {
		  image.setImage(new Image(in));
	      } catch (IOException e) {
		  e.printStackTrace();
	      }

              setText(null);
              setGraphic(container);
          }

      }

}
