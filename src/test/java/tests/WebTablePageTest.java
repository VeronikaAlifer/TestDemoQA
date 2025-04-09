//package tests;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.Assert;
//import org.testng.ITestResult;
//import org.testng.annotations.*;
//import pages.RegistrationFormModalForm;
//import pages.WebTablePage;
//import utils.ExtentReportManager;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class WebTablePageTest {
//    private WebDriver driver;
//    private WebTablePage webTablePage;
//    private RegistrationFormModalForm modalForm;
//    private ExtentReports reports;
//    private ExtentTest log;
//
//    @BeforeClass
//    public void setUpReports() {
//        reports = ExtentReportManager.getInstance();
//    }
//
//    @BeforeMethod
//    public void setUp(Method method) {
//        String className = method.getDeclaringClass().getSimpleName();
//        log = reports.createTest(className + " - " + method.getName())
//                .assignCategory(className);
//
//        driver = new ChromeDriver();
//
//        log.info("Launching browser and navigating to the web page.");
//        driver.get("https://demoqa.com/webtables");
//
//        driver.manage().window().maximize();
//        webTablePage = new WebTablePage(driver);
//    }
//
//    @Test
//    public void verifyPageElementsAreDisplayed() {
//
//        boolean isAddBtnDisplayed = webTablePage.isAddBtnDisplayed();
//        Assert.assertTrue(isAddBtnDisplayed, "Add button is not displayed but should be.");
//
//        boolean isTableDisplayed = webTablePage.isGridDisplayed();
//        Assert.assertTrue(isTableDisplayed, "The grid is not displayed but must be.");
//
//        boolean isSearchBoxDisplayed = webTablePage.isSearchBoxDisplayed();
//        Assert.assertTrue(isSearchBoxDisplayed, "Search box is not displayed but should be.");
//    }
//
//    @Test
//    public void verifyWebTableColumns() {
//        List<String> expectedValue = List.of("First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");
//        List<WebElement> list = webTablePage.getGridColumn();
//
//        log.info("Verify list size match.");
//        Assert.assertEquals(list.size(), expectedValue.size(), "The number of columns does not match the expected number.");
//
//        log.info("Verifying column headers.");
//        for (int i = 0; i < list.size(); i++) {
//            String actualValue = list.get(i).getText().trim();
//            String expectedResult = expectedValue.get(i);
//
//            Assert.assertEquals(actualValue, expectedResult, "Actual value does not match expected value.");
//        }
//    }
//
//    @Test
//    public void verifyAddingNewRecord() {
//        log.info("Open the registration form");
//        webTablePage.pressOnAddBtn();
//
//        log.info("Verifying the registration form is displayed.");
//        modalForm = webTablePage.getModalForm();
//        boolean isModalFormDisplayed = modalForm.isDisplayed();
//        Assert.assertTrue(isModalFormDisplayed, "The registration form is not displayed but should be.");
//
//        log.info("Verifying the registration form title");
//        String expectedTitle = "Registration Form";
//        String actualTitle = modalForm.getTitle();
//        Assert.assertEquals(actualTitle, expectedTitle, "The actual title does not match expected title.");
//
//        Map<String, String> data = new LinkedHashMap<>();
//        data.put("firstName", "First Name");
//        data.put("lastName", "Last Name");
//        data.put("age", "25");
//        data.put("userEmail", "name@example.com");
//        data.put("salary", "25000");
//        data.put("department", "Department");
//
//        log.info("Filling the registration form.");
//        modalForm.fillRegistrationForm(data);
//
//        log.info("Verifying the new record appears in the web table.");
//        boolean isNewRecordPresent = webTablePage.isRecordPresent(data);
//        Assert.assertTrue(isNewRecordPresent, "The new record was not found in the table.");
//    }
//
//    @AfterMethod
//    public void tearDown(ITestResult result) {
//        log.info("Closing browser.");
//        if (result.getStatus() == ITestResult.SUCCESS) {
//            log.pass("Test passed!!");
//        } else if (result.getStatus() == ITestResult.SKIP) {
//            log.skip("Test skipped");
//        } else if (result.getStatus() == ITestResult.FAILURE) {
//            log.fail(result.getThrowable().getMessage())
//                    .addScreenCaptureFromBase64String(getBase64Screenshot());
//        }
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    @AfterClass
//    public void teraDownReports() {
//        reports.flush();
//    }
//
//    public String getBase64Screenshot() {
//        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//    }
//}
