package shared.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.constants.ConfigurationKeys;
import shared.enums.Browsers;
import shared.enums.Platforms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton object that contains all the test run configuration values. The values are set by reading from the
 * testConfig file, the command line at the start, or a combination of the two. The command line overrides testConfig
 * values.
 *
 * Created by developer on 15-9-28.
 */
public class SuiteConfiguration {

    private final Logger LOG = LogManager.getLogger(SuiteConfiguration.class);
    private Properties config = new Properties();

    // singleton object for holding config values
    private static class ConfigurationHolder {
        private final static SuiteConfiguration INSTANCE = new SuiteConfiguration();
    }

    // accessor to the object
    public static SuiteConfiguration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }

    // ctor
    private SuiteConfiguration() throws IllegalArgumentException {
        LOG.info("Read default configuration values");
        readConfigFile();

        LOG.info("Config values review");
        checkForCmdLineOverride(ConfigurationKeys.BASE_URL_KEY);
        checkForCmdLineOverride(ConfigurationKeys.IMPLICIT_TIMEOUT_KEY);
        checkForCmdLineOverride(ConfigurationKeys.JENKINS_HTTP_LOCATION_PREFIX_KEY);
        checkForCmdLineOverride(ConfigurationKeys.RETRY_ATTEMPTS_KEY);
        checkForCmdLineOverride(ConfigurationKeys.SELENIUM_HUB_URL_KEY);
        checkForCmdLineOverride(ConfigurationKeys.CAPTURE_KEY);

        // purge invalid browsers,
        verifyBrowserCmdLineValues(ConfigurationKeys.BROWSER_KEY);

        LOG.info("Config values finalized");

    }

    // Reads the testConfig files and sets initial state for test run configuration values
    private void readConfigFile() {
        InputStream configInputStream;
        try {
            configInputStream = SuiteConfiguration.class.getResourceAsStream(ConfigurationKeys.CONFIG_FILE_PATH);
            try {
                config.load(configInputStream);
            } catch (IOException ioe) {
                LOG.warn("WARNING: failed to read defaultConfigurationValues inputstream. - ", ioe);
            } catch (IllegalArgumentException iae) {
                LOG.warn("WARNING: content of the defaultConfigurationValues inputstream was malformed. - ", iae);
                throw iae;
            }
        } catch (NullPointerException npe) {
            LOG.warn("WARNING: defaultConfigurationValues file name is null. - ", npe);
            throw npe;
        }

        LOG.info("Default values successfully read");
    }


    // updates config values if command line values are defined.
    private void checkForCmdLineOverride(String flag) {
        String systemValue = null;
        try {
            // attempt to read property from system for a key
            systemValue = System.getProperty(flag);
        } catch (SecurityException | NullPointerException | IllegalArgumentException exception) {
            // ignore exception and use testConfig value
            LOG.info("Exception reading system property. Ignoring exception, using defaultConfigurationValues value");
        }
        // if system property != null, set key's value from system, otherwise do nothing
        if (systemValue == null || systemValue.isEmpty()) {
            LOG.info("[default used] " + flag + " = " + config.getProperty(flag));
        } else {
            systemValue = systemValue.toLowerCase().replace(" ", "");
            config.put(flag, systemValue);
            LOG.info("[ OVERRIDING ] " + flag + " = " + systemValue);
        }
    }

    // parses through the browsers config string and purges invalid browsers
    private void verifyBrowserCmdLineValues(String browserKey) {
        StringBuilder verifiedBrowsers = new StringBuilder();

        // get value from confg object
        String parameter = config.getProperty(browserKey);
        // split value into individual browser strings
        String[] individualBrowsers = parameter.split(";");

        // iterate over browser strings, add to verifiedBrowsers if valid
        for (int i = 0; i < individualBrowsers.length; i++) {
            boolean match = false;

            // break browser string into parts
            String[] parts = individualBrowsers[i].split(",");

            // check if platform is supported
            for (Platforms plat : Platforms.values()) {
                if (plat.getName().equalsIgnoreCase(parts[0])) {
                    // platform is supported, check browser name is valid, version is not verified
                    for (Browsers browser : Browsers.values()) {
                        if (browser.getName().equalsIgnoreCase(parts[1])) {
                            if (verifiedBrowsers.length() > 0) {
                                verifiedBrowsers.append(";");
                            }
                            verifiedBrowsers.append(individualBrowsers[i]);
                            match = true;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!match) {
                LOG.info("UNSUPPORTED BROWSER: " + individualBrowsers[i]
                        + " is not a supported browser, removed from test configuration");
            }
        }
        if (verifiedBrowsers.length() < 1) {
            String msg = "All the command line language overrides were invalid codes, can't create valid test matrix, killing suite run";
            LOG.info(msg);
            throw new IllegalArgumentException(msg);
        } else {
            config.setProperty(browserKey, verifiedBrowsers.toString());
        }
    }

    // combines the browser parameters in to a matrix of test runs
    private Object[][] createBrowserMatrix() {
        String[] browserStrings = config.getProperty(ConfigurationKeys.BROWSER_KEY).split(";");
        int browserCount = browserStrings.length;

        Object[][] combinations = new Object[browserCount][1];

        int combo = 0;

        for (int k = 0; k < browserCount; k++) {
            String[] browserParts = browserStrings[k].split(",");
            TestConfiguration tc = new TestConfiguration(browserParts[0], browserParts[1],browserParts[2]);
            combinations[combo][0] = tc;
            combo++;
        }
        return combinations;
    }

    /**
     * Parses the suite configuration parameters to build a matrix of tests configuration objects to cover all the
     * combinations of browser and browser version.
     *
     * @return
     */
    public Object[][] parameterizeTestRuns() {
        return createBrowserMatrix();
    }

    /**
     * Returns soc base url in which the tests are to target.
     *
     * @return String
     */
    public String getBaseURL() {
        return config.getProperty(ConfigurationKeys.BASE_URL_KEY);
    }

    /**
     * Returns the Jenkins instance's url
     *
     * @return String
     */
    public String getJenkinsHttpLocationPrefix() {
        return config.getProperty(ConfigurationKeys.JENKINS_HTTP_LOCATION_PREFIX_KEY);
    }

    /**
     * Returns the selenium hub address that the tests are targeting.
     *
     * @return String
     */
    public String getHubAddress() {
        return config.getProperty(ConfigurationKeys.SELENIUM_HUB_URL_KEY);
    }

    /**
     * Returns the number of seconds SetDriver should wait before giving up on an element search
     *
     * @return long
     */
    public long getImplicitTimeoutSec() {
        return Long.parseLong(config.getProperty(ConfigurationKeys.IMPLICIT_TIMEOUT_KEY));
    }

    /**
     * Returns the number of times to retry a test when it fails
     *
     * @return
     */
    public int getRetryAttempts() {
        return Integer.parseInt(config.getProperty(ConfigurationKeys.RETRY_ATTEMPTS_KEY));
    }

    /**
     * Returns whether SetTestClassNG screen capture method is enabled
     *
     * @return boolean
     */
    public boolean getCapture() {
        return Boolean.parseBoolean(config.getProperty(ConfigurationKeys.CAPTURE_KEY));
    }

}
