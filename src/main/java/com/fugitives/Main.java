package com.fugitives;


import com.fugitives.scraping.FugitiveColorEnum;
import com.fugitives.scraping.FugitiveScraper;

public class Main {

    public static void main(String[] args) {
        System.out.println("Scraping Started");
        FugitiveScraper fugitiveScraper = FugitiveScraper.getInstance();
        fugitiveScraper.scrape(FugitiveColorEnum.RED);
        fugitiveScraper.scrape(FugitiveColorEnum.GREEN);
        fugitiveScraper.scrape(FugitiveColorEnum.ORANGE);
        fugitiveScraper.scrape(FugitiveColorEnum.BLUE);
        fugitiveScraper.scrape(FugitiveColorEnum.GRAY);
        fugitiveScraper.close();
    }
}