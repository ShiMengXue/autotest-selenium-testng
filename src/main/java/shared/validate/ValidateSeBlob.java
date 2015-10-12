package shared.validate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.server.browserlaunchers.Sleeper;
import shared.configuration.SuiteConfiguration;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;

import java.util.List;

/**
 * Class that consolidates all the SeBlob comparative methods used by page and component objects for validating SeBlobs.
 * The methods typically perform 2 actions, finding the element in the SeBlob and then comparing the found instance with
 * the data in the provided seBlob. Depending on the element found, the verification varies. For text evaluation, just
 * the text is checked. For a link evaluation, the text/image of the link is checked, as well as the href of the link,
 * and whether the link is enabled. Different element types have a unique set of attributes that are checked. Any
 * WebElement validation is handled by the ValidateWebElement Class.
 * 
 * @author
 * 
 */

public class ValidateSeBlob {

	private static final Logger LOG = LogManager.getLogger(ValidateSeBlob.class);
	protected SetDriver driver;
	private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

	public ValidateSeBlob(SetDriver driver) {
		super();
		this.driver = driver;

		LOG.debug("ValidateSeBlob class instantiated");
	}

	/**
	 * Verifies if the element represented by the SeBlob is displayed on current page. Allows for a custom amount of
	 * time to wait for the element to appear
	 * 
	 * @param seBlob - element to search for
	 * *@param long - seconds to wait before giving up the search
	 * @return boolean - true if element is found and displayed
	 */
	public boolean isDisplayed(SeBlob seBlob, long secondsToWait) {
		WebElement element;
		boolean displayed = false;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
			displayed = element.isDisplayed();
		} catch (TimeoutException e) {
			LOG.info("isDisplayed did not find element within seconds: " + secondsToWait);
			return false;
		}

