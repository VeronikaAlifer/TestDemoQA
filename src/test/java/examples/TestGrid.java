package examples;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestGrid {

    @Test
    public void testGrid1() throws MalformedURLException {
        URL gridUrl = new URL("http://localhost:4444/wd/hub");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
      //  firefoxOptions.addArguments("--headless");
        firefoxOptions.setCapability("browserName", "firefox");
        firefoxOptions.setCapability("platformName", "Windows"); // Платформа (Windows, Linux, etc.)

        WebDriver driver = new RemoteWebDriver(gridUrl, firefoxOptions);
        driver.get("https://jqueryui.com/droppable/");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        System.out.println(driver.getTitle());
        driver.quit();
    }

    @Test
    public void testGrid2() throws MalformedURLException {
        URL gridUrl = new URL("http://localhost:4444/wd/hub");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        //  firefoxOptions.addArguments("--headless");
        firefoxOptions.setCapability("browserName", "firefox");
        firefoxOptions.setCapability("platformName", "Windows"); // Платформа (Windows, Linux, etc.)

        WebDriver driver = new RemoteWebDriver(gridUrl, firefoxOptions);
        driver.get("https://jqueryui.com/droppable/");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        System.out.println(driver.getTitle());
         driver.quit();
    }
}
