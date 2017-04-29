package main.view;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.constants.EffectConstants;
import main.helpers.ImageCache;
import main.helpers.TextColorHelper;
import main.model.Episode;
import main.model.Season;
import main.model.SeriesDetail;

import java.io.IOException;
import java.util.Arrays;

public class SeasonTreeViewCell extends TreeCell<SeriesDetail> {

    @FXML
    private Label seasonTitle;

    @FXML
    private Label episodeTitle;

    @FXML
    private Label episodeAired;

    @FXML
    private Label seasonDescription;

    @FXML
    private Label episodeDescription;

    @FXML
    private ImageView seasonImage;

    @FXML
    private ImageView episodeImage;

    @FXML
    private HBox seasonContainer;

    @FXML
    private HBox episodeContainer;


    private FXMLLoader seasonFxmlLoader;

    private FXMLLoader episodeFxmlLoader;

    private ImageCache imageCache = new ImageCache();

    @Override
    protected void updateItem(SeriesDetail seriesItem, boolean empty) {
        super.updateItem(seriesItem, empty);

        if(empty || seriesItem == null) {
            setText(null);
            setGraphic(null);
        } else {


            switch(seriesItem.getType()) {

                case SEASON:

                    Season seasonItem = (Season) seriesItem;

                    if(seasonFxmlLoader == null) {
                        seasonFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/season_treeview_cell.fxml"));
                        seasonFxmlLoader.setController(this);
                        try {
                            seasonFxmlLoader.load();
                        } catch (IOException e) {
                            Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
                        }
                    }

                    seasonTitle.setText("Season " + seasonItem.getSeasonNumber());

                    if(seasonItem.getDescription().isEmpty()) {
                        seasonDescription.setText("Season description not available yet.");
                    } else {
                        seasonDescription.setText(seasonItem.getDescription());
                    }


                    if(imageCache.isImageCached(seasonItem.getPosterPath())) {
                        seasonImage.setImage(imageCache.getCachedImage(seasonItem.getPosterPath()));
                    } else {
                        imageCache.downloadAndSetImage(seasonItem.getPosterPath(), seasonImage, false);
                    }

                    setText(null);
                    setGraphic(seasonContainer);

                    break;

                case EPISODE:

                    Episode episodeItem = (Episode) seriesItem;

                    if(episodeFxmlLoader == null) {
                        episodeFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/episode_treeview_cell.fxml"));
                        episodeFxmlLoader.setController(this);
                        try {
                            episodeFxmlLoader.load();
                        } catch (IOException e) {
                            Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
                        }
                    }

                    episodeTitle.setText("Episode " + episodeItem.getEpisodeNumber() + ": " + episodeItem.getEpisodeTitle());

                    if(episodeItem.getAirDate() == null) {
                        episodeAired.setText("Air date unavailable");
                    } else{
                        episodeAired.setText("Air date " + episodeItem.getAirDate());
                    }

                    if(episodeItem.getDescription().isEmpty()) {
                        episodeDescription.setText("Episode description not available yet.");
                    } else {
                        episodeDescription.setText(episodeItem.getDescription());
                    }

                    if(imageCache.isImageCached(episodeItem.getPosterPath())) {
                        episodeImage.setImage(imageCache.getCachedImage(episodeItem.getPosterPath()));
                    } else {
                        imageCache.downloadAndSetImage(episodeItem.getPosterPath(), episodeImage, false);
                    }

                    episodeImage.setEffect(EffectConstants.FROSTED_GLASS_EFFECT_MEDIUM);

                    TextColorHelper.setContentAwareTextColor(new Image(episodeItem.getPosterPath()), Arrays.asList(
                            episodeTitle, episodeAired, episodeDescription));

                    setText(null);
                    setGraphic(episodeContainer);

                    break;

            }

        }

    }

}
