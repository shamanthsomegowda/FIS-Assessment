package com.ebay.utils;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.ebay.driver.Driver;

public class GenericWebFunctions extends Driver {

	WaitsUtil waitsUtil = new WaitsUtil();

	// To perform click operation on desired web element
	public void clickOnElement(WebElement webElement) {
		try {
			webElement.click();
		} catch (NoSuchElementException e) {
			log.warn("Caught NoSuchElementException while trying perform click");
			waitsUtil.fluentWaitForClickOperation(webElement, 30);
			webElement.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// To key value in a text field
	public void keyOrTypeValue(WebElement webElement, String textValue) {
		try {
			webElement.click();
		} catch (NoSuchElementException e) {
			log.warn("Caught NoSuchElementException while trying perform sendkeys");
			waitsUtil.fluentWaitForClickOperation(webElement, 30);
			webElement.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		webElement.clear();
		webElement.sendKeys(textValue);
	}

	// To get value from the node base on attribute
	public String getValueByAttribute(WebElement webElement, String attributeName) {
		String strVal = null;
		try {
			strVal = webElement.getDomAttribute(attributeName);
		} catch (NoSuchElementException e) {
			log.warn("Caught NoSuchElementException while trying perform sendkeys");
			waitsUtil.fluentWaitForVisibility(30, webElement);
			strVal = webElement.getDomAttribute(attributeName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strVal;
	}

	// Move to or hover an element
	public void moveToOrMouseHover(WebElement webElement) {
		Actions actions = new Actions(driver);
		try {
			actions.scrollToElement(webElement);
			actions.moveToElement(webElement);
		} catch (NoSuchElementException e) {
			log.warn("NoSuchElementException exception encountered in moveToOrMouseHover ()");
			waitsUtil.fluentWaitForClickOperation(webElement, 30);
		} catch (ElementNotInteractableException e) {
			log.warn("ElementNotInteractableException exception encountered in moveToOrMouseHover ()");
			waitsUtil.fluentWaitForClickOperation(webElement, 30);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// switch to next window
	public void switchToNextWindow() {
		String currentWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles(); // Get all open windows
		Iterator<String> i1 = allWindows.iterator();
		while (i1.hasNext()) {
			String childwindow = (String) i1.next();
			if (!currentWindow.equalsIgnoreCase(childwindow)) {
				log.info("Switching to next window");
				driver.switchTo().window(childwindow);
			}
		}

	}

	// To perform click operation on desired web element
	public String getElementTextValue(WebElement webElement) {
		log.debug("performing Get Text");
		try {
			return webElement.getText();
		} catch (NoSuchElementException e) {
			log.warn("Caught NoSuchElementException while trying perform get text");
			waitsUtil.fluentWaitForClickOperation(webElement, 30);
			return webElement.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
	}

	// To perform click operation on desired web element
	public boolean isWebElementVisible(WebElement webElement) {
		log.debug("performing visibility check on web element");
		try {
			return webElement.isDisplayed();
		} catch (NoSuchElementException e) {
			log.info("Option confirmation button not applicable to this item");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
