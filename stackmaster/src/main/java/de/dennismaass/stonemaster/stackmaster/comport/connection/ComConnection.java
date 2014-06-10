package de.dennismaass.stonemaster.stackmaster.comport.connection;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import de.dennismaass.stonemaster.stackmaster.comport.command.ComInstructionID;
import de.dennismaass.stonemaster.stackmaster.comport.connection.bytemessage.ByteMessageEvent;
import de.dennismaass.stonemaster.stackmaster.comport.connection.bytemessage.ByteMessageListener;
import de.dennismaass.stonemaster.stackmaster.util.RxtxUtils;

public class ComConnection implements SerialPortEventListener {

	private static final Map<String, ComConnection> connectorMap = new HashMap<>();

	private String comPortName;
	private RXTXPort serialPort;

	private final int baudrate = 9600;
	private final int dataBits = SerialPort.DATABITS_8;
	private final int stopBits = SerialPort.STOPBITS_1;
	private final int parity = SerialPort.PARITY_NONE;

	private boolean connected = false;

	private OutputStream outputStream;
	private InputStream inputStream;

	// private final int readed = 0;
	// private final byte[] readBuffer = new byte[9];

	private final List<ByteMessageListener> byteEventListener = new ArrayList<>();

	private ComConnection(final String comPortName) {
		super();

		setComPortName(comPortName);
	}

	public static ComConnection getInstance(final String comPortName) {
		if (!connectorMap.containsKey(comPortName)) {
			final ComConnection connector = new ComConnection(comPortName);
			connectorMap.put(comPortName, connector);
		}
		return connectorMap.get(comPortName);
	}

	public void send(final byte[] byteArray) {
		if (byteArray != null && outputStream != null) {
			try {
				outputStream.write(byteArray);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(final int address, final int instruction, final int type, final int motor, final int value) {
		send((byte) (address & 0xff), (byte) (instruction & 0xff), (byte) (type & 0xff), (byte) (motor & 0xff), value);
	}

	public void send(final byte address, final byte instruction, final byte type, final byte motor, final int value) {
		final byte[] byteArray = new byte[9];
		byteArray[0] = address;
		byteArray[1] = instruction;
		byteArray[2] = type;
		byteArray[3] = motor;
		byteArray[4] = (byte) (value >> 24 & 0xff);
		byteArray[5] = (byte) (value >> 16 & 0xff);
		byteArray[6] = (byte) (value >> 8 & 0xff);
		byteArray[7] = (byte) (value & 0xff);
		byteArray[8] = computeCheckSum(address, instruction, type, motor, value);
		send(byteArray);
	}

	protected byte computeCheckSum(final byte address, final byte instruction, final byte type, final byte motor,
			final int value) {
		return (byte) (address + instruction + type + motor + (byte) (value >> 24 & 0xff) + (byte) (value >> 16 & 0xff)
				+ (byte) (value >> 8 & 0xff) + (byte) (value & 0xff));
	}

	public boolean connect() throws PortInUseException {
		if (!connected) {

			try {
				final CommPortIdentifier port = RxtxUtils.getCommPortIdentifier(getComPortName());
				if (port == null) {
					return false;
				}
				final CommPort commPort = port.open(getClass().getName(), 2000);

				if (commPort instanceof RXTXPort) {
					serialPort = (RXTXPort) commPort;
					serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
					serialPort.notifyOnDataAvailable(true);
					serialPort.disableReceiveTimeout();
					serialPort.enableReceiveThreshold(1);
					serialPort.enableReceiveTimeout(500);

					try {
						serialPort.addEventListener(this);
					} catch (final TooManyListenersException e) {
						e.printStackTrace();
					}

					inputStream = serialPort.getInputStream();
					outputStream = serialPort.getOutputStream();
				}
				connected = true;
			} catch (final UnsupportedCommOperationException e) {
				connected = false;
				e.printStackTrace();
			}

		}

		return connected;
	}

	public void addByteEventListener(final ByteMessageListener listener) {
		byteEventListener.add(listener);
	}

	public void removeByteEventListener(final ByteMessageListener listener) {
		byteEventListener.remove(listener);
	}

	public int getAvailableBytes() {
		try {
			return inputStream.available();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void disconnect() {
		if (connected) {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}

			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.close();
			}
			connected = false;
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public byte[] receive() {
		final byte[] buffer = new byte[9];
		final byte[] temp = new byte[9];
		int dataIndex = 0;
		for (int i = 0; i < 20; i++) {
			try {
				if (inputStream != null && inputStream.available() > 0) {
					// System.out.println("SR: available data " + inputStream.available());
					final int readBytes = inputStream.read(temp);
					// System.out.println("readBytes: " + readBytes);
					int j;
					for (j = 0; j < readBytes; j++) {
						if (dataIndex + j < 9) {
							buffer[dataIndex + j] = temp[j];
						} else {
							break;
						}
					}
					dataIndex += j;

					if (dataIndex >= 9) {
						byte checksum = 0;
						for (j = 0; j < 8; j++) {
							checksum += buffer[j];
						}

						if (checksum == buffer[8]) {
							return buffer;
						} else {
							// String buff = "";
							// for (int k = 0; k < 8; k++) {
							// buff += buffer[k] + ", ";
							// }
							// buff += buffer[8];
							send(1, ComInstructionID.MOTOR_STOP, 0, 0, 0);
							return null;
						}
					}
				} else {
					// System.out.println("SR: waiting for data ");
					Thread.sleep(10);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void serialEvent(final SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			final byte[] answer = receive();
			sendEvent(answer);
		} else {
			System.out.println("Eventtype: " + event.getEventType());
		}

	}

	protected void sendEvent(final byte[] byteArray) {
		if (byteArray != null && byteArray.length > 0) {
			for (final ByteMessageListener listener : byteEventListener) {
				listener.handleByteMessageEvent(new ByteMessageEvent(this, byteArray));
			}
		}

	}

	public String getComPortName() {
		return comPortName;
	}

	public void setComPortName(final String comPortName) {
		this.comPortName = comPortName;
	}

}
