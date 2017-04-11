package main.view;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.model.MovieReview;

import java.io.IOException;

public class MovieReviewListViewCell extends JFXListCell<MovieReview> {

	@FXML
	private Label author;

	@FXML
	private Label content;

	@FXML
	private VBox container;

	private FXMLLoader fxmlLoader;


	@Override
    public void updateItem(final MovieReview review, final boolean empty) {
		super.updateItem(review, empty);

		if (empty || review == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (fxmlLoader == null) {
				fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/movie_review_cell.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			author.setText(review.getAuthor());
			content.setText(review.getContent());

			setText(null);
			setGraphic(container);
		}

	}

}
