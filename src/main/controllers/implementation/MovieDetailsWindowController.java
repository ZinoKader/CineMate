package main.controllers.implementation;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import main.CineMateApplication;
import main.constants.DetailsWindowConstants;
import main.constants.FXConstants;
import main.constants.TmdbConstants;
import main.controllers.DetailsWindowBase;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledWindow;
import main.helpers.CrewHelper;
import main.model.AppendedQueries;
import main.model.Cast;
import main.model.Crew;
import main.model.Movie;
import main.model.TmdbQuery;
import main.view.CastListViewCell;
import main.view.MovieListViewCell;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed movie information window
 */
public class MovieDetailsWindowController extends DetailsWindowBase implements Initializable, ControlledWindow {

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

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private Movie movie;

    @Override
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    @Override
    public void setScreenParent(ScreenController screenController) {
	this.screenController = screenController;
    }

    @Override
    public void setPassedData(Object passedData) {
	if(passedData instanceof Movie) {
	   movie = (Movie) passedData;
	}
    }

    @Override public void initialize(final URL location, final ResourceBundle resources) {
        super.initialize();

	//Listen to window closing events, on closing, pause the potentially playing video
	Platform.runLater(() -> stage.setOnCloseRequest(closeEvent -> pauseVideo()) );
    }

    @Override
    public void delegateSetData() {
	//TODO: Remove manual API key
	AppendedQueries appendedQueries = new AppendedQueries(Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.RECOMMENDATIONS, TmdbQuery.VIDEOS));

	movie = apiService.getMovieDetailed(movie.getId(), appendedQueries, "4b45808a4d1a83471866761a8d7e5325");
	stage.setTitle(movie.getTitle());

	setBaseDetails();
	setTrailer();
	setRatings();
	setDirector();
	setCast();
	setCrew();
	setRelatedMovies();
    }

    @Override
    public void setBaseDetails() {
	detailsTitle.setText(movie.getTitle());
	detailsDescription.setText(movie.getDescription());
	detailsYear.setText(movie.getReleaseDate());
	detailsRuntime.setText(movie.getRuntime().format(DateTimeFormatter.ISO_TIME));
	detailsBudget.setText("Budget: " + currencyFormat.format(movie.getBudget()));
	detailsRevenue.setText("Revenue: " + currencyFormat.format(movie.getRevenue()));
	detailsBackdrop.setImage(new Image(movie.getBackdropPath()));
	detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT);
    }

    private void setTrailer() {
        detailsTrailerView.getEngine().load(movie.getTrailerUrl());
    }

    private void setDirector() {
	Crew director = CrewHelper.filterDirector(movie.getCrew());
	detailsDirectorName.setText(director.getName());
	detailsDirectorImage.setImage(new Image(director.getProfilePath()));
	detailsDirectorImage.setClip(new Circle(
		detailsDirectorImage.getFitWidth() / 2,
		detailsDirectorImage.getFitHeight() / 2,
		detailsDirectorImage.getFitWidth() / 2));
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
            screenController.loadWindow(CineMateApplication.PERSON_WINDOW_FXML, selectedCast);
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
	    screenController.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
	    closeWindow();
	}
    }

    //set star ratings, show tooltip on hover
    private void setRatings() {

        String rating = movie.getAverageRating();

	Rating ratingsElement = new Rating();
	ratingsElement.setPartialRating(true);
	ratingsElement.setMouseTransparent(true); //disable clicking/rating
	ratingsElement.setUpdateOnHover(false);
	ratingsElement.setRating(Double.parseDouble(rating) / 2);
	ratingsElement.setMax(TmdbConstants.MAX_AVERAGE_RATING / 2);
	detailsStarRatings.getChildren().add(ratingsElement);

	double adjustedRating = Double.parseDouble(rating) / 2;
	ratingToolTip.setText(String.valueOf(adjustedRating));
	Tooltip.install(detailsStarRatings, ratingToolTip);
    }

    private void closeWindow() {
	pauseVideo();
	stage.close();
    }

    /*
    As JavaFX only hides windows instead of killing them completely, on closing, to pause the potentially playing video
    we'll "close" the video/webview by navigating the WebView elsewhere. Ugly solution, but there currently is no other way
    because the JavaFX developers thought killing off windows/stages and its related threads isn't important.
    */
    private void pauseVideo() {
	detailsTrailerView.getEngine().loadContent("");
    }

}
