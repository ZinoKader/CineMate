package main.helpers;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageHelper {

    private Map<String, SoftReference> imageCache = new HashMap<>();

    public ImageHelper() {

    }

    public void addImageToCache(String url, Image image) {
        imageCache.put(url, new SoftReference<Image>(image));
    }

    public boolean isImageCached(String url) {
        return imageCache.get(url) != null;
    }

    public Image getCachedImage(String url) {
        return (Image) imageCache.get(url).get();
    }

    /**
     * 	This task will ensure we run the downloading of the image in a background thread
     *	We run each task like this in a new thread to ensure maximum performance.
     *	When not settings the image in a bg thread, scrolling would lag as the images would not show
     *	until they were fully loaded, blocking the UI thread and causing low fps
     *
     * @param imageUrl url to the image, will be saved as the key in the image cache
     * @param imageView the view which the image should be set on
     */
    public void downloadAndSetImage(String imageUrl, ImageView imageView) {
	new Thread(new Task<Void>() {
	    @Override protected Void call() throws Exception {
		try(InputStream in = new URL(imageUrl).openStream()) {
		    Image downloadedImage = new Image(in);
		    imageView.setImage(downloadedImage);
		    addImageToCache(imageUrl, downloadedImage);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	    }}
	).start();
    }

}
