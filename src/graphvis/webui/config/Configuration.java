package graphvis.webui.config;

public class Configuration {

	public static final String tempDirectory = System.getProperty("java.io.tmpdir") + "/graphvis";
	// Application caters for very large files.
	public static final int MAX_MEMORY_SIZE  = 1024 * 1024 * 1024 * 5;
	public static final int MAX_REQUEST_SIZE = 1024 * 1024 * 1024 * 5;
	public static String hdfsWorkingDirectory  = "graphvis-output";

}
