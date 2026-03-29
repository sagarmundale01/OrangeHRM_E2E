package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import actiondriver.ActionDriver;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	//Define locators using By class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.cssSelector("button[type='submit']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	
	
	public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
		
	}
	
	

	public void login(String username, String password) {
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	public boolean isErrorMessageDisplayed() {
		return actionDriver.getText(errorMessage).equals("Invalid credentials");
	}
	
	public String getErrorMessage() {
		return actionDriver.getText(errorMessage);
	}
	
	public void verifyErrorMessage(String expectedError) {
		
		actionDriver.compareText(errorMessage, expectedError);
		
	}
}
