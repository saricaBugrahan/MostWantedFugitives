package com.fugitives.scraping;
import com.fugitives.common.ImageDownloader;
import com.fugitives.common.Sleep;
import com.fugitives.db.RedisClient;

import com.fugitives.queue.RabbitMQClient;
import lombok.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;
import java.util.logging.Logger;


public class FugitiveScraper {

    private static FugitiveScraper fugitiveScraper;
    private final WebDriver scrapeDriver;
    private final String baseURL = "https://www.terorarananlar.pol.tr";

    private final Logger logger = Logger.getLogger(FugitiveScraper.class.getName());
    private final ImageDownloader imageDownloader;
    private final RabbitMQClient rabbitMQClient;

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
        chromeOptions.addArguments("acceptInsecureCerts");
        chromeOptions.addArguments("--ignore-certificate-errors");
        scrapeDriver = new ChromeDriver(chromeOptions);
        imageDownloader = ImageDownloader.getInstance();
        rabbitMQClient = new RabbitMQClient();

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
        navigateIntoScrapingPage(listColor);
        List<WebElement> listCards = scrapeDriver.findElements(By.cssSelector("div.deactivated-list-card"));
        for(WebElement childElementByClassName: listCards){
            Fugitive fugitive = getFugitive(childElementByClassName);
            if (fugitive == null){
                continue;
            }
            if (isFugitiveDuplicate(fugitive)){
                continue;
            }
            fugitive.setColor(listColor.getColorName());
            saveFugitive(fugitive);
            fugitive.setB64Image(getFugitiveB64Image(childElementByClassName));
            sendFugitiveToQueue(fugitive);
        }
    }

    /**
     * Pushes the button for loading more people if it is available.
     */
    private void pushButtonForLoadingPeople(){
        while (!scrapeDriver.findElements(By.cssSelector("#dahaFazlaYukleBtn")).isEmpty()){
            WebElement getMorePeopleButton = scrapeDriver.findElement(By.cssSelector("#dahaFazlaYukleBtn"));
            if (getMorePeopleButton.isDisplayed()){
                getMorePeopleButton.click();
                Sleep.sleep(100);
            } else
                break;
        }
    }

    /**
     * Accepts the cookie if it is available.
     */
    private void acceptCookie(){
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
    //TODO: Check for the indexOutOfBoundsException
    private String getImage(@NonNull WebElement element){
        try {
            return imageDownloader.downloadImage(
                    baseURL + element.getAttribute("style").substring(
                            element.getAttribute("style").indexOf('"')+1,
                            element.getAttribute("style").lastIndexOf('"'))
            );
        } catch (IndexOutOfBoundsException indexOutOfBoundsException){
            logger.warning("URL does not fit into the format!: " + indexOutOfBoundsException.getMessage());
            return null;
        }
    }

    /**
     * Checks if the fugitive is duplicate.
     * @param fugitive the fugitive
     * @return true if the fugitive is duplicate, false otherwise
     */
    private boolean isFugitiveDuplicate(Fugitive fugitive){
        return RedisClient.getInstance().getObject(fugitive.getHashID()) != null;
    }

    /**
     * Saves the fugitive to the Redis.
     * @param fugitive the fugitive
     */
    private void saveFugitive(Fugitive fugitive){
        RedisClient.getInstance().saveObject(fugitive.getHashID(),fugitive);
    }

    /**
     * Navigates into the scraping page for the given color of the list.
     * @param listColor the color of the list
     */
    private void navigateIntoScrapingPage(FugitiveColorEnum listColor){
        scrapeDriver.get(baseURL);
        Sleep.sleep(1000);
        WebElement element = scrapeDriver.findElement(By.cssSelector("div.wanted-group:nth-child("+listColor.getColor()+")"));
        acceptCookie();
        element.click();
        Sleep.sleep(3000);
        pushButtonForLoadingPeople();
    }

    /**
     * Gets the fugitive from the given WebElement.
     * @param childElement the WebElement
     * @return the fugitive
     */
    //TODO: Check for the not displayed elements
    private Fugitive getFugitive(WebElement childElement){
        if(!childElement.isDisplayed()){
            logger.warning("Web Element is not displayed");
            return null;
        }
        if(childElement.getText().isEmpty()){
            logger.warning("Web Element text is empty");
            return null;
        }
        String[] childElementText = childElement.getText().split("\n");
        String[] name = childElementText[0].split(" ");
        String[] birthPlaceAndDate = childElementText[1].split("-");
        return new Fugitive(name[0], name[1], birthPlaceAndDate[0], birthPlaceAndDate[1],
                childElementText[2], null,null);
    }

    /**
     * Gets the image URL from the given WebElement.
     * @param childElement the WebElement
     * @return the image URL
     */
    private String getFugitiveB64Image(WebElement childElement){
        WebElement childElementImageByClassName;
        try {
            childElementImageByClassName = childElement.findElement(By.id("terroristPhoto"));
        } catch (NoSuchElementException noSuchElementException){
            logger.warning("Web Element does not have image file"+noSuchElementException.getMessage());
            return null;
        }
        return getImage(childElementImageByClassName);
    }

    private void sendFugitiveToQueue(Fugitive fugitive){
        rabbitMQClient.sendMessage(fugitive);
    }

}