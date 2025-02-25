package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RadioButtonPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By yesRadioBtn = By.xpath("//label[text()='Yes']");
    private By impressiveRadioBtn = By.xpath("//label[text()='Impressive']");
    private By noRadioBtn = By.id("noRadio");
    private By resultTextLocator = By.className("text-success");

    public RadioButtonPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void openPage() {
        driver.get("https://demoqa.com/radio-button");

    }

    public void selectYesRadioBtn() {
        driver.findElement(yesRadioBtn).click();

    }

    public void selectImpressiveRadioBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(impressiveRadioBtn))).click();
    }

    public void selectNoRadio() {
        driver.findElement(noRadioBtn).click();
    }

    public boolean isYesRadioBtnSelected() {
        return driver.findElement(By.id("yesRadio")).isSelected();
    }

    public boolean isImpressiveRadioBtnSelected() {
        return driver.findElement(By.id("impressiveRadio")).isSelected();
    }

    public String getResultText() {
        try {
            return driver.findElement(resultTextLocator).getText();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public WebElement getResultTextElement() {
        try {
            return driver.findElement(resultTextLocator);
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    public boolean isElementDisplayed(WebElement element) {
        if (element == null) {
            return false;
        }
        return element.isDisplayed();
    }
}
