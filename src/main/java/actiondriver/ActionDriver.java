package actiondriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;
import freemarker.log.Logger;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	//public static final Logger logger = BaseClass.logger;   /2026.03.27 /INFO: Logger is not working properly, so I have commented it out for now. I will fix it later.
	
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		
		//logger.info("WebDriver instance is created.");
	}
	
	
	//method tyo click an element
	public void click(By by) {
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
		} catch (Exception e) {
			System.out.println("Unable to click element: " + e.getMessage());
		}
	}
	
	//Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			System.out.println("Unable to enter the value: "+ e.getMessage());
		}
	}
	
	//method to get Text from an input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println("Unable to get Text: " + e.getMessage());
			return "";
		}
	}
	
	//Method to compare Two text
	public void compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if(expectedText.equals(actualText)) {
				System.out.println("Text are matching: " + actualText+ " equals "+ expectedText);
			}
			else {
				System.out.println("Text are not matching: " + actualText+ " equals "+ expectedText);
			}
		} catch (Exception e) {
			System.out.println("Unable to compare Text: "+ e.getMessage());
		}
	}
	
	//Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return  driver.findElement(by).isDisplayed();
			
		} catch (Exception e) {
			System.out.println("Element is not displayed: "+ e.getMessage());
			return false;
		}
	}
	
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page did not load within " + timeOutInSec + "seconds. exception: " + e.getMessage());
		}
	}
	
	
	//Scroll to an element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0], scrollIntoView(true);", element);
		} catch (Exception e) {
			System.out.println("Unable to locate element: "+e.getMessage());
		}
	}
	
	
	
	
	//wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("element is not clickable: " + e.getMessage());
		}
		
	}
	
	//wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("element is not visible: " + e.getMessage());
		}
	}
	
	
}
