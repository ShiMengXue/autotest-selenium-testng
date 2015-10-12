package shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import shared.configuration.SuiteConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * This class implements a repeat on failure functionality for the framework. It is controlled by the set.retry_attempts
 * parameter, which is set through the testConfig or the command line
 * 
 * @author ricsimps
 * 
 */
public class Retry implements IRetryAnalyzer, IAnnotationTransformer {

	private static final Logger LOG = LogManager.getLogger(Retry.class);

	private int retryCount = 0;
	private int maxRetryCount = SuiteConfiguration.getInstance().getRetryAttempts();

	/**
	 * This is the method that evaluates whether to repeat a test.
	 * 
	 */
	@Override
	public boolean retry(ITestResult result) {

		if (retryCount < maxRetryCount) {
			LOG.info("Retrying failed test: " + result.getInstanceName() + "." + result.getName());
			retryCount++;
			return true;
		}
		LOG.info("Retry maximum reached for: " + result.getInstanceName() + "." + result.getName());
		return false;
	}

	/**
	 * This method sets the retry analyzer for each method with the @Test annotation applied
	 * 
	 */
	@Override
	public void transform(ITestAnnotation annotation, @SuppressWarnings("rawtypes") Class testClass,
			@SuppressWarnings("rawtypes") Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(Retry.class);
	}

}
