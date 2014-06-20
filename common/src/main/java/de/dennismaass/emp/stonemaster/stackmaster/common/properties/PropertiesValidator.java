package de.dennismaass.emp.stonemaster.stackmaster.common.properties;

public class PropertiesValidator {

	public static final int MIN_MODE = 1;
	public static final int MAX_MODE = 8;

	public static final long MIN_SLEEP = 1;
	public static final long MAX_SLEEP = Long.MAX_VALUE;

	public static final int MIN_SPEED = 1;
	public static final int MAX_SPEED = 2047;

	public PropertiesValidator() {

	}

	public boolean validate(final ComConnectionProperties comConnectionProperties) {
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

	public boolean isValidSpeed(final int value) {
		if (value >= PropertiesValidator.MIN_SPEED && value <= PropertiesValidator.MAX_SPEED) {
			return true;
		}
		return false;
	}

	public boolean isValidSleep(final long value) {
		if (value >= PropertiesValidator.MIN_SLEEP && value <= PropertiesValidator.MAX_SLEEP) {
			return true;
		}
		return false;
	}

	public boolean isValidStepcount(final int value) {
		return true;
	}

	public boolean isValidMicrostepResolutionMode(final int value) {
		if (value >= PropertiesValidator.MIN_MODE && value <= PropertiesValidator.MAX_MODE) {
			return true;
		}
		return false;
	}

}
