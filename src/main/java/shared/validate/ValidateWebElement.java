package shared.validate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

/**
 * The Class ValidateWebElement.
 * 
 * ValidateWebElement provides a common location for validating Selenium WebElements This allows for consistent logging
 * and handling of WebElements across the test harness. Each method provides validation for a particular item of a
 * WebElement like it's text value or it's attributes. That validation is of a specific type that's relevant to that
 * item like equals or endsWith. These methods are also used as the base WebElement validation for the SeBlob
 * validation. ValidateWebElement does not do any dictionary lookup for localization. The localized strings need to be
 * passed in. It does however build the correct go url based on language being displayed.
 * 
 * @author pwarwick
 * 
 */
public class ValidateWebElement {

	private static final Logger LOG = LogManager.getLogger(ValidateWebElement.class);

	/**
	 * Validate the text value of the element is equal to the expectedText.
	 * 
	 * @param element the element
	 * @param expectedText the expected text
	 * @return true, if successful
	 */
	public static boolean validateTextEqual(WebElement element, String expectedText) {
		String displayedText = element.getText();

		LOG.info("validateTextEqual Expected:" + expectedText + ", Found:" + displayedText);

		if (expectedText == null || !expectedText.equals(displayedText)) {
			LOG.warn("validateTextEqual FALSE\n\tExpected: " + expectedText + "\n\tFound   : " + displayedText);
			return false;
		} else {
			LOG.debug("validateTextEqual found expectedText: " + expectedText);
			return true;
		}
	}

	/**
	 * Validate the String value of href contains the expectedHref String.
	 * 
	 * @param element the element
	 * @param expectedHref the expected href
	 * @return true, if successful
	 */
	public static boolean validateAdobeDotComHrefContains(WebElement element, String expectedHref) {
		String foundHref = element.getAttribute("href");

		if (expectedHref == null) {
			LOG.info("validateHrefContains DOES NOT CONTAIN Expected:" + expectedHref + ", Found:" + foundHref);
			return false;
		}

		LOG.info("Expected:" + expectedHref);
		LOG.info("Found   :" + foundHref);

		boolean elementVerified = foundHref.contains(expectedHref);
		if (!elementVerified) {
			LOG.info("Expected href " + expectedHref + " does not contain " + foundHref);
		}
		return elementVerified;
	}

	/**
	 * Validate the String value of href contains the expectedHref String.
	 * 
	 * @param element the element
	 * @param expectedHref the expected href
	 * @return true, if successful
	 */
	public static boolean validateHrefContains(WebElement element, String expectedHref) {
		String originalExpectedHref = expectedHref;
		String foundHref = element.getAttribute("href");

		if (expectedHref == null) {
			LOG.error("validateHrefContains expectedHref is null. Update SeBlob / parameters with valid href.");
			return false;
		}

		LOG.info("validateHrefContains Expected:" + expectedHref + ", Found:" + foundHref);

		boolean elementVerified = foundHref.contains(expectedHref);
		if (!elementVerified) {
			LOG.warn("validateHrefContains FALSE\n\tExpected:" + expectedHref + "\n\tFound   :" + foundHref);
		} else {
			LOG.info("validateHrefContains TRUE");
		}
		return elementVerified;
	}

	/**
	 * Validate the String value of specified attribute equals the String expectedText.
	 * 
	 * @param element the element
	 * @param attribute the attribute
	 * @param expectedText the expected text
	 * @return true, if successful
	 */
	public static boolean validateAttributeTextEquals(WebElement element, String attribute, String expectedText) {
		String displayedText = element.getAttribute(attribute);

		LOG.info("validateAttributeTextEquals Attribute:" + attribute + ", Expected:" + expectedText + ", Found:"
				+ displayedText);
		if (expectedText == null) {
			LOG.info("validateAttributeTextEquals NOT EQUAL Attribute:" + attribute + ", Expected:" + expectedText
					+ ", Found:" + displayedText);
			return false;
		}
		boolean elementVerified = expectedText.equals(displayedText);
		if (!elementVerified) {
			LOG.info("validateAttributeTextEquals NOT EQUAL Attribute:" + attribute + ", Expected:" + expectedText
					+ ", Found:" + displayedText);
		}
		return elementVerified;
	}

	/**
	 * Validate the String value of specified attribute endsWith the String expectedText.
	 * 
	 * @param element the element
	 * @param attribute the attribute
	 * @param expectedText the expected text
	 * @return true, if successful
	 */
	public static boolean validateAttributeTextEndsWith(WebElement element, String attribute, String expectedText) {
		if (attribute == null) {
			LOG.info("validateAttributeTextEndsWith DOES NOT END WITH Attribute:" + attribute + ", Expected:"
					+ expectedText + ", Found:null");
			return false;
		}

		String displayedText = element.getAttribute(attribute);
		if (displayedText == null) {
			LOG.info("validateAttributeTextEndsWith DOES NOT END WITH Attribute:" + attribute + ", Expected:"
					+ expectedText + ", Found:" + displayedText);
			return false;
		}

		LOG.info("validateAttributeTextEndsWith Attribute: " + attribute + ", Expected:" + expectedText + ", Found:"
				+ displayedText);
		boolean elementVerified = displayedText.endsWith(expectedText);
		if (!elementVerified) {
			LOG.info("validateAttributeTextEndsWith DOES NOT END WITH Attribute:" + attribute + ", Expected:"
					+ expectedText + ", Found:" + displayedText);
		}
		return elementVerified;
	}

	/**
	 * Validate the String value of specified attribute contains the expectedText string.
	 * 
	 * @param element the element
	 * @param attribute the attribute
	 * @param expectedText the expected text
	 * @return true, if actual text contains expectedText
	 */
	public static boolean validateAttributeTextContains(WebElement element, String attribute, String expectedText) {
		if (attribute == null) {
			LOG.info("validateAttributeTextContains does not contain attribute to proceed further validations");
			return false;
		}

		String displayedText = element.getAttribute(attribute);
		if (displayedText == null) {
			LOG.info("validateAttributeTextContains DOES NOT CONTAINS text:" + " Expected:" + expectedText + ", Found:"
					+ displayedText);
			return false;
		}

		LOG.info("validateAttributeTextContains text: " + " Expected:" + expectedText + ", Found:" + displayedText);

		return displayedText.contains(expectedText);
	}

	/**
	 * Validate the text value of the element contains to the expectedText.
	 * 
	 * @param element the element
	 * @param expectedText the expected text
	 * @return true, if successful
	 * 
	 */
	public static boolean validateTextContains(WebElement element, String expectedText) {

		String displayedText = element.getText();

		if (displayedText == null || !displayedText.contains(expectedText)) {
			LOG.warn("validateTextContains returns FALSE\n\tExpected: " + expectedText + "\n\tFound   : "
					+ displayedText);
			return false;
		} else {
			LOG.debug("validateTextContains returns TRUE\n\tExpected: " + expectedText + "\n\tFound   : "
					+ displayedText);
			return true;
		}
	}

}
