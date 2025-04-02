package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<WebElement> getGridColumn() {
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

    public boolean isRecordPresent(Map<String, String> data) {
        WebElement gridElement = driver.findElement(grid);

        List<WebElement> rows = gridElement.findElements(By.className("rt-tr-group"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));

            if (data.get("firstName").equals(cells.getFirst().getText())) {
                List<String> texts = cells.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
                texts.removeLast();

                return texts.equals(data.values().stream().toList());
            }
        }

        return false;
    }
}
