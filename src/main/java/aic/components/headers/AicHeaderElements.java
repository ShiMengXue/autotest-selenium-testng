package aic.components.headers;

import shared.page.PageElements;
import shared.page.SeBlob;

public class AicHeaderElements extends PageElements {

	/** finds the Status menu in the header */
	public static final SeBlob STATUS_MENU_BTN = new SeBlob("#status-menu-btn");

	/** finds the Help menu in the header */
	public static final SeBlob HELP_MENU_BTN = new SeBlob("#help-menu-btn", "Help");

	/** finds the Account menu in the header */
	public static final SeBlob ACCOUNT_MENU_BTN = new SeBlob("#account-menu-btn");

	/** finds the Language item in the Account menu */
	public static final SeBlob ACCOUNT_MENU_LANGUAGE_ITEM = new SeBlob("#lang-selector", "Language...");

	/** finds the Language item in the Account menu */
	public static final SeBlob ACCOUNT_MENU_LANGUAGE_LIST = new SeBlob("#div.aic-languages");

	/** finds the Sign out item in the Account menu */
	public static final SeBlob ACCOUNT_MENU_SIGNOUT_ITEM = new SeBlob("#sign_out-selector", "Sign Out");

	/** finds the Remove History item in the Status menu */
	public static final SeBlob STATUS_MENU_REMOVE_ITEM = new SeBlob("#status-menu .status-menu-remove-history-btn",
			"Remove History");

	/** finds the CreatePDF footer download item in the Status menu */
	public static final SeBlob STATUS_MENU_CREATEPDF_DOWNLOAD_ITEM = new SeBlob(
			"#status-menu .aic-createpdf-status .aic-status-footer > div >input");

	/** finds the CreatePDF footer download all item in the Status menu */
	public static final SeBlob STATUS_MENU_CREATEPDF_DOWNLOADALL_ITEM = new SeBlob(
			"#status-menu .aic-createpdf-status .aic-status-footer > div >input:nth-child(2)");

	/** finds the All create files in the Status menu */
	public static final SeBlob STATUS_MENU_ALL_CREATE_FILES = new SeBlob(
			"#status-menu .aic-createpdf-status .aic-createpdf-status");

	/** finds the ExportPDF footer download item in the Status menu */
	public static final SeBlob STATUS_MENU_EXPORTPDF_DOWNLOAD_ITEM = new SeBlob(
			"#status-menu .aic-exportpdf-status .aic-status-footer > div >input");

	/** finds the ExportPDF footer download all item in the Status menu */
	public static final SeBlob STATUS_MENU_EXPORTPDF_DOWNLOADALL_ITEM = new SeBlob(
			"#status-menu .aic-exportpdf-status .aic-status-footer > div >input:nth-child(2)");

	/** finds the All export files in the Status menu */
	public static final SeBlob STATUS_MENU_ALL_EXPORT_FILES = new SeBlob(
			"#status-menu .aic-exportpdf-status .aic-exportpdf-status");
	
	/** finds the All export files Complete status in the Status menu */
	public static final SeBlob STATUS_MENU_ALL_EXPORT_FILES_COMPLETE_STATUS = new SeBlob(
			".aic-status-details-container .aic-4-Complete");

	/** finds the CombinePDF footer download item in the Status menu */
	public static final SeBlob STATUS_MENU_COMBINEPDF_DOWNLOAD_ITEM = new SeBlob(
			"#status-menu .aic-combine-status .aic-status-footer > div >input");

	/** finds the CombinePDF footer download all item in the Status menu */
	public static final SeBlob STATUS_MENU_COMBINEPDF_DOWNLOADALL_ITEM = new SeBlob(
			"#status-menu .aic-combine-status .aic-status-footer > div >input:nth-child(2)");

	/** finds the All combine files in the Status menu */
	public static final SeBlob STATUS_MENU_ALL_COMBINE_FILES = new SeBlob(
			"#status-menu .aic-combine-status .aic-combine-status");

}