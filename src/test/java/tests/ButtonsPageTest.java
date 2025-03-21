package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.ButtonsPage;
import utils.ExtentReportManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;


public class ButtonsPageTest {
    private WebDriver driver;
    private ButtonsPage buttonsPage;
    private ExtentReports reports;
    private ExtentTest log;
    private final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String URL = "https://demoqa.com/buttons";
    private static final String DYNAMIC_MSG = "You have done a dynamic click";
    private static final String DOUBLE_CLICK_MSG = "You have done a double click";
    private static final String RIGHT_CLICK_MSG = "You have done a right click";

    @BeforeClass
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {

        String className = method.getDeclaringClass().getSimpleName();
        String testName = method.getName();
        Test testAnnotation = method.getAnnotation(Test.class);
        String description = (testAnnotation != null) ? testAnnotation.description() : "No description";
        log = reports.createTest(className + " - " + testName)
                .assignCategory(className)
                .info(description);
        test.set(log);

        log.info("Initializing WebDriver ...");
        driver = new ChromeDriver();

        log.info("Maximizing window and setting timeouts.");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        log.info("Opening Buttons page.");
        driver.get(URL);

        buttonsPage = new ButtonsPage(driver);
    }

    @Test(description = "Verify the functionality of the 'Click Me' button when clicked once.")
    public void testDynamicClickButton() {
        log.info("Clicking the 'Click Me' button.");
        buttonsPage.performClickMeBtn();

        log.info("Verifying the success message.");
        String actualMessage = buttonsPage.getDynamicClickMessage();
        Assert.assertEquals(actualMessage, DYNAMIC_MSG, "Dynamic click message is incorrect!");
    }

    @Test(description = "Verify the functionality of the 'Double Click Me' button when double-clicked.")
    public void testDoubleClickButton() {
        log.info("Performing double-click  on the 'Double-Click' button.");
        buttonsPage.performDoubleClickBtn();

        log.info("Verifying the double-click success message.");
        String actualMessage = buttonsPage.getDoubleClickMessage();
        Assert.assertEquals(actualMessage, DOUBLE_CLICK_MSG, "Double click message is incorrect!");
    }

    @Test(description = "Verify the functionality of the 'Right Click Me' button when right-clicked.")
    public void testRightClickButton() {
        log.info("Performing a right-click on 'Right Click Me' button.");
        buttonsPage.performRightClickBtn();

        log.info("Verifying the right-click success message.");
        String actualMessage = buttonsPage.getRightClickMessage();
        Assert.assertEquals(actualMessage, RIGHT_CLICK_MSG, "Right click message is incorrect!");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                log.pass("Test passed successfully!");
            } else if (result.getStatus() == ITestResult.SKIP) {
                log.skip("Test was skipped!");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                log.fail(result.getThrowable().getMessage())
                        .addScreenCaptureFromBase64String(getBase64Screenshot());
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

    private String getBase64Screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
