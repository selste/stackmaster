package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesFileHandler;

public class ComConnectionPropertiesFileHandler extends PropertiesFileHandler {
	private static final Logger LOGGER = Logger.getLogger(ComConnectionPropertiesFileHandler.class);

	private static final String MAX_ACCELERATION = "maxAcceleration";
	private static final String TRANSLATION = "translation";
	private static final String BASE_TRANSLATION = "baseTranslation";
	private static final String MAX_SPEED = "maxSpeed";
	private static final String PULSE_DURATION = "pulseDuration";
	private static final String SLEEP_PICTURE_MOVEMENT = "sleepPictureMovement";
	private static final String SLEEP_MOVEMENT_MIRROR = "sleepMovementMirror";
	private static final String SLEEP_MIRROR_PICTURE = "sleepMirrorPicture";
	private static final String SLEEP_WHILE_MOVE = "sleepWhileMove";
	private static final String LAST_STEP = "lastStep";
	private static final String MICROSTEP_RESOLUTION_MODE = "microstepResolutionMode";
	private static final String REVERSE = "reverse";
	private static final String BASE_STEPS_PER_MM = "baseStepsPerMm";
	private static final String FAST_DOWN_SPEED = "fastDownSpeed";
	private static final String MIDDLE_DOWN_SPEED = "middleDownSpeed";
	private static final String SLOW_DOWN_SPEED = "slowDownSpeed";
	private static final String SLOW_UP_SPEED = "slowUpSpeed";
	private static final String MIDDLE_UP_SPEED = "middleUpSpeed";
	private static final String FAST_UP_SPEED = "fastUpSpeed";
	private static final String COM_CONNECTION_NAME = "comConnectionName";



	public ComConnectionProperties read(File file) {
		Properties properties = readProperties(file);

		ComConnectionProperties comConnectionProperties = new ComConnectionProperties();

		try {
			String comConnectionName = properties.getProperty(COM_CONNECTION_NAME);
			comConnectionProperties.setComConnectionName(comConnectionName);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+COM_CONNECTION_NAME, e);
		}


		try {
			Integer fastUpSpeed = Integer.parseInt(properties.getProperty(FAST_UP_SPEED));
			comConnectionProperties.setFastUpSpeed(fastUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+FAST_UP_SPEED, e);
		}

