package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.RegistrationFormModalForm;
import pages.WebTablePage;
import pages.WebTableRegistrationForm;
import testdata.Person;
import utils.CSVReader;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.util.*;

public class WebTablePageTest {
    private WebDriver driver;
    private WebTablePage webTablePage;
    private WebTableRegistrationForm modalForm;
    private ExtentReports reports;
    private ExtentTest log;

    @BeforeClass
    public void setUpReports() {
        reports = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        log = reports.createTest(className + " - " + method.getName())
                .assignCategory(className);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        log.info("Launching browser and navigating to the web page.");
        driver.get("https://demoqa.com/webtables");

        driver.manage().window().maximize();
        webTablePage = new WebTablePage(driver);
    }

    @Test(description = "TC_001 – Отображение таблицы после загрузки страницы")
    public void verifyWebTableIsVisible() {
        List<String> columnHeaders = List.of("First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");

        log.info("Verify that the grid is visible.");
        boolean isTableDisplayed = webTablePage.isGridDisplayed();
        Assert.assertTrue(isTableDisplayed, "The grid is not displayed but must be.");

        log.info("Verify list size match.");
        List<WebElement> actualColumnsHeaders = webTablePage.getGridColumn();
        Assert.assertEquals(columnHeaders.size(), actualColumnsHeaders.size(), "The number of columns does not match the expected number.");

        log.info("Verify column header.");
        for (int i = 0; i < columnHeaders.size(); i++) {
            String actualValue = actualColumnsHeaders.get(i).getText();
            String expectedValue = columnHeaders.get(i);
            Assert.assertEquals(actualValue, expectedValue, "Actual value does not match expected value.");
        }
        String path = "src/test/resources/default_webTable_data.csv";
        List<Person> defaultPersons = CSVReader.readPeopleFromCsv(path);

        List<WebElement> recordList = webTablePage.getRecordsList();

        for (int i = 0; i < recordList.size(); i++) {
            List<WebElement> cells = recordList.get(i).findElements(By.cssSelector("div.rt-td"));
            String expectedFirstName = defaultPersons.get(i).getFirstName();
            String expectedLastName = defaultPersons.get(i).getLastName();

            String actualFirstName = cells.get(0).getText();
            String actualLastName = cells.get(1).getText();

            Assert.assertEquals(actualFirstName, expectedFirstName, "The first name does not match");
            Assert.assertEquals(actualLastName, expectedLastName, "The last name does not match.");
        }
    }

    @Test(description = "TC_001-(a) – Отображение данных таблицы после загрузки страницы используя List.")
    public void verifyWebTableDataVisibility() {
        List<List<String>> expectedData = List.of(
                List.of("Cierra", "Vega", "39", "cierra@example.com", "10000", "Insurance"),
                List.of("Alden", "Cantrell", "45", "alden@example.com", "12000", "Compliance"),
                List.of("Kierra", "Gentry", "29", "kierra@example.com", "2000", "Legal")
        );

        log.info("Verify that the table is visible.");
        boolean isVisible = webTablePage.isGridDisplayed();
        Assert.assertTrue(isVisible, "the table should be visible, but it isn't");

        log.info("Read actual table data.");
        List<WebElement> rows = webTablePage.getRecordsList();
        List<List<String>> actualData = new LinkedList<>();


        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cells = rows.get(i).findElements(By.cssSelector("div.rt-td"));
            List<String> rowData = new ArrayList<>();

            for (int j = 0; j < cells.size() - 1; j++) {
                String value = cells.get(j).getText();
                rowData.add(j, value);
            }
            actualData.add(i, rowData);
        }


        log.info("Soft-verify each value.");
        SoftAssert softAssert = new SoftAssert();

