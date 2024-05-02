package org.scrape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws IOException {
        FugitiveScraper fugitiveScraper = FugitiveScraper.getInstance();
        fugitiveScraper.scrape(FugitiveColorEnum.BLUE);
        fugitiveScraper.scrape(FugitiveColorEnum.GREEN);
        fugitiveScraper.scrape(FugitiveColorEnum.ORANGE);
        fugitiveScraper.scrape(FugitiveColorEnum.BLUE);
        fugitiveScraper.scrape(FugitiveColorEnum.GRAY);
        fugitiveScraper.close();
    }
}