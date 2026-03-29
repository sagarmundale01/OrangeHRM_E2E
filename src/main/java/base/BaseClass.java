package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseClass {

	protected static Properties prop;
	//public static Logger logger;

	protected WebDriver driver;
	protected ChromeOptions options;     // FirefoxOptions options = new FirefoxOptions();
										// EdgeOptions options = new EdgeOptions();

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
	}

	// initialize the webdriver

	private void launchBrowser() {		

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			options = new ChromeOptions();
			options.addArguments("--start-maximized");
			
			driver = new ChromeDriver(options);

		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}

		else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}
	}

	public void configureBrowser() {

		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Navigate too url
		driver.get(prop.getProperty("url"));

		try {
			driver.get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to url: " + e.getMessage());
		}
	}

	@BeforeMethod
	public void setUp() throws IOException {

		System.out.println("Setting up WebDriver for: " + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);

	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " + e.getMessage());
			}
		}
	}

	// getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// driver getter method
	public WebDriver getDriver() {
		return driver;
	}

	// driver setter method
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
