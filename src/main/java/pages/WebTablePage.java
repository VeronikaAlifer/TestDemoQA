package pages;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class WebTablePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebTableRegistrationForm registrationForm;

    private By addNewRecordButton = By.id("addNewRecordButton");
    private By searchBox = By.id("searchBox");
    private By grid = By.className("rt-table");
    private By records = By.xpath("//*[contains(@class, 'rt-tr') and (contains(@class, '-even') or contains(@class, '-odd')) and not(contains(@class, '-padRow'))]");

    public WebTablePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        registrationForm = new WebTableRegistrationForm(driver);
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

    public WebTableRegistrationForm getRegistrationForm() {
        return registrationForm;
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

    public List<WebElement> getRecordsList() {
        return driver.findElements(records);
    }

    public boolean isRecordPresent() throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader("src/test/resources/person_test_data.csv"));
        List<String[]> list = reader.readAll();
        List<String[]> recordList = new LinkedList<>();

        List<WebElement> rows = getRecordsList();

        for (int index = 3; index < rows.size(); index++) {
            WebElement row = rows.get(index);
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            String[] text = new String[6];

            for (int i = 0; i < cells.size() - 1; i++) {
                text[i] = cells.get(i).getText().trim();
            }
            recordList.add(text);
        }

        list.removeFirst();

        boolean isEqual = true;

        for (int i = 0; i < list.size(); i++) {
            String[] csvRecord = list.get(i);
            String[] webRecord = recordList.get(i);

            Set<String> list1 = new HashSet<>(Arrays.asList(csvRecord));
            Set<String> list2 = new HashSet<>(Arrays.asList(webRecord));

            if (!list1.equals(list2)) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }


    public boolean isNewUserPresent(String userFirstName, String userLastName) {
        List<WebElement> records = getRecordsList();
        boolean isTrue = false;
        for (WebElement record : records) {
            List<WebElement> cells = record.findElements(By.className("rt-td"));
            String firstName = cells.get(0).getText();
            String latsName = cells.get(1).getTagName();

            if (firstName.equals(userFirstName) || latsName.equals(userLastName)) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

}
