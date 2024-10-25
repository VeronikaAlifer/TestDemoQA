package additionalPracticeTest;

import additionalPractice.W3SchoolTablePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class W3SchoolTablePageTest {
    WebDriver driver;
    W3SchoolTablePage w3SchoolTablePage;

    @BeforeTest
    public void setUp() {

        driver = new ChromeDriver();
        driver.get("https://www.w3schools.com/html/html_tables.asp");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        w3SchoolTablePage = new W3SchoolTablePage(driver);
        w3SchoolTablePage.clickOnAcceptAllBtn();
    }

    @Test
    public void testTable() {

        HashMap<String, String> rowsData = getRowsData();

        for (int i = 1; i < rowsData.size(); i++) {
            String contactValue = w3SchoolTablePage.getCellData(i, 1);
            String countryValue = w3SchoolTablePage.getCellData(i, 2);

            Assert.assertEquals(rowsData.get(contactValue), countryValue, String.format("The country  for contact %s does not match.", contactValue));
        }

    }

    private HashMap<String, String> getRowsData() {
        HashMap<String, String> rowsData = new HashMap<>();
        rowsData.put("Maria Anders", "Germany");
        rowsData.put("Francisco Chang", "Mexico");
        rowsData.put("Roland Mendel", "Austria");
        rowsData.put("Helen Bennett", "UK");
        rowsData.put("Yoshi Tannamuri", "Canada");
        rowsData.put("Giovanni Rovelli", "Italy");
        return rowsData;
    }



}
