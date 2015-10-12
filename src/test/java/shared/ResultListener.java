package shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import shared.configuration.TestConfiguration;
import shared.setDriver.SetDriver;

import java.util.*;

/**
 * This class Listens to for the test results. Specifically it monitors onFailure and onFinish.
 * 
 * @author
 * 
 */
public class ResultListener extends TestListenerAdapter {

	private static final Logger LOG = LogManager.getLogger(ResultListener.class);

	/**
	 * If a method with @BeforeMethod annotation fails, capture an image.
	 * 
	 * @param result - ITestResult
	 */
	@Override
	public void onConfigurationFailure(ITestResult result) {
		super.onConfigurationFailure(result);
		LOG.info("Starting onConfigurationFailure().");

		captureFailedImage(result);
	}

	@Override
	public void onTestStart(ITestResult result) {
		LOG.info("****************************************");
		LOG.info("Starting test: " + result.getInstanceName() + "." + result.getName());
		// FIXME: This "TestConfig" LOG will be changed to driver.testConfig; post-SetDriver merge
		LOG.info("TestConfiguration: " + ((SetTestClassNG) result.getInstance()).getTestConfiguration());
		for (Object o : result.getParameters()) {
			LOG.info("TestParam: " + o.toString());
		}

		setUpDriver(result.getParameters());
	}

	/**
	 * Listen for a failing test and capture an image.
	 * 
	 * @param result - ITestResult
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		super.onTestFailure(result);
		LOG.info("Starting onTestFailure().");

		captureFailedImage(result);

		cleanUpDriver(result.getParameters());
	}

	/**
	 * Listen for a passing test and log.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		super.onTestSuccess(result);
		LOG.info("Test result: Passed");

		cleanUpDriver(result.getParameters());
	}

	/**
	 * Listen for a skipped test and log.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		super.onTestSkipped(result);
		LOG.info("Test result: Skipped");

		cleanUpDriver(result.getParameters());
	}

	private void setUpDriver(Object[] parameters) {
		if (parameters.length > 0 && parameters[0] instanceof SetDriver) {
			SetDriver driver = (SetDriver) parameters[0];
			TestConfiguration testConfig = driver.testConfig;

			try {
				driver.configureDriver();
			} catch (WebDriverException | NullPointerException ex) {
				LOG.warn("WebDriver may not have been properly configured for test.");
				LOG.warn("Please verify that " + testConfig.getBrowser().toUpperCase() + " driver is working properly.");
				LOG.warn("Logged the exception caught during capture", ex);
			}
		}
	}

	private void cleanUpDriver(Object[] parameters) {
		if (parameters.length > 0 && parameters[0] instanceof SetDriver) {
			SetDriver driver = (SetDriver) parameters[0];
			TestConfiguration testConfig = driver.testConfig;

			try {
				driver.quit();
			} catch (WebDriverException | NullPointerException ex) {
				LOG.warn("WebDriver may not have been properly configured for test.");
				LOG.warn("Please verify that " + testConfig.getBrowser().toUpperCase() + " driver is working properly.");
				LOG.warn("Logged the exception caught during capture", ex);
			}
		}
	}

	/**
	 * If the result is a test cast it to SetTestClassNG, and capture a failure image.
	 * 
	 * @param result - ITestResult
	 */
	private void captureFailedImage(ITestResult result) {
		LOG.info("Starting captureFailedImage().");

		// Get an instance of what was running and store it in Object
		Object obj = result.getInstance();

		// We have an Object, lets figure out what type it Object it was.
		if (obj instanceof SetTestClassNG) {
			((SetTestClassNG) obj).captureFailedTest(result);
		} else if (obj != null) {
			// Not able to recognized the Object type. If the type is new, it needs to be added this if statement.
			LOG.warn("ResultListener.captureFailedImage() NOT able to recognize object type:" + obj.toString()
					+ ". You need to add the new object type to this list.");
		} else {
			LOG.warn("ResultListener.captureFailedImage() passed a null object");
		}
	}

	/**
	 * When the tests are done this method goes through the results and sees if a failed test passed on retry, and
	 * removes it from the results if it did. It also remove duplicate failures from the results.
	 */
	@Override
	public void onFinish(ITestContext testContext) {
		List<ITestResult> testsToBeRemoved = new ArrayList<>();

		Set<Integer> passedTestIds = new HashSet<>();
		for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
			passedTestIds.add(getId(passedTest));
		}

		Set<Integer> failedTestIds = new HashSet<>();
		for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {

			int failedTestId = getId(failedTest);

			if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);
			} else {
				// TODO is this needed?
				failedTestIds.add(failedTestId);
			}
		}

		for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator
				.hasNext();) {
			ITestResult testResult = iterator.next();
			if (testsToBeRemoved.contains(testResult)) {
				LOG.info("Removing: " + testResult.getInstanceName() + "." + testResult.getName());
				iterator.remove();
			}
		}
	}

	// helper to find the id of a result
	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = 31 * id + result.getMethod().getMethodName().hashCode();
		id = 31 * id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}
}
