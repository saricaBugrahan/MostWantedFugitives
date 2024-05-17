package com.fugitives.organization;

import com.fugitives.common.ImageDownloader;
import com.fugitives.common.Sleep;
import com.fugitives.db.RedisClient;
import com.fugitives.model.Fugitive;
import com.fugitives.model.FugitiveSeverityEnum;
import com.fugitives.queue.RabbitMQClient;
import com.fugitives.service.FugitiveService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class Arananlar implements Runnable {

    private Logger logger = Logger.getLogger(Arananlar.class.getName());
    private final FugitiveService fugitiveService;
    private final String BASE_URL = "https://www.terorarananlar.pol.tr";
    private final String API_PATH = BASE_URL+"/ISAYWebPart/TArananlar/GetTerorleArananlarList";
    private final String API_IMAGE_KEY = "IlkGorselURL";
    RabbitMQClient rabbitMQClient;
    ImageDownloader imageDownloader;


    public Arananlar(){
        fugitiveService = FugitiveService.getFugitiveServiceInstance();
        rabbitMQClient = new RabbitMQClient();
        imageDownloader = ImageDownloader.getInstance();
    }

    private Fugitive createFugitive(Map<String, Object> person, FugitiveSeverityEnum severity) {
        Fugitive fugitive = new Fugitive();
        fugitive.setName(person.get("Adi").toString());
        fugitive.setSurname(person.get("Soyadi").toString());
        fugitive.setBirthPlace(Objects.requireNonNullElse(person.get("DogumYeri"), "").toString());
        fugitive.setBirthDate(Objects.requireNonNullElse(person.get("DogumTarihi"), "").toString());
        fugitive.setOrganization(person.getOrDefault("TOrgutAdi","").toString());
        fugitive.setColor(severity.getColorName());
        return fugitive;
    }

    private void sendFugitiveToQueue(Fugitive fugitive){
        rabbitMQClient.sendMessage(fugitive);
    }

    private void saveFugitiveToRedis(Fugitive fugitive){
        RedisClient.getInstance().saveObject(fugitive.getHashID(),fugitive);
    }
    private boolean isFugitiveInRedis(Fugitive fugitive){
        return RedisClient.getInstance().getObject(fugitive.getHashID()) != null;
    }

    private String downloadImage(String url){
        return imageDownloader.downloadImage(BASE_URL+url);
    }

    private void extractFugitivesFromAPIResponse(Map<String, List<Map<String, Object>>> map){
        for(Map.Entry<String, List<Map<String, Object>>> list: map.entrySet()){
            FugitiveSeverityEnum severity = getSeverity(list.getKey());
            for (Map<String,Object> person: list.getValue()){
                Fugitive fugitive = createFugitive(person,severity);
                if (!isFugitiveInRedis(fugitive)){
                    saveFugitiveToRedis(fugitive);
                    fugitive.setB64Image(downloadImage(person.get(API_IMAGE_KEY).toString()));
                    sendFugitiveToQueue(fugitive);
                }
            }
        }

    }
    private FugitiveSeverityEnum getSeverity(String color){
        switch (color) {
            case "kirmizi":
                return FugitiveSeverityEnum.RED;
            case "mavi":
                return FugitiveSeverityEnum.BLUE;
            case "turuncu":
                return FugitiveSeverityEnum.ORANGE;
            case "yesil":
                return FugitiveSeverityEnum.GREEN;
            case "gri":
                return FugitiveSeverityEnum.GRAY;
            default:
                return null;
        }
    }



    @Override
    public void run() {
        Optional<String> response = fugitiveService.sendPostRequest(API_PATH);
        if (response.isEmpty()){
            logger.warning("Error while sending the request to the API");
            return;
        }
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();
        Map<String, List<Map<String, Object>>> map = gson.fromJson(response.get(), mapType);
        extractFugitivesFromAPIResponse(map);
    }
}
