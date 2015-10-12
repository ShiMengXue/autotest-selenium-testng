package soc.pages;

import shared.configuration.SuiteConfiguration;
import shared.page.Page;
import shared.setDriver.SetDriver;

/**
 * Created by developer on 15-9-28.
 */
public class SocPage extends Page {

    public SocPage(SetDriver driver) {
        super(driver);
        domain = SuiteConfiguration.getInstance().getBaseURL();
    }
}
