package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import utils.ScrollUtils;

import java.util.List;

public class PracticeFormPage {
    private WebDriver driver;
    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By userEmailField = By.id("userEmail");
    private By userNumberField = By.id("userNumber");
    private By dateOfBirthInput = By.id("dateOfBirthInput");
    private By subjectsContainer = By.id("subjectsContainer");
    private By submitBtn = By.id("submit");
    private By genderRadioBtn = By.xpath("//input[@name='gender']");
    private By table = By.xpath("//table[@class='table table-dark table-striped table-bordered table-hover']");
    private By modalForm = By.className("modal-content");


    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
    }

    public void typeFirsName(String name) {
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
        List<WebElement> genders = driver.findElements(genderRadioBtn);
        Actions actions = new Actions(driver);

        for (WebElement element : genders) {
            String gender = element.getAttribute("value");
            if (gender.equals(parameter)) {
                actions.click(element).perform();

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
        switch (elementLabel) {
            case "First Name":
                return isElementDisplayed(firstNameField);
            case "Last Name":
               return isElementDisplayed(lastNameField);
            case "Email":
                return isElementDisplayed(userEmailField);
            case "Mobile":
                return isElementDisplayed(userNumberField);
            default:
                return false;
        }
    }

    public boolean isModalFormVisible(){
        try {
            return driver.findElement(modalForm).isDisplayed();
        }catch (NoSuchElementException | StaleElementReferenceException e){
            return false;
        }
    }
}