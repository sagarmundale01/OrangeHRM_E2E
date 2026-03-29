package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.HomePage;
import pages.LoginPage;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setUp() {

		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());

	}

	@Test
	public void verifyValidLoginTest() {

		loginPage.login("Admin", "admin123");
	//	assert homePage.isAdminTabVisible() : "Admin tab is not visible, login might have failed.";
		Assert.assertFalse(homePage.isAdminTabVisible(), "Admin tab is not visible, login might have failed.");
		
		homePage.logout();
		staticWait(2);

	}

}
