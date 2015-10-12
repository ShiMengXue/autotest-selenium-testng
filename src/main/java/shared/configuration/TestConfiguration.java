package shared.configuration;

/**
 * Test configuration object class that holds all the data related to configuring individual tests for execution
 * Created by developer on 15-9-28.
 */
public class TestConfiguration {

    private String os = null;
    private String browser = null;
    private String version = null;

    public TestConfiguration(String os,String browser, String version) {
        this.os = os;
        this.browser = browser;
        this.version = version;
    }

    /**
     * Returns the os to run the test on
     *
     * @return String
     */
    public String getOS() {
        return os;
    }

    /**
     * Returns the browser name to run the test in
     *
     * @return String
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * Returns the browser version to run the test in. Any indicates any version is OK.
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        String description = "browser(" + os + ", " + browser + ", " + version + ")";
        return description;
    }

}
