package shared.setDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

/**
 * The Class BrowserWindow. This class is designed to be a member of SetDriver adding helper methods to set browser
 * window position and dimensions.
 * 
 * @author
 */
public class BrowserWindow {

	private static final Logger LOG = LogManager.getLogger(BrowserWindow.class);
	private WebDriver driver = null;

	/**
	 * Instantiates a new browser window. Designed to be a member of SetDriver
	 * 
	 * *@param WebDriver driver
	 */
	public BrowserWindow(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Sets the broser dimensions.
	 * 
	 * *@param Dimension browserDimension
	 */
	public void setDimensions(Dimension browserDimensions) {
		if (driver != null) {
			driver.manage().window().setSize(browserDimensions);
			if (driver.manage().window().getSize().equals(browserDimensions)) {
				LOG.info("Browser resized to: " + browserDimensions);
			} else {
				LOG.warn("Browser desired size: " + browserDimensions + ", Actual size: "
						+ driver.manage().window().getSize());
			}

		} else {
			LOG.error("Browser window setDimensions called before being initialized with a WebDriver");
		}

	}

	/**
	 * Sets the browser dimensions.
	 * 
	 * @param width the width
	 * @param height the height
	 */
	public void setDimensions(int width, int height) {
		Dimension dim = new Dimension(width, height);
		setDimensions(dim);
	}

	/**
	 * Maximize browser window.
	 */
	public void maximize() {
		if (driver != null) {
			driver.manage().window().maximize();
			LOG.info("Browser maximized. Size is now: " + driver.manage().window().getSize());

		} else {
			LOG.error("Browser window maximize called before being initialized with a WebDriver");
		}
	}

	/**
	 * Sets the browser top left corner position.
	 * 
	 * *@param Point topLeftCorner
	 */
	public void setPosition(Point topLeftCorner) {
		if (driver != null) {
			driver.manage().window().setPosition(topLeftCorner);
			LOG.info("Browser top left corner repositioned to: " + topLeftCorner);

		} else {
			LOG.error("Browser window setPosition called before being initialized with a WebDriver");
		}
	}

	/**
	 * Sets the browser top left corner position.
	 * 
	 * @param x the x
	 * @param y the y
	 */
	public void setPosition(int x, int y) {
		Point pt = new Point(x, y);
		setPosition(pt);
	}

}
