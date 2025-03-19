package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.PracticeFormPage;
import utils.ExtentReportManager;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class PracticeFormPageWithCSVTest {

    private WebDriver driver;
    private PracticeFormPage formPage;
    private List<String[]> rows;
    private ExtentReports reports;
    private ExtentTest log;

    private static final String URL = "https://demoqa.com/automation-practice-form";
    private static final String TestDataFile = "src/test/resources/test_data.csv";

    @BeforeTest
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) throws IOException, CsvException {
        log = reports.createTest(method.getName());
        log.info("Initializing WebDriver...");
        driver = new ChromeDriver();

        log.info("Maximizing window and setting timeouts...");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        log.info("Reading test data from CSV file.");
        try (CSVReader reader = new CSVReader(new FileReader(TestDataFile))) {
            rows = reader.readAll();
        } catch (IOException | CsvException e) {
            log.fail("Error reading CSV file: " + e.getMessage());
            throw new RuntimeException("Failed to read test data from CSV", e);
        }

        formPage = new PracticeFormPage(driver);

    }

    @Test
    public void setNameFieldsUsingListTest() {
        log.info("Opening the automation practice form page.");
        driver.get(URL);

        log.info("Checking visibility of required fields.");
        Assert.assertTrue(formPage.isElementVisible("First Name"),
                "First Name field is not visible.");
        Assert.assertTrue(formPage.isElementVisible("Last Name"),
                "Last Name field is not visible.");
        Assert.assertTrue(formPage.isElementVisible("Email"),
                "Email field field is not visible.");

        log.info("Filling out the form with test data.");
        String firstName = rows.get(1)[0];
        String lastName = rows.get(1)[1];
        String email = rows.get(1)[2];
        String phone = rows.get(1)[3];

        formPage.typeFirsName(firstName);
        formPage.typeLastName(lastName);
        formPage.typeUserNumber(phone);
        formPage.typeUserEmail(email);

        log.info("Clicking submit button.");
        formPage.clickSubmitButton();

        log.info("Verifying that form submission was not successful.");
        Assert.assertFalse(formPage.isModalFormVisible(), "The modal form visible but should not.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log.pass("Test pass successfully");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                log.fail(result.getThrowable().getMessage());
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterTest
    public void tearDownReports() {
        reports.flush();
    }
}
