package aic.pages.exportpdf;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.server.browserlaunchers.Sleeper;

import shared.configuration.SuiteConfiguration;
import shared.constants.Delays;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;
import shared.validate.ValidateWebElement;
import aic.components.selectfiles.SelectFiles;
import aic.pages.AicPage;

public class ExportPDFPage extends AicPage {

	public SelectFiles selectFiles;

	private static final Logger LOG = LogManager.getLogger(SelectFiles.class);
	private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

	public ExportPDFPage(SetDriver driver) {
		super(driver);
		selectFiles = new SelectFiles(driver);
		LOG.info("Export PDF Page created");
	}

	/**
	 * Verifies the page title is displayed and localized.
	 * 
	 * @return
	 */
	public boolean verifyPageTitle() {
		toolbar.clickTabExportPDF();
		return verifyText(ExportPDFElements.PAGE_TITLE);
	}

	/**
	 * Verifies the function of File Export By Recent.
	 * 
	 * @return
	 */
	public boolean verifyFileExportByRecent() {
		toolbar.clickTabExportPDF();
		clickSelectFiles();
		if (selectFiles.checkHasNoFileUnderRecent()) {
			LOG.info("There has no available files under my Recent tab.");
			return true;
		} else {
			String filename = selectFiles.chooseFirstFileUnderRecent();
			clickExport();
			return findFileInListView(filename, ".docx") || findFuzzyFileInListView(filename, ".docx");
		}
	}

	/**
	 * Verifies the function of File Export By Recent.
	 * 
	 * @return
	 */
	public boolean verifyFileExportByAIC() {
		toolbar.clickTabExportPDF();
		clickSelectFiles();
		if (selectFiles.checkHasNoFileUnderAIC()) {
			LOG.info("There has no available files under AIC tab.");
			return true;
		} else {
			String filename = selectFiles.chooseFirstFileUnderAIC();
			clickExport();
			return findFileInListView(filename, ".docx") || findFuzzyFileInListView(filename, ".docx");
		}
	}

	/**
	 * Verifies the function of File Export By Recent.
	 * 
	 * @return
	 */
	public boolean verifyFileExportByMyCom(String filepath) {
		toolbar.clickTabExportPDF();
		clickSelectFiles();
		String[] file = selectFiles.chooseFileUnderMyCom(filepath);
		clickExport();
		selectFiles.deleteFiles(file[1]);
		return findFileInListView(file[0], ".docx");
	}

	/**
	 * Clicks the Select PDF Files to Export BTN
	 */
	public void clickSelectFiles() {
		LOG.info("click Select PDF Files to Export BTN");
		clickElement(ExportPDFElements.SELECT_FILES_BTN);
	}

	/**
	 * Clicks the Export BTN
	 */
	public void clickExport() {
		LOG.info("click Export BTN");
		clickElement(ExportPDFElements.EXPORT_BTN);
		Sleeper.sleepTightInSeconds(Delays.DELAY_20_SECONDS);
	}

	/**
	 * find exits of the "fileName" under list view
	 */
	public boolean findFileInListView(String fileName, String type) {
		header.verifyAllExportedFilesReady();
		String[] fname = fileName.split("\\.");
		fileName = fname[0] + type;
		List<WebElement> elements = getFileElements();
		for (WebElement e : elements) {
			if (e.getText().equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Fuzzy find exits of the "fileName" under list view
	 * like find file test(dhiaseruieurise).docx export from test.pdf 
	 */
	public boolean findFuzzyFileInListView(String fileName, String type) {
		header.verifyAllExportedFilesReady();
		String[] fname = fileName.split("\\.");
		fileName = fname[0] + type;
		List<WebElement> elements = getFileElements();
		for (WebElement e : elements) {
			if (e.getText().contains(fname[0]) && e.getText().contains(type)) {
				return true;
			}
		}
		return false;
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
        String expectedText = seblob.getText();
//		String expectedText = dictionary.get(seblob.getText());
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

	private List<WebElement> getFileElements() {
		List<WebElement> filesElements = driver.findElements(ExportPDFElements.ALL_FILESNAME_HISTORY.getBy());
		return filesElements;
	}

}
