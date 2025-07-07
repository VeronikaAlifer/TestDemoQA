package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.List;

public class Ex {
    @Test
    public void test() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://demoqa.com/webtables");

        WebElement table = driver.findElement(By.xpath("//div[@class = 'rt-table']"));
        List<WebElement> list = table.findElements(By.xpath("//div[@class = 'rt-tr-group']"));
        String value = list.get(1).getText();
        System.out.println(value);
    }

}
