package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class RegistrationFormModalForm {
    private WebDriver driver;
    private WebDriverWait wait;
    private By modalForm = By.className("modal-content");
    private By title = By.id("example-modal-sizes-title-lg");
    private By submitBtn = By.id("submit");
    private By userForm = By.id("userForm");


    public RegistrationFormModalForm(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public String getTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(title));
        return driver.findElement(title).getText();
    }

    public boolean isDisplayed() {
        try {
            return driver.findElement(modalForm).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickSubmitBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        driver.findElement(submitBtn).click();
    }

}
