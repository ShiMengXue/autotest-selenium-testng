package shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import shared.configuration.SuiteConfiguration;
import shared.configuration.TestConfiguration;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by developer on 15-9-28.
 */
public class SetTestClassNG {

    private static final Logger LOG = LogManager.getLogger(SetTestClassNG.class);
    private static final SuiteConfiguration suiteConfig = SuiteConfiguration.getInstance();
    protected static SetDriver driver;
    private TestConfiguration testConfig;
    protected String testName;

    /**
     * Reads configuration file and system properties to build a matrix of test configurations
     *
     * @return
     * @throws java.io.IOException
     */
    @DataProvider(name = "configureRun")
    public static Object[][] configureRun() throws IOException {
        return suiteConfig.parameterizeTestRuns();
    }

    public SetTestClassNG(TestConfiguration testConfig) {
        this.testConfig = testConfig;
    }

    /**
     * Before each test case create a driver
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeEachTest() {
        driver = new SetDriver(testConfig);
        driver.configureDriver();
    }

    /**
     * Captures the current test case name
     *
     * @param method
     */
    @BeforeMethod(alwaysRun = true)
    public void trackTestCaseName(Method method) {
        testName = method.getName();
    }

    /**
     * After each test, if thekeepBrowserOpen flag is false, kill the browser window.
     *
     * @param result of the test
     */
    @AfterMethod(alwaysRun = true)
    public void afterEachTest(ITestResult result) {
//        LOG.info("@AfterMethod: take a capture for last result page");
//        screenShot("result_page");
        LOG.info("Entering afterEachTest @AfterMethod");
        LOG.info("(@AfterMethod) checking if the browser should be left open");

        // check to see if browser should be closed or left open
        if (driver != null) {
            driver.quit();
            LOG.info("(@AfterMethod) quiting browser");
        }
        LOG.info("Exiting afterEachTest @AfterMethod");
    }

    /**
     * If a test fails, log the test details and attempt to capture a failure image.
     *
     * @param result
     */
    public void captureFailedTest(ITestResult result) {
        LOG.info("-----------------------------------------");
        LOG.info("TEST FAILURE: [" + getClass().getSimpleName() + "] " + result.getName());
        LOG.info("Configuration: " + testConfig.getOS() + "/" + testConfig.getBrowser() );

        if (driver == null) {
            LOG.warn("SetDriver was not initialized properly; cannot take screenshot.");
            LOG.warn("Please verify that " + testConfig.getBrowser().toUpperCase() + " driver is working properly.");
            return;
        }

        try {
            LOG.info("Final URL: " + driver.getCurrentUrl());
            // test failed, so capture screen image
            ImageCapture.captureFailure(driver, getClass().getSimpleName(), result.getName(), testConfig);
        } catch (WebDriverException ex) {
            LOG.warn("WebDriver may not have been properly configured for test.");
            LOG.warn("Logged the exception caught during capture", ex);
        }
    }

    /**
     * Returns a copy of the current test case's configuration object
     *
     * @return -TestConfiguration object
     */
    protected TestConfiguration getTestConfiguration() {
        return testConfig;
    }

    /**
     * Takes a full page screenshot of the AUT at it's present state. Unique names must be supplied for each capture to
     * avoid file name collisions when multiple captures are made on the same page. If the name aren't unique, then
     * images will be overwritten.
     *
     * @param screenShotName - unique identifier for the image
     */
    protected void screenShot(String screenShotName) {
        if (suiteConfig.getCapture()) {
            ImageCapture.capture(driver, this.getClass().getSimpleName(), testName, screenShotName, testConfig);
        }
    }

    /**
     * Takes a full page screenshot of the AUT at it's present state and draws a red box around each of the elements
     * represent by the SeBlobs in the array. Unique names must be supplied for each capture to avoid file name
     * collisions when multiple captures are made on the same page. If the name aren't unique, then images will be
     * overwritten. If there is any problem finding the element on the page, the full page is captured but no box is
     * drawn the particular element.
     *
     * @param screenShotName - unique identifier for the image
     * @param elements - an array of SeBlobs for a group of elements on the page that need boxed
     */
    protected void screenShot(String screenShotName, SeBlob... elements) {
        if (suiteConfig.getCapture()) {
            ImageCapture.capture(driver, this.getClass().getSimpleName(), testName, screenShotName, testConfig,
                    elements);
        }
    }
}
