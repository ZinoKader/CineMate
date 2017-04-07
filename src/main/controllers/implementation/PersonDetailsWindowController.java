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
    private Label detailsAge;

    @FXML
    private Label detailsName;

    @FXML
    private JFXListView<MotionPicture> movieAppearanceListView;

    @FXML
    private JFXListView<MotionPicture> seriesAppearanceListView;

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
        setAppearances();
    }

    @Override public void setBaseDetails() {
        detailsName.setText(person.getName());
        detailsBiography.setText(person.getBiograhy());
        detailsBorn.setText("Born: " + person.getBirthday() + " in " + person.getBirthplace());
        if(person.isDead()) {
            detailsDead.setText("Dead: " + person.getDeathday());
            detailsAge.setText("Died aged at " + person.getAge());
        } else {
            detailsDead.setVisible(false);
            detailsAge.setText("Age " + person.getAge());
        }
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsPersonProfile, true);
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(DetailsWindowConstants.FROSTED_GLASS_EFFECT_HIGH);
    }

    private void setAppearances() {
        ObservableList<MotionPicture> movieAppearanceList = FXCollections.observableArrayList();
        ObservableList<MotionPicture> seriesAppearanceList = FXCollections.observableArrayList();

        movieAppearanceList.addAll(person.getMovieCredits().getMovies());
        seriesAppearanceList.addAll(person.getSeriesCredits().getSeries());

        movieAppearanceListView.setItems(movieAppearanceList);
        movieAppearanceListView.setCellFactory(listView -> new MotionPictureListViewCell());

        seriesAppearanceListView.setItems(seriesAppearanceList);
        seriesAppearanceListView.setCellFactory(listView -> new MotionPictureListViewCell());
    }

    public void handleMovieAppearanceClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(movieAppearanceListView, clickEvent);
    }

    public void handleSeriesAppearanceClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(seriesAppearanceListView, clickEvent);
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
