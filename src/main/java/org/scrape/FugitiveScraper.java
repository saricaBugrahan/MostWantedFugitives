package org.scrape;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class FugitiveScraper {

    private static FugitiveScraper fugitiveScraper;
    private final WebDriver scrapeDriver;
    private FugitiveScraper(){
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--accept-cookies"); // Automatically accept cookies
        scrapeDriver = new ChromeDriver(chromeOptions);
    }

    public static FugitiveScraper getInstance(){
        if (fugitiveScraper == null){
            fugitiveScraper = new FugitiveScraper();
        }
        return fugitiveScraper;
    }

    public void scrape(){
        scrapeDriver.get("https://www.terorarananlar.pol.tr/tarananlar");
        pushButtonForLoadingPeople();
        WebElement childElementByClassName;
        for(int i = 1; i < 430; i++){
            try {
                childElementByClassName = scrapeDriver.findElement(By.cssSelector("div.deactivated-list-card:nth-child(" + i + ")"));
                System.out.println(childElementByClassName.getText());
                if(i%10 == 0){
                    pushButtonForLoadingPeople();
                }
            } catch (Exception e) {
                System.out.println("Element not found");
            }

        }
    }
    public void pushButtonForLoadingPeople(){
        WebElement getMorePeopleButton = scrapeDriver.findElement(By.cssSelector("#dahaFazlaYukleBtn"));
        getMorePeopleButton.click();
        sleep(2000);
        if (!scrapeDriver.findElements(By.className("alert")).isEmpty()) {
            WebElement acceptButton = scrapeDriver.findElement(By.cssSelector(".alert .acceptcookies"));
            acceptButton.click();
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//div.deactivated-list-card:nth-child(1)
//div.deactivated-list-card:nth-child(354)