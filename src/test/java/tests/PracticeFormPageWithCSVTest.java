package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
        log = reports.createTest(method.getDeclaringClass().getSimpleName() + " - " + method.getName())
                .assignCategory(method.getDeclaringClass().getSimpleName());

        log.info("Initializing WebDriver...");
        driver = new ChromeDriver();

        log.info("Maximizing window and setting timeouts...");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        formPage = new PracticeFormPage(driver);
    }

    @Test
    public void setNameFieldsUsingListTest() {
        try (CSVReader reader = new CSVReader(new FileReader(TestDataFile))) {
            rows = reader.readAll();
        } catch (IOException | CsvException e) {
            log.fail("Error reading CSV file: " + e.getMessage());
            throw new RuntimeException("Failed to read test data from CSV", e);
        }

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

        formPage.typeFirstName(firstName);
        formPage.typeLastName(lastName);
        formPage.typeUserNumber(phone);
        formPage.typeUserEmail(email);

        log.info("Clicking submit button.");
        formPage.clickSubmitButton();

        log.info("Verifying that form submission was not successful.");
        Assert.assertFalse(formPage.isModalFormVisible(), "The modal form visible but should not.");
    }

    @Test(dataProvider = "CSVData")
    public void setNameFieldsUsingDataProviderTest(String firstName, String lastName, String email, String phoneNumber) {
        log.info("Opening the automation practice form page.");
        driver.get(URL);

        log.info("Checking visibility of required fields.");
        String[] fieldLabels = {"First Name", "Last Name", "Email", "Mobile"};

        for (String value : fieldLabels) {
            Assert.assertTrue(formPage.isElementVisible(value), String.format("%s element is not visible", value));
        }

        log.info("Filling out the form with test data.");
        formPage.typeFirstName(firstName);
        formPage.typeLastName(lastName);
        formPage.typeUserEmail(email);
        formPage.typeUserNumber(phoneNumber);

        log.info("Clicking submit button.");
        formPage.clickSubmitButton();

        log.info("Verifying that form submission should fail due to validation errors.");
        Assert.assertFalse(formPage.isModalFormVisible(),
                "The modal form was displayed, but submission was expected to fail.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log.pass("Test passed successfully!");
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

    @DataProvider(name = "CSVData")
    private Object[][] readCSVData() {
        try (CSVReader reader = new CSVReader(new FileReader(TestDataFile))) {
            rows = reader.readAll();
        } catch (IOException | CsvException e) {
            log.fail("Error reading CSV file: " + e.getMessage());
            throw new RuntimeException("Failed to read test data from CSV", e);
        }

        Object[][] data = new Object[rows.size() - 1][4];

        for (int i = 1; i < rows.size(); i++) {
            data[i - 1][0] = rows.get(i)[0];
            data[i - 1][1] = rows.get(i)[1];
            data[i - 1][2] = rows.get(i)[2];
            data[i - 1][3] = rows.get(i)[3];
        }

        return data;
    }
}
