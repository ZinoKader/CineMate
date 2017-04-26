package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.CineMateApplication;
import main.constants.EffectConstants;
import main.constants.FXConstants;
import main.controllers.DetailsWindowBase;
import main.model.*;
import main.view.MotionPictureListViewCell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed person informaton window
 */
public class PersonDetailsWindowController extends DetailsWindowBase {

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
    private JFXTextField filterMoviesTextField;

    @FXML
    private JFXListView<MotionPicture> seriesAppearanceListView;

    @FXML
    private JFXTextField filterSeriesTextField;

    private ObservableList<MotionPicture> movieAppearanceList = FXCollections.observableArrayList();
    private ObservableList<MotionPicture> seriesAppearanceList = FXCollections.observableArrayList();

    private Person person;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        filterMoviesTextField.textProperty().addListener((observable, oldText, filterText) -> {
            movieAppearanceList.filtered(motionPicture -> {
                Movie movie = (Movie) motionPicture;
                return movie.getTitle().toLowerCase().contains(filterText.toLowerCase());
            });
        });

        filterSeriesTextField.textProperty().addListener((observable, oldText, filterText) -> {
            movieAppearanceList.filtered(motionPicture -> {
                Series series = (Series) motionPicture;
                return series.getTitle().toLowerCase().contains(filterText.toLowerCase());
            });
        });
    }

    @Override
    public void delegateSetData() {

        if(!passedInTmdbObject.getMediaType().equals(MediaType.PERSON)) {
            Log.debug("Wrong media type passed in for this controller type. Closing window.");
            super.closeWindow();
            return;
        }

        AppendedQueries appendedQueries = new AppendedQueries(Arrays.asList(TmdbQuery.MOVIE_CREDITS, TmdbQuery.TV_CREDITS, TmdbQuery.IMAGES));

        apiService.getPersonDetailed(passedInTmdbObject.getId(), appendedQueries).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                person = response.body();
                if(response.isSuccessful()) {
                    Platform.runLater( () -> {
                        stage.setTitle(person.getName());
                        setBaseDetails();
                        setAppearances();
                    });
                } else {
                    Platform.runLater( () -> {
                        messageHelper.showMessage("Could not fetch person details, closing...");
                        delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                        Log.debug("Failed to fetch person details");
                    });
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable throwable) {
                Platform.runLater( () -> {
                    messageHelper.showMessage("Something went wrong when requesting data, closing...");
                    delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
                    Log.debug("Failed to fetch person details: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
            }
        });

    }

    @Override
    public void setBaseDetails() {
        detailsName.setText(person.getName());
        detailsBiography.setText(person.getBiograhy());
        detailsBorn.setText("Born: " + person.getBirthday() + " in " + person.getBirthplace());
        if(person.isDead()) {
            detailsDead.setText("Died: " + person.getDeathday());
            detailsAge.setText("Died aged at " + person.getAge());
        } else {
            detailsDead.setVisible(false);
            detailsAge.setText("Age " + person.getAge());
        }
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsPersonProfile, true);
        imageHelper.downloadAndSetImage(person.getProfilePath(), detailsBackdrop, false);
        detailsBackdrop.setEffect(EffectConstants.FROSTED_GLASS_EFFECT_HIGH);
    }

    private void setAppearances() {
        movieAppearanceList.addAll(person.getMovieCredits().getMovies());
        seriesAppearanceList.addAll(person.getSeriesCredits().getSeries());

        movieAppearanceListView.setItems(movieAppearanceList);
        movieAppearanceListView.setCellFactory(listView -> new MotionPictureListViewCell());

        seriesAppearanceListView.setItems(seriesAppearanceList);
        seriesAppearanceListView.setCellFactory(listView -> new MotionPictureListViewCell());
    }

    @FXML
    private void handleMovieAppearanceClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(movieAppearanceListView, clickEvent);
    }

    @FXML
    private void handleSeriesAppearanceClicked(MouseEvent clickEvent) {
        handleMotionPictureClicked(seriesAppearanceListView, clickEvent);
    }

    private void handleMotionPictureClicked(JFXListView<? extends MotionPicture> targetListView, MouseEvent clickEvent) {
        if(clickEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
            switch(targetListView.getSelectionModel().getSelectedItem().getMediaType()) {
                case ACCREDITED_MOVIE:
                    Movie selectedMovie = (Movie) targetListView.getSelectionModel().getSelectedItem();
                    screenParent.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
                    break;
                case ACCREDITED_SERIES:
                    Series selectedSeries = (Series) targetListView.getSelectionModel().getSelectedItem();
                    screenParent.loadWindow(CineMateApplication.SERIES_WINDOW_FXML, selectedSeries);
                    break;
            }
        }
    }
}
