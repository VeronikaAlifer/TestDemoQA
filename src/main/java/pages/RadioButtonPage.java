package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class RadioButtonPage {

    private WebDriver driver;
    private By yesRadioBtn = By.id("yesRadio");
    private By impressiveRadioBtn = By.id("impressiveRadio");
    private By noRadioBtn = By.id("noRadio");

    public RadioButtonPage (WebDriver driver) {
        this.driver = driver;
    }

    public  void openPage() {
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/radio-button");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
    }

    public void clickOnYesRadioBtn(){
        driver.findElement(yesRadioBtn).click();
    }

    public void clickOnImpressiveRadioBtn(){
        driver.findElement(impressiveRadioBtn).click();
    }

    public void clickOnNoRadio (){
        driver.findElement(noRadioBtn).click();
    }
}
