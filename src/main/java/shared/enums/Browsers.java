package shared.enums;

/**
 * Created by developer on 15-9-28.
 */
public enum Browsers {
    CHROME ("chrome"), FIREFOX ("firefox");

    private String commonName;

    Browsers(String name) {
        this.commonName = name;
    }

    public String getName() {
        return this.commonName;
    }
}
