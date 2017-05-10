package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import main.CineMateApplication;
import main.constants.EffectConstants;
import main.constants.FXConstants;
import main.controllers.DetailsMotionPictureWindowBase;
import main.helpers.SeriesDetailComparator;
import main.helpers.TextColorHelper;
import main.model.*;
import main.view.SeasonTreeViewCell;
import main.view.SeriesListViewCell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller implementation for detailed movie information window
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class SeriesDetailsWindowController extends DetailsMotionPictureWindowBase {

    @FXML
    private VBox contentHolder;

    @FXML
    private HBox detailsStarRatings;

    private Tooltip ratingToolTip = new Tooltip();

    @FXML
    private Label detailsTitle;

    @FXML
    private Label detailsYear;

    @FXML
    private Label detailsRuntime;

    @FXML
    private Label detailsDescription;

    @FXML
    private ImageView detailsBackdrop;

    @FXML
    private StackPane keyPersonSection;

    @FXML
    private Separator keyPersonSeparator;

    @FXML
    private Label detailsKeyPersonJob;

    @FXML
    private ImageView detailsKeyPersonImage;

    @FXML
    private Label detailsKeyPersonName;

    @FXML
    private WebView detailsTrailerView;

    @FXML
    private JFXListView<Cast> castListView;

    @FXML
    private JFXListView<Series> relatedSeriesListView;

    @FXML
    private JFXTreeView<SeriesDetail> seasonsTreeView;

    private Series series;

    private final static int SEASON_CELL_HEIGHT = 210;

    @Override
    public void delegateSetData() {

        if(!(passedInTmdbObject.getMediaType().equals(MediaType.SERIES)
                || passedInTmdbObject.getMediaType().equals(MediaType.ACCREDITED_SERIES))) {
            Log.debug("Wrong media type passed in for this controller type. Expected: "
                    + MediaType.SERIES + " or " + MediaType.ACCREDITED_SERIES + ", received: " + passedInTmdbObject.getMediaType());
            closeWindow();
            return;
        }


        AppendedQueries appendedQueries = new AppendedQueries(
                Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.RECOMMENDATIONS, TmdbQuery.VIDEOS, TmdbQuery.REVIEWS));

        apiService.getSeriesDetailed(passedInTmdbObject.getId(), appendedQueries).enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                if(response.isSuccessful()) {
                    series = response.body();
                    Platform.runLater(() -> {
                        setBaseDetails();
                        setRatings(series, detailsStarRatings, ratingToolTip);
                        setTrailer(series);
                        setKeyPerson();
                        setCast(series, castListView);
                        setSeasons();
                        setRelatedSeries();
                    });
                } else {
                    Platform.runLater(() -> {
                        messageHelper.showMessage("Could not fetch series details, closing...");
                        delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                        Log.debug("Failed to fetch series details");
                    });
                }
            }

            @Override
            public void onFailure(Call<Series> call, Throwable throwable) {
                Platform.runLater( () -> {
                    messageHelper.showMessage("Something went wrong when requesting data, closing...");
                    delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                    Log.debug("Failed to fetch series details: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
            }
        });

    }



    @Override
    public void setBaseDetails() {
        detailsTitle.setText(series.getTitle());
        detailsDescription.setText(series.getDescription());
        detailsYear.setText(series.getReleaseDate());
        detailsRuntime.setText(series.getRuntime().format(DateTimeFormatter.ISO_TIME));
        imageCache.downloadAndSetImage(series.getBackdropPath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(EffectConstants.FROSTED_GLASS_EFFECT_NORMAL);

        TextColorHelper.setContentAwareTextColor(new Image(series.getBackdropPath()),
                Arrays.asList(detailsTitle, detailsDescription, detailsYear, detailsRuntime));

    }

    private void setKeyPerson() {
        List<Crew> keyPeople = series.getCrew();
        if(keyPeople.isEmpty()) {
            //remove key person section if there are no key people
            contentHolder.getChildren().removeAll(Arrays.asList(keyPersonSection, keyPersonSeparator));
            Log.debug("Key person not available for this title. Did not set key person.");
        } else {
            Crew keyPerson = keyPeople.get(0); //just set the first and most significant director in the list
            detailsKeyPersonJob.setText(keyPerson.getJob());
            detailsKeyPersonName.setText(keyPerson.getName());
            imageCache.downloadAndSetImage(keyPerson.getProfilePath(), detailsKeyPersonImage, true);
        }
    }

    @FXML
    public void handleCastClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            Cast selectedCast = castListView.getSelectionModel().getSelectedItem();
            screenParent.loadWindow(CineMateApplication.PERSON_WINDOW_FXML, selectedCast);
            closeWindow();
        }
    }

    private void setSeasons() {

        TreeItem<SeriesDetail> treeRoot = new TreeItem<>();
        treeRoot.setExpanded(true);

        for(int seasonNumber = 1; seasonNumber < series.getNumberOfSeasons() + 1; seasonNumber++) {

            int tempSeasonNumber = seasonNumber;

            apiService.getSeriesSeason(series.getId(), seasonNumber).enqueue(new Callback<Season>() {
                @Override
                public void onResponse(Call<Season> call, Response<Season> response) {
                    if(response.isSuccessful()) {
                        Season season = response.body();

                        TreeItem<SeriesDetail> seasonItem = new TreeItem<>(season);

                        //create episode nodes (TreeItems) from episodes and add them to our season nodes
                        seasonItem.getChildren().addAll(season.getEpisodes()
                                .stream()
                                .map(TreeItem<SeriesDetail>::new)
                                .collect(Collectors.toList()));

                        //sort episode nodes under season nodes
                        seasonItem.getChildren().sort(new SeriesDetailComparator());

                        //add season node to root node
                        treeRoot.getChildren().add(seasonItem);

                        //sort season nodes under root node
                        treeRoot.getChildren().sort(new SeriesDetailComparator());

                        //set height of our treeview to amount of seasons (in the treeroot) times the height of a season cell
                        seasonsTreeView.setPrefHeight(treeRoot.getChildren().size() * SEASON_CELL_HEIGHT);

                    } else {
                        Platform.runLater(() -> {
                            messageHelper.showMessage("Could not fetch season " + tempSeasonNumber);
                            Log.debug("Failed to fetch season details for season " + tempSeasonNumber);
                        });
                    }
                }

                @Override
                public void onFailure(Call<Season> call, Throwable throwable) {
                    messageHelper.showMessage("Something went wrong when fetching season " + tempSeasonNumber);
                    Log.debug("Failed to fetch season details for season " + tempSeasonNumber);
                }
            });
        }

        seasonsTreeView.setCellFactory(treeView -> new SeasonTreeViewCell());
        seasonsTreeView.setShowRoot(false);
        seasonsTreeView.setRoot(treeRoot);
    }

    private void setRelatedSeries() {
        ObservableList<Series> observableSeries = FXCollections.observableArrayList();
        observableSeries.addAll(series.getRecommendationResults().getSeries());
        relatedSeriesListView.setItems(observableSeries);
        relatedSeriesListView.setCellFactory(listView -> new SeriesListViewCell());
    }

    @FXML
    public void handleRelatedSeriesClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            Series selectedSeries = relatedSeriesListView.getSelectionModel().getSelectedItem();
            screenParent.loadWindow(CineMateApplication.SERIES_WINDOW_FXML, selectedSeries);
            closeWindow();
        }
    }


    @Override
    public WebView getTrailerView() {
        return detailsTrailerView;
    }
}
