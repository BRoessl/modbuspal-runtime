/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modbuspal.recorder;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import modbuspal.slave.ModbusSlaveAddress;
import modbuspal.toolkit.HexaTools;

/**
 * Records incoming and outgoing data
 * 
 * @author nnovic
 */
public class ModbusPalRecorder
//implements Runnable
{
	private static ModbusPalRecorder uniqInst = new ModbusPalRecorder();
	private Date start = null;

	/**
	 * Adds a record into the file. The record is marked as being "incoming", which
	 * means of type RECORD_IN.
	 * 
	 * @param slaveID   the slave number that is the target of the incoming request
	 * @param buffer    buffer containing the incoming data (pdu request)
	 * @param offset    offset where the actua data starts in the buffer
	 * @param pduLength the length of the incoming pdu
	 */
	public static void recordIncoming(ModbusSlaveAddress slaveID, byte[] buffer, int offset, int pduLength) {
		uniqInst.record("in", slaveID, buffer, offset, pduLength);
	}

	/**
	 * Adds a record into the file. The record is marked as being "incoming", which
	 * means of type RECORD_IN.
	 * 
	 * @param slaveID   the slave number that is the target of the incoming request
	 * @param buffer    buffer containing the incoming data (pdu request)
	 * @param offset    offset where the actua data starts in the buffer
	 * @param pduLength the length of the incoming pdu
	 */
	public static void recordOutgoing(ModbusSlaveAddress slaveID, byte[] buffer, int offset, int pduLength) {
		uniqInst.record("out", slaveID, buffer, offset, pduLength);
	}

	/**
	 * Dummy function that ModbusPalPane will call to force the runtime to load this
	 * class.
	 */
	public static void touch() {
		return;
	}

	// private PipedInputStream input;
	// private PipedOutputStream output;
	// private boolean running;
	private OutputStreamWriter fileWriter = null;

	ModbusPalRecorder() {
		// output = new PipedOutputStream();
		// Thread thread = new Thread(this);
		// running=true;
		// thread.start();
	}

	private synchronized void setOutput(OutputStreamWriter writer) throws IOException {
		if (fileWriter != null) {
			fileWriter.flush();
			fileWriter.close();
		}
		fileWriter = writer;
		start = null;
	}

	/*
	 * @Override public void run() {
	 * 
	 * try { // create input reader that the recorder will use to get // data that
	 * has to be written into the record file. input = new PipedInputStream(output,
	 * 1024 * 1024); InputStreamReader reader = new InputStreamReader(input);
	 * BufferedReader buffer = new BufferedReader(reader);
	 * 
	 * System.out.println("Start recorder");
	 * 
	 * while (running == true) { try { // read a line from the pipe String line =
	 * buffer.readLine();
	 * 
	 * // if recording is enabled... if (fileWriter != null) {
	 * fileWriter.write(line+"\r\n"); } } catch (IOException ex) {
	 * ex.printStackTrace(); System.out.println("pipe broken, recreate pipe"); input
	 * = new PipedInputStream(output, 1024 * 1024); reader = new
	 * InputStreamReader(input); buffer = new BufferedReader(reader); } }
	 * 
	 * try { buffer.close(); reader.close(); input.close(); } catch (IOException ex)
	 * { Logger.getLogger(ModbusPalRecorder.class.getName()).log(Level.SEVERE, null,
	 * ex); } finally { System.out.println("Stop recorder"); } } catch (IOException
	 * ex) { Logger.getLogger(ModbusPalRecorder.class.getName()).log(Level.SEVERE,
	 * null, ex); } }
	 */

	/**
	 * Starts the recording of a session. This method will summon a FileChooser so
	 * that the user can specify the file where the data will be written. The actual
	 * recording only becomes effective after the file has been selected.
	 * 
	 * @throws IOException
	 */
	public static void start() throws IOException {
//		XFileChooser fc = new XFileChooser(XFileChooser.RECORDER_FILE);
//		fc.showSaveDialog(null);
//		File destFile = fc.getSelectedFile();
//		if (destFile != null) {
			System.out.println("recording to console");
			uniqInst.setOutput(new OutputStreamWriter(System.out));
//		}
	}

	/**
	 * Stops the recording.
	 * 
	 * @throws IOException
	 */
	public static void stop() throws IOException {
		uniqInst.setOutput(null);
	}

	private synchronized void record(String tag, ModbusSlaveAddress slaveID, byte[] buffer, int offset, int pduLength) {
		Date now = new Date();
		if (start == null) {
			start = now;
		}
		String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

		String open = String.format("<%s timestamp=%s slave=%s>", tag, timestamp, slaveID.toString());
		String hexa = HexaTools.toHexa(buffer, offset, pduLength);
		String close = String.format("</%s>\r\n", tag);

		// if recording is enabled...
		if (fileWriter != null) {
			try {
				fileWriter.write(open + hexa + close);
				fileWriter.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		/*
		 * System.out.println(open+hexa+close); try { output.write(open.getBytes());
		 * output.write(hexa.getBytes()); output.write(close.getBytes()); } catch
		 * (IOException ex) {
		 * Logger.getLogger(ModbusPalRecorder.class.getName()).log(Level.SEVERE, null,
		 * ex); }
		 */
	}

}
