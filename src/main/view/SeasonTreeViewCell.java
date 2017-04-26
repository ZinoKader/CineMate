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
    protected void updateItem(SeriesDetail seriesItem, boolean empty) {
        super.updateItem(seriesItem, empty);

        if(empty || seriesItem == null) {
            setText(null);
            setGraphic(null);
        } else {


            switch(seriesItem.getType()) {

                case SEASON:

                    Season seasonItem = (Season) seriesItem;

                    if(fxmlLoader == null) {
                        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/season_treeview_cell.fxml"));
                        fxmlLoader.setController(this);
                        try {
                            fxmlLoader.load();
                        } catch (IOException e) {
                            Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
                        }
                    }

                    title.setText("Season " + seasonItem.getSeasonNumber());

                    if(seasonItem.getDescription().isEmpty()) {
                        description.setText("Season description not available yet.");
                    } else {
                        description.setText(seasonItem.getDescription());
                    }


                    if(imageHelper.isImageCached(seasonItem.getPosterPath())) {
                        image.setImage(imageHelper.getCachedImage(seasonItem.getPosterPath()));
                    } else {
                        imageHelper.downloadAndSetImage(seasonItem.getPosterPath(), image, false);
                    }

                    break;

                case EPISODE:

                    Episode episodeItem = (Episode) seriesItem;

                    if(fxmlLoader == null) {
                        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/episode_treeview_cell.fxml"));
                        fxmlLoader.setController(this);
                        try {
                            fxmlLoader.load();
                        } catch (IOException e) {
                            Log.debug("Could not load FXML file for " + getClass().getSimpleName(), e);
                        }
                    }

                    title.setText(episodeItem.getEpisodeTitle());

                    if(episodeItem.getDescription().isEmpty()) {
                        description.setText("Episode description not available yet.");
                    } else {
                        description.setText(episodeItem.getDescription());
                    }


                    if(imageHelper.isImageCached(episodeItem.getPosterPath())) {
                        image.setImage(imageHelper.getCachedImage(episodeItem.getPosterPath()));
                    } else {
                        imageHelper.downloadAndSetImage(episodeItem.getPosterPath(), image, false);
                    }

                    break;

            }


            setText(null);
            setGraphic(container);
        }

    }

}
