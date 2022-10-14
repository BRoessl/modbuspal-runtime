/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModbusPalGui.java
 *
 * Created on 16 déc. 2008, 08:35:06
 */

package modbuspal.main;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import modbuspal.automation.Automation;
import modbuspal.link.*;
import modbuspal.recorder.ModbusPalRecorder;
import modbuspal.slave.ModbusSlave;
import modbuspal.slave.ModbusSlaveAddress;
import org.xml.sax.SAXException;

/**
 * The core of the runtime
 * 
 * @author nnovic, broessl
 */
public class ModbusPalRuntime
		implements ModbusPalXML, ModbusLinkListener {
	/** Name and version of this application */
	public static final String APP_STRING = "ModbusPal 1.6c";

	/** Base registry key for the configuration of the application. */
	public static final String BASE_REGISTRY_KEY = "modbuspal";

	/** Default TCP/IP port in a string to be loaded into the GUI. */
	public static final String DEFAULT_PORT_TEXT = "502";


	private ModbusLink currentLink = null;
	ModbusPalProject modbusPalProject;

	/**
	 * Defines the project that ModbusPal must execute.
	 * 
	 * @param project the project to execute.
	 */
	public void setProject(ModbusPalProject project) {
		modbusPalProject=project;
		System.out.printf("Set project %s\r\n", project.getName());
		System.out.printf("[%s] Project set\r\n", modbusPalProject.getName());
	}


	public ModbusPalRuntime() {
		this(false);
	}

	/** Creates new form ModbusPalPane */
	public ModbusPalRuntime(boolean useInternalConsole) {

		installRecorder();

		String initialLoadProjectFilePath = ModbusPalProjectRunner.getProjectFilePath();
		ModbusPalProject project = null;
		File fileCheck = new File(initialLoadProjectFilePath);
		if (initialLoadProjectFilePath != "" && fileCheck.isFile()) {
			try {
				System.out.println("Loading the project file: " + initialLoadProjectFilePath);
				project = loadProject(initialLoadProjectFilePath);

				// Now that we have loaded a project from an initial project file, it is time to
				// start all of the
				// automations that have been loaded from the project file.			
				Automation[] automations = project.getAutomations();
				for (Automation automation : automations) {
					automation.start();
				}
				ModbusPalRecorder.start();
				startTcpIpLink(false);
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println(
						"Could not load the initial project file path \"" + initialLoadProjectFilePath + "\".");
				System.out.println("Check the path you inputted into the command line arguments.");
			}
		}
	}

	private void installRecorder() {
		ModbusPalRecorder.touch();
	}


	private void startTcpIpLink(boolean isMaster) {
		// portTextField.setEnabled(false);
		int port = -1;

		try {
			String portNumber = "502";
			port = Integer.valueOf(portNumber);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return;
		}

		try {
			System.out.printf("[%s] Start TCP/link (port=%d)\r\n", modbusPalProject.getName(), port);
			currentLink = new ModbusTcpIpLink(modbusPalProject, port);

			if (isMaster) {
				currentLink.startMaster(this);
			} else {
				currentLink.start(this);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}

	/**
	 * This method will load the specified project file by creating a new
	 * ModbusPalProject instance, will set it as the current project by calling
	 * setProject(), and return it.
	 * 
	 * @param path complete pathname of the project file to load
	 * @return the instance of ModbusPalProject, created after the specified project
	 *         file, that has been set as the current project.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ModbusPalProject loadProject(String path) throws ParserConfigurationException, SAXException, IOException,
			InstantiationException, IllegalAccessException {
		File projectFile = new File(path);
		return loadProject(projectFile);
	}

	/**
	 * This method will load the specified project file by creating a new
	 * ModbusPalProject instance, will set it as the current project by calling
	 * setProject(), and return it.
	 * 
	 * @param projectFile the project file to load
	 * @return the instance of ModbusPalProject, created after the specified project
	 *         file, that has been set as the current project.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ModbusPalProject loadProject(File projectFile) throws ParserConfigurationException, SAXException,
			IOException, InstantiationException, IllegalAccessException {
		ModbusPalProject mpp = ModbusPalProject.load(projectFile);
		setProject(mpp);
		return mpp;
	}

	/**
	 * Enables or disables the specified slave.
	 * 
	 * @param id      the MODBUS id of the slave
	 * @param enabled true to enable the slave, false to disable it
	 */
	public void setModbusSlaveEnabled(ModbusSlaveAddress id, boolean enabled) {
		ModbusSlave slave = modbusPalProject.getModbusSlave(id, modbusPalProject.isLeanModeEnabled());
		if (slave != null) {
			slave.setEnabled(enabled);
		}
	}

	public void startAllAutomations() {
		System.out.printf("[%s] Start all automations\r\n", modbusPalProject.getName());
		Automation automations[] = modbusPalProject.getAutomations();
		for (int i = 0; i < automations.length; i++) {
			automations[i].start();
		}
	}

	/**
	 * Stops all the automations of the project
	 */
	public void stopAllAutomations() {
		System.out.printf("[%s] Stop all automations\r\n", modbusPalProject.getName());
		Automation automations[] = modbusPalProject.getAutomations();
		for (int i = 0; i < automations.length; i++) {
			automations[i].stop();
		}
	}


	public ModbusPalProject getProject() {
		return modbusPalProject;
	}


	@Override
	public void linkBroken() {
		// TODO Auto-generated method stub
		
	}


}
