package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.controller.calculator.Calculator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.ComInstructionID;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswer;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.instruction.ComInstruction;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.instruction.StopComInstruction;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.ComConnection;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage.ByteMessageEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage.ByteMessageListener;
import gnu.io.PortInUseException;

public class ComCommunicator implements ByteMessageListener {

	public static final int MIN_HARDWARE_MODE = 1;
	public static final int MAX_HARDWARE_MODE = 8;
	public static final int MIN_HARDWARE_SPEED = 1;
	public static final int MAX_HARDWARE_SPEED = 800;

	public static double MIN_STEP = -250.0, MAX_STEP = 250.0;
	public static long MIN_SLEEP = MIN_HARDWARE_SPEED;
	public static long MAX_SLEEP = Long.MAX_VALUE;
	public static int MAX_SPEED = MAX_HARDWARE_SPEED;


	private static Logger LOGGER = Logger.getLogger(ComCommunicator.class);

	private static Map<String, ComCommunicator> communicatorMap = new HashMap<>();


	private ComConnection connector;

	private Calculator calculator;
	private boolean activMotor;
	private List<ComAnswerListener> answerListener = new ArrayList<>();

	private ComCommunicator(String comPort, double stepsPerMm) {
		super();
		connector = ComConnection.getInstance(comPort);
		calculator = new Calculator(stepsPerMm, 1.0);
	}


	public static ComCommunicator getInstance(String comPortName, double stepsPerMm) {
		if (!communicatorMap.containsKey(comPortName)) {
			ComCommunicator communicator = new ComCommunicator(comPortName, stepsPerMm);
			communicatorMap.put(comPortName, communicator);
		}
		return communicatorMap.get(comPortName);
	}

	public void setPositionReached(int positionInSteps) {
		send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_POSITION_REACHED, 0,
				positionInSteps);
	}

	public void enableInterrupts() {
		send(1, ComInstructionID.ENABLE_INTERRUPTS, ComInstructionID.ENABLE_INTERRUPTS_POSITION_REACHED, 0, 0);
	}

	public void setMaxSpeed(int speed) {
		send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_MAX_SPEED, 0, speed);
	}
	public void setMaxAcceleration (int acceleration) {
		send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_MAX_ACCELERATION, 0, acceleration);
	}

	protected void pause(long delayInMillis) {
		try {
			Thread.sleep(delayInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addAnswerListener(ComAnswerListener listener) {
		answerListener.add(listener);
	}

	public void removeAnswerListener(ComAnswerListener listener) {
		answerListener.remove(listener);
	}

	public ComConnection getConnector() {
		return connector;
	}

	public void getPosition() {
		send(1, ComInstructionID.GET_AXIS_PARAMETER, ComInstructionID.GET_AXIS_PARAMETER_ACTUAL_POSITION, 0, 0);
	}

	public void rotate(int direction, int speed) {
		activMotor = true;
		send(1, direction, 0, 0, speed);
	}

	public void rotateRight(int speed) {
		rotate(ComInstructionID.ROTATE_LEFT, speed);
	}

	public void rotateLeft(int speed) {
		rotate(ComInstructionID.ROTATE_RIGHT, speed);
	}

	public void stop() {
		if (activMotor) {
			StopComInstruction stop = new StopComInstruction(1, 0);
			send(stop);
			activMotor = false;
		}
	}

	public void moveTo(int positionInSteps) {
		activMotor = true;
		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_ABSOLUTE, 0, positionInSteps);
	}

	public void moveTo(double positionInMillimeter) {
		activMotor = true;

		double countOfNumerator = calculator.getCountOfNumerator(positionInMillimeter);
		int countOfNumeratorInt = (int) Math.rint(countOfNumerator);

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_ABSOLUTE, 0, countOfNumeratorInt);
	}

	public void move(int steps) {
		activMotor = true;

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_RELATIVE, 0, steps);
	}

	public void move(double millimeter) {
		activMotor = true;

		double countOfNumerator = calculator.getCountOfNumerator(millimeter);
		int countOfNumeratorInt = (int) Math.rint(countOfNumerator);

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_RELATIVE, 0, countOfNumeratorInt);
	}

	public void send(int address, int instruction, int type, int motor, int value) {
		connector.send(address, instruction, type, motor, value);
		LOGGER.info("send address:" + address + ",instruction:" + instruction + ",type:" + type + ",motor:" + motor
				+ ",value:" + value);
	}

	public void send(ComInstruction command) {
		connector.send(command.toByteMessage());
	}

	public boolean isActiv() {
		return activMotor;
	}

	public boolean isConnected() {
		return connector.isConnected();
	}

	public void disconnect() {
		doFinal();
		connector.disconnect();
	}

	protected void doFinal() {
		pause(100);
		// TODO
		storePosition(0);
		pause(100);

		connector.removeByteEventListener(this);
	}

	public boolean connect() throws PortInUseException {
		connector.addByteEventListener(this);
		return connector.connect();
	}

	protected void sendEvent(ComAnswer answer) {
		for (ComAnswerListener listener : answerListener) {
			listener.handleAnswer(new ComAnswerEvent(this, answer, calculator.getCountOfDenominator(answer.getValue())));
		}
	}

	@Override
	public void handleByteMessageEvent(ByteMessageEvent event) {
		byte[] byteArray = event.getByteArray();
		if (byteArray != null && byteArray.length > 0) {
			ComAnswer answer = ComAnswer.build(byteArray);
			LOGGER.info("answer address:" + answer.getAddress() + ",instruction:" + answer.getInstruction() + ",value:"
					+ answer.getValue() + ",host:" + answer.getHost() + ",checksum" + answer.getChecksum());
			sendEvent(answer);
		}

	}

	public void storePosition(int position) {
		send(1, ComInstructionID.STORE_AXIS_PARAMETER, ComInstructionID.GET_AXIS_PARAMETER_ACTUAL_POSITION, 0, position);
	}

	public void setMicrostepResolution(int mode) {
		if (mode >= 1 && mode <= 8) {
			activMotor = true;
			send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_MICROSTEP_RESOLUTION, 0,
					mode);

		}
	}

	public void setSIO(int number, boolean on) {
		if (number >= 0 && number <= 4) {
			int value;
			if (on) {
				value = 1;
			} else {
				value = 0;
			}
			send(1, ComInstructionID.SIO, ComInstructionID.SIO_DOUT2, 2, value);
		}
	}

	public String getComPort() {
		return connector.getComPortName();
	}

	public void setComPort(String comPort) {
		connector.setComPortName(comPort);
	}

	public double getStepsPerMm() {
		return calculator.getNumerator();
	}

	public void setStepsPerMm(double stepsPerMm) {
		calculator = new Calculator(stepsPerMm, 1.0);
	}

}
