package tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class TestTest {
    private static Logger logger;


    @Test
    public void testLogFile1() {
        logger = LogManager.getLogger(TestTest.class);
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
        logger.debug("This is a debug message");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

    }
}

