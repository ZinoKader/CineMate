package main.view;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.helpers.ImageHelper;
import main.model.Episode;
import main.model.Season;
import main.model.SeriesDetail;

import java.io.IOException;

public class SeasonTreeViewCell extends TreeCell<SeriesDetail> {

    @FXML
    private Label seasonTitle;

    @FXML
    private Label episodeTitle;

    @FXML
    private Label seasonDescription;

    @FXML
    private Label episodeDescription;

    @FXML
    private ImageView seasonImage;

    @FXML
    private HBox seasonContainer;

    @FXML
    private HBox episodeContainer;


    private FXMLLoader seasonFxmlLoader;

    private FXMLLoader episodeFxmlLoader;

    private ImageHelper imageHelper = new ImageHelper();

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


                    if(imageHelper.isImageCached(seasonItem.getPosterPath())) {
                        seasonImage.setImage(imageHelper.getCachedImage(seasonItem.getPosterPath()));
                    } else {
                        imageHelper.downloadAndSetImage(seasonItem.getPosterPath(), seasonImage, false);
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

                    episodeTitle.setText(episodeItem.getEpisodeTitle());

                    if(episodeItem.getDescription().isEmpty()) {
                        episodeDescription.setText("Episode description not available yet.");
                    } else {
                        episodeDescription.setText(episodeItem.getDescription());
                    }

                    setText(null);
                    setGraphic(episodeContainer);

                    break;

            }

        }

    }

}
