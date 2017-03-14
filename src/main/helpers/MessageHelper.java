package main.helpers;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

/**
 * Helps managing and showing snackbar messages (small alert messages at the bottom of the given pane).
 */
public class MessageHelper {

    Pane targetPane;

    private static final int MESSAGE_DEFAULT_DURATION = 2500;

    /**
     *
     * @param targetPane the pane which the message should be shown in
     */
    public MessageHelper(Pane targetPane) {
        this.targetPane = targetPane;
    }

    public void showMessage(String message, int duration) {
	JFXSnackbar snackbar = new JFXSnackbar(targetPane);
 	snackbar.show(message, duration);
    }

    public void showMessage(String message) {
	JFXSnackbar snackbar = new JFXSnackbar(targetPane);
 	snackbar.show(message, MESSAGE_DEFAULT_DURATION);
    }

}