		try {
			Integer middleUpSpeed = Integer.parseInt(properties.getProperty(MIDDLE_UP_SPEED));
			comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+MIDDLE_UP_SPEED, e);
		}

		try {
			Integer slowUpSpeed = Integer.parseInt(properties.getProperty(SLOW_UP_SPEED));
			comConnectionProperties.setSlowUpSpeed(slowUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLOW_UP_SPEED, e);
		}

		try {
			Integer slowDownSpeed = Integer.parseInt(properties.getProperty(SLOW_DOWN_SPEED));
			comConnectionProperties.setSlowDownSpeed(slowDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLOW_DOWN_SPEED, e);
		}

		try {
			Integer middleDownSpeed = Integer.parseInt(properties.getProperty(MIDDLE_DOWN_SPEED));
			comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+MIDDLE_DOWN_SPEED, e);
		}

		try {
			Integer fastDownSpeed = Integer.parseInt(properties.getProperty(FAST_DOWN_SPEED));
			comConnectionProperties.setFastDownSpeed(fastDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+FAST_DOWN_SPEED, e);
		}

		try {
			Integer stepsPerMm = Integer.parseInt(properties.getProperty(BASE_STEPS_PER_MM));
			comConnectionProperties.setBaseStepsPerMm(stepsPerMm);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+BASE_STEPS_PER_MM, e);
		}

		try {
			Boolean reverse = Boolean.parseBoolean(properties.getProperty(REVERSE));
			comConnectionProperties.setReverseSteps(reverse);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+REVERSE, e);
		}

		try {
			Integer microstepResolutionMode = Integer.parseInt(properties.getProperty(MICROSTEP_RESOLUTION_MODE));
			comConnectionProperties.setMicrostepResolutionMode(microstepResolutionMode);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+MICROSTEP_RESOLUTION_MODE, e);
		}

		try {
			Double lastStep = Double.parseDouble(properties.getProperty(LAST_STEP));
			comConnectionProperties.setStepSize(lastStep);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+LAST_STEP, e);
		}

		try {
			Long sleepWhileMove = Long.parseLong(properties.getProperty(SLEEP_WHILE_MOVE));
			comConnectionProperties.setSleepWhileMove(sleepWhileMove);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLEEP_WHILE_MOVE, e);
		}

		try {
			Long sleepMirrorPicture = Long.parseLong(properties.getProperty(SLEEP_MIRROR_PICTURE));
			comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLEEP_MIRROR_PICTURE, e);
		}

		try {
			Long sleepMovementMirror = Long.parseLong(properties.getProperty(SLEEP_MOVEMENT_MIRROR));
			comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLEEP_MOVEMENT_MIRROR, e);
		}

		try {
			Long sleepPictureMovement = Long.parseLong(properties.getProperty(SLEEP_PICTURE_MOVEMENT));
			comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+SLEEP_PICTURE_MOVEMENT, e);
		}

		try {
			Long pulseDuration = Long.parseLong(properties.getProperty(PULSE_DURATION));
			comConnectionProperties.setPulseDuration(pulseDuration);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+PULSE_DURATION, e);
		}

		try {
			Integer maxSpeed = Integer.parseInt(properties.getProperty(MAX_SPEED));
			comConnectionProperties.setMaxSpeed(maxSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+MAX_SPEED, e);
		}

		try {
			Double translation = Double.parseDouble(properties.getProperty(TRANSLATION));
			comConnectionProperties.setTranslation(translation);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+TRANSLATION, e);
		}

		try {
			Double baseTranslation = Double.parseDouble(properties.getProperty(BASE_TRANSLATION));
			comConnectionProperties.setBaseTranslation(baseTranslation);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+BASE_TRANSLATION, e);
		}

		try {
			Integer maxAcceleration = Integer.parseInt(properties.getProperty(MAX_ACCELERATION));
			comConnectionProperties.setMaxAcceleration(maxAcceleration);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing "+MAX_ACCELERATION, e);
		}

		return comConnectionProperties;

	}

	public void write(File file, ComConnectionProperties comConnectionProperties) {
		Properties properties = new Properties();

		String comConnectionName = comConnectionProperties.getComConnectionName();
		if (StringUtils.isNotEmpty(comConnectionName)) {
			properties.setProperty(COM_CONNECTION_NAME, comConnectionName);
		}
		int fastUpSpeed = comConnectionProperties.getFastUpSpeed();
		properties.setProperty(FAST_UP_SPEED, Integer.toString(fastUpSpeed));
		int middleUpSpeed = comConnectionProperties.getMiddleUpSpeed();
		properties.setProperty(MIDDLE_UP_SPEED, Integer.toString(middleUpSpeed));
		int slowUpSpeed = comConnectionProperties.getSlowUpSpeed();
		properties.setProperty(SLOW_UP_SPEED, Integer.toString(slowUpSpeed));
		int fastDownSpeed = comConnectionProperties.getFastDownSpeed();
		properties.setProperty(FAST_DOWN_SPEED, Integer.toString(fastDownSpeed));
		int middleDownSpeed = comConnectionProperties.getMiddleDownSpeed();
		properties.setProperty(MIDDLE_DOWN_SPEED, Integer.toString(middleDownSpeed));
		int slowDownSpeed = comConnectionProperties.getSlowDownSpeed();
		properties.setProperty(SLOW_DOWN_SPEED, Integer.toString(slowDownSpeed));
		int stepsPerMm = comConnectionProperties.getBaseStepsPerMm();
		properties.setProperty(BASE_STEPS_PER_MM, Integer.toString(stepsPerMm));
		boolean reverse = comConnectionProperties.isReverseSteps();
		properties.setProperty(REVERSE, Boolean.toString(reverse));
		int microstepResolutionMode = comConnectionProperties.getMicrostepResolutionMode();
		properties.setProperty(MICROSTEP_RESOLUTION_MODE, Integer.toString(microstepResolutionMode));
		long pulseDuration = comConnectionProperties.getPulseDuration();
		properties.setProperty(PULSE_DURATION, Long.toString(pulseDuration));

		long sleepPictureMovement = comConnectionProperties.getSleepPictureMovement();
		properties.setProperty(SLEEP_PICTURE_MOVEMENT, Long.toString(sleepPictureMovement));

		long sleepWhileMove = comConnectionProperties.getSleepWhileMove();
		properties.setProperty(SLEEP_WHILE_MOVE, Long.toString(sleepWhileMove));

		long sleepMirrorPicture = comConnectionProperties.getSleepMirrorPicture();
		properties.setProperty(SLEEP_MIRROR_PICTURE, Long.toString(sleepMirrorPicture));

		long sleepMovementMirror = comConnectionProperties.getSleepMovementMirror();
		properties.setProperty(SLEEP_MOVEMENT_MIRROR, Long.toString(sleepMovementMirror));

		double lastStep = comConnectionProperties.getStepSize();
		properties.setProperty(LAST_STEP, Double.toString(lastStep));

		double translation = comConnectionProperties.getTranslation();
		properties.setProperty(TRANSLATION, Double.toString(translation));

		double baseTranslation = comConnectionProperties.getBaseTranslation();
		properties.setProperty(BASE_TRANSLATION, Double.toString(baseTranslation));

		int maxSpeed = comConnectionProperties.getMaxSpeed();
		properties.setProperty(MAX_SPEED, Integer.toString(maxSpeed));

		int maxAcceleration = comConnectionProperties.getMaxAcceleration();
		properties.setProperty(MAX_ACCELERATION, Integer.toString(maxAcceleration));

		writeProperties(file, properties);

	}

}
