package main.helpers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Helper class for deciding text color based on background image.
 */
public final class TextColorHelper {

    private static final int BRIGHTNESS_THRESHOLD = 200;
    private static final int LAST_8_BITS = 0xff; //0xff effectively ignores everything but the last 8 bits, as we only need values from 0-255 for RGB
    private static final Color BRIGHT_IMAGE_TEXT_COLOR = Color.BLACK;
    private static final Color DARK_IMAGE_TEXT_COLOR = Color.WHITE;

    private TextColorHelper() {
    }

    /**
     * Static because we don't need to save state with this operation. Tasks are independent of each other.
     *
     * Checks if the image is brighter than a certain threshold
     * Useful for finding out if text on top of said image should be black or white
     * For a good demonstration of this in work, search for the series "Fargo" and open up season 3
     * @param image the image to check the brightness of
     * @param textTargets the text nodes to modify based on image brightness
     */
    public static void setContentAwareTextColor(Image image, List<Label> textTargets) {

        Runnable textColorTask = () -> {
            int strideLength = 4; //we use ARGB, therefore each stride is 4 values (although we ignore the alpha value)

            int imageWidth = (int) image.getWidth();
            int imageHeight = (int) image.getHeight();
            int scanLineRowLength = imageWidth * strideLength;

            byte[] buffer = new byte[imageHeight * scanLineRowLength];
            //read pixel data from image into byte array
            image.getPixelReader().getPixels(0, 0, imageWidth, imageHeight, PixelFormat.getByteBgraInstance(), buffer, 0, scanLineRowLength);

            int redTotal = 0;
            int greenTotal = 0;
            int blueTotal = 0;

            //loop through our byte array, adding each block of pixels to our total
            for(int i = 0; i < buffer.length; i++) {
                if(i + 2 >= buffer.length) {
                    break; //stop before we hit the end of the byte array
                }

                int red = (buffer[i + 2] & LAST_8_BITS);
                int green = (buffer[i + 1] & LAST_8_BITS);
                int blue = (buffer[i] & LAST_8_BITS);

                redTotal += red;
                greenTotal += green;
                blueTotal += blue;
            }

            int redAverage = redTotal / buffer.length;
            int greenAverage = greenTotal / buffer.length;
            int blueAverage = blueTotal / buffer.length;

            //Log.debug("RGB average: (" + redAverage + ", " + greenAverage + ", " + blueAverage + ")");

            //we run the text-setting on the UI thread with the help of Platform.runLater()
            if(redAverage > BRIGHTNESS_THRESHOLD && greenAverage > BRIGHTNESS_THRESHOLD && blueAverage > BRIGHTNESS_THRESHOLD) {
                //Log.debug("that shit bright, yo");
                Platform.runLater( () -> textTargets.forEach(label -> label.setTextFill(BRIGHT_IMAGE_TEXT_COLOR)));
            } else {
                Platform.runLater( () -> textTargets.forEach(label -> label.setTextFill(DARK_IMAGE_TEXT_COLOR)));
            }
        };

        Thread runInBackground = new Thread(textColorTask);
        runInBackground.setDaemon(true); //terminate thread if application is exited
        runInBackground.start();

    }

}