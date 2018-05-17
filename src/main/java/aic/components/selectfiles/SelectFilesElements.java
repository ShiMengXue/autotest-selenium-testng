package aic.components.selectfiles;

import shared.page.PageElements;
import shared.page.SeBlob;

public class SelectFilesElements extends PageElements {

	/** finds the MyComputer tab in the SelectFiles dialog */
	public static final SeBlob MY_COMPUTER_TAB = new SeBlob("#my_computer-selector", "My Computer");

	/** finds the Acrobat.com Files tab in the SelectFiles dialog */
	public static final SeBlob AIC_FILES_TAB = new SeBlob(".file-selection-dlg-home-views-selector > div:nth-child(2) > div:nth-child(4)", "Document Cloud");

	/** finds the Recent Files tab in the SelectFiles dialog */
	public static final SeBlob RECENT_FILES_TAB = new SeBlob("#my_recent_files_select-selector", "Recent Files");

	/** finds the Continue BTN in the SelectFiles dialog */
	public static final SeBlob CONTINUE_BTN = new SeBlob("#file-selection-dlg-continue-btn");

	/** finds the Cancel BTN in the SelectFiles dialog */
	public static final SeBlob CANCEL_BTN = new SeBlob("#file-selection-dlg-cancel-btn");

	/** finds the Choose files BTN under MyComputer tab the SelectFiles dialog */
	public static final SeBlob CHOOSE_BTN_UNDER_MYCOM = new SeBlob("#my-computer-choose-files-btn");

	/** finds the Choose file under MyComputer tab the SelectFiles dialog */
	public static final SeBlob CHOOSE_FILE_UNDER_MYCOM = new SeBlob("#my-computer-choose-files-btn>input");

	/** finds the No file message under AIC tab the SelectFiles dialog */
	public static final SeBlob NO_FILE_UNDER_AIC_FILES = new SeBlob(
			".file-selection-dlg-home-views-content > div:nth-child(3) > div:nth-child(2) .files-no-search-results .instruction-text-folder",
			"There are no applicable files in this folder.");

	/** finds the Choose first file under AIC tab the SelectFiles dialog */
	public static final SeBlob CHOOSE_FIRST_FILE_UNDER_AIC_FILES = new SeBlob(
			".file-selection-dlg-home-views-content > div:nth-child(3) .collection-item-checkbox");

	/** finds the Choose first file name under AIC tab the SelectFiles dialog */
	public static final SeBlob FIRST_FILE_UNDER_AIC_FILES = new SeBlob(".file-selection-dlg-home-views-content > div:nth-child(3) .collection-item-name");

	/** finds the No file message under Recent tab the SelectFiles dialog */
	public static final SeBlob NO_FILES_UNDER_RECENT_FILES = new SeBlob(
			"#my_recent_files_select-plugin .files-search-results .instruction-text", "No Search Results.");

	/** finds the Choose first file under Recent tab the SelectFiles dialog */
	public static final SeBlob CHOOSE_FIRST_FILE_UNDER_RECENT_FILES = new SeBlob(
			"#my_recent_files_select-plugin .collection-item-checkbox");

	/** finds the Choose first file name under Recent tab the SelectFiles dialog */
	public static final SeBlob FIRST_FILE_UNDER_RECENT_FILES = new SeBlob(
			"#my_recent_files_select-plugin .collection-item-name");

}
