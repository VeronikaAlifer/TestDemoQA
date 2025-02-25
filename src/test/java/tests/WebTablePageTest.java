package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.WebTablePage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WebTablePageTest {
    private WebDriver driver;
    private WebTablePage webTablePage;
    private Logger logger;


    @BeforeTest
    public void setUp() {
        logger = LogManager.getLogger(WebTablePageTest.class);
        driver = new ChromeDriver();

        logger.info("Launching browser and navigating to the web page.");
        driver.get("https://demoqa.com/webtables");

        driver.manage().window().maximize();
        webTablePage = new WebTablePage(driver);
    }

    @Test
    public void verifyPageElementsAreDisplayed() {
        boolean isAddBtnDisplayed = webTablePage.isAddBtnDisplayed();
        Assert.assertTrue(isAddBtnDisplayed, "Add button is not displayed but should be.");

        boolean isTableDisplayed = webTablePage.isGridDisplayed();
        Assert.assertTrue(isTableDisplayed, "The grid is not displayed but must be.");

        boolean isSearchBoxDisplayed = webTablePage.isSearchBoxDisplayed();
        Assert.assertTrue(isSearchBoxDisplayed, "Search box is not displayed but should be.");
    }

    @Test
    public void verifyWebTableColumns() {
        List<String> expectedValue = List.of("First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");
        List<WebElement> list = webTablePage.getGritColumn();

        logger.info("Verify list size match.");
        Assert.assertEquals(list.size(), expectedValue.size(), "The number of columns does not match the expected number.");

        logger.info("Verifying column headers.");
        for (int i = 0; i < list.size(); i++) {
            String actualValue = list.get(i).getText().trim();
            String expectedResult = expectedValue.get(i);

            Assert.assertEquals(actualValue, expectedResult, "Actual value does not match expected value.");
        }
    }

    @Test
    public void verifyAddingNewRecord(){

    }

    @AfterTest
    public void tearDown() {
        logger.info("Closing browser.");
        if (driver != null) {
            driver.quit();
        }
    }
}