		LOG.info("isDisplayed = " + displayed);
		return displayed;
	}

	/**
	 * Verifies if the element represented by the SeBlob is displayed on current page.
	 * 
	 * @param seBlob - element to search for
	 * @return boolean - true if element is found and displayed
	 */
	public boolean isDisplayed(SeBlob seBlob) {
		return this.isDisplayed(seBlob, defaultWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob can be selected on current page. The element must be visible to
	 * be selectable. Allows for a custom amount of time to wait for the element to appear on the page
	 * 
	 * @param seBlob - element to search for
	 * *@param long - seconds to wait before giving up search
	 * @return boolean - true if element is selectable
	 * @throws Exception
	 */
	public boolean isSelected(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		WebElement element;
		boolean selected = false;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
			selected = element.isSelected();
		} catch (TimeoutException e) {
			LOG.info("isSelected did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		LOG.info("isSelected = " + selected);
		return selected;
	}

	/**
	 * Verifies if the element represented by the SeBlob can be selected on current page. The element must be visible to
	 * be selectable.
	 * 
	 * @param seBlob - element to search for
	 * @return boolean - true if element is selectable
	 * @throws Exception
	 */
	public boolean isSelected(SeBlob seBlob) throws TimeoutException {
		return this.isSelected(seBlob, defaultWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob is enabled on current page. The element must be visible to be
	 * enabled. Allows for a custom amount of time to wait for the element to appear on the page
	 * 
	 * @param seBlob - element to search for
	 * *@param long - seconds to wait before giving up search
	 * @return boolean - true if element is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean isEnabled(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		WebElement element;
		boolean enabled = false;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
			enabled = element.isEnabled();
		} catch (TimeoutException e) {
			LOG.info("isEnabled did not find the element within seconds: " + secondsToWait);
			throw e;

		}

		LOG.info("isEnabled = " + enabled);
		return enabled;
	}

	/**
	 * Verifies if the element represented by the SeBlob is enabled on current page. The element must be visible to be
	 * enabled.
	 *
	 * @param seBlob - element to search for
	 * @return boolean - true if element is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean isEnabled(SeBlob seBlob) throws TimeoutException {
		return this.isEnabled(seBlob, defaultWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link to a Adobe.com page that matches the expected
	 * text, has the correct href, and is enabled. Allows for custom text comparison when the default dictionary look is
	 * not applicable for the element.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param String - a special string,different from the SeBlob, that is expected
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyAdobeDotComTextLink(SeBlob seBlob, String expectedText) throws TimeoutException {
		return this.verifyAdobeDotComTextLink(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link to a Adobe.com page that matches the expected
	 * text, has the correct href, and is enabled. Allows for custom text comparison when the default dictionary look is
	 * not applicable for the element. Allows for custom amount of time to wait for the element to appear on the page.
	 *
	 * Note: The expected href to actual href comparison in this method must use 'contains' not 'equals' because the
	 * non-goUrl seBlobs do not contain the environment (such as 'https://stage.adobecc.com'). In addition for pages
	 * with IDs in the url (such as 'https://stage.adobecc.com/team/admin/C805A541E868246D8C2B'), we do not know the ID
	 * to create the full url for 'equals', so 'contains' should be enough to verify the url.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param String - a special string,different from the SeBlob, that is expected
	 * *@param long - seconds to wait before giving up search
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyAdobeDotComTextLink(SeBlob seBlob, String expectedText, long secondsToWait)
			throws TimeoutException {
		WebElement element;
		boolean elementVerified = true;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyTextLink did not find the element within seconds: " + secondsToWait);
			throw e;
		}

		if (!ValidateWebElement.validateTextEqual(element, expectedText)) {
			elementVerified = false;
		}
		if (!ValidateWebElement.validateAdobeDotComHrefContains(element, seBlob.getHref())) {
			elementVerified = false;
		}
		return elementVerified;
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link that matches the expected text, has the correct
	 * href, and is enabled. Allows for custom text comparison when the default dictionary look is not applicable for
	 * the element. Allows for custom amount of time to wait for the element to appear on the page.
	 *
	 * Note: The expected href to actual href comparison in this method must use 'contains' not 'equals' because the
	 * non-goUrl seBlobs do not contain the environment (such as 'https://stage.adobecc.com'). In addition for pages
	 * with IDs in the url (such as 'https://stage.adobecc.com/team/admin/C805A541E868246D8C2B'), we do not know the ID
	 * to create the full url for 'equals', so 'contains' should be enough to verify the url.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param String - a special string,different from the SeBlob, that is expected
	 * *@param long - seconds to wait before giving up search
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextLink(SeBlob seBlob, String expectedText, long secondsToWait) throws TimeoutException {
		WebElement element;
		boolean elementVerified = true;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyTextLink did not find the element within seconds: " + secondsToWait);
			throw e;
		}

		if (!ValidateWebElement.validateTextEqual(element, expectedText)) {
			elementVerified = false;
		}
		if (!ValidateWebElement.validateHrefContains(element, seBlob.getHref())) {
			elementVerified = false;
		}
		return elementVerified;
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link that matches the expected text, has the correct
	 * href, and is enabled. Allows for custom text comparison when the default dictionary look is not applicable for
	 * the element.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param String - a special string,different from the SeBlob, that is expected
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextLink(SeBlob seBlob, String expectedText) throws TimeoutException {
		return this.verifyTextLink(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link that matches the expected text, has the correct
	 * href, and is enabled. Allows for custom amount of time to wait for the element to appear on the page.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param long - seconds to wait before giving up search
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextLink(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		String expectedTextForLink = seBlob.getText();
		return verifyTextLink(seBlob, expectedTextForLink, secondsToWait);
	}

	/**
	 * Verifies if the element represented by the SeBlob is a text link that matches the expected text, has the correct
	 * href, and is enabled.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * @return boolean - true if link text and href match expected, and is enabled
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextLink(SeBlob seBlob) throws TimeoutException {
		String expectedTextForLink = seBlob.getText();
		return this.verifyTextLink(seBlob, expectedTextForLink, defaultWait);
	}

	/**
	 * Verifies that the element represented by the SeBlob is an image that has a src that matches the expected. Allows
	 * for custom amount of time to wait for image to appear on page
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param long - seconds to wait for link to appear
	 * @return boolean - true if the src matches and is displayed
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyImage(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyImage did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		return ValidateWebElement.validateAttributeTextEndsWith(element, "src", seBlob.getHref());

	}

	/**
	 * Verifies that the element represented by the SeBlob is an image that has a src which contains the expected.
	 * Allows for custom amount of time to wait for image to appear on page
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * *@param long - seconds to wait for link to appear
	 * @return boolean - true if the src contains the expected string
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyAvatar(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyAvatar did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		return ValidateWebElement.validateAttributeTextContains(element, "src", seBlob.getHref());

	}

	/**
	 * Verifies that the element represented by the SeBlob is an image that has a src that matches the expected.
	 *
	 * @param seBlob - seBlob for the element and it's attributes for comparison
	 * @return boolean - true if the src matches and is displayed
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyImage(SeBlob seBlob) throws TimeoutException {
		return this.verifyImage(seBlob, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that matches the expected. Allows for custom text
	 * for situations where the standard dictionary look up is not appropriate. Allows for custom amount of time to wait
	 * for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text that should be on the page
	 * *@param long - seconds to wait for element before giving up search
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextEqual(SeBlob seBlob, String expectedText, long secondsToWait) throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyTextEqual did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		return ValidateWebElement.validateTextEqual(element, expectedText);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that matches the expected. Allows for custom text
	 * for situations where the standard dictionary look up is not appropriate.
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text that should be on the page
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextEqual(SeBlob seBlob, String expectedText) throws TimeoutException {
		return this.verifyTextEqual(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that contains the expected. Allows for custom text
	 * for situations where the standard dictionary look up is not appropriate. Allows for custom amount of time to wait
	 * for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text that should be on the page
	 * *@param long - seconds to wait for element before giving up search
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextContain(SeBlob seBlob, String expectedText, long secondsToWait) throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyTextContains did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		return ValidateWebElement.validateTextContains(element, expectedText);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that contains the expected. Allows for custom text
	 * for situations where the standard dictionary look up is not appropriate.
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text that should be on the page
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextContain(SeBlob seBlob, String expectedText) throws TimeoutException {
		return this.verifyTextContain(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that matches the expected. Allows for custom amount
	 * of time to wait for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param long - seconds to wait for element before giving up search
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextEqual(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		String expectedText = seBlob.getText();
		return this.verifyTextEqual(seBlob, expectedText, secondsToWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has text that matches the expected.
	 *
	 * @param seBlob - page element to find
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyTextEqual(SeBlob seBlob) throws TimeoutException {
		String expectedText = seBlob.getText();
		return this.verifyTextEqual(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has placeholder text displayed that matches the provided
	 * expected text. Allows for custom text for situations where the standard dictionary look up is not appropriate.
	 * Allows for custom amount of time to wait for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text in the input control
	 * *@param long - seconds to wait before giving up search for element
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyPlaceHolderTextEqual(SeBlob seBlob, String expectedText, long secondsToWait)
			throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyPlaceHolderTextEqual did not find the element within seconds: " + secondsToWait);
			throw e;
		}

		return ValidateWebElement.validateAttributeTextEquals(element, "placeholder", expectedText);
	}

	/**
	 * Verifies that the element represented by the seBlob has placeholder text displayed that matches the provided
	 * expected text. Allows for custom text for situations where the standard dictionary look up is not appropriate.
	 *
	 * @param seBlob - page element to find
	 * *@param String - expected text in the input control
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyPlaceHolderTextEqual(SeBlob seBlob, String expectedText) throws TimeoutException {
		return this.verifyPlaceHolderTextEqual(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has placeholder text displayed that matches the provided
	 * expected text. Allows for custom amount of time to wait for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param long - seconds to wait before giving up search for element
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyPlaceHolderTextEqual(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		String expectedText = seBlob.getText();
		return this.verifyPlaceHolderTextEqual(seBlob, expectedText, secondsToWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has placeholder text displayed that matches the provided
	 * expected text.
	 *
	 * @param seBlob - page element to find
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyPlaceHolderTextEqual(SeBlob seBlob) throws TimeoutException {
		String expectedText = seBlob.getText();
		return this.verifyPlaceHolderTextEqual(seBlob, expectedText, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has a href that matches the provided href and is enabled.
	 * Intended for verification of links when the link doesn't have an applicable text/image token, or the href is
	 * modified because of language. Allows for custom amount of time to wait for the element to appear on the page
	 *
	 * @param seBlob - element on page to find and verify
	 * @param expectedHref - custom href to verify against rather than SeBlob href
	 * @param secondsToWait - seconds to wait before giving up search
	 * @return boolean - true if the expected href is contained in the found href
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyHref(SeBlob seBlob, String expectedHref, long secondsToWait) throws TimeoutException {
		WebElement element;

		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyHref did not find the element within seconds: " + secondsToWait);
			throw e;

		}
		return ValidateWebElement.validateHrefContains(element, expectedHref);
	}

	/**
	 * Verifies that the element represented by the seBlob has a href that matches the provided href and is enabled.
	 * Intended for verification of links when the link doesn't have an applicable text/image token, or the href is
	 * modified because of language.
	 *
	 * @param seBlob - element on page to find and verify
	 * @param expectedHref - custom href to verify against rather than SeBlob href
	 * @return boolean - true if the expected href is contained in the found href
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyHref(SeBlob seBlob, String expectedHref) throws TimeoutException {
		return verifyHref(seBlob, expectedHref, defaultWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has a href that matches the provided href and is enabled.
	 * Intended for verification of links when the link doesn't have an applicable text/image token. Allows for custom
	 * amount of time to wait for the element to appear on the page
	 *
	 * @param seBlob - element on page to find and verify
	 * @param secondsToWait - seconds to wait before giving up search
	 * @return boolean - true if the expected href is contained in the found href
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyHref(SeBlob seBlob, long secondsToWait) throws TimeoutException {
		return verifyHref(seBlob, seBlob.getHref(), secondsToWait);
	}

	/**
	 * Verifies that the element represented by the seBlob has a href that matches the provided href and is enabled.
	 * Intended for verification of links when the link doesn't have an applicable text/image token.
	 *
	 * @param seBlob - element on page to find and verify
	 * @return boolean - true if the expected href is contained in the found href
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyHref(SeBlob seBlob) throws TimeoutException {
		return verifyHref(seBlob, seBlob.getHref(), defaultWait);
	}

	/**
	 * Verifies the browser url contains the expected url string
	 *
     * *@param String - the expected url
	 * *@param long - seconds to wait before giving up
	 * @return boolean - true if url contains expected url
	 */
	public boolean verifyBrowserUrl(String expectedUrl, long secondsToWait) {
		long waited = 0;
		String currentUrl = "";

		LOG.info("verifyBrowserUrl Expected: " + expectedUrl);
		while (waited < secondsToWait) {
			currentUrl = driver.getCurrentUrl();
			// Check if current url matches (contains) expected
			if (currentUrl.contains(expectedUrl)) {
				LOG.info("verifyBrowserUrl Found:    " + currentUrl);
				return true;

			} else {
				Sleeper.sleepTightInSeconds(1);
				waited += 1;
			}

		}

		// Failed to find expected Url
		LOG.info("verifyBrowserUrl did not find the expected url within seconds: " + secondsToWait);
		LOG.info("verifyBrowserUrl Found:              " + currentUrl);
		return false;
	}

	/**
	 * Verifies the browser url contains the expected url string
	 *
	 * *@param String - the expected url
	 * @return boolean - true if url contains expected url
	 */
	public boolean verifyBrowserUrl(String expectedUrl) {
		return verifyBrowserUrl(expectedUrl, defaultWait);
	}

	/**
	 * This method finds the supplied SeBlob, then finds all the child elements within it that the supplied css. It then
	 * compares the number of children found to the expected number of children contained in the SeBlob. It also
	 * verifies that each feature in the SeBlob is in the children set. If the count is wrong, or an expected child is
	 * not found, the method returns false. The SeBlob supplied must have a TextList member defined.
	 *
	 * @param seBlob
	 * @param css
	 * @param waitTime
	 * @return
	 */
	public boolean verifyFeatureList(SeBlob seBlob, String css, long waitTime) {
		// find list of features
		WebElement featureList = driver.waitForElementVisible(seBlob, waitTime);
		List<WebElement> foundFeatures = featureList.findElements(By.cssSelector(css));

		List<String> expectedFeatures = seBlob.getTextList();

		// translate list
		for (int i = 0; i < expectedFeatures.size(); i++) {
			String trans = expectedFeatures.get(i);
			trans = trans.replaceAll("\\{\\d+\\}", "").trim();
			expectedFeatures.set(i, trans);
		}

		// check each feature has a match
		boolean check = true;
		for (WebElement feature : foundFeatures) {

			if (!expectedFeatures.contains(feature.getText())) {
				LOG.info("ERROR: '" + feature.getText() + "' should not be in the list");
				check = false;
			}
			LOG.info("FOUND: " + feature.getText());
		}
		if (check == true) {
			LOG.info("All the expected feature were found on the page");
		}

		// compare number of features count
		int foundCount = foundFeatures.size();
		int expectedCount = expectedFeatures.size();

		if (foundCount != expectedCount) {
			LOG.info("Expected " + expectedCount + " feature(s), but found " + foundCount);
			check = false;
		}
		if (check) {
			LOG.info("Expected number of features found on page");
		}
		return check;
	}

	/**
	 * Verifies that the element represented by the seBlob has attribute 'value' that matches the expected. Allows for
	 * custom amount of time to wait for the element to appear on the page
	 *
	 * @param seBlob - page element to find
	 * *@param long - seconds to wait for element before giving up search
	 * @return boolean - true if the text is displayed and matches the expected
	 * @throws org.openqa.selenium.TimeoutException
	 */
	public boolean verifyAttributeValue(SeBlob seBlob, String expectedValue, long secondsToWait)
			throws TimeoutException {
		WebElement element;
		try {
			element = driver.waitForElementVisible(seBlob, secondsToWait);
		} catch (TimeoutException e) {
			LOG.info("verifyHref did not find the element within seconds: " + secondsToWait);
			throw e;
		}
		return ValidateWebElement.validateAttributeTextEquals(element, "value", expectedValue);
	}
}
