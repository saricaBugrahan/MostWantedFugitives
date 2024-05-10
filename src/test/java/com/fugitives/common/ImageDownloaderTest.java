package com.fugitives.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageDownloaderTest {

    private  String validURL;
    private  String invalidURL;
    @BeforeEach
    public void setUp(){
        validURL = "https://www.terorarananlar.pol.tr/kurumlar/terorarananlar.pol.tr/Arsiv/643/FotoOrj.jpeg";
        invalidURL = "https://www.terorarananlar.pol.tr/kurumlar/terorarananlar.pol.tr/Arsiv/-1/FotoOrj.jpeg";
    }

    @Test
    public void testDownloadImageSuccess(){
        ImageDownloader imageDownloader = ImageDownloader.getInstance();
        String base64Image = imageDownloader.downloadImage(validURL);
        assertNotNull(base64Image);
    }

    @Test
    public void testDownloadImageError(){
        ImageDownloader imageDownloader = ImageDownloader.getInstance();
        String base64Image = imageDownloader.downloadImage(invalidURL);
        assertNull(base64Image);
    }

}