package main.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import main.constants.TmdbConstants;
import main.controllers.contract.TrailerView;
import main.model.Cast;
import main.model.MotionPicture;
import main.view.CastListViewCell;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A specialized DetailsWindowBase for MotionPicture-related window-controllers
 *
 * Holds shared methods between motionpicture-related windows that have enough in common to warrant a generalization.
 * For now, MovieDetailsWindowController and SeriesDetailsWindowController extend this class and make use of
 * its generalized methods that they both share.
 */
public abstract class DetailsMotionPictureWindowBase extends DetailsWindowBase implements TrailerView {


    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        super.initialize(location, resources);

        //Listen to window closing events, on closing, pause the potentially playing video
        Platform.runLater(() -> stage.setOnCloseRequest(closeEvent -> pauseVideo()) );
    }

    protected void setCast(MotionPicture motionPicture, JFXListView<Cast> castListView) {
        ObservableList<Cast> castList = FXCollections.observableArrayList();
        castList.setAll(motionPicture.getCast());
        castListView.setItems(castList);
        castListView.setCellFactory(listView -> new CastListViewCell());
    }

    //set star ratings, show tooltip on hover
    protected void setRatings(MotionPicture motionPicture, HBox targetNode, Tooltip ratingToolTip) {

        String rating = motionPicture.getAverageRating();

        Rating ratingsElement = new Rating();
        ratingsElement.setPartialRating(true);
        ratingsElement.setMouseTransparent(true); //disable clicking/rating
        ratingsElement.setUpdateOnHover(false);
        ratingsElement.setRating(Double.parseDouble(rating) / 2);
        ratingsElement.setMax(TmdbConstants.MAX_AVERAGE_RATING / 2);
        targetNode.getChildren().add(ratingsElement);

        double adjustedRating = Double.parseDouble(rating) / 2;
        ratingToolTip.setText(String.valueOf(adjustedRating));
        Tooltip.install(targetNode, ratingToolTip);
    }

    protected void setTrailer(MotionPicture motionPicture) {
        getTrailerView().getEngine().load(motionPicture.getTrailerUrl());
    }

    //Overrides DetailsWindowBase's closeWindow method to pause the video before closing it
    @Override
    public void closeWindow() {
        pauseVideo();
        stage.close();
    }

    /*
    As JavaFX only hides windows instead of killing them completely, on closing, to pause the potentially playing video
    we'll "close" the video/webview by navigating the WebView elsewhere. Ugly solution, but there currently is no other way
    because the JavaFX developers thought killing off windows/stages and its related threads isn't important.
    */
    private void pauseVideo() {
        getTrailerView().getEngine().loadContent("");
    }

}
