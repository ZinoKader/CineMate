package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import main.CineMateApplication;
import main.constants.DetailsWindowConstants;
import main.constants.FXConstants;
import main.controllers.DetailsMotionPictureWindowBase;
import main.model.*;
import main.view.SeriesListViewCell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Controller implementation for detailed movie information window
 */
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

    private Series series;


    @Override
    public void delegateSetData() {

        series = (Series) passedInTmdbObject;

        AppendedQueries appendedQueries = new AppendedQueries(
                Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.RECOMMENDATIONS, TmdbQuery.VIDEOS, TmdbQuery.REVIEWS));

        apiService.getSeriesDetailed(passedInTmdbObject.getId(), appendedQueries).enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                series = response.body();
                Platform.runLater( () -> {
                    setBaseDetails();
                    setRatings(series, detailsStarRatings, ratingToolTip);
                    setTrailer(series);
                    setKeyPerson();
                    setCast(series, castListView);
                    setRelatedSeries();
                });
            }

            @Override
            public void onFailure(Call<Series> call, Throwable throwable) {

            }
        });

    }

    @Override
    public void setBaseDetails() {
        detailsTitle.setText(series.getTitle());
        detailsDescription.setText(series.getDescription());
        detailsYear.setText(series.getReleaseDate());
        detailsRuntime.setText(series.getRuntime().format(DateTimeFormatter.ISO_TIME));
        imageHelper.downloadAndSetImage(series.getBackdropPath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT_NORMAL);
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
            imageHelper.downloadAndSetImage(keyPerson.getProfilePath(), detailsKeyPersonImage, true);
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
