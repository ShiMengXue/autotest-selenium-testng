package shared.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.constants.Delays;
import shared.setDriver.SetDriver;
import shared.validate.ValidateSeBlob;

/**
 * Base page class for page objects. Instantiates the header and SUSI objects. The footer, domain, and flag fields need
 * to be set by subclasses.
 * 
 * @author ricsimps
 * 
 */
public abstract class Page extends ValidateSeBlob {

	private static final Logger LOG = LogManager.getLogger(Page.class);
//	public Susi susi = null;
	public String domain = null;
	public String flag = null;

	/**
	 * Creates page object: gets the language from Configuration, initializes components, and sets the page language
	 * (setPageLanguage method updates the dictionary).
	 * 
	 * @param driver
	 */
	public Page(SetDriver driver) {
		super(driver);

//		susi = new Susi(driver);

		LOG.info("Page object created");
	}

	/**
	 * Open a page to the specified page path. Use the CCM UI base URL and feature set that is in Configuration.
	 * 
	 * @param pagePath - page path to open
	 */
	public void navigateToPage(String pagePath) {
		navigateToPage(pagePath, "");
	}

	/**
	 * Open a page to the specified page path then wait 60 seconds for the passed in SeBlob to display on the page. Use
	 * the CCM UI base URL and feature set that is in Configuration.
	 * 
	 * Note: Do not use this method for pages that redirect to SUSI (such as the join pages), because the SUSI
	 * components will display not the page elements, so the page SeBlob will not be found.
	 * 
	 * @param pagePath - String of page path to open
	 * @param seblob_on_page - SeBlob of element on page to wait for
	 */
	public void navigateToPage(String pagePath, SeBlob seblob_on_page) {
		navigateToPage(pagePath, seblob_on_page, Delays.DELAY_60_SECONDS);
	}

	/**
	 * Open a page to the specified page path then wait the passed in amount of time for the passed in SeBlob to display
	 * on the page. Use the CCM UI base URL and feature set that is in Configuration.
	 * 
	 * Note: Do not use this method for pages that redirect to SUSI (such as the join pages), because the SUSI
	 * components will display not the page elements, so the page SeBlob will not be found.
	 * 
	 * @param pagePath - String of page path to open
	 * @param seblob_on_page - SeBlob of element on page to wait for
	 * @param secondsToWait the seconds to wait
	 */
	public void navigateToPage(String pagePath, SeBlob seblob_on_page, long secondsToWait) {
		navigateToPage(pagePath, "");

		// Wait for page to finish loading
		LOG.info("Waiting up to " + secondsToWait + " seconds for element '" + seblob_on_page.toString()
				+ "' to display on page");
		driver.waitForElementVisible(seblob_on_page, secondsToWait);
	}

	/**
	 * If we are NOT currently at the desired page, this method will navigate us there. We retrieve the CC ui base URL
	 * that was passed in, appends the page path, feature set, and additionalQuries. We then call the driver.get method
	 * to execute the URL navigation. If you need to navigate outside the CCM UI base URL, then you need need to call
	 * out to driver directly and do your own navigation.
	 * 
	 * @param pagePath - page path to open IE: 'join/pro'
	 * @param additionalQueries - any extra args (not the feature_flag) IE: 'locale=ja'
	 */
	public void navigateToPage(String pagePath, String additionalQueries) {

		// WARNING: If the user does NOT set the protocol in the set.soc_base_url parameter, Chrome will throw
		// 'Cannot navigate to invalid URL' exception.
		String newUrl = domain;

		// Add language to url if the test is running behind a feature set that supports SEO.
		// newUrl += "/" + getPageLanguage();

		// Add the page path that was passed in via the parameter to the new URL.
		if (!pagePath.isEmpty()) {
			pagePath = pagePath.startsWith("/") ? pagePath.substring(1) : pagePath;
			newUrl += "/" + pagePath;
		}

		// Add the feature set key and the feature set flag value. FEATURE_SET contains ? and =
		// We always set the Feature set so that it is clear, if there is or is NOT, a flag set.
		// newUrl += flag + SuiteConfiguration.getInstance().getFeatureSet();

		// If the user wants to come in with additional queries, add it.
		if (additionalQueries != null && !additionalQueries.isEmpty()) {
			if (additionalQueries.startsWith("&")) {
				newUrl += additionalQueries;
			} else {
				newUrl += "&" + additionalQueries;
			}
		}

		// Check if already on the page.
		if (!driver.getCurrentUrl().equalsIgnoreCase(newUrl.toString())) {
			// We are NOT on the page, so navigate to the page.
			// Start building the new URL, add the protocol, domain, port(if applicable).
			LOG.info("Navigating to '" + newUrl + "'");

			// Navigate to the page.
			driver.get(newUrl);
		} else {
			// We are already on the page, do nothing.
			LOG.info("ALREADY ON THE PAGE! " + driver.getCurrentUrl() + " vs: " + pagePath);
		}
	}


}
