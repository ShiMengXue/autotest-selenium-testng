package aic.components.toolbar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import shared.components.Component;
import shared.configuration.SuiteConfiguration;
import shared.constants.Delays;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;
import shared.validate.ValidateWebElement;

public class ToolBar extends Component {

	private static final Logger LOG = LogManager.getLogger(ToolBar.class);
	private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

	public ToolBar(SetDriver driver) {
		super(driver);

		LOG.info("toolbar created");
	}

	/**
	 * Verifies the tab Files is displayed and localized.
	 * 
	 * @return
	 */
	public boolean verifyTabFiles() {
		// WebElement element = driver.findElement(ToolBarElements.TAB_FILES.getBy());
		// // WebElement element = tab.findElement(ToolBarElements.TAB_FILES.getBy());
		// String expectedText = dictionary.get(ToolBarElements.TAB_FILES.getText());
		// boolean value = expectedText.equalsIgnoreCase(element.getText());
		// return value;
		return verifyText(ToolBarElements.TAB_FILES);
	}

	/**
	 * Verifies the tab Export PDF is displayed and localized.
	 * 
	 * @return
	 */
	public boolean verifyTabExportPDF() {
		return verifyText(ToolBarElements.TAB_EXPORT_PDF);
	}

	/**
	 * Verifies the tab Create PDF is displayed and localized.
	 * 
	 * @return
	 */
	public boolean verifyTabCreatePDF() {
		return verifyText(ToolBarElements.TAB_CREATE_PDF);
	}

	/**
	 * Verifies the tab Combine PDF is displayed and localized.
	 * 
	 * @return
	 */
	public boolean verifyTabCombinePDF() {
		return verifyText(ToolBarElements.TAB_COMBINE_PDF);
	}

	/**
	 * Clicks the tab Files
	 */
	public void clickTabFiles() {
		LOG.info("click Files Tab");
		clickElement(ToolBarElements.TAB_FILES);
	}

	/**
	 * Clicks the tab Export PDF
	 */
	public void clickTabExportPDF() {
		LOG.info("click Export PDF Tab");
		clickElement(ToolBarElements.TAB_EXPORT_PDF);
	}

	/**
	 * Clicks the tab Create PDF
	 */
	public void clickTabCreatePDF() {
		LOG.info("click Create PDF Tab");
		clickElement(ToolBarElements.TAB_CREATE_PDF);
	}

	/**
	 * Clicks the tab Combine PDF
	 */
	public void clickTabCombinePDF() {
		LOG.info("click Combine PDF Tab");
		clickElement(ToolBarElements.TAB_COMBINE_PDF);
	}

	/**
	 * Finds the SeBlob provided and verifies the text is displayed and localized
	 * 
	 * @param tab
	 * @param seblob
	 * @return
	 */
	private boolean verifyText(SeBlob seblob) {
		driver.waitForElementVisible(seblob, Delays.DELAY_45_SECONDS);
		WebElement element = driver.findElement(seblob.getBy());
		String expectedText = dictionary.get(seblob.getText());
		return ValidateWebElement.validateTextEqual(element, expectedText);
	}

	/**
	 * Finds the provided SeBlob and clicks on it
	 * 
	 * @param tab
	 * @param seblob
	 */
	private void clickElement(SeBlob seblob) {
		driver.waitForElementVisible(seblob, Delays.DELAY_45_SECONDS);
		WebElement element = driver.findElement(seblob.getBy());
		if (isContainsChromeBrowser){
			driver.executeScript("arguments[0].click()", element);
		}else{
			element.click();
		}
	}
}
