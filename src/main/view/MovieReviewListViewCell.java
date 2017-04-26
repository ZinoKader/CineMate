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
	private Label reviewAuthor;

	@FXML
	private Label reviewContent;

	@FXML
	private VBox reviewContainer;

	private FXMLLoader reviewFxmlLoader;


	@Override
    public void updateItem(final MovieReview review, final boolean empty) {
		super.updateItem(review, empty);

		if (empty || review == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (reviewFxmlLoader == null) {
				reviewFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/movie_review_cell.fxml"));
				reviewFxmlLoader.setController(this);
				try {
					reviewFxmlLoader.load();
				} catch (IOException e) {
					Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
				}
			}

			reviewAuthor.setText(review.getAuthor());
			reviewContent.setText(review.getContent());

			setText(null);
			setGraphic(reviewContainer);
		}

	}

}
