package aic.components.toolbar;

import shared.page.PageElements;
import shared.page.SeBlob;

public class ToolBarElements extends PageElements {

	/** finds the Files tab in the toolbar */
	public static final SeBlob TAB_FILES = new SeBlob("#home-selector", "Files");

	/** finds the Export PDF tab in the toolbar */
	public static final SeBlob TAB_EXPORT_PDF = new SeBlob("#exportpdf-selector", "Export PDF");

	/** finds the Create PDF tab in the toolbar */
	public static final SeBlob TAB_CREATE_PDF = new SeBlob("#convertpdf-selector", "Create PDF");

	/** finds the Combine PDF tab in the toolbar */
	public static final SeBlob TAB_COMBINE_PDF = new SeBlob("#combinepdf-selector", "Combine Files");

}
