package aic.pages;

import shared.configuration.SuiteConfiguration;
import shared.page.Page;
import shared.setDriver.SetDriver;
import aic.components.headers.AicHeader;
import aic.components.toolbar.ToolBar;

public class AicPage extends Page {

	public AicHeader header = new AicHeader(driver);
	public ToolBar toolbar = new ToolBar(driver);
	public boolean isContainsChromeBrowser;

	public AicPage(SetDriver driver) {
		super(driver);
		isContainsChromeBrowser=SuiteConfiguration.getInstance().isContainsChromeBrowser();
		domain = SuiteConfiguration.getInstance().getAicBaseURL();
	}
	
}