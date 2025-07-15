package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.WebTablePage;
import pages.WebTableRegistrationForm;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.time.Duration;

public class WebTableRegistrationFormTest {

    private static final String VALIDATION_FIELD_MESSAGE = "Please match the format requested.";
    private static final String WEB_TABLE_URL = "https://demoqa.com/webtables";

    private WebTableRegistrationForm modalForm;
    private WebTablePage webTablePage;
    private WebDriver driver;
    private ExtentReports reports;
    private ExtentTest log;

    @BeforeClass
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setUp(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        log = reports.createTest(className + " - " + method.getName())
                .assignCategory(className);

        driver = new ChromeDriver();

        log.info("Launching browser and navigating to the web page.");
        driver.get(WEB_TABLE_URL);
        driver.manage().window().maximize();
        webTablePage = new WebTablePage(driver);
    }

    @Test(description = "TC_003 – Добавление записи с пустыми полями")
    public void verifyAddingEmptyFields() {
        log.info("Verify that the table is present.");
        boolean isTablePresent = webTablePage.isGridDisplayed();
        Assert.assertTrue(isTablePresent, "The table isn't present, but it should be.");

        openModalForm();

        log.info("Verify that the modal form is visible and is not validated.");
        boolean isModalFormPresent = modalForm.isDisplayed();
        Assert.assertTrue(isModalFormPresent, "Expected the modal form to be visible after clicking 'Add'.");

        boolean isValidated = modalForm.isUserFormValidated();
        Assert.assertFalse(isValidated, "Expected form to be not validated before submission.");

        log.info("Clicking on the button 'Submit'");
        modalForm.submitForm();
        isValidated = modalForm.isUserFormValidated();
        isModalFormPresent = modalForm.isDisplayed();

        Assert.assertTrue(isValidated, "Expected form to be validated after submitting empty fields.");
        Assert.assertTrue(isModalFormPresent, "Expected modal form to remain visible after validation.");
        Assert.assertTrue(modalForm.areFormFieldsEmpty(), "Expected modal forms fields must be empty");
    }

    @Test(description = "TC_004 – Валидация email при добавлении записи")
    public void testEmailField() {
        openModalForm();

        log.info("Fill email field with invalid format");
        modalForm.enterEmail("ivan@").submitForm();

        log.info("Verify modal form");
        boolean isValidated = modalForm.isUserFormValidated();
        boolean isModalFormPresent = modalForm.isDisplayed();
        String validationMessage = modalForm.getEmailValidationMessage();

        Assert.assertEquals(validationMessage, VALIDATION_FIELD_MESSAGE, "The validation message does not match.");
        Assert.assertTrue(isValidated, "Expected form to be validated after submitting form with invalid email field.");
        Assert.assertTrue(isModalFormPresent, "Expected modal form to remain visible after validation.");
    }

    @Test(description = "TC_016 – Валидация salary при добавлении записи")
    public void testValidationSalaryField() {
        String invalidSalaryValue = "salary";
        openModalForm();

        log.info("Fill salary input field with invalid format.");
        modalForm.enterSalary(invalidSalaryValue);
        modalForm.submitForm();

        log.info("Verify modal form");
        boolean isValidated = modalForm.isUserFormValidated();
        Assert.assertTrue(modalForm.isDisplayed(), "Expected modal form to remain visible after validation.");

        String validationMessage = modalForm.getSalaryValidationMessage();
        Assert.assertEquals(validationMessage, VALIDATION_FIELD_MESSAGE, "The validation message does not match.");
        Assert.assertTrue(isValidated, "Expected form to be validated after submitting form with invalid salary field.");
    }

    @Test(description = "TC_017– Валидация age при добавлении записи")
    public void testValidationAgeField() {
        String invalidAgeValue = "fd";
        openModalForm();

        log.info("Fill age input field with invalid format.");
        modalForm.enterAge(invalidAgeValue).submitForm();

        log.info("Verify modal form.");
        boolean isValidated = modalForm.isUserFormValidated();
        Assert.assertTrue(modalForm.isDisplayed(), "Expected modal form to remain visible after validation.");

        String validationMessage = modalForm.getAgeValidationMessage();
        Assert.assertEquals(validationMessage, VALIDATION_FIELD_MESSAGE, "The validation message does not match.");
        Assert.assertTrue(isValidated, "Expected form to be validated after submitting form with invalid age field.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            log.pass("Test passed!!");
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.skip("Test was skipped!");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            log.fail(result.getThrowable().getMessage())
                    .addScreenCaptureFromBase64String(getScreenShotBase64String());
        }
        if (driver != null) {
            driver.quit();
            log.info("Closing browser.");
        }
    }

    @AfterClass
    public void tearDownReports() {
        reports.flush();
    }

    private String getScreenShotBase64String() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    private void openModalForm() {
        log.info("Open modal form.");
        modalForm = webTablePage.clickAdd();
        boolean isModalFormPresent = modalForm.isDisplayed();
        Assert.assertTrue(isModalFormPresent, "Expected modal form is visible.");

    }
}
