package shared.setDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.*;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.server.browserlaunchers.Sleeper;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import shared.configuration.SuiteConfiguration;
import shared.configuration.TestConfiguration;
import shared.constants.Delays;
import shared.enums.Browsers;
import shared.enums.Platforms;
import shared.page.SeBlob;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by developer on 15-9-28.
 */
public class SetDriver implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
        FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities,
        TakesScreenshot {
    private static final Logger LOG = LogManager.getLogger(SetDriver.class);
    private static String currentRunningBrowser = "";
    private SuiteConfiguration suiteConfig = SuiteConfiguration.getInstance();
    public TestConfiguration testConfig;
    public RemoteWebDriver driver;
    public BrowserWindow browserWindow;

    public SetDriver(TestConfiguration testConfig) {
        this.testConfig = testConfig;
    }

    public void configureDriver() {
        LOG.debug("Configuring driver");
        // Check if browser has changed, if browser has changed close the existing driver,
        // null it out so new browser can be opened
        if (driver != null && !currentRunningBrowser.contains(testConfig.getBrowser())) {
            driver.quit();
            driver = null;
        }

        // if browser has not been opened yet or, keepBrowserOpen is false, open a new browser instance
        if (driver == null) {
            DesiredCapabilities target = null;

            // if not targeting a hub, use local driver
            if (suiteConfig.getHubAddress().equalsIgnoreCase("http://local")) {

                // select local driver based on config
                if (testConfig.getBrowser().equalsIgnoreCase(Browsers.FIREFOX.getName())) {
                    driver = new FirefoxDriver();
                    LOG.debug("driver set to local FF driver");
                }else if (testConfig.getBrowser().equalsIgnoreCase(Browsers.CHROME.getName())) {
                    driver = new ChromeDriver();
                    LOG.debug("driver set to local CH driver");
                }
            }else{
                // set target browser for remote node execution
                if (testConfig.getBrowser().equalsIgnoreCase(Browsers.FIREFOX.getName())) {
                    target=DesiredCapabilities.firefox();
                    target.setCapability("","");
                    LOG.debug("driver set to local FF driver");
                }else if (testConfig.getBrowser().equalsIgnoreCase(Browsers.CHROME.getName())) {
                    target = DesiredCapabilities.chrome();
//                    target.setCapability("webdriver.chrome.driver","/usr/bin/browser-drivers/chromedriver");
                    LOG.debug("driver set to local CH driver");
                }

                // set target Platform
                if (testConfig.getOS().equalsIgnoreCase(Platforms.WINDOWS.getName())) {
                    target.setPlatform(Platform.WINDOWS);
                    LOG.debug("remote driver set to win platform");
                } else if (testConfig.getOS().equalsIgnoreCase(Platforms.LINUX.getName())) {
                    target.setPlatform(Platform.LINUX);
                    LOG.debug("remote driver set to linux platform");
                }

                // set target browser version for remote node execution
                if (!testConfig.getVersion().equalsIgnoreCase("any")) {
                    target.setVersion(testConfig.getVersion());
                    LOG.debug("remote driver browser version set to " + testConfig.getVersion());
                }

                // create url for remote hub
                URL urlToHub = null;

                try {
                    urlToHub = new URL(suiteConfig.getHubAddress());
                    driver = new RemoteWebDriver(urlToHub, target);
                } catch (MalformedURLException e) {
                    LOG.error("ERROR: Unable to create URL object to instantiate remote selenium hub, switching to local driver");
                    driver = new FirefoxDriver();
                }
//                catch(Exception e){
//                    System.out.println(e);
//                }
            }

            // Instantiate browserWindow member with driver
            this.browserWindow = new BrowserWindow(driver);
            // Resize browser to be maximized
            // needs to be wider than 1125 to ensure full desktop size of icons and images
            browserWindow.maximize();

        }
        // keep track of current browser so when keepBrowserOpen is true
        // the browser can be closed when switching to a different browser
        currentRunningBrowser = driver.getClass().toString().toLowerCase();

        // set how long driver looks for page elements
        // if passed in on command line use command line value otherwise use
        // Constants default
        LOG.debug("Driver implicit wait seconds set to : " + suiteConfig.getImplicitTimeoutSec());
        driver.manage().timeouts().implicitlyWait(suiteConfig.getImplicitTimeoutSec(), TimeUnit.SECONDS);

        LOG.info("Completed driver creation");
    }
    /**
     * Wait for element. Given a locator poll for that element, this method will wait until the element is visible,
     * enabled, and the element does not have the class "disabled" before returning the element. If the element is not
     * found in the alloted time, a TimeoutException will be logged and thrown instead.
     *
     * @param locator the By locator to search by
     * @param secondsToWait the seconds to wait
     * @return the web element
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public WebElement waitForElementEnabled(final By locator, long secondsToWait) {
        long started = System.currentTimeMillis();
        ExpectedCondition<WebElement> conditionToCheck = new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(locator);
                    if (!element.getAttribute("class").contains("disabled") && element.isDisplayed()
                            && element.isEnabled()) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "enabled status of " + locator;
            }
        };

        Wait<WebDriver> wait = new WebDriverWait(driver, secondsToWait, 150L);
        WebElement element;
        try {
            element = wait.until(conditionToCheck);
            double timeTaken = (System.currentTimeMillis() - started) / 1000.0;
            LOG.info("waitForEnabledElement: Request completed in " + timeTaken + " seconds.");
        } catch (TimeoutException error) {
            LOG.warn("waitForEnabledElement: Timeout waiting for element to become enabled. Waited " + secondsToWait
                    + " seconds. ");
            throw error;
        }

        return element;
    }

    /**
     * Wait for element. Given an SeBlob object poll for that element
     *
     * Helper method that passes SeBlob through to By locator
     *
     * @param element the element
     * @param secondsToWait the seconds to wait
     * @return the web element
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public WebElement waitForElementVisible(SeBlob element, long secondsToWait) throws TimeoutException {
        return this.waitForElementVisible(element.getBy(), secondsToWait);
    }

    /**
     * Wait for element. Given a By selector object poll for that element
     *
     * This method should only be used when needing to wait a variable amount of time. If you need to wait between 5 and
     * 120 seconds for example this method is what you want. Use to find the last element to load by polling then the
     * rest of the elements your looking for should already be visible. If you need to return a list of WebElement use
     * waitForElementsVisible.
     *
     * @param locator the By locator to search by
     * @param secondsToWait the seconds to wait
     * @return the web element
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public WebElement waitForElementVisible(By locator, long secondsToWait) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, secondsToWait);
        WebElement foundElement;
        long start = System.currentTimeMillis();

        try {
            // Keep poling until element is visible or we have exceeded
            // secondsToWait if it's NOT visible a time out exception is thrown
            foundElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException toe) {
            LOG.info("SetDriver.waitForElementVisible() was NOT able to find " + locator.toString() + " within "
                    + secondsToWait + " seconds.");
            throw toe;
        }

        double secondsWaited = (System.currentTimeMillis() - start) / 1000.0;
        LOG.info("SetDriver.waitForElementVisible() found: " + locator.toString() + " in " + secondsWaited
                + " seconds out of " + secondsToWait + " seconds.");

        // return the element found
        return foundElement;
    }

    /**
     * Wait for list of WebElements. Given a By selector object poll for that element
     *
     * This method should only be used when needing to wait a variable amount of time. If you need to wait between 5 and
     * 120 seconds for example this method is what you want. Use to find the last element to load by polling then the
     * rest of the elements your looking for should already be visible. This method will return a list of elements where
     * the waitForElementVisible will only return a single WebElement
     *
     * @param locator the By locator to search by
     * @param secondsToWait the seconds to wait
     * @return the list of WebElements found
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public List<WebElement> waitForAllElementsVisible(By locator, long secondsToWait) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, secondsToWait);
        List<WebElement> foundElements;
        long start = System.currentTimeMillis();

        try {
            // Keep poling until element is visible or we have exceeded
            // secondsToWait if it's NOT visible a time out exception is thrown
            foundElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException toe) {
            LOG.info("SetDriver.waitForAllElementsVisible() was NOT able to find " + locator.toString() + " within "
                    + secondsToWait + " seconds.");
            throw toe;
        }

        double secondsWaited = (System.currentTimeMillis() - start) / 1000.0;
        LOG.info("SetDriver.waitForAllElementsVisible() found: " + locator.toString() + " in " + secondsWaited
                + " seconds out of " + secondsToWait + " seconds.");

        // return the list of WebElement found
        return foundElements;
    }

    /**
     * Wait for list of WebElements. Given an SeBlob object poll for that element
     *
     * Helper method that passes SeBlob through to By locator
     *
     * @param element the element
     * @param secondsToWait the seconds to wait
     * @return the list of WebElements found
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public List<WebElement> waitForAllElementsVisible(SeBlob element, long secondsToWait) throws TimeoutException {
        return this.waitForAllElementsVisible(element.getBy(), secondsToWait);
    }

    /**
     * Wait for any of a group of Elements found by their respective locator to become visible. Return the first one
     * that does.
     *
     * Useful Context example: wait for the payment frame on the join page, or an error that the payment system is
     * unavailable.
     *
     * @param locators
     * @param secondsToWait
     * @return
     */
    public WebElement waitForOneOfAGroupToBeVisible(List<By> locators, long secondsToWait) {
        long start = System.currentTimeMillis();
        WebElement ret = null;
        while (true) {
            for (int i = 0; i < locators.size(); i++) {
                try {
                    // Use a short wait, we want to check for the next guy in our loop:
                    ret = waitForElementVisible(locators.get(i), 1L);
                    // we found it, return it:
                    if (ret != null) {
                        return ret;
                    }
                } catch (TimeoutException toe) {
                    // if we're out of time, throw, otherwise, do nothing:
                    if ((System.currentTimeMillis() - start) > (secondsToWait * 1000)) {
                        throw toe;
                    }
                }
            }
        }
    }

    /**
     * Wait for a SeBlob to be invisible.
     *
     * @param blob - SeBlob to be invisible
     * @param secondsToWait - long seconds to wait for SeBlob to be invisible.
     * @return boolean - true if item became invisible, false if item is still visible.
     */
    public boolean waitForInvisible(SeBlob blob, long secondsToWait) {
        LOG.info("Waiting for seblob to be invisible.");
        WebDriverWait wait = new WebDriverWait(driver, secondsToWait);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(blob.getBy()));
        } catch (TimeoutException toe) {
            // The SeBlob did NOT become invisible within the allowed time, log it and return false.
            LOG.info("SeBlob '" + blob.getBy() + "' is still visible after " + secondsToWait + ".");
            return false;
        }
    }

    /**
     * Wait for a SeBlob to be invisible. This method used the implicit wait time.
     *
     * @param blob - SeBlob to be invisible
     * @return boolean - true if item became invisible, false if item is still visible.
     */
    public boolean waitForInvisible(SeBlob blob) {
        return waitForInvisible(blob, SuiteConfiguration.getInstance().getImplicitTimeoutSec());
    }

    /**
     * Wait for element. Given a SeBlob object poll for by element and the text it contains
     *
     * Helper method that passes SeBlob through to By locator, text
     * *@param locator the locator
     * *@param text the text
     * @param secondsToWait the seconds to wait
     * @return true, if successful
     */
    public boolean waitForElementText(SeBlob seBlob, long secondsToWait) {
        return waitForElementText(seBlob.getBy(), seBlob.getText(), secondsToWait);
    }

    /**
     * Wait for element. Given a By selector object poll for that element and the text it contains
     *
     * This method should only be used when needing to wait a variable amount of time. If you need to wait between 5 and
     * 120 seconds for example this method is what you want. Use to find the last element to load by polling then the
     * rest of the elements your looking for should already be visible.
     *
     * @param locator the locator
     * @param text the text
     * @param secondsToWait the seconds to wait
     * @return true, if successful
     */
    public boolean waitForElementText(By locator, String text, long secondsToWait) {
        WebDriverWait wait = new WebDriverWait(driver, secondsToWait);
        boolean result;
        String message;
        long start = System.currentTimeMillis();

        try {
            // Keep polling until element is visible or we have exceeded
            // secondsToWait if it's NOT visible a time out exception is thrown
            result = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            message = "SetDriver.waitForElementText() found: ";
        } catch (TimeoutException toe) {
            // catch TimeoutException exception, log it, set result to false.
            message = "SetDriver.waitForElementText() did NOT find: ";
            result = false;
        }

        double secondsWaited = (System.currentTimeMillis() - start) / 1000.0;
        LOG.info(message + locator.toString() + " in " + secondsWaited + " seconds out of " + secondsToWait
                + " seconds.");

        return result;
    }

    /**
     * Wait for an element to become stale. Useful when a javascript call has refreshed the page, or something on the
     * page.
     *
     * @param locator
     * @param secondsToWait
     * @return true if it became stale, false otherwise
     * @throws org.openqa.selenium.TimeoutException If command does not complete in enough time
     */
    public boolean waitForElementStaleness(By locator, long secondsToWait) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, secondsToWait, 100L);
        // Set the implicit wait low, otherwise "wait.until" will wait a minimum of the implicit wait.

        driver.manage().timeouts().implicitlyWait(1L, TimeUnit.SECONDS);

        WebElement element = driver.findElement(locator);
        boolean ret = false;

        // until element is stale or we have exceeded secondsToWait
        ret = wait.until(ExpectedConditions.stalenessOf(element));

        // reset the timeout.
        driver.manage().timeouts()
                .implicitlyWait(SuiteConfiguration.getInstance().getImplicitTimeoutSec(), TimeUnit.SECONDS);

        return ret;
    }

    /**
     * Scrolls browser to top of the page
     *
     */
    public void scrollToTopOfPage() {
        JavascriptExecutor js = driver;
        js.executeScript("window.scrollTo(0,0);");
    }

    /**
     * Scrolls browser to element allowing elements that would be invisible to be scrolled into view 160 points are
     * removed if possible from element y point to prevent it from being hidden under headers
     *
     * @param element the element
     */
    public void scrollToElement(WebElement element) {
        Point p = element.getLocation();
        int x = p.getX();
        int y = p.getY();
        // remove 160 from y to prevent scrolling too far and hiding element under header
        if (y > 161) {
            y = y - 160;
        }
        JavascriptExecutor js = driver;
        js.executeScript("window.scrollTo(" + x + "," + y + ");");
    }

    /**
     * Close.
     */
    @Override
    public void close() {
        driver.close();

    }

    /**
     * Find element.
     *
     * @param arg0 the arg0
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElement(By arg0) throws NoSuchElementException {
        return driver.findElement(arg0);
    }

    /**
     * Find elements.
     *
     * @param arg0 the arg0
     * @return the list
     */
    @Override
    public List<WebElement> findElements(By arg0) {
        return driver.findElements(arg0);
    }

    /**
     * Gets the.
     *
     * @param arg0 the arg0
     */
    @Override
    public void get(String arg0) {
        driver.get(arg0);
    }

    /**
     * Gets the current url.
     *
     * @return the current url
     */
    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets the page source.
     *
     * @return the page source
     */
    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the window handle.
     *
     * @return the window handle
     */
    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Gets the window handles.
     *
     * @return the window handles
     */
    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Maximize the browser window/page.
     *
     * driver.manage().window().maximize(); does NOT maximize the width of the browser on Chrome Mac to the width of the
     * screen. The following method does maximize the width and height of all browsers to the size of the screen(minus
     * the start/icon bar).
     */
    public void maximizeBrowserWindow() {
        driver.manage().window().setPosition(new Point(0, 0));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
        driver.manage().window().setSize(dim);
        // give the OS time to redraw the browser to the correct size
        Sleeper.sleepTightInSeconds(Delays.DELAY_1_SECOND);
    }

    /**
     * Manage.
     *
     * @return the options
     */
    @Override
    public Options manage() {
        return driver.manage();
    }

    /**
     * Navigate.
     *
     * @return the navigation
     */
    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    /**
     * Quit.
     */
    @Override
    public void quit() {
        driver.quit();

    }

    /**
     * Switch to.
     *
     * @return the target locator
     */
    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    /**
     * Gets the capabilities.
     *
     * @return the capabilities
     */
    @Override
    public Capabilities getCapabilities() {
        return driver.getCapabilities();
    }

    /**
     * Gets the keyboard.
     *
     * @return the keyboard
     */
    @Override
    public Keyboard getKeyboard() {
        return driver.getKeyboard();
    }

    /**
     * Gets the mouse.
     *
     * @return the mouse
     */
    @Override
    public Mouse getMouse() {
        return driver.getMouse();
    }

    /**
     * Find element by x path.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByXPath(String using) throws NoSuchElementException {
        return driver.findElementByXPath(using);
    }

    /**
     * Find elements by x path.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return driver.findElementsByXPath(using);
    }

    /**
     * Find element by tag name.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByTagName(String using) throws NoSuchElementException {
        return driver.findElementByTagName(using);
    }

    /**
     * Find elements by tag name.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByTagName(String using) {
        return driver.findElementsByTagName(using);
    }

    /**
     * Find element by css selector.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByCssSelector(String using) throws NoSuchElementException {
        return driver.findElementByCssSelector(using);
    }

    /**
     * Find elements by css selector.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return driver.findElementsByCssSelector(using);
    }

    /**
     * Find element by name.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByName(String using) throws NoSuchElementException {
        return driver.findElementByName(using);
    }

    /**
     * Find elements by name.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByName(String using) {
        return driver.findElementsByName(using);
    }

    /**
     * Find element by link text.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByLinkText(String using) throws NoSuchElementException {
        return driver.findElementByLinkText(using);
    }

    /**
     * Find elements by link text.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return driver.findElementsByLinkText(using);
    }

    /**
     * Find element by partial link text.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByPartialLinkText(String using) throws NoSuchElementException {
        return driver.findElementByPartialLinkText(using);
    }

    /**
     * Find elements by partial link text.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return driver.findElementsByPartialLinkText(using);
    }

    /**
     * Find element by class name.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementByClassName(String using) throws NoSuchElementException {
        return driver.findElementByClassName(using);
    }

    /**
     * Find elements by class name.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return driver.findElementsByClassName(using);
    }

    /**
     * Find element by id.
     *
     * @param using the using
     * @return the web element
     * @throws org.openqa.selenium.NoSuchElementException If no matching elements are found
     */
    @Override
    public WebElement findElementById(String using) throws NoSuchElementException {
        return driver.findElementById(using);
    }

    /**
     * Find elements by id.
     *
     * @param using the using
     * @return the list
     */
    @Override
    public List<WebElement> findElementsById(String using) {
        return driver.findElementsById(using);
    }

    /**
     * Execute script.
     *
     * @param script the script
     * @param args the args
     * @return the object
     */
    @Override
    public Object executeScript(String script, Object... args) {
        return driver.executeScript(script, args);
    }

    /**
     * Execute async script.
     *
     * @param script the script
     * @param args the args
     * @return the object
     */
    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return driver.executeAsyncScript(script, args);
    }

    /**
     * Gets the screenshot as. This method works for both locally and on the grid.
     *
     * @param <X> the generic type
     * @param target the target
     * @return the screenshot as
     * @throws WebDriverException the web driver exception
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        if (isRemoteWebDriver()) {
            // The Augmenter allows the screen capture to work on the grid.
            driver = (RemoteWebDriver) new Augmenter().augment(driver);
            return ((TakesScreenshot) driver).getScreenshotAs(target);
        } else {
            // Capture the image when running locally
            return ((TakesScreenshot) driver).getScreenshotAs(target);
        }
    }

    /**
     * Return true if driver is a remote web driver for the grid or false if it is a web driver running locally.
     *
     * @return boolean
     */
    public boolean isRemoteWebDriver() {
        return driver.getClass().getName().equals(RemoteWebDriver.class.getName());
    }

}
