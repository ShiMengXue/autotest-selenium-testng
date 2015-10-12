package shared.page;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

/**
 * Selenium blob class. Represents a "blob" of information about an element on a page that is commonly handing in
 * finding and evaluating elements with Selenium.
 * 
 * "css" is a string that can be used to find a page element on a page. "text" is the text displayed by the element in
 * the web page "link" if an element is a link, it's the href that the link redirects to.
 * 
 * @author
 * 
 */
public class SeBlob {

	private String css = null;
	private String id = null;
	private String xpath=null;
	private String text = null;
	private List<String> textList = null;
	private String href = null;

	/**
	 * Creates blob for elements that are links, contains the css to find the element, the displayed link text, and the
	 * href
	 * 
	 * @param css
	 * @param text
	 * @param href
	 */
	public SeBlob(String css, String text, String href) {
		this.css = css;
		this.text = text;
		this.href = href;
	}

	/**
	 * Creates blob for elements that are links, contains the css to find the element, the displayed link text, the
	 * href, and/or an ID for the web element. Any of these can be null.
	 * 
	 * @param css
	 * @param text
	 * @param href
	 * @param id
	 */
	public SeBlob(String css, String text, String href, String id) {
		this.css = css;
		this.text = text;
		this.href = href;
		this.id = id;
	}
	
	/**
	 * Creates blob for elements that are links, contains the css to find the element, the displayed link text, the
	 * href, and/or an ID for the web element. Any of these can be null.
	 * 
	 * @param css
	 * @param text
	 * @param href
	 * @param id
	 * @param xpath
	 */
	public SeBlob(String css, String text, String href, String id, String xpath) {
		this.css = css;
		this.text = text;
		this.href = href;
		this.id = id;
		this.xpath=xpath;
	}

	/**
	 * Creates blob for elements that are not links . It contains the css to find the element and the displayed link
	 * text. The href value is null. Good for links where the href is not used for the redirect.
	 * 
	 * @param css
	 * @param text
	 */
	public SeBlob(String css, String text) {
		this.css = css;
		this.text = text;
	}

	/**
	 * Set the css and a list of strings in that css. Works create for bullet list.
	 * 
	 * @param css
	 * @param textList Stores a reference to textList, does not make a deep copy.
	 */
	public SeBlob(String css, List<String> textList) {
		this.css = css;
		this.textList = textList;
	}

	/**
	 * Creates blob for elements that are not links or display text, for example icons, images, etc.
	 * 
	 * @param css
	 */
	public SeBlob(String css) {
		this.css = css;
	}

	/**
	 * Gets a string that can be used in a cssSelector to find the element on the page
	 * 
	 * @return String
	 */
	public String getCSS() {
		return css;
	}

	/**
	 * Gets the string that an element should be displaying in the UI
	 * 
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * returns a list of text, NULL if correct constructor is NOT called.
	 * 
	 * @return Array of strings.
	 */
	public List<String> getTextList() {
		return new ArrayList<String>(textList);
	}

	/**
	 * Gets the href for a link if the element is a link. If the element isn't a link, null is returned.
	 * 
	 * @return String or null
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Return the id of the object. Null if not passed to constructor.
	 * 
	 * @return id String of a web element
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Return the xpath of the object. Null if not passed to constructor.
	 * 
	 * @return xpath String of a web element
	 */
	public String getXPATH() {
		return xpath;
	}

	public By getBy() {

		if (getCSS() != null) {
			return By.cssSelector(getCSS());
		} else if (getID() != null) {
			return By.id(getID());
		} else if(getXPATH()!= null){
			return By.xpath(getXPATH());
		}
		return null;
	}

	@Override
	public String toString() {
		String blobStr = getCSS();

		if (this.text != null) {
			blobStr += ", \"" + text + "\"";
		}

		return blobStr;
	}
}