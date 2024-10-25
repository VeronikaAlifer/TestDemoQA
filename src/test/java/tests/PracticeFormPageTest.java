package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.PracticeFormPage;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PracticeFormPageTest {

    WebDriver driver;
    PracticeFormPage practiceFormPage;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/automation-practice-form");
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        practiceFormPage = new PracticeFormPage(driver);
    }

    @Test
    public void testSuccessfulFormFiling() throws InterruptedException {
        String firstName = "First Name";
        String lastName = "Last Name";
        String studentName = firstName + " " + lastName;
        String expectedTitleText = "Thanks for submitting the form";

        HashMap<String, String> formData = new HashMap<>();
        formData.put("Student Name", studentName);
        formData.put("Student Email", "example@gmail.com");
        formData.put("Mobile", "0123456789");
        formData.put("Gender", "Female");

        practiceFormPage.typeFirsName(firstName);
        practiceFormPage.typeLastName(lastName);
        practiceFormPage.typeUserNumber(formData.get("Mobile"));
        practiceFormPage.pickDateOfBirth();
        practiceFormPage.typeUserEmail(formData.get("Student Email"));
        practiceFormPage.selectGender(formData.get("Gender"));
   //     practiceFormPage.clickSubmitButton();

        WebElement modalTitle = driver.findElement(By.id("example-modal-sizes-title-lg"));
        Assert.assertTrue(modalTitle.isDisplayed(), "The modal title is not displayed.");

        String actualText = modalTitle.getText();
        Assert.assertEquals(actualText, expectedTitleText, "The modal title text does not match!");


        for (int i = 0; i < formData.size(); i++) {
            String label = practiceFormPage.getCellData(i, 0);
            String value = practiceFormPage.getCellData(i, 1);
            String expectedValue = formData.get(label);
            Assert.assertEquals(value, expectedValue, "Value mismatch for label: " + label);


        }
    }
}
