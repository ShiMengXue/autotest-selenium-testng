package soc.pages.funcations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.configuration.SuiteConfiguration;
import shared.setDriver.SetDriver;
import soc.pages.SocPage;

/**
 * Created by developer on 15-9-28.
 */
public class FunclistPage extends SocPage {

    private static final Logger LOG = LogManager.getLogger(FunclistPage.class);
    private long defaultWait = SuiteConfiguration.getInstance().getImplicitTimeoutSec();

    public FunclistPage(SetDriver driver) {
        super(driver);
        LOG.info("Funcations list Page created");
    }




}
