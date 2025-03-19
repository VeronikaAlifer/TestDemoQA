package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class WebTablePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private RegistrationFormModalForm modalForm;

    private By addNewRecordButton = By.id("addNewRecordButton");
    private By searchBox = By.id("searchBox");
    private By grid = By.className("rt-table");

    public WebTablePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        modalForm = new RegistrationFormModalForm(driver);
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

    public List<WebElement> getGritColumn() {
        WebElement gridElement = driver.findElement(grid);
        return gridElement.findElements(By.xpath("//div[@role='columnheader']"));
    }

    public void pressOnAddBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewRecordButton));
        driver.findElement(addNewRecordButton).click();
    }

    public RegistrationFormModalForm getModalForm() {
        return modalForm;
    }

    public boolean isRecordPresent (Map<String, String> data)
    {
        WebElement gridElement = driver.findElement(grid);

        List <WebElement> rows =  gridElement.findElements(By.className("rt-tr-group"));

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);

            List<WebElement> cells = row.findElements(By.className("rt-td"));
           

        }

        return false;
    }


}
