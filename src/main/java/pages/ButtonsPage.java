package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScrollUtils;

import java.time.Duration;

public class ButtonsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By clickMeBtn = By.xpath("//button[text()='Click Me']");
    private By dynamicClickMessage = By.id("dynamicClickMessage");
    private By doubleClickBtn = By.id("doubleClickBtn");
    private By doubleClickMessage = By.id("doubleClickMessage");
    private By rightClickBtn = By.id("rightClickBtn");
    private By rightClickMessage = By.id("rightClickMessage");

    public ButtonsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void performClickMeBtn() {
        scrollToClickMeButton();
        wait.until(ExpectedConditions.elementToBeClickable(clickMeBtn)).click();
    }

    public String getDynamicClickMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicClickMessage));
        return driver.findElement(dynamicClickMessage).getText();
    }

    public String getDoubleClickMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(doubleClickMessage));
        return driver.findElement(doubleClickMessage).getText();
    }

    public void performDoubleClickBtn() {
        Actions actions = new Actions(driver);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(doubleClickBtn));
        actions.doubleClick(element).perform();
    }

    public void performRightClickBtn() {
        scrollToRightClickButton();
        wait.until(ExpectedConditions.elementToBeClickable(rightClickBtn));
        Actions actions = new Actions(driver);
        actions.contextClick(driver.findElement(rightClickBtn)).perform();
    }

    public String getRightClickMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(rightClickMessage));
        return driver.findElement(rightClickMessage).getText();
    }

    public void scrollToClickMeButton() {
        ScrollUtils.scroll(driver, driver.findElement(clickMeBtn));
    }

    public void scrollToRightClickButton() {
        ScrollUtils.scroll(driver, driver.findElement(rightClickBtn));
    }
}
