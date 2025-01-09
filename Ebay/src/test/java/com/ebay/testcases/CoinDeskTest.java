package com.ebay.testcases;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.ebay.utils.ExcelDataManipulator;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CoinDeskTest extends ExcelDataManipulator {

	@Test
	public void validateCoinDeskTestOne() {
		testClassName = this.getClass().getSimpleName();
		extentTest = extentReports.createTest("Validate Coin Desk API Details");

		try {
			// Step-1: Send the GET request
			RestAssured.baseURI = getTestData("validateCoinDeskTestOne", "URI");
			RequestSpecification request = RestAssured.given();
			Response response = request.get();
			response.then().assertThat().statusCode(200);
			log.info("HTTP status is: " + response.statusLine());
			extentTest.log(Status.PASS, "API Executed successfully as expected.");

			// Step-2: Verify the response details
			// Parse the response body as a JSON object
			Map<String, Object> responseBodyAsMap = response.jsonPath().getMap("$");
			Map<String, Map<String, String>> bpiDetails = (Map<String, Map<String, String>>) responseBodyAsMap
					.get("bpi");

			// Iterating over map to check the expected values
			String[] expectedValues = getTestData("validateCoinDeskTestOne", "CheckOne").split(";");
			Assert.assertTrue(bpiDetails.size() == 3);
			extentTest.log(Status.PASS, "2a. Only 3 currencies displayed as expected");
			for (String expectedValue : expectedValues) {
				log.info(bpiDetails.get(expectedValue));
				Assert.assertTrue(bpiDetails.containsKey(expectedValue), expectedValue + " details not available.");
				extentTest.log(Status.INFO, expectedValue + " is available");
			}

			Assert.assertEquals(response.jsonPath().get("bpi.GBP.description"),
					getTestData("validateCoinDeskTestOne", "GBP_Description"), "GBP Description not matched");
			extentTest.log(Status.PASS, "2b. GBP Description matched as expected");

		} catch (Exception e) {
			extentTest.log(Status.FAIL, "Unknown Failure while validating method validateCoinDeskTestOne", e, null);
			log.fatal("UNKNOWN FAILURE! - Method Name: validateCoinDeskTestOne");
			e.printStackTrace();
		}

	}
}
