package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
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
import main.view.MovieListViewCell;
import main.view.MovieReviewListViewCell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Controller implementation for detailed movie information window
 */
public class MovieDetailsWindowController extends DetailsMotionPictureWindowBase {

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
    private JFXListView<Movie> relatedMoviesListView;

    @FXML
    private JFXListView<MovieReview> reviewsListView;

    @FXML
    private StackPane reviewsSection;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private Movie movie;

    @Override
    public void delegateSetData() {

        movie = (Movie) passedInTmdbObject;

        AppendedQueries appendedQueries = new AppendedQueries(
                Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.RECOMMENDATIONS, TmdbQuery.VIDEOS, TmdbQuery.REVIEWS));

        apiService.getMovieDetailed(passedInTmdbObject.getId(), appendedQueries).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.isSuccessful()) {
                    movie = response.body();
                    Platform.runLater( () -> {
                        stage.setTitle(movie.getTitle());
                        setBaseDetails();
                        setTrailer(movie);
                        setRatings(movie, detailsStarRatings, ratingToolTip);
                        setDirector();
                        setCast();
                        setCrew();
                        setRelatedMovies();
                        setReviews();
                    });
                } else {
                    Platform.runLater(() -> {
                        messageHelper.showMessage("Could not fetch movie details, closing...");
                        delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                        Log.debug("Failed to fetch movie details");
                    });
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable throwable) {
                Platform.runLater( () -> {
                    messageHelper.showMessage("Something went wrong when requesting data, closing...");
                    delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                    Log.debug("Failed to fetch movie details: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
            }
        });

    }

    @Override
    public void setBaseDetails() {
        detailsTitle.setText(movie.getTitle());
        detailsDescription.setText(movie.getDescription());
        detailsYear.setText(movie.getReleaseDate());
        detailsRuntime.setText(movie.getRuntime().format(DateTimeFormatter.ISO_TIME));
        detailsBudget.setText("Budget: " + currencyFormat.format(movie.getBudget()));
        detailsRevenue.setText("Revenue: " + currencyFormat.format(movie.getRevenue()));
        imageHelper.downloadAndSetImage(movie.getBackdropPath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT_NORMAL);
    }

    private void setDirector() {
        Crew director = CrewHelper.filterDirector(movie.getCrew());
        detailsDirectorName.setText(director.getName());
        imageHelper.downloadAndSetImage(director.getProfilePath(), detailsDirectorImage, true);
    }

    private void setCast() {
        ObservableList<Cast> castList = FXCollections.observableArrayList();
        castList.setAll(movie.getCast());
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
        for(Crew crewMember : movie.getCrew()) {
            Label crewText = new Label();
            crewText.setText("("+crewMember.getDepartment()+")" + " " + crewMember.getJob() + " : " + crewMember.getName());
            crewTexts.add(crewText);
        }
        crewListView.setItems(crewTexts);
    }

    private void setRelatedMovies() {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        movieList.addAll(movie.getRecommendationResults().getMovies());
        relatedMoviesListView.setItems(movieList);
        relatedMoviesListView.setCellFactory(listView -> new MovieListViewCell());
    }

    @FXML
    public void handleRelatedMovieClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            Movie selectedMovie = relatedMoviesListView.getSelectionModel().getSelectedItem();
            screenParent.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
            closeWindow();
        }
    }

    private void setReviews() {
        ObservableList<MovieReview> reviewsList = FXCollections.observableArrayList();
        if(movie.getReviews().getResults().isEmpty()) {
            //remove reviews section if there are no reviews to display
            contentHolder.getChildren().remove(reviewsSection);
        } else {
            reviewsList.addAll(movie.getReviews().getResults());
            reviewsListView.setItems(reviewsList);
            reviewsListView.setCellFactory(listView -> new MovieReviewListViewCell());
        }
    }

    @Override
    public WebView getTrailerView() {
        return detailsTrailerView;
    }
}
