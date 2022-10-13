/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modbuspal.master;

/**
 *
 * @author JMC15
 */
public class ModbusMasterDelay {
	public static ModbusMasterDelay getDelay(int milliseconds) {
		ModbusMasterDelay output = new ModbusMasterDelay();
		output.delayMs = milliseconds;

		String caption = String.format("Delay (%d milliseconds)", milliseconds);
		return output;
	}

	private int delayMs = 0;

	public int getDelay() {
		return delayMs;
	}
}
