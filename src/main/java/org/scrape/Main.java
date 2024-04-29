package org.scrape;

public class Main {
    public static void main(String[] args) {
        FugitiveScraper fugitiveScraper = FugitiveScraper.getInstance();
        fugitiveScraper.scrape(FugitiveColorEnum.RED);
        fugitiveScraper.scrape(FugitiveColorEnum.GREEN);
        fugitiveScraper.scrape(FugitiveColorEnum.ORANGE);
        fugitiveScraper.scrape(FugitiveColorEnum.BLUE);
        fugitiveScraper.scrape(FugitiveColorEnum.GRAY);
        fugitiveScraper.close();
    }
}