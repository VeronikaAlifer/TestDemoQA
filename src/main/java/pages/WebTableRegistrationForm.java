package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testdata.Person;

import java.time.Duration;
import java.util.Map;

public class WebTableRegistrationForm {
    private WebDriver driver;
    private WebDriverWait wait;

    private By submitBtn = By.id("submit");
    private By firstName = By.id("firstName");
    private By lastName = By.id("lastName");
    private By userEmail = By.id("userEmail");
    private By age = By.id("age");
    private By salary = By.id("salary");
    private By department = By.id("department");
    private By modalForm = By.className("modal-content");
    private By registrationFormTitle = By.id("registration-form-modal");

    public WebTableRegistrationForm(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void submitForm() {
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        driver.findElement(submitBtn).click();
    }

    public void enterFirstName(String firstName) {
        driver.findElement(this.firstName).sendKeys(firstName);
    }


    public void enterLastName(String firstName) {
        driver.findElement(this.lastName).sendKeys(firstName);
    }


    public void enterEmail(String userEmail) {
        driver.findElement(this.userEmail).sendKeys(userEmail);
    }

    public void enterAge(String age) {
        driver.findElement(this.age).sendKeys(age);
    }

    public void enterSalary(String salary) {
        driver.findElement(this.salary).sendKeys(salary);
    }

    public void enterDepartment(String department) {
        driver.findElement(this.department).sendKeys(department);
    }

    public void fillAnsSubmitForm(Person person) {
        enterFirstName(person.getFirstName());
        enterLastName(person.getLastName());
        enterEmail(person.getEmail());
        enterAge(person.getAge());
        enterSalary(person.getSalary());
        enterDepartment(person.getDepartment());
        submitForm();
    }

    public void fillRegistrationForm(Map<String, String> formData) {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(modalForm));

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            By inputField = By.id(entry.getKey());
            WebElement element = form.findElement(inputField);
            element.clear();
            element.sendKeys(entry.getValue());
        }

        submitForm();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalForm));
    }

    public boolean isDisplayed() {
        try {

            return wait.until(ExpectedConditions.visibilityOfElementLocated(modalForm)).isDisplayed();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public String getTitle() {
        return driver.findElement(registrationFormTitle).getText();
    }
}
