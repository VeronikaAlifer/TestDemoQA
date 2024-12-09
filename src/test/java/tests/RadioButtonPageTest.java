package tests;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.RadioButtonPage;

import java.time.Duration;

public class RadioButtonPageTest {
    WebDriver driver;
    RadioButtonPage radioButtonPage;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));

        radioButtonPage = new RadioButtonPage(driver);
        radioButtonPage.openPage();
    }

    @Test
    public void testSelectingYesRadioBtn() {
        Assert.assertFalse(radioButtonPage.isElementDisplayed(radioButtonPage.getResultTextElement()), "Result text should not be displayed before selecting any radio button.");

        radioButtonPage.selectYesRadioBtn();
        Assert.assertTrue(radioButtonPage.isYesRadioBtnSelected(), "The 'Yes' radio button should be selected after clicking it.");

        String expectedText = "Yes";
        String actualText = radioButtonPage.getResultText();
        Assert.assertEquals(actualText, expectedText, "The actual text does not match with expected text");
        driver.quit();
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

}
