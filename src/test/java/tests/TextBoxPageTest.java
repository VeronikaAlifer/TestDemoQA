package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.TextBoxPage;

import java.time.Duration;
import java.util.List;

public class TextBoxPageTest {
    WebDriver driver;
    TextBoxPage textBoxPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/text-box");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        textBoxPage = new TextBoxPage(driver);
    }

    @Test
    public void testSuccessFilingFields() throws InterruptedException {
        String userName = "Jon Doe";
        String userEmail = "john.doe@example.com";
        String currentAddress = "123 Main St, New York, NY";
        String permanentAddress = "456 Elm St, New York, NY";

        textBoxPage.fillAllFields(userName, userEmail, currentAddress, permanentAddress);

        Assert.assertFalse(textBoxPage.getOutputField().isDisplayed(), "Output field should be hidden before form submission.");
        textBoxPage.clickSubmitBtn();

        Assert.assertTrue(textBoxPage.getOutputField().isDisplayed());
        List<WebElement> data = textBoxPage.getOutputData();
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Name") && element.getText().contains(userName)), "The name is incorrect.");
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Email") && element.getText().contains(userEmail)), "The email is incorrect.");
        Assert.assertTrue(data.stream().anyMatch(element -> element.getText().contains("Current Address") && element.getText().contains(currentAddress)), "The current address is incorrect.");
        Assert.assertTrue(data.get(3).getText().contains("Permananet Address") && data.get(3).getText().contains(permanentAddress), "The permanent address is incorrect.");
    }


    @Test
    public void testValidateRequiredFields() throws InterruptedException {
        Assert.assertFalse(textBoxPage.getOutputField().isDisplayed(), "Output field should be hidden before form submission.");
        Assert.assertTrue(textBoxPage.getSubmitBtnElement().isEnabled(), "The Submit button is not clickable!");
        textBoxPage.clickSubmitBtn();

    }

    @Test
    public void testInvalidEmailFormat() {
        String userName = "Jon Doe";
        String invalidUserEmail = "john";
        String currentAddress = "123 Main St, New York, NY";
        String permanentAddress = "456 Elm St, New York, NY";
        textBoxPage.fillAllFields(userName, invalidUserEmail, currentAddress, permanentAddress);

    }

//    @AfterMethod
//    public void closeResources() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}