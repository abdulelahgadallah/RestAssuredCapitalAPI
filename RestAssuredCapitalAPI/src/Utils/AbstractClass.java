package Utils;

import io.restassured.RestAssured;

import java.io.IOException;
import java.io.PrintStream;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class AbstractClass {

	static PrintStream requestCapture;
	protected static ExtentTest test;
	protected static ExtentReports extentReports;
	
	@BeforeSuite
	public void init(ITestContext context) {
		extentReports = new ExtentReports(System.getProperty("user.dir")
				+ "/" + "reports" + "/"
				+ this.getClass().getSimpleName().toString() + ".html");
	}
	
	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, result.getThrowable());
		}
	}

	@AfterTest
	public void end() {

		extentReports.flush();
		extentReports.close();

	}
}
