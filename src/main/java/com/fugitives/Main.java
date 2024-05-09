package com.fugitives;


import com.fugitives.scraping.FugitiveColorEnum;
import com.fugitives.scraping.FugitiveScraper;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        logger.info("Starting the fugitives scraping");
        FugitiveScraper fugitiveScraper = FugitiveScraper.getInstance();
        fugitiveScraper.scrape(FugitiveColorEnum.RED);
        fugitiveScraper.scrape(FugitiveColorEnum.GREEN);
        fugitiveScraper.scrape(FugitiveColorEnum.ORANGE);
        fugitiveScraper.scrape(FugitiveColorEnum.BLUE);
        fugitiveScraper.scrape(FugitiveColorEnum.GRAY);
        fugitiveScraper.close();
    }
}