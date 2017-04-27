package main.helpers;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Helps with image caching, image color detection and other image related tasks.
 * Image caching is implemented using a HashMap of the image URL and a soft reference to the Image object.
 */

public class ImageHelper {

    private Map<String, SoftReference> imageCache = new HashMap<>();
    private static final int BRIGHTNESS_THRESHOLD = 220;

    public ImageHelper() {
    }

    /**
     * This task will ensure we run the downloading of the image in a background thread
     * This will ensure maximum performance, especially when scrolling
     * If not loaded in a BG thread, the downloading would be performed on the UI thread, blocking it
     * and causing frame drops
     *
     * @param imageUrl url to the image, will be saved as the key in the image cache
     * @param imageView the view which the image should be set on
     */

    public void downloadAndSetImage(String imageUrl, ImageView imageView, boolean shouldClipCircular) {

        new Thread( () -> {

            try(InputStream in = new URL(imageUrl).openStream()) {
                Image downloadedImage = new Image(in);
                imageView.setImage(downloadedImage);
                addImageToCache(imageUrl, downloadedImage);

                if(shouldClipCircular) {
                    Platform.runLater( () -> imageView.setClip(new Circle(
                            imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, imageView.getFitWidth() / 2)));
                }
            } catch (IOException e) {
                Log.debug("Could not download image. Check your connection or the source. " + "Attempted image URL: " + imageUrl);
                e.printStackTrace();
            }

        }).start();

    }

    /**
     * Adds an image to the cache hashmap
     * @param url URL to the image, will be used as a key for the image in the cache
     * @param image the Image object to cache
     */
    private void addImageToCache(String url, Image image) {
        imageCache.put(url, new SoftReference<>(image));
    }

    /**
     * Checks the cache for keys matching the provided URL
     * @param url The URL to check against the cache
     */
    public boolean isImageCached(String url) {
        return imageCache.get(url) != null;
    }

    /**
     *  Fetches an image from the cache
     * @param url URL to the image, used as a key for the image in the cache
     * @return returns the cache Image object
     */
    public Image getCachedImage(String url) {
        return (Image) imageCache.get(url).get();
    }

    /**
     * Checks if the image is brighter than a certain threshold
     * Useful for finding out if text on top of said image should be black or white
     * @param image the image to check the brightness of
     * @return true if image is on average considered bright (a lot of white), false if not
     */
    public boolean isImageBright(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int strideLength = 4; //we actually use RGBA but we ignore the alpha (A), therefore each stride is 4 values

        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        int scanLineRowLength = imageWidth * strideLength;

        byte[] buffer = new byte[imageHeight * scanLineRowLength];
        //read pixel data from image into byte array
        pixelReader.getPixels(0, 0, imageWidth, imageHeight, PixelFormat.getByteBgraInstance(), buffer, 0, scanLineRowLength);

        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;

        //loop through our byte array, adding each block of pixels to our total
        for(int i = 0; i < buffer.length; i++) {
            if(i + 2 >= buffer.length) {
                break; //stop before we hit the end of the byte array
            }
            int red = (buffer[i+2] & 0xff);
            int green = (buffer[i+1] & 0xff);
            int blue = (buffer[i] & 0xff);

            redTotal += red;
            greenTotal += green;
            blueTotal += blue;
        }

        int redAverage = redTotal / buffer.length;
        int greenAverage = greenTotal / buffer.length;
        int blueAverage = blueTotal / buffer.length;

        Log.debug("RGB average: (" + redAverage + ", " + greenAverage + ", " + blueAverage + ")");

        if(redAverage > BRIGHTNESS_THRESHOLD && greenAverage > BRIGHTNESS_THRESHOLD && blueAverage > BRIGHTNESS_THRESHOLD) {
            Log.debug("that shit bright, yo");
            return true;
        }

        Log.debug("not the brightest image in town");
        return false;
    }

}
