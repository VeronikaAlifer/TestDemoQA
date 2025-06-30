package tests;

import org.testng.annotations.Test;
import utils.DBTest;

public class DBexampleTest {

    @Test
    public void test(){
        DBTest.dbConnection();
    }
}
