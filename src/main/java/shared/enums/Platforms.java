package shared.enums;

public enum Platforms
{
	WINDOWS ("windows"), LINUX ("linux");

	private String commonName;

	Platforms(String name) {
		this.commonName = name;
	}

	public String getName() {
		return this.commonName;
	}
}
