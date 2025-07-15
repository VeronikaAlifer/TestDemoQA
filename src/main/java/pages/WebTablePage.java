package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScrollUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebTablePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebTableRegistrationForm registrationForm;

    private By addNewRecordButton = By.id("addNewRecordButton");
    private By searchBox = By.id("searchBox");
    private By grid = By.className("rt-table");
    private By records = By.xpath("//*[contains(@class, 'rt-tr') and (contains(@class, '-even') or contains(@class, '-odd')) and not(contains(@class, '-padRow'))]");
    private By rowsSizeDropDownElm = By.tagName("select");
    private By previousBtn = By.xpath("//div[@class = '-previous']");
    private By nextBtn = By.xpath("//div[@class = '-next']");
    private By pageInfo = By.cssSelector("input[aria-label='jump to page']");

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

    public List<String> getGridColumnHeaders() {
        WebElement gridElement = driver.findElement(grid);
//        gridElement.findElements(By.xpath("//div[@role='columnheader']"));
        List<WebElement> headerElements = gridElement.findElements(By.className("rt-resizable-header-content"));
        List<String> headers = new ArrayList<>();

        for (int i = 0; i < headerElements.size(); i++) {
            headers.add(i, headerElements.get(i).getText());
        }
        return headers;
    }

    public WebTableRegistrationForm clickAdd() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewRecordButton)).click();
        return new WebTableRegistrationForm(driver);
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

    public boolean isUserPresent(String userFirstName, String userLastName) {
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

    public WebTableRegistrationForm performEditAction(String firstName, String lastName) {

        String xpath = String.format(
                "//div[contains(@class,'rt-tr')" +
                        "  and .//div[normalize-space(.)='%s']" +
                        "  and .//div[normalize-space(.)='%s']" +
                        "]//span[@title='Edit']",
                firstName, lastName
        );
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            element.click();
            return new WebTableRegistrationForm(driver);
        } catch (TimeoutException e) {
            throw new NoSuchElementException(
                    String.format("Edit button not found for '%s %s'", firstName, lastName), e);
        }
    }

    public String getColumnDataByName(String firstName, String lastName, String columnName) {
        List<String> headers = getGridColumnHeaders();
        int salaryColumnIndex = headers.indexOf(columnName);
        int firstNameColumnIndex = headers.indexOf("First Name");
        int lastNameColumnIndex = headers.indexOf("Last Name");

        List<WebElement> list = getRecordsList();
        List<WebElement> cells;
        for (int i = 0; i < list.size(); i++) {
            cells = list.get(i).findElements(By.xpath("//div[@role ='gridcell']"));
            String firstValue = cells.get(firstNameColumnIndex).getText();
            String secondValue = cells.get(lastNameColumnIndex).getText();

            if (firstValue.equals(firstName) && secondValue.equals(lastName)) {
                return cells.get(salaryColumnIndex).getText();
            }
        }
        return null;
    }

    public List<WebElement> getRows() {
        return driver.findElements(By.xpath(".//div[@class = 'rt-tr-group']"));
    }

    public WebElement getRow(int rowIndex) {
        List<WebElement> rows = getRows();
        if (rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException("Row index out of bounds");
        }
        return rows.get(rowIndex);
    }

    public WebElement getCell(int rowIndex, int cellIndex) {
        WebElement row = getRow(rowIndex);
        List<WebElement> cells = getCellsFromRow(row);
        return cells.get(cellIndex);
    }

    public List<WebElement> getCellsFromRow(WebElement row) {
        return row.findElements(By.xpath(".//div[@class = 'rt-td']"));
    }

    public String geCellText(int rowIndex, int cellIndex) {
        WebElement row = getRow(rowIndex);
        List<WebElement> cells = getCellsFromRow(row);
        if (cellIndex >= cells.size()) {
            throw new IndexOutOfBoundsException("Column index out of bounds");
        }
        return cells.get(cellIndex).getText();
    }

    public void deleteRowByName(String firstName, String lastName) {
        String xpath = String.format(
                "//div[contains(@class,'rt-tr')" +
                        "  and .//div[normalize-space(.)='%s']" +
                        "  and .//div[normalize-space(.)='%s']" +
                        "]//span[@title='Delete']",
                firstName, lastName
        );

        driver.findElement(By.xpath(xpath)).click();
    }

    public void enterTextInSearchBox(String text) {
        WebElement element = driver.findElement(searchBox);
        element.click();
        element.sendKeys(text);
    }

    public void cleanSearchBox() {
        driver.findElement(searchBox).clear();
    }

    public void setUpGridRowsSize(String rowSize) {
        Select select = getSelectElement();
        select.selectByValue(rowSize);
    }

    public String getSelectedOption() {
        Select select = getSelectElement();
        return select.getFirstSelectedOption().getText();
    }

    private Select getSelectElement() {
        WebElement element = driver.findElement(rowsSizeDropDownElm);
        return new Select(element);
    }

    public void clickNextBtn() {
        clickBtn(nextBtn);
    }

    public void clickPreviousBtn() {
        clickBtn(previousBtn);
    }
    public String getPageInfo() {
        return driver.findElement(pageInfo).getAttribute("value");
    }

    private void clickBtn(By by) {
        WebElement element = driver.findElement(by);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ScrollUtils.scroll(driver, element);
        element.click();
    }
}
