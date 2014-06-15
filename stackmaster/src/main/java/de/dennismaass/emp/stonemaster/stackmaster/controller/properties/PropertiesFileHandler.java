package de.dennismaass.emp.stonemaster.stackmaster.controller.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class PropertiesFileHandler {
	private static final Logger LOGGER = Logger.getLogger(PropertiesFileHandler.class);

	protected Properties readProperties(final File fileName) {
		final Properties properties = new Properties();

		try {
			LOGGER.info("try loading properties");
			final ZipFile zipFile = new ZipFile(fileName);
			final Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				final ZipEntry zipEntry = entries.nextElement();

				final InputStream inputStream = zipFile.getInputStream(zipEntry);
				properties.load(inputStream);
				inputStream.close();
			}
			zipFile.close();
			LOGGER.info("properties loaded");
		} catch (final IOException e) {
			LOGGER.error("Error before or while reading properties file", e);
			e.printStackTrace();
		}

		return properties;
	}

	public UiProperties readConnectionProperties(final File file) {
		final Properties properties = readProperties(file);

		final UiProperties comConnectionProperties = new UiProperties();

		try {
			final Integer fastUpSpeed = Integer.parseInt(properties.getProperty("fastUpSpeed"));
			comConnectionProperties.setFastUpSpeed(fastUpSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing fastUpSpeed", e);
		}

		try {
			final Integer middleUpSpeed = Integer.parseInt(properties.getProperty("middleUpSpeed"));
			comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing middleUpSpeed", e);
		}

		try {
			final Integer slowUpSpeed = Integer.parseInt(properties.getProperty("slowUpSpeed"));
			comConnectionProperties.setSlowUpSpeed(slowUpSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing slowUpSpeed", e);
		}

		try {
			final Integer slowDownSpeed = Integer.parseInt(properties.getProperty("slowDownSpeed"));
			comConnectionProperties.setSlowDownSpeed(slowDownSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing slowDownSpeed", e);
		}

		try {
			final Integer middleDownSpeed = Integer.parseInt(properties.getProperty("middleDownSpeed"));
			comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing middleDownSpeed", e);
		}

		try {
			final Integer fastDownSpeed = Integer.parseInt(properties.getProperty("fastDownSpeed"));
			comConnectionProperties.setFastDownSpeed(fastDownSpeed);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing fastDownSpeed", e);
		}

		try {
			final Integer stepsPerMm = Integer.parseInt(properties.getProperty("stepsPerMm"));
			comConnectionProperties.setStepsPerMm(stepsPerMm);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing stepsPerMm", e);
		}

		try {
			final Boolean reverse = Boolean.parseBoolean(properties.getProperty("reverse"));
			comConnectionProperties.setReverseSteps(reverse);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing reverse", e);
		}

		try {
			final Integer microstepResolutionMode = Integer.parseInt(properties.getProperty("microstepResolutionMode"));
			comConnectionProperties.setMicrostepResolutionMode(microstepResolutionMode);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing microstepResolutionMode", e);
		}

		try {
			final Double lastStep = Double.parseDouble(properties.getProperty("lastStep"));
			comConnectionProperties.setStepSize(lastStep);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing lastStep", e);
		}

		try {
			final Long sleepWhileMove = Long.parseLong(properties.getProperty("sleepWhileMove"));
			comConnectionProperties.setSleepWhileMove(sleepWhileMove);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing sleepWhileMove", e);
		}

		try {
			final Long sleepMirrorPicture = Long.parseLong(properties.getProperty("sleepMirrorPicture"));
			comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing sleepMirrorPicture", e);
		}

		try {
			final Long sleepMovementMirror = Long.parseLong(properties.getProperty("sleepMovementMirror"));
			comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing sleepMovementMirror", e);
		}

		try {
			final Long sleepPictureMovement = Long.parseLong(properties.getProperty("sleepPictureMovement"));
			comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing sleepPictureMovement", e);
		}

		try {
			final Long pulseDuration = Long.parseLong(properties.getProperty("pulseDuration"));
			comConnectionProperties.setPulseDuration(pulseDuration);
		} catch (final NumberFormatException e) {
			LOGGER.error("error by parsing pulseDuration", e);
		}

		return comConnectionProperties;

	}

	public void writeConnectionProperties(final File file, final UiProperties comConnectionProperties) {

		final Properties properties = new Properties();
		// final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));

		final int fastUpSpeed = comConnectionProperties.getFastUpSpeed();
		properties.setProperty("fastUpSpeed", Integer.toString(fastUpSpeed));
		final int middleUpSpeed = comConnectionProperties.getMiddleUpSpeed();
		properties.setProperty("middleUpSpeed", Integer.toString(middleUpSpeed));
		final int slowUpSpeed = comConnectionProperties.getSlowUpSpeed();
		properties.setProperty("slowUpSpeed", Integer.toString(slowUpSpeed));
		final int fastDownSpeed = comConnectionProperties.getFastDownSpeed();
		properties.setProperty("fastDownSpeed", Integer.toString(fastDownSpeed));
		final int middleDownSpeed = comConnectionProperties.getMiddleDownSpeed();
		properties.setProperty("middleDownSpeed", Integer.toString(middleDownSpeed));
		final int slowDownSpeed = comConnectionProperties.getSlowDownSpeed();
		properties.setProperty("slowDownSpeed", Integer.toString(slowDownSpeed));
		final int stepsPerMm = comConnectionProperties.getStepsPerMm();
		properties.setProperty("stepsPerMm", Integer.toString(stepsPerMm));
		final boolean reverse = comConnectionProperties.isReverseSteps();
		properties.setProperty("reverse", Boolean.toString(reverse));
		final int microstepResolutionMode = comConnectionProperties.getMicrostepResolutionMode();
		properties.setProperty("microstepResolutionMode", Integer.toString(microstepResolutionMode));
		final long pulseDuration = comConnectionProperties.getPulseDuration();
		properties.setProperty("pulseDuration", Long.toString(pulseDuration));

		final long sleepPictureMovement = comConnectionProperties.getSleepPictureMovement();
		properties.setProperty("sleepPictureMovement", Long.toString(sleepPictureMovement));

		final long sleepWhileMove = comConnectionProperties.getSleepWhileMove();
		properties.setProperty("sleepWhileMove", Long.toString(sleepWhileMove));

		final long sleepMirrorPicture = comConnectionProperties.getSleepMirrorPicture();
		properties.setProperty("sleepMirrorPicture", Long.toString(sleepMirrorPicture));

		final long sleepMovementMirror = comConnectionProperties.getSleepMovementMirror();
		properties.setProperty("sleepMovementMirror", Long.toString(sleepMovementMirror));

		final double lastStep = comConnectionProperties.getStepSize();
		properties.setProperty("lastStep", Double.toString(lastStep));

		// byte[] buffer = new byte[1024];

		try {

			final FileOutputStream fos = new FileOutputStream(file);
			final ZipOutputStream zos = new ZipOutputStream(fos);
			final ZipEntry ze = new ZipEntry("stackmaster.properties");
			zos.putNextEntry(ze);

			// int len;
			// while ((len = in.read(buffer)) > 0) {
			// zos.write(buffer, 0, len);
			// }
			properties.storeToXML(zos, "stackmaster properties", "UTF-8");

			zos.closeEntry();

			// remember close it
			zos.close();

		} catch (final IOException ex) {
			ex.printStackTrace();
		}

	}

}
