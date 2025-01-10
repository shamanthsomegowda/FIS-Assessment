package com.ebay.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.ebay.utils.GenericWebFunctions;

public class EBayBookScenarioPage extends GenericWebFunctions {

	@FindBy(id = "gh-ac")
	WebElement searchBarTxt;

	@FindBy(xpath = "//input[@value='Search']")
	WebElement searchLegendBtn;

	@FindBy(xpath = "//li[@id='gh-minicart-hover']//i")
	WebElement cartItemsCountIcon;

	@FindAll(@FindBy(xpath = "//div[@id='srp-river-results']//div[@class='s-item__title']"))
	List<WebElement> booksResultsList;

	@FindBy(xpath = "//span[text()='Add to cart']") // button[text()='Yes, continue']
	WebElement addToCartBtn;

	@FindBy(xpath = "//button[text()='Yes, continue']")
	WebElement confirmBtn;

	public EBayBookScenarioPage() {
		PageFactory.initElements(driver, this);
	}

	// open a website
	public void launchWebsite(String url) {
		driver.get(url);
	}

	// to retrieve the page title
	public String getPageTitle() {
		String pageTitle = "";
		pageTitle = driver.getTitle();
		log.info("User directed to " + pageTitle);
		return pageTitle;
	}

	// to perform search operation
	public String doSearch(String searchKey) {
		String pageTitle = "";
		keyOrTypeValue(searchBarTxt, searchKey);
		clickOnElement(searchLegendBtn);
		pageTitle = driver.getTitle();
		log.info("Product search initiated");
		return pageTitle;
	}

	// selecting nth book from the list
	public boolean selectNthBook(int n) {
		boolean isSelected = false;
		try {
			if (booksResultsList.size() == 0) {
				log.error("No results returned from web for given search criteria");
			} else {
				clickOnElement(booksResultsList.get(n-1));
				switchToNextWindow();
				isSelected = addToCartBtn.isEnabled();
			}
		} catch (Exception e) {
			log.error("Failed select the book from the result");
			e.printStackTrace();
		}
		return isSelected;
	}

	// selecting nth book from the list
	public void addItemToCart() {
		clickOnElement(addToCartBtn);
		if (isWebElementVisible(confirmBtn)) {
			clickOnElement(confirmBtn);
		}
	}

	public boolean getCartItemsCount(int expectedCount) {
		int actualCount = Integer.parseInt(getElementTextValue(cartItemsCountIcon));
		if (actualCount == expectedCount) {
			return true;
		} else
			return false;
	}

}
