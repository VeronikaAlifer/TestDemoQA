package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebTablePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By addNewRecordButton = By.id("addNewRecordButton");
    private By searchBox = By.id("searchBox");
    private By grid = By.className("rt-table");


    public WebTablePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isAddBtnDisplayed() {
        return driver.findElement(addNewRecordButton).isDisplayed();
    }

    public boolean isSearchBoxDisplayed() {
        return driver.findElement(searchBox).isDisplayed();
    }

    public boolean isGridDisplayed() {
        return driver.findElement(grid).isDisplayed();
    }
}
