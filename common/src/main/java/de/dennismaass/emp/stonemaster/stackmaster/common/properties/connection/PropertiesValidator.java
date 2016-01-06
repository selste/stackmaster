package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

public class PropertiesValidator {

	public static long MIN_SLEEP = 1;
	public static long MAX_SLEEP = Long.MAX_VALUE;

	public static final int MIN_HARDWARE_MODE = 1;
	public static final int MAX_HARDWARE_MODE = 8;
	public static final int MIN_HARDWARE_SPEED = 1;
	public static final int MAX_HARDWARE_SPEED = 800;

	public PropertiesValidator() {

	}

	public boolean validate(ComConnectionProperties comConnectionProperties) {
		if (!isValidSpeed(comConnectionProperties.getFastUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getMiddleUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getSlowUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getFastDownSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getMiddleDownSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getSlowDownSpeed())) {
			return false;
		}
		if (!isValidStepcount(comConnectionProperties.getStepsPerMm())) {
			return false;
		}
		if (!isValidMicrostepResolutionMode(comConnectionProperties.getMicrostepResolutionMode())) {
			return false;
		}
		// TODO: reverse, sleeps,

		return true;
	}

	public boolean isValidSpeed(int value) {
		if (value >= PropertiesValidator.MIN_HARDWARE_SPEED && value <= PropertiesValidator.MAX_HARDWARE_SPEED) {
			return true;
		}
		return false;
	}

	public boolean isValidSleep(long value) {
		if (value >= PropertiesValidator.MIN_SLEEP && value <= PropertiesValidator.MAX_SLEEP) {
			return true;
		}
		return false;
	}

	public boolean isValidStepcount(int value) {
		return true;
	}

	public boolean isValidMicrostepResolutionMode(int value) {
		if (value >= PropertiesValidator.MIN_HARDWARE_MODE && value <= PropertiesValidator.MAX_HARDWARE_MODE) {
			return true;
		}
		return false;
	}

}
