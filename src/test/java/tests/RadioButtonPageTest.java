package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.RadioButtonPage;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.time.Duration;

public class RadioButtonPageTest {
    private WebDriver driver;
    private RadioButtonPage radioButtonPage;
    private ExtentReports reports;
    private ExtentTest log;


    @BeforeTest
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        log = reports.createTest(method.getName());
        log.info("Initializing webDriver...");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        radioButtonPage = new RadioButtonPage(driver);
        log.info("Opening radio button page.");
        radioButtonPage.openPage();
    }

    @Test
    public void testSelectingYesRadioBtn() {
        log.info("Checking visibility of required text.");
        Assert.assertFalse(radioButtonPage.isElementDisplayed(radioButtonPage.getResultTextElement()),
                "Result text should not be displayed before selecting any radio button.");

        log.info("Selecting 'Yes' radio button.");
        radioButtonPage.selectYesRadioBtn();

        log.info("Checking that the 'Yes' radio button was selected.");
        Assert.assertTrue(radioButtonPage.isYesRadioBtnSelected(),
                "The 'Yes' radio button should be selected after clicking it.");

        log.info("Checking visibility of required text.");
        String expectedText = "Yes";
        String actualText = radioButtonPage.getResultText();
        Assert.assertEquals(actualText, expectedText, "The actual text does not match with expected text");
    }

    @Test
    public void testSelectingImpressiveRadioBtn() {
        Assert.assertFalse(radioButtonPage.isElementDisplayed(radioButtonPage.getResultTextElement()), "Result text should not be displayed before selecting any radio button.");

        radioButtonPage.selectImpressiveRadioBtn();
        Assert.assertTrue(radioButtonPage.isImpressiveRadioBtnSelected(), "The 'Impressive' radio button should be selected after clicking it.");

        String expectedText = "Impressive";
        String actualText = radioButtonPage.getResultText();
        Assert.assertEquals(actualText, expectedText, "The actual text does not match with expected text");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void tearDownReports() {
        reports.flush();
    }
}
