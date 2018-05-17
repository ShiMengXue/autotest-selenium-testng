package aic.pages.exportpdf;

import shared.page.PageElements;
import shared.page.SeBlob;

public class ExportPDFElements extends PageElements {

	/** finds the Page title */
	public static final SeBlob PAGE_TITLE = new SeBlob("#export_tool > .aic-service-title", "Export From PDF");

	/** finds the Select PDF Files to Export BTN */
	public static final SeBlob SELECT_FILES_BTN = new SeBlob("#export_tool > .aic-export-file-select");

	/** finds the all files name in history */
	public static final SeBlob ALL_FILESNAME_HISTORY = new SeBlob(".aic-recent-filenames-row > .aic-preview-name");

	/** finds the all files name in history */
	public static final SeBlob EXPORT_BTN = new SeBlob("#export_pdf_convert");

}
