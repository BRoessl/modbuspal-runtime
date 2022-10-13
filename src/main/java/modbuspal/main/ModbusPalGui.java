/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModbusPalFrame.java
 *
 * Created on 22 oct. 2010, 10:48:15
 */

package modbuspal.main;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Utilitary methods for creating new instances of ModbusPal
 * 
 * @author nnovic
 */
public class ModbusPalGui {

	private static final HashMap<Object, ModbusPalPane> instances = new HashMap<Object, ModbusPalPane>();
	public static final int MAX_PORT_NUMBER = 65536;

	private static String initialLoadFilePath = "";

	/**
	 * This method gets the initial load file path to load specified by the user in
	 * the command line arguments.
	 * 
	 * @return {String} The absolute initial load file path. Returns "" if no
	 *         argument was given.
	 */
	public static String getInitialLoadFilePath() {
		return initialLoadFilePath;
	}

	/**
	 * @param {String[]} args The command line arguments
	 */
	public static void main(String args[]) {
		initialLoadFilePath = System.getenv("MODBUSPAL_PROJECT");
		if (!Paths.get(initialLoadFilePath).toFile().exists()) {
			System.err.println(String.format("Project File '%s' does not exist", initialLoadFilePath));
			System.exit(-1);
		}
		new ModbusPalPane();
	}

}
