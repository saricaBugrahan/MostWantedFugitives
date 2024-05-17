package com.fugitives.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class FugitiveService implements Service{
    private final Logger logger = Logger.getLogger(FugitiveService.class.getName());

    private static FugitiveService fugitiveService;
    private FugitiveService(){}

    public static FugitiveService getFugitiveServiceInstance(){
        if(fugitiveService == null){
            fugitiveService = new FugitiveService();
        }
        return fugitiveService;
    }

    public Optional<String> sendPostRequest(String apiPath){
        try {
            HttpURLConnection connection = getHttpURLConnection(apiPath);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("Fugitive is sent to the API");
            } else {
                logger.warning("Fugitive is not sent to the API "+ responseCode);
            }
            InputStream inputStream = connection.getInputStream();
            byte[] response = inputStream.readAllBytes();
            return Optional.of(new String(response));


        } catch (IOException e) {
            logger.warning("Error while sending the request to the API: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> sendGetRequest(String apiPath) {
        return Optional.empty();
    }


    private HttpURLConnection getHttpURLConnection(String apiPath) throws IOException {
        URL url = new URL(apiPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.flush();
        osw.close();
        os.close();
        connection.connect();
        return connection;
    }
}
