package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.api.ApiService;
import main.api.MovieApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class CineMateController implements Initializable {

    private MovieApi movieApi = new MovieApi();
    private ApiService apiService = movieApi.getService();

    //@FXML exposes the fields to the fxml file while keeping the fields private, nice!

    @FXML
    private TextField apiKeyTextField;

    @FXML
    private Label entryValidityStatusText;

    @Override public void initialize(final URL location, final ResourceBundle resources) {

    }

    public void handleSubmit(ActionEvent actionEvent) {
	String apiKey = apiKeyTextField.getText();

	apiService.getResponse(apiKey, new Callback<Response>() {
	    @Override public void success(final Response response, final Response response2) {
		//UI can't be updated from non-application thread, run this later on the UI thread
	        Platform.runLater( () -> entryValidityStatusText.setText("Success! You're being logged in..."));
	    }

	    @Override public void failure(final RetrofitError retrofitError) {
		// -11-
	        Platform.runLater( () -> entryValidityStatusText.setText("Your API key is incorrect or invalid."));
	    }
	});

    }
}
