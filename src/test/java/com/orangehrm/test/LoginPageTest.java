package com.orangehrm.test;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.HomePage;
import pages.LoginPage;
import utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setUp() throws IOException {

		// Ensure a fresh browser is created for each test
		super.setUp();

		// Register driver for current thread so Extent can take screenshots
		WebDriver driver = getDriver();
		ExtentManager.registerDriver(driver);

		// Create page objects after the driver is initialized
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);

		// Ensure page objects have the initialized driver
		loginPage.setDriver(driver);
		homePage.setDriver(driver);

	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		// Capture a final screenshot for both success and failure before quitting the driver
		WebDriver driver = ExtentManager.getDriverForCurrentThread();
		try {
			if (driver != null) {
				if (result.getStatus() == ITestResult.FAILURE) {
					ExtentManager.logFailure(driver, "Test failed: " + result.getName(), null);
				} else {
					ExtentManager.logStepWithScreenshot(driver, "Test completed: " + result.getName(), null);
				}
			}
		} catch (Exception e) {
			System.err.println("[LoginPageTest] Exception while attaching screenshot: " + e.getMessage());
			e.printStackTrace();
		}

		// Flush extent at end of test
		ExtentManager.endTest();

		// Close browser instance from BaseClass (this will quit the driver)
		super.tearDown();

		// Unregister thread-local driver reference
		ExtentManager.registerDriver(null);
	}

	@Test
	public void verifyValidLoginTest() {

		ExtentManager.startTest("Verify Valid Login Test");
		loginPage.login("Admin", "admin123");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible, login might have failed.");
		homePage.logout();
	    ExtentManager.logStep("Logged out successfully after verifying valid login.");
		staticWait(2);

	}
	
	@Test
	public void invalidLoginTest() {
		ExtentManager.startTest("Verify Invalid Login Test");
		ExtentManager.logStep("Attempting to login with invalid credentials and verifying error message.");
		
		loginPage.login("Admin", "wrongpassword");
		Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid login.");
		ExtentManager.logStep("Error message displayed as expected for invalid login.");
	
	}
}