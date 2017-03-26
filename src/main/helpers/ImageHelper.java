package main.helpers;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
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
     *	This will ensure maximum performance, especially when scrolling
     *	If not loaded in a BG thread, the downloading would be performed on the UI thread, blocking it
     *	and causing frame drops
     *
     * @param imageUrl url to the image, will be saved as the key in the image cache
     * @param imageView the view which the image should be set on
     */
    public void downloadAndSetImage(String imageUrl, ImageView imageView, boolean shouldClipCircular) {
	new Thread(new Task<Void>() {
	    @Override protected Void call() throws Exception {
		try(InputStream in = new URL(imageUrl).openStream()) {
		    Image downloadedImage = new Image(in);
		    imageView.setImage(downloadedImage);
		    addImageToCache(imageUrl, downloadedImage);

		    if(shouldClipCircular) {
			Platform.runLater( () -> imageView.setClip(
				new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, imageView.getFitWidth() / 2)));
		    }
		} catch (IOException e) {
		    Log.debug("Could not download image. Check your connection. " + "Image: " + imageUrl);
		    e.printStackTrace();
		}
		return null;
	    }}
	).start();
    }

}
