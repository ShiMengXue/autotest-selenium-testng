package aic.components.headers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.server.browserlaunchers.Sleeper;

import shared.components.Component;
import shared.configuration.SuiteConfiguration;
import shared.constants.Delays;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;

public class AicHeader extends Component {
	private static final Logger LOG = LogManager.getLogger(AicHeader.class);
	private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

	public AicHeader(SetDriver driver) {
		super(driver);

		LOG.info("header created");
	}
	
	public boolean verifyAllExportedFilesReady(){
		List<WebElement> filesElements = getFileElements(AicHeaderElements.STATUS_MENU_ALL_EXPORT_FILES);
		for(int i=0; i<filesElements.size(); i++){
			if(!filesElements.get(i).findElement(AicHeaderElements.STATUS_MENU_ALL_EXPORT_FILES_COMPLETE_STATUS.getBy()).isDisplayed()){
				Sleeper.sleepTightInSeconds(Delays.DELAY_3_SECONDS);
				for(int j=0; j<4; j++){
					if(!filesElements.get(i).findElement(AicHeaderElements.STATUS_MENU_ALL_EXPORT_FILES_COMPLETE_STATUS.getBy()).isDisplayed()){
						Sleeper.sleepTightInSeconds(Delays.DELAY_3_SECONDS);
					}else{
						break;
					}
				}
			}
		}
		return true;
	}
	

	/**
	 * Finds all Exported Files
	 * 
	 * @param tab
	 * @param seblob
	 */
	private List<WebElement> getFileElements(SeBlob seblob) {
		List<WebElement> filesElements = driver.findElements(seblob.getBy());
		return filesElements;
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
