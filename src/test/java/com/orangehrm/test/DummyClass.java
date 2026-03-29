package com.orangehrm.test;

import org.testng.annotations.Test;

import base.BaseClass;

public class DummyClass extends BaseClass {

	@Test
	public void dummyTest() {

		String title = driver.getTitle();

		assert title.equals("OrangeHRM") : "Test Failed - Title is not Matching";
		System.out.println("Test Passed - Title is Matching");
		
		//Assert.assertEquals(title, "OrangeHRM", "Test Passed - Title is Matching" );
		
		
	}
}
