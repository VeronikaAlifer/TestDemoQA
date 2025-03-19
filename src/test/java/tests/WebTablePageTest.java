//package tests;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.Assert;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//import pages.RegistrationFormModalForm;
//import pages.WebTablePage;
//
//import java.util.*;
//
//public class WebTablePageTest {
//    private WebDriver driver;
//    private WebTablePage webTablePage;
//    private Logger logger;
//
//
//    @BeforeMethod
//    public void setUp() {
//        logger = LogManager.getLogger(WebTablePageTest.class);
//        driver = new ChromeDriver();
//
//
//        logger.info("Launching browser and navigating to the web page.");
//        driver.get("https://demoqa.com/webtables");
//
//        driver.manage().window().maximize();
//        webTablePage = new WebTablePage(driver);
//    }
//
//    @Test
//    public void verifyPageElementsAreDisplayed() {
//
//        System.out.println(Thread.currentThread().getId() + " - Test method");
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
//        List<WebElement> list = webTablePage.getGritColumn();
//
//        logger.info("Verify list size match.");
//        Assert.assertEquals(list.size(), expectedValue.size(), "The number of columns does not match the expected number.");
//
//        logger.info("Verifying column headers.");
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
//        logger.info("Open the registration form");
//        webTablePage.pressOnAddBtn();
//
//        logger.info("Verifying the registration form is displayed.");
//        RegistrationFormModalForm modalForm = webTablePage.getModalForm();
//        boolean isModalFormDisplayed = modalForm.isDisplayed();
//        Assert.assertTrue(isModalFormDisplayed, "The registration form is not displayed but should be.");
//
//        logger.info("Verifying the registration form title");
//        String expectedTitle = "Registration Form";
//        String actualTitle = modalForm.getTitle();
//        Assert.assertEquals(actualTitle, expectedTitle, "The actual title does not match expected title.");
//
//        HashMap<String, String> data = new HashMap<>();
//        data.put("firstName", "First Name");
//        data.put("lastName", "Last Name");
//        data.put("userEmail", "name@example.com");
//        data.put("age", "25");
//        data.put("salary", "25000");
//        data.put("department", "Department");
//
//        logger.info("Filling the registration form.");
//
//
//        logger.info("Verifying the new record appears in the web table.");
//        boolean isNewRecordPresent = webTablePage.isRecordPresent(data);
//        Assert.assertTrue(isNewRecordPresent, "The new record was not found in the table.");
//    }
//
////    @AfterTest
////    public void tearDown() {
////        logger.info("Closing browser.");
////        if (driver != null) {
////            driver.quit();
////        }
////    }
//}
