package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesFileHandler;

public class ComConnectionPropertiesFileHandler extends PropertiesFileHandler {
	private static Logger LOGGER = Logger.getLogger(ComConnectionPropertiesFileHandler.class);

	public ComConnectionProperties read(File file) {
		Properties properties = readProperties(file);

		ComConnectionProperties comConnectionProperties = new ComConnectionProperties();

		try {
			String comConnectionName = properties.getProperty("comConnectionName");
			comConnectionProperties.setComConnectionName(comConnectionName);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing comConnectionName", e);
		}

		try {
			Integer fastUpSpeed = Integer.parseInt(properties.getProperty("fastUpSpeed"));
			comConnectionProperties.setFastUpSpeed(fastUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing fastUpSpeed", e);
		}

		try {
			Integer middleUpSpeed = Integer.parseInt(properties.getProperty("middleUpSpeed"));
			comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing middleUpSpeed", e);
		}

		try {
			Integer slowUpSpeed = Integer.parseInt(properties.getProperty("slowUpSpeed"));
			comConnectionProperties.setSlowUpSpeed(slowUpSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing slowUpSpeed", e);
		}

		try {
			Integer slowDownSpeed = Integer.parseInt(properties.getProperty("slowDownSpeed"));
			comConnectionProperties.setSlowDownSpeed(slowDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing slowDownSpeed", e);
		}

		try {
			Integer middleDownSpeed = Integer.parseInt(properties.getProperty("middleDownSpeed"));
			comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing middleDownSpeed", e);
		}

		try {
			Integer fastDownSpeed = Integer.parseInt(properties.getProperty("fastDownSpeed"));
			comConnectionProperties.setFastDownSpeed(fastDownSpeed);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing fastDownSpeed", e);
		}

		try {
			Integer stepsPerMm = Integer.parseInt(properties.getProperty("stepsPerMm"));
			comConnectionProperties.setStepsPerMm(stepsPerMm);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing stepsPerMm", e);
		}

		try {
			Boolean reverse = Boolean.parseBoolean(properties.getProperty("reverse"));
			comConnectionProperties.setReverseSteps(reverse);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing reverse", e);
		}

		try {
			Integer microstepResolutionMode = Integer.parseInt(properties.getProperty("microstepResolutionMode"));
			comConnectionProperties.setMicrostepResolutionMode(microstepResolutionMode);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing microstepResolutionMode", e);
		}

		try {
			Double lastStep = Double.parseDouble(properties.getProperty("lastStep"));
			comConnectionProperties.setStepSize(lastStep);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing lastStep", e);
		}

		try {
			Long sleepWhileMove = Long.parseLong(properties.getProperty("sleepWhileMove"));
			comConnectionProperties.setSleepWhileMove(sleepWhileMove);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing sleepWhileMove", e);
		}

		try {
			Long sleepMirrorPicture = Long.parseLong(properties.getProperty("sleepMirrorPicture"));
			comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing sleepMirrorPicture", e);
		}

		try {
			Long sleepMovementMirror = Long.parseLong(properties.getProperty("sleepMovementMirror"));
			comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing sleepMovementMirror", e);
		}

		try {
			Long sleepPictureMovement = Long.parseLong(properties.getProperty("sleepPictureMovement"));
			comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing sleepPictureMovement", e);
		}

		try {
			Long pulseDuration = Long.parseLong(properties.getProperty("pulseDuration"));
			comConnectionProperties.setPulseDuration(pulseDuration);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing pulseDuration", e);
		}

		return comConnectionProperties;

	}

	public void write(File file, ComConnectionProperties comConnectionProperties) {
		Properties properties = new Properties();

		String comConnectionName = comConnectionProperties.getComConnectionName();
		if (StringUtils.isNotEmpty(comConnectionName)) {
			properties.setProperty("comConnectionName", comConnectionName);
		}
		int fastUpSpeed = comConnectionProperties.getFastUpSpeed();
		properties.setProperty("fastUpSpeed", Integer.toString(fastUpSpeed));
		int middleUpSpeed = comConnectionProperties.getMiddleUpSpeed();
		properties.setProperty("middleUpSpeed", Integer.toString(middleUpSpeed));
		int slowUpSpeed = comConnectionProperties.getSlowUpSpeed();
		properties.setProperty("slowUpSpeed", Integer.toString(slowUpSpeed));
		int fastDownSpeed = comConnectionProperties.getFastDownSpeed();
		properties.setProperty("fastDownSpeed", Integer.toString(fastDownSpeed));
		int middleDownSpeed = comConnectionProperties.getMiddleDownSpeed();
		properties.setProperty("middleDownSpeed", Integer.toString(middleDownSpeed));
		int slowDownSpeed = comConnectionProperties.getSlowDownSpeed();
		properties.setProperty("slowDownSpeed", Integer.toString(slowDownSpeed));
		int stepsPerMm = comConnectionProperties.getStepsPerMm();
		properties.setProperty("stepsPerMm", Integer.toString(stepsPerMm));
		boolean reverse = comConnectionProperties.isReverseSteps();
		properties.setProperty("reverse", Boolean.toString(reverse));
		int microstepResolutionMode = comConnectionProperties.getMicrostepResolutionMode();
		properties.setProperty("microstepResolutionMode", Integer.toString(microstepResolutionMode));
		long pulseDuration = comConnectionProperties.getPulseDuration();
		properties.setProperty("pulseDuration", Long.toString(pulseDuration));

		long sleepPictureMovement = comConnectionProperties.getSleepPictureMovement();
		properties.setProperty("sleepPictureMovement", Long.toString(sleepPictureMovement));

		long sleepWhileMove = comConnectionProperties.getSleepWhileMove();
		properties.setProperty("sleepWhileMove", Long.toString(sleepWhileMove));

		long sleepMirrorPicture = comConnectionProperties.getSleepMirrorPicture();
		properties.setProperty("sleepMirrorPicture", Long.toString(sleepMirrorPicture));

		long sleepMovementMirror = comConnectionProperties.getSleepMovementMirror();
		properties.setProperty("sleepMovementMirror", Long.toString(sleepMovementMirror));

		double lastStep = comConnectionProperties.getStepSize();
		properties.setProperty("lastStep", Double.toString(lastStep));

		writeProperties(file, properties);

	}

}
