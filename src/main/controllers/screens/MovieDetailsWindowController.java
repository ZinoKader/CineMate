package main.controllers.screens;

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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.constants.DetailsWindowConstants;
import main.constants.TmdbConstants;
import main.controllers.ControlledWindow;
import main.helpers.CrewHelper;
import main.model.AppendedQueries;
import main.model.Cast;
import main.model.Crew;
import main.model.Movie;
import main.model.TmdbQuery;
import main.view.CastListViewCell;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MovieDetailsWindowController implements Initializable, ControlledWindow {

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
    private ImageView detailsDirectorImage;

    @FXML
    private Label detailsDirectorName;

    @FXML
    private JFXListView<Cast> castListView;

    @FXML
    private JFXListView<Label> crewListView;

    private Stage stage;


    private List<Movie> movies = new ArrayList<>();
    ApiAdapater apiAdapater;
    ApiService apiService;

    @Override public void setStage(Stage stage) {
	this.stage = stage;
    }

    @Override public void setPassedData(final List<?> passedData) {
	for(Object object : passedData) {
	    if(object instanceof Movie) {
		movies.add((Movie) object);
	    }
	}
    }

    @Override public void initialize(final URL location, final ResourceBundle resources) {
	apiAdapater = new ApiAdapater();
	apiService = apiAdapater.getService();

	//Ensures that we get our data when the window has been initialized and setPassedData() has been called
	Platform.runLater( () -> delegateSetData(movies.get(0).getId()));
    }

    private void delegateSetData(String movieId) {
	//TODO: Remove manual API key
	List<TmdbQuery> queries = new ArrayList<>();
	queries.addAll(Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.SIMILAR, TmdbQuery.VIDEOS));
	AppendedQueries appendedQueries = new AppendedQueries(queries);

	Movie movie = apiService.getMovieDetailed(movieId, appendedQueries, "4b45808a4d1a83471866761a8d7e5325");
	stage.setTitle(movie.getTitle());

	setBaseDetails(movie);
	setRatings(movie.getAverageRating());
	setDirector(movie);
	setCast(movie);
	setCrew(movie);
    }

    private void setBaseDetails(Movie movie) {
	detailsTitle.setText(movie.getTitle());
	detailsDescription.setText(movie.getDescription());
	detailsYear.setText(movie.getReleaseDate());
	detailsRuntime.setText(movie.getRuntime().format(DateTimeFormatter.ISO_TIME));
	detailsBackdrop.setImage(new Image(movie.getBackdropPath()));
	detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT);
    }

    private void setDirector(Movie movie) {
	Crew director = CrewHelper.filterDirector(movie.getCrew());
	detailsDirectorName.setText(director.getName());
	detailsDirectorImage.setImage(new Image(director.getProfilePath()));
	detailsDirectorImage.setClip(new Circle(
		detailsDirectorImage.getFitWidth() / 2,
		detailsDirectorImage.getFitHeight() / 2,
		detailsDirectorImage.getFitWidth() / 2));
    }

    private void setCast(Movie movie) {
        ObservableList<Cast> castList = FXCollections.observableArrayList();
        castList.setAll(movie.getCast());
        castListView.setItems(castList);
        castListView.setCellFactory(listView -> new CastListViewCell());
    }

    private void setCrew(Movie movie) {
        ObservableList<Label> crewTexts = FXCollections.observableArrayList();
	for(Crew crewMember : movie.getCrew()) {
	    Label crewText = new Label();
	    crewText.setText("("+crewMember.getDepartment()+")" + " " + crewMember.getJob() + " : " + crewMember.getName());
	    crewTexts.add(crewText);
	}
	crewListView.setItems(crewTexts);
    }

    //set star ratings, show tooltip on hover
    private void setRatings(String rating) {

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

}
