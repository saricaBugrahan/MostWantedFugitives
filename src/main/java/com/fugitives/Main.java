package com.fugitives;


import com.fugitives.organization.Arananlar;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        logger.info("Starting the fugitives scraping");
        new Thread(new Arananlar()).start();
    }
}