        for (int i = 0; i < expectedData.size(); i++) {
            List<String> expectedSellsValue = expectedData.get(i);
            List<String> actualSellsValue = actualData.get(i);
            for (int j = 0; j < expectedSellsValue.size(); j++) {
                String expectedValue = expectedSellsValue.get(j);
                String actualValue = actualSellsValue.get(j);
                softAssert.assertEquals(actualValue, expectedValue,
                        String.format("Mismatch at row %d, column %d: expected [%s], actual [%s]",
                                i + 1, j + 1, expectedValue, actualValue));
            }
        }
        softAssert.assertAll();

    }

    @Test(description = "TC_001 (b) – Отображение таблицы после загрузки страницы.POJO")
    public void verifyWebTableDataVisibilityPOJO() {
        List<Person> people = List.of(
                new Person("Cierra", "Vega", "cierra@example.com", "39", "10000", "Insurance"),
                new Person("Alden", "Cantrell", "alden@example.com", "45", "12000", "Compliance"),
                new Person("Kierra", "Gentry", "kierra@example.com", "29", "2000", "Legal")
        );

        log.info("Verify that the table is visible.");
        Assert.assertTrue(webTablePage.isGridDisplayed(), "The table is not visible, but it should be.");

        log.info("Read actual table data.");
        List<WebElement> elementList = webTablePage.getRecordsList();
        List<Person> actualPeople = new ArrayList<>();

        for (int i = 0; i < elementList.size(); i++) {
            List<WebElement> elements = elementList.get(i).findElements(By.cssSelector("div.rt-td"));
            Person person = new Person();
            person.setFirstName(elements.get(0).getText());
            person.setLastName(elements.get(1).getText());

            person.setEmail(elements.get(3).getText());
            person.setAge(elements.get(2).getText());
            person.setSalary(elements.get(4).getText());
            person.setDepartment(elements.get(5).getText());
            actualPeople.add(i, person);
        }
        log.info("Compare the table data");
        Assert.assertEquals(actualPeople, people, "Mismatch data.");
    }


    @Test(description = "TC_002 – Успешное добавление новой записи")
    public void verifyAddingNewRecordTC_002() {
        log.info("Verify that the table is present.");
        boolean isPresent = webTablePage.isGridDisplayed();
        Assert.assertTrue(isPresent, "The table isn't visible, but it should be.");
        List<WebElement> list = webTablePage.getRecordsList();
        int initialSize = list.size();

        log.info("Clicking on 'Add' button.");
        webTablePage.pressOnAddBtn();
        modalForm = webTablePage.getRegistrationForm();
        Assert.assertTrue(modalForm.isDisplayed(), "Registration form isn't visible, but it should be.");

        log.info("Adding new record to the table.");
        Person expectedPerson = new Person("Lierra", "Mega", "lierra@example.com", "45", "20000", "Compliance");
        modalForm.fillAnsSubmitForm(expectedPerson);

        log.info("Verifying the new record appears in the web table.");
        List<WebElement> actualRecords = webTablePage.getRecordsList();

        Assert.assertEquals(actualRecords.size(), initialSize + 1, "Record count should increase by 1.");

        log.info("Verify new record.");

        List<WebElement> cells = actualRecords.get(actualRecords.size() - 1).findElements(By.cssSelector("div.rt-td"));
        Person newPerson = new Person();
        newPerson.setFirstName(cells.get(0).getText());
        newPerson.setLastName(cells.get(1).getText());
        newPerson.setAge(cells.get(2).getText());
        newPerson.setEmail(cells.get(3).getText());
        newPerson.setSalary(cells.get(4).getText());
        newPerson.setDepartment(cells.get(5).getText());
        Assert.assertEquals(newPerson, expectedPerson, "The new record was not found in the table.");
    }

    /////////////////////////old test

    @Test
    public void verifyPageElementsAreDisplayed() {

        boolean isAddBtnDisplayed = webTablePage.isAddBtnDisplayed();
        Assert.assertTrue(isAddBtnDisplayed, "Add button is not displayed but should be.");

        boolean isTableDisplayed = webTablePage.isGridDisplayed();


        boolean isSearchBoxDisplayed = webTablePage.isSearchBoxDisplayed();
        Assert.assertTrue(isSearchBoxDisplayed, "Search box is not displayed but should be.");
    }

    @Test
    public void verifyWebTableColumns() {
        List<String> expectedValue = List.of("First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");
        List<WebElement> list = webTablePage.getGridColumn();

        log.info("Verify list size match.");
        Assert.assertEquals(list.size(), expectedValue.size(), "The number of columns does not match the expected number.");

        log.info("Verifying column headers.");
        for (int i = 0; i < list.size(); i++) {
            String actualValue = list.get(i).getText().trim();
            String expectedResult = expectedValue.get(i);

            Assert.assertEquals(actualValue, expectedResult, "Actual value does not match expected value.");
        }
    }

    @Test
    public void verifyAddingNewRecord() {
        log.info("Open the registration form");
        webTablePage.pressOnAddBtn();

        log.info("Verifying the registration form is displayed.");
        modalForm = webTablePage.getRegistrationForm();
        boolean isModalFormDisplayed = modalForm.isDisplayed();
        Assert.assertTrue(isModalFormDisplayed, "The registration form is not displayed but should be.");

        log.info("Verifying the registration form title");
        String expectedTitle = "Registration Form";
        String actualTitle = modalForm.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "The actual title does not match expected title.");

        Map<String, String> data = new LinkedHashMap<>();
        data.put("firstName", "First Name");
        data.put("lastName", "Last Name");
        data.put("age", "25");
        data.put("userEmail", "name@example.com");
        data.put("salary", "25000");
        data.put("department", "Department");

        log.info("Filling the registration form.");
        modalForm.fillRegistrationForm(data);

        log.info("Verifying the new record appears in the web table.");
        boolean isNewRecordPresent = webTablePage.isRecordPresent(data);
        Assert.assertTrue(isNewRecordPresent, "The new record was not found in the table.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        log.info("Closing browser.");
        if (result.getStatus() == ITestResult.SUCCESS) {
            log.pass("Test passed!!");
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.skip("Test skipped");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            log.fail(result.getThrowable().getMessage())
                    .addScreenCaptureFromBase64String(getBase64Screenshot());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void teraDownReports() {
        reports.flush();
    }

    public String getBase64Screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
