package shared;

/**
 * Test case class designed to simplify running Selenium based
 * tests against CCM pages
 *
 * The class reads the testConfig properties files to
 * determine how/where to execute the test run.  In the properties
 * file a user can set what environment to target (qe, stage,
 * prod), what hub to use (local, SET), and what browser combination
 * to use.  See comments in the testConfig properties file for
 * details.
 *
 * Additionally command line properties can be used to override the
 * environment, hub and browser of the testConfig properties file.
 * To override the defaults, enter alternative values for the
 * following system properties: set.hub, set.environment, set.browsers.
 * For example:
 *
 * mvn test -Dset.hub=XXX:4444/wd/hub
 *
 * @author
 *
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import shared.configuration.TestConfiguration;
import shared.constants.FrameworkLocations;

/**
 * This extends SetTestCase and uses Phil's parameter code to read data from json files. This class may need to be
 * upgraded/generalized in the future to be used for more then the detail page tests.
 *
 * @author
 *
 */
public class SetDataTestCase extends SetTestClassNG {

    private static final Logger LOG = LogManager.getLogger(SetDataTestCase.class);
    private static final String TESTDATE_FOLDER_PATH = FrameworkLocations.TESTDATA_LOCATION;

    // constructor
    public SetDataTestCase(TestConfiguration testConfig) {
        super(testConfig);
    }

}
