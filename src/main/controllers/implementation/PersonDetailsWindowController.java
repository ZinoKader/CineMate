package main.controllers.implementation;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.CineMateApplication;
import main.constants.DetailsWindowConstants;
import main.constants.FXConstants;
import main.controllers.DetailsWindowBase;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledWindow;
import main.model.*;
import main.view.MotionPictureListViewCell;

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed person information window
 */
public class PersonDetailsWindowController extends DetailsWindowBase implements Initializable, ControlledWindow {

    @FXML
    private ImageView detailsPersonProfile;

    @FXML
    private ImageView detailsBackdrop;

    @FXML
    private Label detailsBiography;

    @FXML
    private Label detailsBorn;

    @FXML
    private Label detailsDead;

    @FXML
    private Label detailsName;

    @FXML
    private JFXListView<MotionPicture> knownForListView;

    @FXML
    private JFXListView<MotionPicture> castInListView;

    private Person person;

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
        if(passedData instanceof Person) {
            person = (Person) passedData;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
    }

    @Override
    public void delegateSetData() {
        //TODO: Remove manual API key
        AppendedQueries appendedQueries = new AppendedQueries(Arrays.asList(TmdbQuery.MOVIE_CREDITS, TmdbQuery.TV_CREDITS, TmdbQuery.IMAGES));

        person = apiService.getPersonDetailed(person.getId(), appendedQueries, "4b45808a4d1a83471866761a8d7e5325");
        stage.setTitle(person.getName());

        setBaseDetails();
    }

    @Override public void setBaseDetails() {
        detailsName.setText(person.getName());
        detailsBiography.setText(person.getBiograhy());
        detailsBorn.setText(person.getBirthday());
        if(!person.getDeathday().isEmpty()) {
            detailsDead.setText(person.getDeathday());
        } else {
            detailsDead.setVisible(false);
        }
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsPersonProfile, true);
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT_HIGH);

        setKnownFor();
    }

    private void setKnownFor() {
        ObservableList<MotionPicture> knownForList = FXCollections.observableArrayList();

        Iterator<AccreditedMovie> movieIterator = person.getMovieCredits().getMovies().iterator();
        Iterator<AccreditedSeries> seriesIterator = person.getSeriesCredits().getSeries().iterator();

        //sort our knownForList to show every other movie/series based on the original order (which is based on popularity)
        while(movieIterator.hasNext() || seriesIterator.hasNext()) {
            if(movieIterator.hasNext()) knownForList.add(movieIterator.next());
            if(seriesIterator.hasNext()) knownForList.add(seriesIterator.next());
        }

        knownForListView.setItems(knownForList);
        knownForListView.setCellFactory(listView -> new MotionPictureListViewCell());
    }

    public void handleKnownForClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(knownForListView, clickEvent);
    }

    public void handleCastInClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(castInListView, clickEvent);
    }

    private void handleMotionPictureClicked(JFXListView<? extends MotionPicture> targetListView, MouseEvent clickEvent) {
        if(clickEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            switch(targetListView.getSelectionModel().getSelectedItem().getMediaType()) {
                case MOVIE:
                    Movie selectedMovie = (Movie) targetListView.getSelectionModel().getSelectedItem();
                    screenController.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
                    break;
                case SERIES:
                    Series selectedSeries = (Series) targetListView.getSelectionModel().getSelectedItem();
                    screenController.loadWindow(CineMateApplication.SERIES_WINDOW_FXML, selectedSeries);
                    break;
            }
        }
    }
}
