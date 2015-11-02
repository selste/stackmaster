package de.dennismaass.emp.stonemaster.stackmaster.common.profile;

import java.io.File;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionPropertiesFileHandler;

public class ProfileFileHandler {
	private static Logger LOGGER = Logger.getLogger(ProfileFileHandler.class);

	private ComConnectionPropertiesFileHandler propertiesFileHandler = new ComConnectionPropertiesFileHandler();

	public Profile read(File file) {
		ComConnectionProperties connectionProperties = propertiesFileHandler.read(file);

		Profile profile = new Profile();
		profile.setProperties(connectionProperties);

		return profile;
	}

	public void write(File file, Profile profile) {
		LOGGER.info("write profile " + profile + " in file " + file);
		propertiesFileHandler.write(file, profile.getProperties());
	}
}
