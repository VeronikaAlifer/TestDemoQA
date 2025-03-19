package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.PracticeFormPage;
import pages.RegistrationFormModalForm;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PracticeFormPageTest {

    private WebDriver driver;
    private PracticeFormPage practiceFormPage;
    private ExtentReports reports;
    private ExtentTest log;
    private RegistrationFormModalForm modalForm;

    private static final String URL = "https://demoqa.com/automation-practice-form";

    @BeforeClass
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        log = reports.createTest(method.getName());
        log.info("Initializing WevDriver ...");
        driver = new ChromeDriver();

        log.info("Maximizing window and setting timeouts...");
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        log.info("Opening the automation practice form page.");
        driver.get(URL);
        practiceFormPage = new PracticeFormPage(driver);
        modalForm = new RegistrationFormModalForm(driver);
    }

    @Test
    public void testSuccessfulFormFiling() throws InterruptedException {
        String firstName = "UserFirstName";
        String lastName = "UserLastName";
        String studentName = firstName + " " + lastName;
        String expectedTitleText = "Thanks for submitting the form";

        HashMap<String, String> formData = new HashMap<>();
        formData.put("Student Name", studentName);
        formData.put("Student Email", "example@gmail.com");
        formData.put("Mobile", "0123456789");
        formData.put("Gender", "Female");

        log.info("Filling out the form with test data.");
        practiceFormPage.typeFirstName(firstName);
        practiceFormPage.typeLastName(lastName);
        practiceFormPage.typeUserNumber(formData.get("Mobile"));
        practiceFormPage.typeUserEmail(formData.get("Student Email"));
        practiceFormPage.selectGender(formData.get("Gender"));
        practiceFormPage.pickDateOfBirth();

        log.info("Clicking 'submit' button.");
        practiceFormPage.clickSubmitButton();

        log.info("Verifying that form submission was successful.");
        Assert.assertTrue(practiceFormPage.isModalFormVisible(), "The modal title is not displayed.");

        log.info("Verifying modal form title.");
        String actualText = modalForm.getTitle();
        Assert.assertEquals(actualText, expectedTitleText, "The modal title text does not match!");

        log.info("Checking that the modal form is filled out correctly.");
        for (int i = 0; i < formData.size(); i++) {
            String label = practiceFormPage.getCellData(i, 0);
            String value = practiceFormPage.getCellData(i, 1);
            String expectedValue = formData.get(label);
            Assert.assertEquals(value, expectedValue, "Value mismatch for label: " + label);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log.pass("The test passed successfully.");
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
