package actiondriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import base.BaseClass;


public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	//public static final Logger logger = BaseClass.logger;
	private static final Logger logger = LoggerFactory.getLogger(ActionDriver.class);

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		logger.info("ActionDriver constructor called; driver passed = " + driver);
		
		// initialize wait only when driver is available
		try {
			if (driver != null && BaseClass.getProp() != null) {
				int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
				this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
			}
		} catch (Exception e) {
			logger.warn("Unable to initialize explicit wait in ActionDriver: " + e.getMessage());
		}
		logger.info("ActionDriver instance is created.");

	}

	/**
	 * Allow setting or updating the WebDriver instance after construction.
	 * This is useful when page objects are created before the test's WebDriver is ready.
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
		try {
			if (driver != null && BaseClass.getProp() != null) {
				int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
				this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
				logger.info("ActionDriver WebDriver and wait initialized. driver=" + driver);
			}
		} catch (Exception e) {
			logger.error("Failed to set driver in ActionDriver: " + e.getMessage());
		}
	}

	// method tyo click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			if (driver == null) {
				logger.error("Cannot click element because WebDriver is null: " + elementDescription);
				return;
			}
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			logger.info("Clicked on element: " + elementDescription);
			
		} catch (Exception e) {
			// System.out.println("Unable to click element: " + e.getMessage());
			logger.error("Unable to click element: " + e.getMessage());
		}
	}

	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			if (driver == null) {
				logger.error("Cannot enter text because WebDriver is null: " + truncateString(value, 20));
				return;
			}
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on : " + truncateString(value, 20) + " into element: " + getElementDescription(by));
		
		} catch (Exception e) {
			// System.out.println("Unable to enter the value: " + e.getMessage());
			logger.error("Unable to enter the value: " + e.getMessage());
		}
	}

	// method to get Text from an input field
	public String getText(By by) {
		try {
			if (driver == null) {
				logger.error("Cannot get text because WebDriver is null for locator: " + by);
				return "";
			}
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println("Unable to get Text: " + e.getMessage());
			logger.error("Unable to get Text: " + e.getMessage());
			return "";
		}
	}

	// Method to compare Two text
	public void compareText(By by, String expectedText) {
		try {
			if (driver == null) {
				logger.error("Cannot compare text because WebDriver is null for locator: " + by);
				return;
			}
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				// System.out.println("Text are matching: " + actualText + " equals " +
				// expectedText);
				logger.info("Text are matching: " + actualText + " equals " + expectedText);

			} else {
				// System.out.println("Text are not matching: " + actualText + " equals " +
				// expectedText);
				logger.error("Text are not matching: " + actualText + " equals " + expectedText);
			}
		} catch (Exception e) {
			// System.out.println("Unable to compare Text: " + e.getMessage());
			logger.error("Unable to compare Text: " + e.getMessage());
		}
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			if (driver == null) {
				logger.error("Cannot determine visibility because WebDriver is null for locator: " + by);
				return false;
			}
			waitForElementToBeVisible(by);
			logger.info("Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			// System.out.println("Element is not displayed: " + e.getMessage());
			logger.error("Element is not displayed: " + e.getMessage());
			return false;
		}
	}

	public void waitForPageLoad(int timeOutInSec) {
		try {
			if (driver == null || wait == null) {
				logger.warn("Skipping waitForPageLoad because WebDriver or wait is null.");
				return;
			}
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
			// System.out.println("Page did not load within " + timeOutInSec + "seconds.
			// exception: " + e.getMessage());
			logger.error("Page did not load within " + timeOutInSec + "seconds. exception: " + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			if (driver == null) {
				logger.error("Cannot scroll because WebDriver is null for locator: " + by);
				return;
			}
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			logger.info("Scrolled to element: " + getElementDescription(by));

		} catch (Exception e) {
			// System.out.println("Unable to locate element: " + e.getMessage());
			logger.error("Unable to locate element: " + e.getMessage());
		}
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			if (wait == null) {
				logger.warn("Explicit wait is not initialized; skipping waitForElementToBeClickable for: " + by);
				return;
			}
			wait.until(ExpectedConditions.elementToBeClickable(by));
			logger.info("Element is clickable: " + getElementDescription(by));
		} catch (Exception e) {
			// System.out.println("element is not clickable: " + e.getMessage());
			logger.error("element is not clickable: " + e.getMessage());
		}

	}

	// wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			if (wait == null) {
				logger.warn("Explicit wait is not initialized; skipping waitForElementToBeVisible for: " + by);
				return;
			}
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			logger.info("Element is visible: " + getElementDescription(by));
		} catch (Exception e) {
			// System.out.println("element is not visible: " + e.getMessage());
			logger.error("element is not visible: " + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator

	public String getElementDescription(By locator) {

		if (driver == null) {
			return "driver is null";
		}
		if (locator == null) {
			return "locator is null";
		}

		// find the element using the locator
		try {
			WebElement element = driver.findElement(locator);

			// get element attribute
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			}
			if (isNotEmpty(id)) {
				return "Element with id: " + id;
			}
			if (isNotEmpty(text)) {
				return "Element with text: " + text;
			}
			if (isNotEmpty(className)) {
				return "Element with class: " + className;
			}
			if (isNotEmpty(placeHolder)) {
				return "Element with placeholder: " + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to get element description: " + e.getMessage());

		}

		return "";
	}

	// Utility method to check if a string is not null and not empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	// Utility method to truncate long string
	private String truncateString(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

}