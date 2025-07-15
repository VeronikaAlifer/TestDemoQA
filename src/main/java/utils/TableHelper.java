package utils;

import com.google.common.annotations.VisibleForTesting;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TableHelper {

    public int getRowCount(WebElement table) {
        return table.findElements(By.xpath("//div[@class = 'rt-tr-group']")).size();
    }

    public void getColumnText() {
    }

    public void getRowText() {
    }


}
