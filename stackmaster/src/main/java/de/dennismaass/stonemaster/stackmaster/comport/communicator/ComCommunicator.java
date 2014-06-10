package de.dennismaass.stonemaster.stackmaster.comport.communicator;

import gnu.io.PortInUseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennismaass.stonemaster.stackmaster.calculator.Calculator;
import de.dennismaass.stonemaster.stackmaster.comport.command.ComInstructionID;
import de.dennismaass.stonemaster.stackmaster.comport.command.answer.ComAnswer;
import de.dennismaass.stonemaster.stackmaster.comport.command.answer.ComAnswerEvent;
import de.dennismaass.stonemaster.stackmaster.comport.command.answer.ComAnswerListener;
import de.dennismaass.stonemaster.stackmaster.comport.command.instruction.ComInstruction;
import de.dennismaass.stonemaster.stackmaster.comport.command.instruction.StopComInstruction;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ComConnection;
import de.dennismaass.stonemaster.stackmaster.comport.connection.bytemessage.ByteMessageEvent;
import de.dennismaass.stonemaster.stackmaster.comport.connection.bytemessage.ByteMessageListener;

public class ComCommunicator implements ByteMessageListener {

	private static final Map<String, ComCommunicator> communicatorMap = new HashMap<>();

	public static final int MIN_MODE = 1;
	public static final int MAX_MODE = 8;

	public static final long MIN_SLEEP = 1;
	public static final long MAX_SLEEP = Long.MAX_VALUE;

	public static final int MIN_SPEED = 1;
	public static final int MAX_SPEED = 2047;

	private final ComConnection connector;

	private Calculator calculator;
	private boolean activMotor;
	private final List<ComAnswerListener> answerListener = new ArrayList<>();

	private boolean reverse = false;

	private ComCommunicator(final String comPort, final double stepsPerMm) {
		super();
		connector = ComConnection.getInstance(comPort);
		calculator = new Calculator(stepsPerMm, 1.0);
		doInit();
	}

	public static ComCommunicator getInstance(final String comPortName, final double stepsPerMm) {
		if (!communicatorMap.containsKey(comPortName)) {
			final ComCommunicator communicator = new ComCommunicator(comPortName, stepsPerMm);
			communicatorMap.put(comPortName, communicator);
		}
		return communicatorMap.get(comPortName);
	}

	protected void doInit() {
		pause(100);
		setMaxSpeed(MAX_SPEED);
		pause(100);
		enableInterrupts();
		pause(100);
	}

	public void setPositionReached(final int positionInSteps) {
		send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_POSITION_REACHED, 0,
				positionInSteps);
	}

	public void enableInterrupts() {
		send(1, ComInstructionID.ENABLE_INTERRUPTS, ComInstructionID.ENABLE_INTERRUPTS_POSITION_REACHED, 0, 0);
	}

	public void setMaxSpeed(final int speed) {
		send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_MAX_SPEED, 0, speed);
	}

	protected void pause(final long delayInMillis) {
		try {
			Thread.sleep(delayInMillis);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addAnswerListener(final ComAnswerListener listener) {
		answerListener.add(listener);
	}

	public void removeAnswerListener(final ComAnswerListener listener) {
		answerListener.remove(listener);
	}

	public ComConnection getConnector() {
		return connector;
	}

	public void getPosition() {
		send(1, ComInstructionID.GET_AXIS_PARAMETER, ComInstructionID.GET_AXIS_PARAMETER_ACTUAL_POSITION, 0, 0);
	}

	public void rotate(final int direction, final int speed) {
		activMotor = true;
		send(1, direction, 0, 0, speed);
	}

	public void rotateRight(final int speed) {
		if (!isReverse()) {
			rotate(ComInstructionID.ROTATE_LEFT, speed);
		} else {
			rotate(ComInstructionID.ROTATE_RIGHT, speed);
		}
	}

	public void rotateLeft(final int speed) {
		if (!isReverse()) {
			rotate(ComInstructionID.ROTATE_RIGHT, speed);
		} else {
			rotate(ComInstructionID.ROTATE_LEFT, speed);
		}
	}

	public void stop() {
		if (activMotor) {
			final StopComInstruction stop = new StopComInstruction(1, 0);
			send(stop);
			activMotor = false;
		}
	}

	public void moveTo(final int positionInSteps) {
		activMotor = true;
		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_ABSOLUTE, 0, positionInSteps);
	}

	public void moveTo(final double positionInMillimeter) {
		activMotor = true;

		final double countOfNumerator = calculator.getCountOfNumerator(positionInMillimeter);
		final int countOfNumeratorInt = (int) Math.rint(countOfNumerator);

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_ABSOLUTE, 0, countOfNumeratorInt);
	}

	public void move(final int steps) {
		activMotor = true;

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_RELATIVE, 0, steps);
	}

	public void move(final double millimeter) {
		activMotor = true;

		final double countOfNumerator = calculator.getCountOfNumerator(millimeter);
		final int countOfNumeratorInt = (int) Math.rint(countOfNumerator);

		send(1, ComInstructionID.MOVE_POSITION, ComInstructionID.MOVE_POSITION_RELATIVE, 0, countOfNumeratorInt);
	}

	public void send(final int address, final int instruction, final int type, final int motor, final int value) {
		connector.send(address, instruction, type, motor, value);
	}

	public void send(final ComInstruction command) {
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

	protected void sendEvent(final ComAnswer answer) {
		for (final ComAnswerListener listener : answerListener) {
			listener.handleAnswer(new ComAnswerEvent(this, answer, calculator.getCountOfDenominator(answer.getValue())));
		}
	}

	@Override
	public void handleByteMessageEvent(final ByteMessageEvent event) {
		final byte[] byteArray = event.getByteArray();
		if (byteArray != null && byteArray.length > 0) {
			final ComAnswer answer = ComAnswer.build(byteArray);
			sendEvent(answer);
		}

	}

	public void storePosition(final int position) {
		send(1, ComInstructionID.STORE_AXIS_PARAMETER, ComInstructionID.GET_AXIS_PARAMETER_ACTUAL_POSITION, 0, position);
	}

	public void setMicrostepResolution(final int mode) {
		if (mode >= 1 && mode <= 8) {
			activMotor = true;
			send(1, ComInstructionID.SET_AXIS_PARAMETER, ComInstructionID.SET_AXIS_PARAMETER_MICROSTEP_RESOLUTION, 0,
					mode);

		}
	}

	public void setSIO(final int number, final boolean on) {
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

	public void setComPort(final String comPort) {
		connector.setComPortName(comPort);
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(final boolean reverse) {
		this.reverse = reverse;
	}

	public double getStepsPerMm() {
		return calculator.getNumerator();
	}

	public void setStepsPerMm(final double stepsPerMm) {
		calculator = new Calculator(stepsPerMm, 1.0);
	}

}
