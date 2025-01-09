package com.ebay.testcases;

import org.openqa.selenium.InvalidArgumentException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.ebay.pages.EBayBookScenarioPage;
import com.ebay.utils.ExcelDataManipulator;

public class EbayTest extends ExcelDataManipulator {

	@AfterMethod
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	public void validateEbayTestOne() {

		testClassName = this.getClass().getSimpleName();
		extentTest = extentReports.createTest("Validate Ebay Order Details");
		String actualPageTitle = null;

		try {
			// Step-1: Open browser
			launchBrowser();
			extentTest.log(Status.PASS, "Browser Launched successfully as expected.");
			eBayBookScenarioPage = new EBayBookScenarioPage();

			// Step-2: Open ebay website
			try {
				eBayBookScenarioPage.launchWebsite(getTestData("validateEbayTestOne", "URL"));
			} catch (InvalidArgumentException e) {
				extentTest.log(Status.FAIL, "Failure! Incorrect URL", e, null);
				log.error("InvalidArgumentException Failure! - Method Name: validateEbayTestOne");
				e.printStackTrace();
			}
			actualPageTitle = eBayBookScenarioPage.getPageTitle();
			Assert.assertEquals(actualPageTitle, getTestData("validateEbayTestOne", "PageTitle"),
					"User directed to incorrect webpage");
			extentTest.log(Status.PASS, "Website Launched successfully as expected.");

			// Step-3: Search for ‘book’
			actualPageTitle = eBayBookScenarioPage.doSearch(getTestData("validateEbayTestOne", "SearchTerm"));
			Assert.assertEquals(actualPageTitle, getTestData("validateEbayTestOne", "BookPageTitle"),
					"User directed to incorrect webpage");
			extentTest.log(Status.PASS, "Results for the search text returned successfully as expected.");

			// Step-4: Click on the first book in the list
			int intemNumber = Integer.parseInt(getTestData("validateEbayTestOne", "ResultOrder"));
			Assert.assertTrue(eBayBookScenarioPage.selectNthBook(intemNumber));
			extentTest.log(Status.PASS, "First item selected successfully as expected.");

			// Step-5: In the item listing page, click on ‘Add to cart’
			eBayBookScenarioPage.addItemToCart();
			extentTest.log(Status.PASS, "Item added to cart successfully as expected.");

			// Step-6: verify the cart has been updated and displays the number of items in
			// the cart
			Assert.assertTrue(eBayBookScenarioPage
					.getCartItemsCount(Integer.parseInt(getTestData("validateEbayTestOne", "ItemCount"))));
			extentTest.log(Status.PASS, "Selected number of Item displayed as expected.");

		} catch (AssertionError e) {
			extentTest.log(Status.FAIL, "Assertion Failure while validating method validateEbayTestOne", e, null);
			log.error("Assertion Failure! - Method Name: validateEbayTestOne");
			e.printStackTrace();
		} catch (NullPointerException e) {
			extentTest.log(Status.FAIL, "Failure! empty URL", e, null);
			log.error("NullPointerException Failure! - Method Name: validateEbayTestOne");
			e.printStackTrace();
		} catch (Exception e) {
			extentTest.log(Status.FAIL, "Unknown Failure while validating method validateEbayTestOne", e, null);
			log.fatal("UNKNOWN FAILURE! - Method Name: validateEbayTestOne");
			e.printStackTrace();
		}

	}

}
