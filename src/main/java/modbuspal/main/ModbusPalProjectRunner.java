package modbuspal.main;

import java.nio.file.Paths;

/**
 * Main Program
 * 
 * @author broessl
 */
public class ModbusPalProjectRunner {

	private static String MODBUSPAL_PROJECT = "";

	public static String getProjectFilePath() {
		return MODBUSPAL_PROJECT;
	}

	public static void main(String args[]) {
		MODBUSPAL_PROJECT = System.getenv("MODBUSPAL_PROJECT");
		if (MODBUSPAL_PROJECT==null || !Paths.get(MODBUSPAL_PROJECT).toFile().exists()) {
			System.err.println(String.format("Project File '%s' does not exist", MODBUSPAL_PROJECT));
			System.exit(-1);
		}
		new ModbusPalRuntime();
	}

}
