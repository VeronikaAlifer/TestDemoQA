package additionalPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class W3SchoolTablePage {
    private WebDriver driver;
    private By table = By.xpath("//table[@class='ws-table-all']");
    private  By acceptAllBtn = By.id("accept-choices");

    public W3SchoolTablePage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getRows() {
        WebElement tableElement = driver.findElement(table);
        return tableElement.findElements(By.cssSelector("tbody tr"));
    }

    public String getCellData(int rowIndex, int collIndex) {
        List<WebElement> rows = getRows();
        if (rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds.");
        }
        List<WebElement> cells = rows.get(rowIndex).findElements(By.tagName("td"));

        if (collIndex >= cells.size()) {
            return "Cell index " + collIndex + " is out of bounds.";
        }
        return cells.get(collIndex).getText();
    }

    public void clickOnAcceptAllBtn(){
        driver.findElement(acceptAllBtn).click();
    }
}
