package shared;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import shared.configuration.SuiteConfiguration;
import shared.configuration.TestConfiguration;
import shared.constants.FrameworkLocations;
import shared.page.SeBlob;
import shared.setDriver.SetDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that captures the AUT browser window.
 * 
 * @author ricsimps
 * 
 */
public class ImageCapture {

	private static final Logger LOG = LogManager.getLogger(ImageCapture.class);
	private static SimpleDateFormat datestamp = new SimpleDateFormat("yyyy_MM_dd");
	private static SimpleDateFormat timestamp = new SimpleDateFormat("HH.mm.ss");
	private static Date date = new Date();

	/**
	 * Captures the entire browser window and draws a box around the element represented by the SeBlob. The screenshot
	 * name provided should be unique to avoid any chance of overwriting other capture images.
	 * 
	 * @param driver
	 * @param className - name of test class, of the test case method, calling the capture method
	 * @param testName - name of the test case method calling the capture method
	 * @param screenShotName - name applied to the capture file
	 * @param testConfig - test config object for the test case
	 * @param elements - SeBlob array of elements to find and draw a box around
	 */
	public static void capture(SetDriver driver, String className, String testName, String screenShotName,
			TestConfiguration testConfig, SeBlob... elements) {

		// get date
		Date datenow = new Date();

		// directory for mid-test screenshots
		String location = FrameworkLocations.SCREENSHOT_LOCATION;

		try {
			// grab screenshot
			File srcFile = driver.getScreenshotAs(OutputType.FILE);

			if (srcFile != null) {
				// build file path:
				// .../screenshots/<class>/<test>/name_<country>_<language>_<date><time>.png
				String filePath = location + className + FrameworkLocations.FILE_SEPARATOR + testName
						+ FrameworkLocations.FILE_SEPARATOR + screenShotName + "_" + datestamp.format(date)
                        + timestamp.format(date) + ".png";

				// build file path:
				// Screenshots/<date>/<country>_<language>/<os>_<browser>_<version>/<date><time>/screenshots/<class>/<test>/name_<country>_<language>_<date><time>.png
//				String filePath = location + datestamp.format(date) + FrameworkLocations.FILE_SEPARATOR+ testConfig.getOS()
//						+ "_" + testConfig.getBrowser() + "_" + testConfig.getVersion() + FrameworkLocations.FILE_SEPARATOR
//						+ datestamp.format(date) + timestamp.format(date) + FrameworkLocations.FILE_SEPARATOR + "screenshots"
//						+ FrameworkLocations.FILE_SEPARATOR +className + FrameworkLocations.FILE_SEPARATOR + testName
//						+ FrameworkLocations.FILE_SEPARATOR + screenShotName + "_"
//						+ testConfig.getCountry().getCountryCode() + "_" + testConfig.getLanguage() + "_"
//						+ datestamp.format(datenow) + timestamp.format(datenow) + ".png";
				
				// store captured image to disk...
				FileUtils.copyFile(srcFile, new File(filePath));

				if (elements != null) {
					// iterate over elements, and draw box around element
					for (SeBlob element : elements) {
						boxElement(driver, filePath, element);
					}
				}

				// Log image location
				logImageLocation(filePath);
			} else {
				LOG.info("capturing a screenshot failed for " + screenShotName + ". file == null");
			}
		} catch (Exception ex) {
			// dump any unexpected exceptions
			LOG.info("Caught unexpected exception during 'capture' method", ex);
		}
	}

