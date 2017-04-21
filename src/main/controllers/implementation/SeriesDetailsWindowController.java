package main.controllers.implementation;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import main.helpers.CrewHelper;
import main.model.*;
import main.view.CastListViewCell;
import main.view.SeriesListViewCell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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
    private Label detailsBudget;

    @FXML
    private Label detailsRevenue;

    @FXML
    private ImageView detailsBackdrop;

    @FXML
    private ImageView detailsDirectorImage;

    @FXML
    private Label detailsDirectorName;

    @FXML
    private WebView detailsTrailerView;

    @FXML
    private JFXListView<Cast> castListView;

    @FXML
    private JFXListView<Label> crewListView;

    @FXML
    private JFXListView<Series> relatedSeriesListView;

    @FXML
    private JFXListView<MovieReview> reviewsListView;

    @FXML
    private StackPane reviewsSection;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

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
                setBaseDetails();
                setTrailer(series);
                setRatings(series, detailsStarRatings, ratingToolTip);
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

    private void setDirector() {
        Crew director = CrewHelper.filterDirector(series.getCrew());
        detailsDirectorName.setText(director.getName());
        imageHelper.downloadAndSetImage(director.getProfilePath(), detailsDirectorImage, true);
    }

    private void setCast() {
        ObservableList<Cast> castList = FXCollections.observableArrayList();
        castList.setAll(series.getCast());
        castListView.setItems(castList);
        castListView.setCellFactory(listView -> new CastListViewCell());
    }

    @FXML
    public void handleCastClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            Cast selectedCast = castListView.getSelectionModel().getSelectedItem();
            screenParent.loadWindow(CineMateApplication.PERSON_WINDOW_FXML, selectedCast);
            closeWindow();
        }
    }

    private void setCrew() {
        ObservableList<Label> crewTexts = FXCollections.observableArrayList();
        for(Crew crewMember : series.getCrew()) {
            Label crewText = new Label();
            crewText.setText("("+crewMember.getDepartment()+")" + " " + crewMember.getJob() + " : " + crewMember.getName());
            crewTexts.add(crewText);
        }
        crewListView.setItems(crewTexts);
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
