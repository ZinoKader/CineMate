package main.helpers;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Helps with image caching.
 * Image caching is implemented using a HashMap of the image URL and a soft reference to the Image object.
 */

public class ImageCache {

    private Map<String, SoftReference> imageCache = new HashMap<>();

    public ImageCache() {
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
                addImageToCache(imageUrl, downloadedImage);

                Platform.runLater( () -> imageView.setImage(downloadedImage));
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

}

