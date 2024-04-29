package org.scrape;
import lombok.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.LinkedList;
import java.util.logging.Logger;


public class FugitiveScraper {

    private static FugitiveScraper fugitiveScraper;
    private final WebDriver scrapeDriver;
    private static final String URL = "https://www.terorarananlar.pol.tr";
    private LinkedList<Fugitive> fugitives;
    private final Logger logger = Logger.getLogger(FugitiveScraper.class.getName());



    /**
     * Constructor for FugitiveScraper
     * It initializes the ChromeDriver with headless mode
     * and initializes the fugitives list.
     */
    private FugitiveScraper(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        scrapeDriver = new ChromeDriver(chromeOptions);
        fugitives = new LinkedList<>();
    }

    /**
     * Singleton pattern for FugitiveScraper
     * @return fugitiveScraper
     */
    public static FugitiveScraper getInstance(){
        if (fugitiveScraper == null){
            fugitiveScraper = new FugitiveScraper();
        }
        return fugitiveScraper;
    }
    /**
     * Scrapes the website for the given color of the list and adds the fugitives to the list.
     * @param listColor the color of the list
     */
    public void scrape(@NonNull FugitiveColorEnum listColor){
        scrapeDriver.get(URL);
        WebElement element = scrapeDriver.findElement(By.cssSelector("div.wanted-group:nth-child("+listColor.getColor()+")"));
        element.click();
        scrapeDriver.navigate().refresh();
        Sleep.sleep(2000);
        acceptCookie();
        WebElement childElementByClassName;
        for(int i = 1; i < 431; i++){
            try {
                if (scrapeDriver.findElements(By.cssSelector("div.deactivated-list-card:nth-child(" + i + ")")).isEmpty())
                    continue;
                childElementByClassName = scrapeDriver.findElement(By.cssSelector("div.deactivated-list-card:nth-child(" + i + ")"));
                if (!childElementByClassName.isDisplayed())
                    continue;

                if (childElementByClassName.getText().isEmpty()){
                    logger.warning("The"+i+"th element is empty.");
                    continue;
                }
                String[] childElementText = childElementByClassName.getText().split("\n");
                String[] name = childElementText[0].split(" ");
                String[] birthPlaceAndDate = childElementText[1].split("-");
                Fugitive fugitive = new Fugitive(name[0], name[1], birthPlaceAndDate[0], birthPlaceAndDate[1], childElementText[2], listColor.getColorName());
                fugitives.add(fugitive);
                System.out.println(fugitive);
                pushButtonForLoadingPeople();
                sleepPeriodically();

            } catch (NoSuchElementException noSuchElementException){
                logger.warning("The"+i+"th element is not found in the card list.");
            }
        }
        System.out.println("Fugitive list size: "+fugitives.size());

    }

    /**
     * Pushes the button for loading more people if it is available.
     */
    public void pushButtonForLoadingPeople(){
        if (!scrapeDriver.findElements(By.cssSelector("#dahaFazlaYukleBtn")).isEmpty()){
            WebElement getMorePeopleButton = scrapeDriver.findElement(By.cssSelector("#dahaFazlaYukleBtn"));
            if (getMorePeopleButton.isDisplayed())
                getMorePeopleButton.click();
        }
    }

    /**
     * Accepts the cookie if it is available.
     */
    public void acceptCookie(){
        if(!scrapeDriver.findElements(By.cssSelector(".alert .acceptcookies")).isEmpty()){
            WebElement acceptButton = scrapeDriver.findElement(By.cssSelector(".alert .acceptcookies"));
            if (acceptButton.isDisplayed())
                acceptButton.click();
        }
    }

    /**
     * Sleeps the thread for 20 milliseconds if the size of the fugitives list is a multiple of 10.
     */
    public void sleepPeriodically(){
        if (fugitives.size() % 10 == 0)
            Sleep.sleep(20);
    }

    /**
     * Closes the ChromeDriver.
     */
    public void close(){
        scrapeDriver.close();
    }
}