	/**
	 * Captures the entire browser window when a test case fails. Saves image to a directory named failedScreenshots
	 * 
	 * @param driver
	 * @param testName - name of test case
	 * @param testConfig - configuration object for the test case
	 */
	public static void captureFailure(SetDriver driver, String className, String testName, TestConfiguration testConfig) {
		try {
			// get date
			Date datenow = new Date();

			// directory for failures
			String location = FrameworkLocations.FAILED_TEST_SCREENSHOT_LOCATION;

			// capture the image
			File srcFile = driver.getScreenshotAs(OutputType.FILE);

			if (srcFile != null) {

				// build file path:
				// .../failedScreenshots/<date>/<class>/<test>/<test>_<os>_<browser>_<version>_<time>.png
				String filePath = location + datestamp.format(date) + FrameworkLocations.FILE_SEPARATOR + className
						+ FrameworkLocations.FILE_SEPARATOR + testName + FrameworkLocations.FILE_SEPARATOR + testName
						+ "_" + testConfig.getOS() + "_" + testConfig.getBrowser() + "_" + testConfig.getVersion()
						+ "_" + timestamp.format(date) + ".png";
				
				// build file path:
		        // Screenshots/<date>/<country>_<language>/<os>_<browser>_<version>/<date><time>/failedScreenshots/<class>/<test>/name_<country>_<language>_<date><time>.png
//				String filePath = location + datestamp.format(date) + FrameworkLocations.FILE_SEPARATOR + testConfig.getCountry().getCountryCode()
//						+ "_" + testConfig.getLanguage() + FrameworkLocations.FILE_SEPARATOR + testConfig.getOS()
//						+ "_" + testConfig.getBrowser() + "_" + testConfig.getVersion() + FrameworkLocations.FILE_SEPARATOR
//						+ datestamp.format(date) + timestamp.format(date) + FrameworkLocations.FILE_SEPARATOR + "failedScreenshots"
//						+ FrameworkLocations.FILE_SEPARATOR +className + FrameworkLocations.FILE_SEPARATOR + testName
//						+ FrameworkLocations.FILE_SEPARATOR + testConfig.getCountry().getCountryCode() + "_" + testConfig.getLanguage()
//						+ "_" + datestamp.format(datenow) + timestamp.format(datenow) + ".png";
//

				// copy screen capture to disk
				FileUtils.copyFile(srcFile, new File(filePath));

				// Log image location
				logImageLocation(filePath);
			} else {
				LOG.info("driver failed to capture image. file == null");
			}
		} catch (Exception ex) {
			// dump any unexpected exceptions
			LOG.info("Caught unexpected exception during 'capture' method", ex);
		}
	}

	/**
	 * Logs the screen capture file's location. If a http prefix url is passed in from Jenkins it is prepend. The
	 * directory is always the same, the prefix is the http path to the file.
	 * 
	 * @param  - location of the file
	 */
	private static void logImageLocation(String path) {
		String httpWebPrefixURL = SuiteConfiguration.getInstance().getJenkinsHttpLocationPrefix();
		if (httpWebPrefixURL != null && !httpWebPrefixURL.isEmpty()) {
			LOG.info("Image at: " + httpWebPrefixURL + path);
		} else {
			LOG.info("Image at: " + path);
		}
	}

	/**
	 * Draws a box around the element represented by the SeBlob. If the element isn't found nothing happens
	 * 
	 * @param driver
	 * @param filePath - path to the full page capture
	 * @param seBlob - the element to draw a box around
	 */
	private static void boxElement(SetDriver driver, String filePath, SeBlob seBlob) {
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;

		// find element and get location and size
		try {
			WebElement element = driver.findElement(By.cssSelector(seBlob.getCSS()));
			Point point = element.getLocation();
			Dimension dimensions = element.getSize();
			x = point.getX();
			y = point.getY();
			w = dimensions.getWidth();
			h = dimensions.getHeight();
			LOG.info("found image and retrieved dimensions");
		} catch (NoSuchElementException e) {
			LOG.info("unable to locate element on page, file unaltered");
		}

		BufferedImage bufferedImage = null;
		try {
			// buffer image for drawing
			bufferedImage = ImageIO.read(new File(filePath));
			Graphics2D graphic = bufferedImage.createGraphics();

			// define box appearance
			graphic.setPaint(Color.RED);
			int spacer = 4;

			// adjust box size to enclose image
			x = x - spacer;
			y = y - spacer;
			w = w + spacer + spacer;
			h = h + spacer + spacer;

			// draw box
			graphic.drawLine(x, y, x + w, y);
			graphic.drawLine(x + w, y, x + w, y + h);
			graphic.drawLine(x + w, y + h, x, y + h);
			graphic.drawLine(x, y + h, x, y);

			ImageIO.write(bufferedImage, "PNG", new File(filePath));

		} catch (NullPointerException npe) {
			LOG.info("file path pointed to null");
		} catch (IOException e) {
			LOG.info("unable to buffer screen capture, file unaltered");
		}
		LOG.info("element boxed on image");
	}

}
