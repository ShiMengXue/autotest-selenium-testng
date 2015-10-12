package shared.constants;

/**
 * Paths to items that are relative to this project.
 *
 * Created by developer on 15-10-9.
 */
public class FrameworkLocations {

    /** Local system file separator. Used to build paths */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /** Where screenshots for failed tests end up*/
    public static final String FAILED_TEST_SCREENSHOT_LOCATION = "failedScreenshots" + FILE_SEPARATOR;

    /** Where screenshots are saved */
    public static final String SCREENSHOT_LOCATION =  "screenshots" + FILE_SEPARATOR;

    /** Where testData are saved */
    public static final String TESTDATA_LOCATION = "testData" + FILE_SEPARATOR;
}
