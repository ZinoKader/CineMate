package main.helpers;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * Helps managing and showing snackbar messages (small alert messages at the bottom of the given pane).
 */
public class MessageHelper {

    private Parent targetParent;

    private static final int MESSAGE_DEFAULT_DURATION = 2500;

    /**
     * We use the fact that a pane has a parent to broaden the possible scope of parameters a user can provide
     * when passing in a target object for the message to be shown in
     * @param targetParent the parent to the pane which the message should be shown in
     */
    public MessageHelper(Parent targetParent) {
        this.targetParent = targetParent;
    }

    public void showMessage(String message, int duration) {
        JFXSnackbar snackbar = new JFXSnackbar((Pane) targetParent);
        snackbar.show(message, duration);
    }

    public void showMessage(String message) {
        JFXSnackbar snackbar = new JFXSnackbar((Pane) targetParent);
        snackbar.show(message, MESSAGE_DEFAULT_DURATION);
    }

}
