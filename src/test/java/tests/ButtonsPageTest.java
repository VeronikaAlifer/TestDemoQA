package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.ButtonsPage;

import java.time.Duration;


public class ButtonsPageTest {
    private WebDriver driver;
    private ButtonsPage buttonsPage;
    private static final String URL = "https://demoqa.com/buttons";
    private static final String DYNAMIC_MSG = "You have done a dynamic click";
    private static final String DOUBLE_CLICK_MSG = "You have done a double click";
    private static final String RIGHT_CLICK_MSG = "You have done a right click";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.get(URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        buttonsPage = new ButtonsPage(driver);
    }

    @Test(description = "Verify the functionality of the 'Click Me' button when clicked once.")
    public void testDynamicClickButton() {
        buttonsPage.performClickMeBtn();

        String actualMessage = buttonsPage.getDynamicClickMessage();
        Assert.assertEquals(actualMessage, DYNAMIC_MSG, "Dynamic click message is incorrect!");
    }

    @Test( description = "Verify the functionality of the 'Double Click Me' button when double-clicked.")
    public void testDoubleClickButton() {
        buttonsPage.performDoubleClickBtn();

        String actualMessage = buttonsPage.getDoubleClickMessage();
        Assert.assertEquals(actualMessage, DOUBLE_CLICK_MSG, "Double click message is incorrect!");
    }

    @Test(description = "Verify the functionality of the 'Right Click Me' button when right-clicked.")
    public void testRightClickButton () {
        buttonsPage.performRightClickBtn();

        String actualMessage = buttonsPage.getRightClickMessage();
        Assert.assertEquals(actualMessage, RIGHT_CLICK_MSG, "Right click message is incorrect!");

    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
