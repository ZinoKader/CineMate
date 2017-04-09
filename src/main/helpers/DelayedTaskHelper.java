package main.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Helps us run delayed tasks, hiding the jarring Timeline implementation for timed tasks.
 * This is the recommended way to run delayed tasks in JavaFX, although the methods seem to be meant for animations.
 */
public class DelayedTaskHelper {

    public DelayedTaskHelper() {

    }

    public void delayedClose(Stage stage, double delaySeconds) {
        Timeline closeTimeLine = new Timeline(new KeyFrame(Duration.seconds(delaySeconds), event -> stage.close()));
        closeTimeLine.play();
    }

    public void delayedTask(Task<?> task, double delaySeconds) {
        Timeline runTimeLine = new Timeline(new KeyFrame(Duration.seconds(delaySeconds), event -> task.run()));
        runTimeLine.play();
    }
}
