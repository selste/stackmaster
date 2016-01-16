package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.ComInstructionID;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage.ByteMessageEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage.ByteMessageListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.exception.ChecksumException;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.RxtxUtils;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class ComConnection implements SerialPortEventListener {

	private static Map<String, ComConnection> connectorMap = new HashMap<>();

	private String comPortName;
	private RXTXPort serialPort;

	private int baudrate = 9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

	private boolean connected = false;

	private OutputStream outputStream;
	private InputStream inputStream;

	private List<ByteMessageListener> byteEventListener = new ArrayList<>();

	private boolean stopWhenChecksumError = true;

	private ComConnection(String comPortName) {
		setComPortName(comPortName);
	}

	public static ComConnection getInstance(String comPortName) {
		if (!connectorMap.containsKey(comPortName)) {
			ComConnection connector = new ComConnection(comPortName);
			connectorMap.put(comPortName, connector);
		}
		return connectorMap.get(comPortName);
	}

	public void send(byte[] byteArray) {
		if (byteArray != null && outputStream != null) {
			try {
				outputStream.write(byteArray);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(int address, int instruction, int type, int motor, int value) {
		send((byte) (address & 0xff), (byte) (instruction & 0xff), (byte) (type & 0xff), (byte) (motor & 0xff), value);
	}

	public void send(byte address, byte instruction, byte type, byte motor, int value) {
		byte[] byteArray = new byte[9];
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

	protected byte computeCheckSum(byte address, byte instruction, byte type, byte motor, int value) {
		return (byte) (address + instruction + type + motor + (byte) (value >> 24 & 0xff) + (byte) (value >> 16 & 0xff)
				+ (byte) (value >> 8 & 0xff) + (byte) (value & 0xff));
	}

	public boolean connect() throws PortInUseException {
		if (!connected) {

			try {
				CommPortIdentifier port = RxtxUtils.getCommPortIdentifier(getComPortName());
				if (port == null) {
					return false;
				}
				CommPort commPort = port.open(getClass().getName(), 2000);

				if (commPort instanceof RXTXPort) {
					serialPort = (RXTXPort) commPort;
					serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
					serialPort.notifyOnDataAvailable(true);
					serialPort.disableReceiveTimeout();
					serialPort.enableReceiveThreshold(1);
					serialPort.enableReceiveTimeout(500);

					try {
						serialPort.addEventListener(this);
					} catch (TooManyListenersException e) {
						e.printStackTrace();
					}

					inputStream = serialPort.getInputStream();
					outputStream = serialPort.getOutputStream();
				}
				connected = true;
			} catch (UnsupportedCommOperationException e) {
				connected = false;
				e.printStackTrace();
			}

		}

		return connected;
	}

	public void addByteEventListener(ByteMessageListener listener) {
		byteEventListener.add(listener);
	}

	public void removeByteEventListener(ByteMessageListener listener) {
		byteEventListener.remove(listener);
	}

	public int getAvailableBytes() {
		try {
			return inputStream.available();
		} catch (IOException e) {
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
			} catch (IOException e) {
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

	public byte[] receive() throws ChecksumException {
		byte[] buffer = new byte[9];
		byte[] temp = new byte[9];
		int dataIndex = 0;
		for (int i = 0; i < 20; i++) {
			try {
				if (inputStream != null && inputStream.available() > 0) {
					int readBytes = inputStream.read(temp);
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
							if (isStopWhenChecksumError()) {
								send(1, ComInstructionID.MOTOR_STOP, 0, 0, 0);
							}
							throw new ChecksumException("Checksum-Error in " + buffer);
						}
					}
				} else {
					Thread.sleep(10);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			byte[] answer;
			try {
				answer = receive();
				sendEvent(answer);
			} catch (ChecksumException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Eventtype: " + event.getEventType());
		}

	}

	protected void sendEvent(byte[] byteArray) {
		if (byteArray != null && byteArray.length > 0) {
			for (ByteMessageListener listener : byteEventListener) {
				listener.handleByteMessageEvent(new ByteMessageEvent(this, byteArray));
			}
		}

	}

	public String getComPortName() {
		return comPortName;
	}

	public void setComPortName(String comPortName) {
		this.comPortName = comPortName;
	}

	public boolean isStopWhenChecksumError() {
		return stopWhenChecksumError;
	}

	public void setStopWhenChecksumError(boolean stopWhenChecksumError) {
		this.stopWhenChecksumError = stopWhenChecksumError;
	}

}
