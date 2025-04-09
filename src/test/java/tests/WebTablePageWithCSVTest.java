package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.WebTablePage;
import pages.WebTableRegistrationForm;
import testdata.Person;
import utils.CSVReader;
import utils.ExtentReportManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

public class WebTablePageWithCSVTest {

    private WebDriver driver;
    private WebTablePage webTablePage;
    private WebTableRegistrationForm registrationForm;

    private ExtentReports reports;
    private ExtentTest log;

    @BeforeClass
    public void setReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        log = reports.createTest(className + " - " + method.getName())
                .assignCategory(className);

        log.info("Initializing webDriver....");
        driver = new ChromeDriver();
        webTablePage = new WebTablePage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        log.info("Opening the Web table page.");
        driver.get("https://demoqa.com/webtables");
    }

    @Test
    public void verifyAddingNewRecords() throws IOException, CsvException {
        String fileName = "src/test/resources/person_test_data.csv";
        List<Person> people = CSVReader.readPeopleFromCsv(fileName);

        log.info("Opening the registration form by clicking 'Add' button.");
        webTablePage.pressOnAddBtn();

        log.info("Verifying the registration form is displayed.");
        registrationForm = webTablePage.getRegistrationForm();

        Assert.assertTrue(registrationForm.isDisplayed(), "The registration form is not displayed, but should be.");

        log.info("Filling and submitting the registration form for each user.");
        sillAnsSubmitRegistrationForm(people);
        log.info("Verifying number of records in the table.");
        int actualRecordsListSize = webTablePage.getRecordsList().size();
        int expectedRecordListSize = 7;
        Assert.assertEquals(actualRecordsListSize, expectedRecordListSize, "Expected list size does not match.");


        log.info("Verifying that each new user is present on the grid.");
        for (Person person : people) {
            String userName = person.getFirstName();
            String userLastName = person.getLastName();

            Assert.assertTrue(webTablePage.isNewUserPresent(userName, userLastName),
                    String.format("User %s %s not found on the grid.", userName, userLastName));
        }
    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            log.pass("Test passed!!");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            log.fail(result.getThrowable().getMessage())
                    .addScreenCaptureFromBase64String(takeScreenshotBase64());
        }
        if (driver != null) {
            driver.quit();
            log.info("Driver was closed!!");
        }
    }

    @AfterClass
    public void tearDownReport() {
        reports.flush();
    }

    private String takeScreenshotBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    private void sillAnsSubmitRegistrationForm(List<Person> people){

        for (Iterator<Person> iterator = people.iterator(); iterator.hasNext(); ) {

            Person person = iterator.next();
            log.info(String.format("Adding data for user: %s %s", person.getFirstName(), person.getLastName()));
            registrationForm.fillAnsSubmitForm(person);

            if (iterator.hasNext()) {
                webTablePage.pressOnAddBtn();
            }
        }
    }
}
