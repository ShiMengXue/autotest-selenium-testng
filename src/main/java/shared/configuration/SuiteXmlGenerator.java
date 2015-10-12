package shared.configuration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuiteXmlGenerator {

	public static String SUITE_PROPERTY = "testng.suiteXmlFile";
	public static String PACKAGES_PROPERTY = "testng.packages";
	public static String CLASSES_PROPERTY = "testng.classes";
	public static String THREAD_PROPERTY = "testng.threadCount";
	public static String DP_THREAD_PROPERTY = "dataproviderthreadcount";
	public static String GROUPS_PROPERTY = "testng.groups";
	public static String EX_GROUPS_PROPERTY = "testng.excludedGroups";
	public static String EXISTING_XML_PROPERTY = "testng.useExistingXml";

	public static String DEFAULT_THREAD_COUNT = "5";
	public static String DEFAULT_DP_THREAD_COUNT = "1";

	/**
	 * A simple program to generate acceptable TestNG suite xml files. Be warned, this does not do any filename checking
	 * and will overwrite files that exist if it has permission or might attempt to use files that do not exist if
	 * testng.useExistingXml is true. To run tests in serial, set testng.threadCount to zero.
	 * 
	 * <pre>
	 * Default values are:
	 * testng.threadCount = 5
	 * dataproviderthreadcount = 1
	 * testng.packages = .*
	 * testng.useExistingXml = false
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Check if generating xml step should be skipped. If `testng.useExistingXml == true`: skip xml generation
		String useExistingXml = System.getProperty(EXISTING_XML_PROPERTY);
		if (useExistingXml != null && Boolean.valueOf(useExistingXml) == true) {
			System.out.println("Using an existing file for suiteXml");
			return;
		} else {
			System.out.println("Generating file " + System.getProperty(SUITE_PROPERTY));
		}

		String filename = System.getProperty(SUITE_PROPERTY);

		Path path = Paths.get(filename);

		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >");
			writer.newLine();

			// Write <suite> tag and thread / parallel values
			writer.write(parseSuiteTag());
			writer.newLine();

			// Write required <test> tag
			writer.write("\t<test name=\"SET Group\">");
			writer.newLine();

			// Run based on groups (package and classes will narrow this further)
			if (System.getProperty(GROUPS_PROPERTY) != null || System.getProperty(EX_GROUPS_PROPERTY) != null) {
				for (String line : parseGroupsTag()) {
					writer.write(line);
					writer.newLine();
				}
			}

			// Specify packages and classes to test (default is to use all packages)
			if (System.getProperty(CLASSES_PROPERTY) != null) {
				// Run based on individual classes & methods
				for (String line : parseClassesTag()) {
					writer.write(line);
					writer.newLine();
				}
			} else {
				// Run based on package
				for (String line : parsePackagesTag()) {
					writer.write(line);
					writer.newLine();
				}
			}

			// Close <test> and <suite
			writer.write("\t</test>");
			writer.newLine();

			writer.write("</suite>");
			writer.newLine();

		} catch (IOException e) {
			System.out.println("Could not create " + filename);
		}
	}

	/**
	 * Set the suite tag and relevant parallel / thread count values for the run.
	 * 
	 * If threadCount is not specified, use "5". If threadCount==0, run in serial. If dataproviderthreadcount is not
	 * specified, use "1".
	 * 
	 * @return
	 */
	public static String parseSuiteTag() {
		String suite = "<suite name=\"SET Test Suite\" verbose=\"1\" ";

		String threadCount = System.getProperty(THREAD_PROPERTY);

		// Set default threadCount if not specified
		if (threadCount == null) {
			System.out.println(THREAD_PROPERTY + " not specified. Defaulting to " + DEFAULT_THREAD_COUNT);
			threadCount = DEFAULT_THREAD_COUNT;
		}
		// If threadCount is 0 - ignore to run in serial
		if (!threadCount.equals("0")) {
			suite += "parallel=\"methods\" thread-count=\"" + threadCount + "\"";
		}

		String dataProviderThreadCount = System.getProperty(DP_THREAD_PROPERTY);

		if (dataProviderThreadCount == null) {
			System.out.println(DP_THREAD_PROPERTY + " not specified. Defaulting to " + DEFAULT_DP_THREAD_COUNT);
			dataProviderThreadCount = DEFAULT_DP_THREAD_COUNT;
		}
		suite += " data-provider-thread-count=\"" + dataProviderThreadCount + "\" >";

		return suite;
	}

	/**
	 * Will return a valid "groups" tag for the suite xml file. Included groups based off of "testng.groups". Excluded
	 * groups based off of "testng.excludedGroups". Regex expressions can be used for both included and excluded groups.
	 * 
	 * @return
	 */
	public static List<String> parseGroupsTag() {
		List<String> stringList = new ArrayList<String>();

		stringList.add("\t\t<groups>");
		stringList.add("\t\t\t<run>");

		String groups = System.getProperty(GROUPS_PROPERTY);
		if (groups != null) {
			StringTokenizer stInclude = new StringTokenizer(groups, " \t\n\r\f,");
			while (stInclude.hasMoreTokens()) {
				String groupToken = stInclude.nextToken();
				stringList.add("\t\t\t\t<include name=\"" + groupToken + "\" />");
			}
		}

		String excludedGroups = System.getProperty(EX_GROUPS_PROPERTY);
		if (excludedGroups != null) {
			StringTokenizer stExclude = new StringTokenizer(excludedGroups, " \t\n\r\f,");
			while (stExclude.hasMoreTokens()) {
				String excludedToken = stExclude.nextToken();
				stringList.add("\t\t\t\t<exclude name=\"" + excludedToken + "\" />");
			}
		}

		stringList.add("\t\t\t</run>");
		stringList.add("\t\t</groups>");
		return stringList;
	}

	/**
	 * Creates a valid "classes" tag and "class" tags for the testng xml file.
	 * 
	 * To use classes, the entire class and package will need to be specified. A particular method can be specified by
	 * adding a "#" and the method name to the class.
	 * 
	 * @return
	 */
	public static List<String> parseClassesTag() {
		List<String> stringList = new ArrayList<String>();

		stringList.add("\t\t<classes>");

		String classes = System.getProperty(CLASSES_PROPERTY);
		StringTokenizer stClass = new StringTokenizer(classes, " \t\n\r\f,");
		while (stClass.hasMoreTokens()) {
			String groupToken = stClass.nextToken();

			if (!groupToken.contains("#")) {
				stringList.add("\t\t\t\t<class name=\"" + groupToken + "\" />");
			} else {
				String className = groupToken.substring(0, groupToken.indexOf("#"));
				String methodName = groupToken.substring(groupToken.indexOf("#") + 1);

				stringList.add("\t\t\t\t<class name=\"" + className + "\">");

				stringList.add("\t\t\t\t\t<methods>");
				stringList.add("\t\t\t\t\t\t<include name=\"" + methodName + "\" />");

				stringList.add("\t\t\t\t\t</methods>");
				stringList.add("\t\t\t\t</class>");
			}
		}

		stringList.add("\t\t</classes>");

		return stringList;
	}

	/**
	 * Creates a valid <packages> tag for the testng xml file. Defaults to ".*" (any package / all tests) if
	 * testng.packages not specified.
	 * 
	 * @return
	 */
	public static List<String> parsePackagesTag() {
		List<String> stringList = new ArrayList<String>();

		String packageNames = System.getProperty(PACKAGES_PROPERTY);

		if (packageNames == null) {
			System.out.println(PACKAGES_PROPERTY + " not specified. Defaulting to \".*\"");
			packageNames = ".*";
		}

		stringList.add("\t\t<packages>");
		StringTokenizer stInclude = new StringTokenizer(packageNames, " \t\n\r\f,");
		while (stInclude.hasMoreTokens()) {
			String packToken = stInclude.nextToken();
			stringList.add("\t\t\t<package name=\"" + packToken + "\" />");
		}

		stringList.add("\t\t</packages>");

		return stringList;
	}
}
