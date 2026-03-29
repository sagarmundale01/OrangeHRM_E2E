package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import actiondriver.ActionDriver;

public class HomePage {

	private ActionDriver actionDriver;
	
	private By adminTab = By.xpath("//span[text()= 'Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By loginButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.cssSelector("img[alt='client brand banner']");
	
	
	public HomePage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
		
	}
	
	//method to verify if admin tab is visible or not
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(orangeHRMLogo);
		
	}
	
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(loginButton);
	}
	
	
	
}
