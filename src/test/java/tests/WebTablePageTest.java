package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.WebTablePage;

public class WebTablePageTest {
    private WebDriver driver;
    private WebTablePage webTablePage;


    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/webtables");
        driver.manage().window().maximize();
        webTablePage = new WebTablePage(driver);
    }

    @Test
    public void testPageLoad() {
        boolean isAddBtnDisplayed = webTablePage.isAddBtnDisplayed();
        Assert.assertTrue(isAddBtnDisplayed, "Add button is not displayed but should be.");

        boolean isTableDisplayed = webTablePage.isGridDisplayed();
        Assert.assertTrue(isTableDisplayed, "The grid is not displayed but must be.");

        boolean isSearchBoxDisplayed = webTablePage.isSearchBoxDisplayed();
        Assert.assertTrue(isSearchBoxDisplayed, "Search box is not displayed but should be.");
    }


}
