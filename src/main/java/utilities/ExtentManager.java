package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	// Use ThreadLocal for WebDriver so each thread's screenshots attach correctly
	private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

	// Initialize the ExtentReports instance

	public static ExtentReports getReporter() {

		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
				+ "test" + File.separator + "resources" + File.separator + "ExtentReport" + File.separator
				+ "ExtentReport.html";

			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("OrangeHRM Test Automation Report");
			spark.config().setDocumentTitle("OrageHRM Test Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));

		}

		return extent;
	}

	// start the test

	public static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// end the test
	public static void endTest() {
		getReporter().flush();
	}

	// get the current Thread's test
	public static ExtentTest getTest() {
		return test.get();
	}

	// Method to get the name of the current test method
	public static String getTestName() {
		ExtentTest currentTest = getTest();

		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "NO test is currently avaailable for this thread.";
		}
	}

	// Log a step
	public static void logStep(String logMessage) {
		if (getTest() != null) {
			getTest().info(logMessage);
		}
	}

	// Log a step validation with Screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotPath) {
		if (getTest() == null) {
			return;
		}

		try {
			// fallback to registered driver if caller passed null
			if (driver == null) {
				driver = getDriverForCurrentThread();
			}

			String path = screenshotPath;
			if (path == null || path.isEmpty()) {
				path = takeScreenshot(driver, "StepShot");
			}

			getTest().pass(logMessage,
				MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		} catch (Exception e) {
			// catch any exception (WebDriverException, RuntimeException etc.) and log
			System.err.println("[ExtentManager] Failed to attach step screenshot: " + e.getMessage());
			e.printStackTrace();
			if (getTest() != null) {
				getTest().pass(logMessage + " (screenshot attach failed: " + e.getMessage() + ")");
			}
		}

	}

	// log a failure
	public static void logFailure(WebDriver driver, String logMessage, String screenshotPath) {
		if (getTest() == null) {
			return;
		}

		try {
			// fallback to registered driver if caller passed null
			if (driver == null) {
				driver = getDriverForCurrentThread();
			}

			String path = screenshotPath;
			if (path == null || path.isEmpty()) {
				path = takeScreenshot(driver, "FailureShot");
			}

			getTest().fail(logMessage, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		} catch (Exception e) {
			System.err.println("[ExtentManager] Failed to attach failure screenshot: " + e.getMessage());
			e.printStackTrace();
			if (getTest() != null) {
				getTest().fail(logMessage + " (screenshot attach failed: " + e.getMessage() + ")");
			}
		}
	}

	// Log a skip
	public static void logSkip(String logMessage) {
		if (getTest() != null) {
			getTest().skip(logMessage);
		}
	}

	// Take a screenshot with date and time in the file and return the path
	public static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {

		if (driver == null) {
			throw new IOException("WebDriver is null, cannot take screenshot");
		}

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		String destDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "ExtentReport" + File.separator + "screenshots";
		File dir = new File(destDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = screenshotName + "_" + timeStamp + ".png";
		String destPath = destDir + File.separator + fileName;

		Path dest = Paths.get(destPath);

		try {
			// first try getting a File (common for local drivers)
			try {
				TakesScreenshot ts = (TakesScreenshot) driver;
				File src = ts.getScreenshotAs(OutputType.FILE);
				Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
			} catch (WebDriverException | ClassCastException | IOException inner) {
				// fallback to bytes capture if File approach fails (remote drivers sometimes)
				System.err.println("[ExtentManager] File screenshot failed, trying bytes fallback: " + inner.getMessage());
				try {
					TakesScreenshot ts2 = (TakesScreenshot) driver;
					byte[] bytes = ts2.getScreenshotAs(OutputType.BYTES);
					Files.write(dest, bytes);
				} catch (Exception inner2) {
					// if fallback also fails, rethrow outer exception for higher-level handling
					throw new IOException("Both FILE and BYTES screenshot capture failed: " + inner2.getMessage(), inner2);
				}
			}

			// also log where the screenshot was saved to help debugging
			System.out.println("[ExtentManager] Saved screenshot to: " + dest.toAbsolutePath().toString());
			return dest.toAbsolutePath().toString();
		} catch (IOException e) {
			// rethrow so callers can handle attachment/logging
			throw e;
		}

	}

	// Register WebDriver for the current thread
	public static void registerDriver(WebDriver driver) {
		driverThreadLocal.set(driver);
	}

	// Get WebDriver for current thread
	public static WebDriver getDriverForCurrentThread() {
		return driverThreadLocal.get();
	}

	public static void startTest1(String testName) {
		// backward compatible stub - startTest should be used
		startTest(testName);
	}

}
