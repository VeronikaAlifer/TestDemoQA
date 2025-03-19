package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScrollUtils;

import java.time.Duration;
import java.util.List;

public class PracticeFormPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By userEmailField = By.id("userEmail");
    private By userNumberField = By.id("userNumber");
    private By dateOfBirthInput = By.id("dateOfBirthInput");
    private By subjectsContainer = By.id("subjectsContainer");
    private By submitBtn = By.id("submit");
    private By genderLabel = By.id("genterWrapper");
    private By genderRadioBtn = By.xpath("//input[@name='gender']");
    private By table = By.xpath("//table[@class='table table-dark table-striped table-bordered table-hover']");
    private By modalForm = By.className("modal-content");


    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void typeFirstName(String name) {
        driver.findElement(firstNameField).sendKeys(name);
    }

    public void typeLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    public void typeUserEmail(String email) {
        driver.findElement(userEmailField).sendKeys(email);
    }

    public void typeUserNumber(String phoneNumber) {
        driver.findElement(userNumberField).sendKeys(phoneNumber);
    }

    public void pickDateOfBirth() throws InterruptedException {
        WebElement element = driver.findElement(dateOfBirthInput);
        //    element.click();
        //  element.clear();
        Thread.sleep(2000);
        element.sendKeys("23 Oct 2024");
        // element.click();
    }

    public void typeSubjects() {
    }

    public void clickSubmitButton() {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("scroll(0, 700)");
        ScrollUtils.scroll(driver, driver.findElement(submitBtn));
        driver.findElement(submitBtn).click();
    }

    public void selectGender(String parameter) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(genderLabel));
        List<WebElement> genders = driver.findElements(genderRadioBtn);
        ScrollUtils.scroll(driver, element);
        Actions actions = new Actions(driver);

        for (WebElement el : genders) {
            String gender = el.getAttribute("value");
            if (gender.equals(parameter)) {
                actions.click(el).perform();
            }
        }
    }

    public List<WebElement> getRows() {
        WebElement element = driver.findElement(table);
        return element.findElements(By.cssSelector("tbody tr"));
    }

    public String getCellData(int rowIndex, int collIndex) {
        return getRows().get(rowIndex).findElements(By.tagName("td")).get(collIndex).getText();
    }

    private boolean isElementDisplayed(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementVisible(String elementLabel) {
        return switch (elementLabel) {
            case "First Name" -> isElementDisplayed(firstNameField);
            case "Last Name" -> isElementDisplayed(lastNameField);
            case "Email" -> isElementDisplayed(userEmailField);
            case "Mobile" -> isElementDisplayed(userNumberField);
            default -> false;
        };
    }

    public boolean isModalFormVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalForm));
            return driver.findElement(modalForm).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}