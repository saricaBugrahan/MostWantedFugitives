package com.fugitives.controller;

import com.fugitives.scraping.Fugitive;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class FugitiveController {
    private final Logger logger = Logger.getLogger(FugitiveController.class.getName());
    private final String apiPath;
    public FugitiveController(String apiPath){
        this.apiPath = apiPath;
    }

    /**
     *  Send a POST request to the API
     *  @param fugitive Fugitive object to send to the API
     * */
    public void sendPostRequest(Fugitive fugitive){
        try {
            URL url = new URL(apiPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json"); // Set Content-Type header
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            osw.write(fugitive.toJson());
            osw.flush();
            osw.close();
            os.close();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("Fugitive is sent to the API");
            } else {
                logger.warning("Fugitive is not sent to the API "+ responseCode);
            }
        } catch (IOException e) {
            logger.warning("Error while sending the request to the API: " + e.getMessage());
        }
    }
}
