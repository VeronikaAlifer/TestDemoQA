package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.TextBoxPage;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class TextBoxPageTest {

    private WebDriver driver;
    private TextBoxPage textBoxPage;
    private ExtentReports reports;
    private ExtentTest log;

    private String userName = "Jon Doe";
    private String userEmail = "john.doe@example.com";
    private String currentAddress = "123 Main St, New York, NY";
    private String permanentAddress = "456 Elm St, New York, NY";
    private String invalidUserEmail = "john";

    private static final String URL = "https://demoqa.com/text-box";

    @BeforeClass
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String testName = method.getName();
        Test testAnnotation = method.getAnnotation(Test.class);
        String description = (testAnnotation != null) ? testAnnotation.description() : "no description";

        log = reports.createTest(className + " - " + testName)
                .assignCategory(className)
                .info("<b>"+description+"</b>");

        log.info("Initializing WebDriver..");
        driver = new ChromeDriver();

        log.info("Maximizing the page and setting timeouts.");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        log.info("Opening the Test-box page.");
        driver.get(URL);

        textBoxPage = new TextBoxPage(driver);
    }

    @Test(description = "Verify that all fields are filled correctly and displayed after submission.")
    public void testSuccessFilingFields() throws InterruptedException {
        log.info("Filling out all fields with the test data.");
        textBoxPage.fillAllFields(userName, userEmail, currentAddress, permanentAddress);

        log.info("Verifying that Output field is hidden before form submission.");
        Assert.assertFalse(textBoxPage.getOutputField().isDisplayed(),
                "Output field should be hidden before form submission.");

        log.info("Clicking 'Submit' button.");
        textBoxPage.clickSubmitBtn();

        log.info("Verifying that the Output field is displayed after form submission.");
        Assert.assertTrue(textBoxPage.getOutputField().isDisplayed());

        log.info("Verifying that fields are filled out correctly.");
        List<WebElement> data = textBoxPage.getOutputData();
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Name") && element.getText().contains(userName)), "The name is incorrect.");
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Email") && element.getText().contains(userEmail)), "The email is incorrect.");
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Current Address") && element.getText().contains(currentAddress)), "The current address is incorrect.");
        Assert.assertTrue(data.get(3).getText().contains("Permananet Address") && data.get(3).getText().contains(permanentAddress), "The permanent address is incorrect.");
    }

    @Test(description = "Verifying that Output field is empty after form submission.")
    public void testValidateRequiredFields() throws InterruptedException {
        log.info("Verifying that Output field is hidden.");
        Assert.assertFalse(textBoxPage.getOutputField().isDisplayed(),
                "Output field should be hidden before form submission.");

        Assert.assertFalse(textBoxPage.getOutputField().isDisplayed(),
                "Output field should be hidden before form submission.");

        log.info("Verifying that 'Submit' button is clickable.");
        Assert.assertTrue(textBoxPage.getSubmitBtnElement().isEnabled(),
                "The Submit button is not clickable!");

        log.info("Performing click on 'Submit' button.");
        textBoxPage.clickSubmitBtn();

        log.info("Verifying that the Output data form is empty.");
        Assert.assertTrue(textBoxPage.getOutputData().isEmpty(), "Output data form must be empty!");
    }

    @Test(description = "Verifying that Output field is empty after form submission.")
    public void testInvalidEmailFormat() throws InterruptedException {
        log.info("Filling out the Text-box input fields.");
        textBoxPage.fillAllFields(userName, invalidUserEmail, currentAddress, permanentAddress);

        log.info("Perform click on 'Submit' button.");
        textBoxPage.clickSubmitBtn();

        log.info("Verifying that the Output field is empty");
        Assert.assertTrue(textBoxPage.getOutputData().isEmpty(), "Output data form must be empty!");
    }

    @AfterMethod
    public void closeResources(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log.pass("Test passed successfully!");
            } else if (result.getStatus() == ITestResult.SKIP) {
                log.skip("Test was skipped");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                log.fail(result.getThrowable().getMessage());
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterClass
    public void tearDownReports() {
        reports.flush();
    }
}