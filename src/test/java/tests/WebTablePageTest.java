package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.WebTablePage;
import pages.WebTableRegistrationForm;
import testdata.Person;
import utils.CSVReader;
import utils.ExtentReportManager;

import java.lang.reflect.Method;
import java.time.Duration;
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webTablePage = new WebTablePage(driver);
    }

    @Test(description = "TC_001 – Отображение таблицы после загрузки страницы")
    public void verifyWebTableIsVisible() {
        List<String> columnHeaders = List.of("First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");

        log.info("Verify that the grid is visible.");
        boolean isTableDisplayed = webTablePage.isGridDisplayed();
        Assert.assertTrue(isTableDisplayed, "The grid is not displayed but must be.");

        log.info("Verify list size match.");
        List<String> actualColumnsHeaders = webTablePage.getGridColumnHeaders();
        Assert.assertEquals(columnHeaders.size(), actualColumnsHeaders.size(), "The number of columns does not match the expected number.");

        log.info("Verify column header.");
        for (int i = 0; i < columnHeaders.size(); i++) {
            String actualValue = actualColumnsHeaders.get(i);
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
        modalForm = webTablePage.clickAdd();
        Assert.assertTrue(modalForm.isDisplayed(), "Registration form isn't visible, but it should be.");

        log.info("Adding new record to the table.");
        Person expectedPerson = new Person("Lierra", "Mega", "lierra@example.com", "45", "20000", "Compliance");
        modalForm.fillAndSubmitForm(expectedPerson);

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

    @Test(description = "TC_005 – Успешное редактирование записи")
    public void testEditSalaryField() {
        String newSalary = "88000";
        final String FIRST_NAME = "Cierra";
        final String LAST_NAME = "Vega";
        String oldSalary = webTablePage.getColumnDataByName(FIRST_NAME, LAST_NAME, "Salary");

        log.info(String.format("Open Edit modal for %s %s", FIRST_NAME, LAST_NAME));
        modalForm = webTablePage.performEditAction(FIRST_NAME, LAST_NAME);

        log.info("Update salary.");
        modalForm.enterSalary(newSalary).submitForm();

        log.info("Verify that the main page is opened.");
        boolean isManePageDisplayed = webTablePage.isGridDisplayed();
        Assert.assertTrue(isManePageDisplayed, "The table isn't present, but it should be.");

        log.info("Verify that the salary value is changed.");
        Assert.assertNotEquals(oldSalary, newSalary, "The salary doesn't change, but it should be.");

        String actualSalary = webTablePage.getColumnDataByName(FIRST_NAME, LAST_NAME, "Salary");
        Assert.assertEquals(actualSalary, newSalary, "The new salary doesn't match with  actual salary.");
    }

    @Test(description = "TC_006 – Удаление записи из таблицы")
    public void testDeleteRow() {
        String firstName = "Alden";
        String lastName = "Cantrell";
        boolean isUserPresent = webTablePage.isUserPresent(firstName, lastName);
        Assert.assertTrue(isUserPresent, String.format("User %s %s is not present, but it should be.", firstName, lastName));

        log.info("Click on 'Delete' button.");
        webTablePage.deleteRowByName(firstName, lastName);

        log.info(String.format("Verify that user %s %s is not present in the grid.", firstName, lastName));
        isUserPresent = webTablePage.isUserPresent(firstName, lastName);
        Assert.assertFalse(isUserPresent,
                String.format("The user %s %s present on the grid, but it should nor be.", firstName, lastName));
    }

    @Test(description = "TC_007 – Поиск по имени (существующее значение)")
    public void testInputSearchBoxByExistingName() {

        String userName = "Alden";
        String userLastName = "Cantrell";

        log.info("Verify that the record list > 1");
        int recordListSize = webTablePage.getRecordsList().size();
        Assert.assertTrue(recordListSize > 1, "The list size less than 1");

        log.info("Enter the user name in the search box");
        webTablePage.enterTextInSearchBox(userName);

        log.info("Verify the record list size changed.");
        int actualRecordListSize = webTablePage.getRecordsList().size();
        Assert.assertTrue(actualRecordListSize < recordListSize && recordListSize > 0,
                "Unexpected record count.");

        log.info("Verify that the expected user is present.");
        boolean isPresent = webTablePage.isUserPresent(userName, userLastName);
        Assert.assertTrue(isPresent, "The user does not present.");
    }

    @Test(description = "TC_008 – Поиск по несуществующему значению.")
    public void testSearchRecordByInvalidValue() {
        String invalidValue = "dfghjkl";

        log.info("Verify that the table is not empty.");
        Assert.assertFalse(webTablePage.getRecordsList().isEmpty(), "Record list size should not be empty.");

        log.info("Enter invalid value in search box.");
        webTablePage.enterTextInSearchBox(invalidValue);

        log.info("Get record list size and verify.");
        Assert.assertTrue(webTablePage.getRecordsList().isEmpty(), "Record list size must be empty.");
    }

    @Test(description = "TC_009 – Поиск по email")
    public void testSearchByEmail() {
        String targetEmail = "kierra@example.com";
        String userName = "Kierra";
        String userLastName = "Gentry";

        log.info("Verify that the web‑table contains at least one record before filtering.");
        Assert.assertFalse(webTablePage.getRecordsList().isEmpty(),
                "The table is empty, but it must contain records.");

        log.info(String.format("Enter the e‑mail '%s' into the search box.", targetEmail));
        webTablePage.enterTextInSearchBox(targetEmail);

        log.info("Verify that exactly one row is displayed after filtering.");
        List<WebElement> filteredRecords = webTablePage.getRecordsList();
        Assert.assertEquals(filteredRecords.size(), 1,
                String.format("Expected exactly 1 record after filtering by '%s', but found %d.",
                        targetEmail, filteredRecords.size()));

        log.info(String.format("Verify that the displayed record belongs to %s %s.", userName, userLastName));
        Assert.assertTrue(webTablePage.isUserPresent(userName, userLastName),
                String.format("The expected user %s %s was not found.", userName, userLastName));
    }

    @Test(description = "TC_014 – Изменение количества отображаемых строк")
    public void testChangingAmountOfRows() {
        int defaultRowsListSize = 10;

        String defaultSelectRowsOption = defaultRowsListSize + " rows";
        List<String> rowsValueList = List.of("5", "10", "20");

        log.info("Verify default rows size before filtering.");
        List<WebElement> rows = webTablePage.getRows();
        String actualValue = webTablePage.getSelectedOption();
        int actualRowListSize = rows.size();

        Assert.assertEquals(actualValue, defaultSelectRowsOption, "The default values do not match the expected outcome.");
        Assert.assertEquals(actualRowListSize, defaultRowsListSize, "The default values do not match the expected outcome.");
        Assert.assertTrue(actualValue.contains(String.valueOf(actualRowListSize)), "The values does not match, but the should be.");


        for (String rowOption : rowsValueList) {
            log.info(String.format("Set up %s rows value", rowOption));
            setUpRowsOptions(rowOption);
        }
    }

    @Test(description = "TC_013 – Массовое добавление 50 записей")
    public void testAddingLotsAmountRecords() {
        Random random = new Random();
        log.info("Start adding 50 new records to the table.");

        for (int i = 0; i <= 20; i++) {
            log.info("Adding record #" + (i + 1));
            int randomValue = random.nextInt(50000) + 1;
            int age = 18 + random.nextInt(48);

            modalForm = webTablePage.clickAdd();
            modalForm.enterFirstName("Name" + randomValue);
            modalForm.enterLastName("LastName" + randomValue);
            modalForm.enterAge(String.valueOf(age));
            modalForm.enterEmail(randomValue + "@gmail.com");
            modalForm.enterDepartment("Insurance");
            modalForm.enterSalary(String.valueOf(randomValue));
            modalForm.submitForm();
        }

        log.info("Checking if the first page is displayed.");
        String pageInfoValue = webTablePage.getPageInfo();
        Assert.assertEquals(pageInfoValue, "1",
                "Expected to be on page 1 after adding records, but was on page: " + pageInfoValue);

        log.info("Clicking on 'Next' button.");
        webTablePage.clickNextBtn();
        pageInfoValue = webTablePage.getPageInfo();
        Assert.assertEquals(pageInfoValue, "2",
                "Expected to be on page 2 after clicking next, but was on: " + pageInfoValue);

        log.info("Clicking on 'Previous' button.");
        webTablePage.clickPreviousBtn();
        pageInfoValue = webTablePage.getPageInfo();
        Assert.assertEquals(pageInfoValue, "1",
                "Expected to return to page 1 after clicking previous, but was on: " + pageInfoValue);
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
        List<String> list = webTablePage.getGridColumnHeaders();

        log.info("Verify list size match.");
        Assert.assertEquals(list.size(), expectedValue.size(), "The number of columns does not match the expected number.");

        log.info("Verifying column headers.");
        for (int i = 0; i < list.size(); i++) {
            String actualValue = list.get(i).trim();
            String expectedResult = expectedValue.get(i);

            Assert.assertEquals(actualValue, expectedResult, "Actual value does not match expected value.");
        }
    }

    @Test
    public void verifyAddingNewRecord() {
        log.info("Open the registration form");
        modalForm = webTablePage.clickAdd();

        log.info("Verifying the registration form is displayed.");
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

//    @AfterMethod
//    public void tearDown(ITestResult result) {
//        if (result.getStatus() == ITestResult.SUCCESS) {
//            log.pass("Test passed!!");
//        } else if (result.getStatus() == ITestResult.SKIP) {
//            log.skip("Test skipped");
//        } else if (result.getStatus() == ITestResult.FAILURE) {
//            log.fail(result.getThrowable().getMessage())
//                    .addScreenCaptureFromBase64String(getBase64Screenshot());
//        }
//        if (driver != null) {
//            driver.quit();
//            log.info("Closing browser.");
//
//        }
//    }

    @AfterClass
    public void teraDownReports() {
        reports.flush();
    }

    public String getBase64Screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    private void setUpRowsOptions(String expectedRowCountStr) {
        webTablePage.setUpGridRowsSize(expectedRowCountStr);

        log.info("Verify that the values changed after filtering.");
        Assert.assertEquals(webTablePage.getRows().size(), Integer.valueOf(expectedRowCountStr),
                "The values does not match the expected result.");
        Assert.assertTrue(webTablePage.getSelectedOption().contains(expectedRowCountStr),
                "The rows selected values does not contain expected value.");
    }
}
