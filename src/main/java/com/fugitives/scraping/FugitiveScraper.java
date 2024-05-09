package com.fugitives.scraping;
import com.fugitives.common.ImageDownloader;
import com.fugitives.common.Sleep;
import lombok.Getter;
import lombok.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;


public class FugitiveScraper {

    private static FugitiveScraper fugitiveScraper;
    private final WebDriver scrapeDriver;
    private final String baseURL = "https://www.terorarananlar.pol.tr";

    @Getter
    private static LinkedList<Fugitive> fugitives;
    private final Logger logger = Logger.getLogger(FugitiveScraper.class.getName());
    private final ImageDownloader imageDownloader = ImageDownloader.getInstance();


    /**
     * Constructor for FugitiveScraper
     * It initializes the ChromeDriver with headless mode
     * and initializes the fugitives list.
     */
    private FugitiveScraper(){
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver-linux64/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized"); // open Browser in maximized mode
        chromeOptions.addArguments("disable-infobars"); // disabling infobars
        chromeOptions.addArguments("--disable-extensions"); // disabling extensions
        chromeOptions.addArguments("--disable-gpu"); // applicable to windows os only
        chromeOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model
        chromeOptions.addArguments("--headless"); // run in headless mode
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("acceptInsecureCerts=true");



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
        scrapeDriver.get(baseURL);
        WebElement element = scrapeDriver.findElement(By.cssSelector("div.wanted-group:nth-child("+listColor.getColor()+")"));
        element.click();
        Sleep.sleep(300);
        acceptCookie();

        pushButtonForLoadingPeople();

        List<WebElement> listCards = scrapeDriver.findElements(By.cssSelector("div.deactivated-list-card"));
        WebElement childElementImageByClassName;
        for(WebElement childElementByClassName: listCards){
            if(!childElementByClassName.isDisplayed()){
                logger.warning("Web Element is not displayed");
                continue;
            }
            if(childElementByClassName.getText().isEmpty()){
                logger.warning("Web Element text is empty");
                continue;
            }
            try {
                childElementImageByClassName = childElementByClassName.findElement(By.id("terroristPhoto"));
            } catch (NoSuchElementException noSuchElementException){
                logger.warning("Web Element does not have image file"+noSuchElementException.getMessage());
                continue;
            }
            String[] childElementText = childElementByClassName.getText().split("\n");
            String[] name = childElementText[0].split(" ");
            String[] birthPlaceAndDate = childElementText[1].split("-");

            Fugitive fugitive = new Fugitive(name[0], name[1], birthPlaceAndDate[0], birthPlaceAndDate[1],
                    childElementText[2], listColor.getColorName(),null);
            //getImage(childElementImageByClassName)
            fugitives.add(fugitive);
            System.out.println(fugitive);

        }
        logger.info("Size: "+fugitives.size());
    }

    /**
     * Pushes the button for loading more people if it is available.
     */
    public void pushButtonForLoadingPeople(){
        while (!scrapeDriver.findElements(By.cssSelector("#dahaFazlaYukleBtn")).isEmpty()){
            WebElement getMorePeopleButton = scrapeDriver.findElement(By.cssSelector("#dahaFazlaYukleBtn"));
            if (getMorePeopleButton.isDisplayed()){
                getMorePeopleButton.click();
                Sleep.sleep(400);
            } else
                break;


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
     * Closes the ChromeDriver.
     */
    public void close(){
        scrapeDriver.close();
    }

    /**
     * Gets the image URL from the given WebElement.
     * @param element the WebElement
     * @return the image URL
     */
    public String getImage(@NonNull WebElement element){
        return imageDownloader.downloadImage(
                baseURL + element.getAttribute("style").substring(
                        element.getAttribute("style").indexOf('"')+1,
                        element.getAttribute("style").lastIndexOf('"'))
        );
    }
}