package org.scrape;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Logger;

public class ImageDownloader {

    private static ImageDownloader imageDownloader;
    private final Logger logger = Logger.getLogger(ImageDownloader.class.getName());
    private ImageDownloader(){

    }

    /**
     * Singleton pattern for ImageDownloader
     * @return imageDownloader
     */
    public static ImageDownloader getInstance(){
        if (imageDownloader == null){
            imageDownloader = new ImageDownloader();
        }
        return imageDownloader;
    }

    /**
     * Downloads the image from the given URL and returns it as a base64 string.
     * @param imageURL the URL of the image
     * @return the base64 string of the image
     */
    public String downloadImage(String imageURL){
        try {
            URL  url = new URL(imageURL);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            logger.warning("Error while downloading image: " + e.getMessage());
            return null;
        }
    }

}
