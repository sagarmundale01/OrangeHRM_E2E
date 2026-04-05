package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import actiondriver.ActionDriver;
import utilities.ExtentManager;

public class HomePage {

	private ActionDriver actionDriver;
	
	private By adminTab = By.xpath("//span[text()= 'Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By loginButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.cssSelector("img[alt='client brand banner']");
	
	
	public HomePage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
		
	}
	
	// allow tests to set or update the driver after BaseClass has initialized it
	public void setDriver(WebDriver driver) {
		if (this.actionDriver == null) {
			this.actionDriver = new ActionDriver(driver);
		} else {
			this.actionDriver.setDriver(driver);
		}
	}
	
	//method to verify if admin tab is visible or not
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	public boolean verifyOrangeHRMLogo() {
		
		ExtentManager.startTest("Verify OrangeHRM Logo Test");
		ExtentManager.logStep("Navigating to home page and verifying the presence of the OrangeHRM logo.");
		return actionDriver.isDisplayed(orangeHRMLogo);
		
	}
	
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(loginButton);
	}
	
	
	
}