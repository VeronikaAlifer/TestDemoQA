package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    private static ExtentReports extentReports = new ExtentReports();

    static {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/ExtentReport.html");
        sparkReporter.config().setDocumentTitle("Report");
        sparkReporter.config().setReportName("Test result");
        extentReports.attachReporter(sparkReporter);
    }

    public static ExtentReports getInstance(){
        return extentReports;
    }
}
