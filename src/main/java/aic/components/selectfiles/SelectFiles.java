package aic.components.selectfiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.server.browserlaunchers.Sleeper;

import shared.components.Component;
import shared.configuration.SuiteConfiguration;
import shared.constants.Delays;
import shared.constants.FrameworkLocations;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;
import shared.util.FilesUIUtil;

public class SelectFiles extends Component {

	private static final Logger LOG = LogManager.getLogger(SelectFiles.class);
	private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

	public SelectFiles(SetDriver driver) {
		super(driver);
		LOG.info("Select Files dialog created");
	}

	/**
	 * Choose files under My Computer in SelectFiles dialog
	 */
	public String[] chooseFileUnderMyCom(String filepath) {
		clickTabMyComputer();
		String[] file = setChooseFiles(filepath);
		return file;
	}

	/**
	 * Choose first file under AIC in SelectFiles dialog
	 */
	public String chooseFirstFileUnderAIC() {
		clickTabAICFiles();
		selectFile(SelectFilesElements.CHOOSE_FIRST_FILE_UNDER_AIC_FILES);
		String filename = findFirstFileNameUnderAIC();
		clickContinueBTN();
		return filename;
	}

	/**
	 * find first file name under AIC in SelectFiles dialog
	 */
	public String findFirstFileNameUnderAIC() {
		return findFileName(SelectFilesElements.FIRST_FILE_UNDER_AIC_FILES);
	}

	/**
	 * Choose first file under Recent in SelectFiles dialog
	 */
	public String chooseFirstFileUnderRecent() {

		clickTabRecentFiles();
		selectFile(SelectFilesElements.CHOOSE_FIRST_FILE_UNDER_RECENT_FILES);
		String filename = findFirstFileNameUnderRecent();
		clickContinueBTN();
		return filename;

	}

	/**
	 * check has files under Recent in SelectFiles dialog
	 */
	public boolean checkHasNoFileUnderRecent() {
		clickTabRecentFiles();
		Sleeper.sleepTightInSeconds(Delays.DELAY_1_SECOND);
		return isDisplayed(SelectFilesElements.NO_FILES_UNDER_RECENT_FILES , Delays.DELAY_10_SECONDS);
	}

	/**
	 * check has files under Recent in SelectFiles dialog
	 */
	public boolean checkHasNoFileUnderAIC() {
		clickTabAICFiles();
		Sleeper.sleepTightInSeconds(Delays.DELAY_1_SECOND);
		return isDisplayed(SelectFilesElements.NO_FILE_UNDER_AIC_FILES , Delays.DELAY_10_SECONDS);
	}

	/**
	 * find first file name under Recent in SelectFiles dialog
	 */
	public String findFirstFileNameUnderRecent() {
		return findFileName(SelectFilesElements.FIRST_FILE_UNDER_RECENT_FILES);
	}

	/**
	 * Clicks the tab My Computer
	 */
	public void clickTabMyComputer() {
		LOG.info("click My Computer Tab");
		clickElement(SelectFilesElements.MY_COMPUTER_TAB);
	}

	/**
	 * Clicks the tab Acrobat.com Files
	 */
	public void clickTabAICFiles() {
		LOG.info("click AIC Files Tab");
		clickElement(SelectFilesElements.AIC_FILES_TAB);
	}

	/**
	 * Clicks the tab Recent Files
	 */
	public void clickTabRecentFiles() {
		LOG.info("click Recent Files Tab");
		clickElement(SelectFilesElements.RECENT_FILES_TAB);
	}

	/**
	 * Select first File in the files list
	 */
	private void selectFile(SeBlob seblob) {
		LOG.info("Select first File in the files list");
		clickElement(seblob);
	}

	/**
	 * Select first File in the files list
	 */
	private String findFileName(SeBlob seblob) {
		LOG.info("Select first File in the files list");
		driver.waitForElementVisible(seblob, Delays.DELAY_3_SECONDS);
		return driver.findElement(seblob.getBy()).getText();
	}

	/**
	 * Clicks the continue BTN
	 */
	public void clickContinueBTN() {
		LOG.info("click Continue BTN");
		clickElement(SelectFilesElements.CONTINUE_BTN);
	}

	/**
	 * Clicks the cancel BTN
	 */
	public void clickCancelBTN() {
		LOG.info("click Cancel BTN");
		clickElement(SelectFilesElements.CANCEL_BTN);
	}

	/**
	 * Clicks the Choose Files BTN under my computer
	 */
	public void clickChooseFiles() {
		LOG.info("click Choose Files BTN");
		clickElement(SelectFilesElements.CHOOSE_BTN_UNDER_MYCOM);
	}

	/**
	 * Choose one File under my computer
	 * 
	 * @author mengxue.shi
	 * @return file name
	 */
	public String[] setChooseFiles(String filepath) {
		LOG.info("Choose File " + filepath);
		filepath = FrameworkLocations.TESTDATA_LOCATION + filepath;
		String[] filesInfo = FilesUIUtil.addTimeStamptoAFile(filepath);
		driver.findElement(SelectFilesElements.CHOOSE_FILE_UNDER_MYCOM.getBy()).sendKeys(filesInfo[1]);
		return filesInfo;
	}
	
	/**
	 * Delete TimeStamp files
	 * 
	 * @author mengxue.shi
	 * @return file name
	 */
	public boolean deleteFiles(String filepath) {
		LOG.info("Choose File " + filepath+" to delete");
		try {
			FilesUIUtil.judgeFileExists(filepath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Finds the provided SeBlob and clicks on it
	 * 
	 * @param tab
	 * @param seblob
	 */
	private void clickElement(SeBlob seblob) {
		driver.waitForElementVisible(seblob, Delays.DELAY_3_SECONDS);
		WebElement element = driver.findElement(seblob.getBy());
		if (isContainsChromeBrowser){
			driver.executeScript("arguments[0].click()", element);
		}else{
			element.click();
		}
		
	}

}
