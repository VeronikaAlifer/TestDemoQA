package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class TextBoxPage {
    private WebDriver driver;
    private By userName = By.id("userName");
    private By userEmail = By.id("userEmail");
    private By currentAddress = By.id("currentAddress");
    private By permanentAddress = By.id("permanentAddress");
    private By submitBtn = By.id("submit");
    private By outputField = By.id("output");

    public TextBoxPage(WebDriver driver) {
        this.driver = driver;
    }

    public void typeUserName(String userName) {
        driver.findElement(this.userName).sendKeys(userName);
    }

    public void typeEmail(String email) {
        driver.findElement(userEmail).sendKeys(email);
    }

    public void typeCurrentAddress(String currentAddress) {
        driver.findElement(this.currentAddress).sendKeys(currentAddress);
    }

    public void typePermanentAddress(String permanentAddress) {
        driver.findElement(this.permanentAddress).sendKeys(permanentAddress);
    }

    public void clickSubmitBtn() throws InterruptedException {
//        Thread.sleep(5000);
//        JavascriptExecutor js = (JavascriptExecutor)driver;
//        js.executeScript("scroll(0, 700)");
//        getSubmitBtnElement().click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));

        // Scroll the button into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
    }

    public WebElement getOutputField() {
      return   driver.findElement(outputField);
    }

    public void fillAllFields(String userName, String userEmail, String currentAddress, String permanentAddress){
        typeUserName(userName);
        typeEmail(userEmail);
        typeCurrentAddress(currentAddress);
        typePermanentAddress(permanentAddress);
    }

    public List<WebElement> getOutputData (){
       WebElement element = driver.findElement(By.xpath("//div[@class='border col-md-12 col-sm-12']"));
        return  element.findElements(By.tagName("p"));
    }
    public WebElement getSubmitBtnElement () {
        return driver.findElement(submitBtn);
    }
}
