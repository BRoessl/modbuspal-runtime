/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modbuspal.generator;

import java.io.IOException;
import java.io.OutputStream;
import modbuspal.instanciator.Instantiable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A generator creates a dynamic value to be used in automations. This class
 * defines the common properties and behaviors of all generators. It must be
 * subclassed in order to customize the "getValue" method, which is responsible
 * for generating the dynamic value.
 * 
 * @author nnovic
 */
public abstract class Generator implements Instantiable<Generator> {
	private double duration = 10;
	private double initialValue = 0.0;

	/**
	 * Constructor of the Generator class. Creates a generator with default values,
	 * icon and control panel.
	 */
	public Generator() {
	}


	/**
	 * Get the duration of the generator. The duration is set in the automation
	 * editorby the user of ModbusPal.
	 * 
	 * @return duration of the generator, in seconds.
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Get the initial value of the generator. The initial value is either - the
	 * initial value of the automation if this generator is the first generator
	 * executed. - the last value of the previous generator. Please note that the
	 * generator has no obligation to use this initial value.
	 * 
	 * @return the initial value of the generator.
	 */
	public double getInitialValue() {
		return initialValue;
	}

	/**
	 * Set the initial value of the generator. Usually, it is set by the running
	 * automation and you don't have to modify it.
	 * 
	 * @param value initial value of this generator.
	 */
	public void setInitialValue(double value) {
		initialValue = value;
	}

	/**
	 * Subclasses have to implement this method in order to generate a dynamic
	 * value. The current time is passed in argument so that you can easily create
	 * functions depending on the time. The time is provided in seconds, starting
	 * from the moment when the automation has been started.
	 * 
	 * @param time current time, in seconds.
	 * @return dynamic value created by this generator.
	 */
	public abstract double getValue(double time);

	/**
	 * Loads the parameters of this generator from a DOM tree structure. Usually,
	 * this method is called by ModbusPal when a project is loaded and you don't
	 * have to use it directly. This method will only process the attributes of the
	 * "generator" Node, and delegate the child nodes to the "loadSettings" method.
	 * If your generator has to load paremeters, you have to override the
	 * "loadSettings" method, which by default doesn't do anything.
	 * 
	 * @param genNode the DOM node corresponding to this generator's settings.
	 */
	public final void load(Node genNode) {
		NamedNodeMap attributes = genNode.getAttributes();

		Node durNode = attributes.getNamedItem("duration");
		String durVal = durNode.getNodeValue();
		duration = Double.parseDouble(durVal);

		loadGeneratorSettings(genNode.getChildNodes());
	}

	/**
	 * This method is called during the loading of the generator from a modbuspal
	 * project file. The subclasses must implement this method so that they can
	 * retrieve the settings they have previously save in saveGeneratorSettings().
	 * 
	 * @param list the XML nodes where to read the configuration from.
	 */
	public abstract void loadGeneratorSettings(NodeList list);

	/**
	 * This method is called during the saving of the generator's parameters into a
	 * modbuspal project file. The subclasses must implement this method so that
	 * they can save their specific settings into the project file. The settings
	 * should be saved using XML format.
	 * 
	 * @param out the output stream where to write the settings
	 * @throws IOException
	 */
	public abstract void saveGeneratorSettings(OutputStream out) throws IOException;

	/**
	 * Defines the duration of this generator. The duration is modified by the user
	 * when he/she edits the automation.
	 * 
	 * @param val new duration of the generator.
	 */
	void setDuration(double val) {
		duration = val;
	}

	/**
	 * Save the parameters of this generator with XML formatting, into an
	 * outputstream. Usually, this method is called by ModbusPal when a project is
	 * saved and you don't have to use it directly. This method will only write the
	 * "generator" tag and its attributes, and delegate the writing of the inner
	 * tags to the "saveSettings" method. If your generator has to save paremeters,
	 * you have to override the "saveSettings" method, which by default doesn't do
	 * anything.
	 * 
	 * @param out the output stream into which the XML must be written.
	 * @throws IOException
	 */
	public final void save(OutputStream out) throws IOException {
		String openTag = createOpenTag();
		out.write(openTag.getBytes());

		saveGeneratorSettings(out);

		String closeTag = "</generator>\r\n";
		out.write(closeTag.getBytes());
	}

	/**
	 * Returns the class name of this generator. This function is used by ModbusPal
	 * to garantee that the generator provides a "human readable" class name. This
	 * is not always the case with the java introspection, for example with
	 * Generators written in Python.
	 * 
	 * @return the "human readable" class name of this generator.
	 */
	@Override
	public String getClassName() {
		return getClass().getSimpleName();
	}

	private String createOpenTag() {
		StringBuilder tag = new StringBuilder("<generator");
		tag.append(" class=\"").append(getClassName()).append("\"");
		tag.append(" duration=\"").append(String.valueOf(duration)).append("\"");
		tag.append(">\r\n");
		return tag.toString();
	}

	@Override
	public Generator newInstance() throws InstantiationException, IllegalAccessException {
		return getClass().newInstance();
	}

	@Override
	public void init() {
	}

	@Override
	public void reset() {
		throw new RuntimeException(
				"This method is implemented because of the Instantiale interface, but is never user.");
	}
}
