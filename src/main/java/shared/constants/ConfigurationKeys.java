package shared.constants;

/**
 * Created by developer on 15-9-28.
 */
public class ConfigurationKeys {

    /** path to where there default properties file **/
    public static final String CONFIG_FILE_PATH = "/defaultConfigurationValues.properties";

    /** flag for the url of the selenium hub to run tests on **/
    public static final String SELENIUM_HUB_URL_KEY = "set.sel_hub_url";

    /** flag for the set of browsers and browser versions to run tests in **/
    public static final String BROWSER_KEY = "set.browsers";

    /** flag for SOC environment to target when testing **/
    public static final String BASE_URL_KEY = "set.soc_base_url";

    /** flag for whether to enable screen capturing in test **/
    public static final String CAPTURE_KEY = "set.capture";

    /** flag for the amount of time Selenium will look for elements before giving up **/
    public static final String IMPLICIT_TIMEOUT_KEY = "set.implicit_timeout_seconds";

    /** flag for the url of jenkins server **/
    public static final String JENKINS_HTTP_LOCATION_PREFIX_KEY = "set.jenkins_http_location";

    /** flag for number of times to retry a test when the retry analyzer is active **/
    public static final String RETRY_ATTEMPTS_KEY = "set.retry_attempts";

}
