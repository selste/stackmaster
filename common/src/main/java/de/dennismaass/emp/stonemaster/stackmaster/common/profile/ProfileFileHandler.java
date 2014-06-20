package de.dennismaass.emp.stonemaster.stackmaster.common.profile;

import java.io.File;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.UiProperties;

public class ProfileFileHandler {
	private static final Logger LOGGER = Logger.getLogger(ProfileFileHandler.class);

	private final PropertiesFileHandler propertiesFileHandler = new PropertiesFileHandler();

	public Profile readProfile(final File file) {
		final UiProperties connectionProperties = propertiesFileHandler.readConnectionProperties(file);

		final Profile profile = new Profile();
		profile.setProperties(connectionProperties);

		return profile;
	}

	public void writeProfile(final File file, final Profile profile) {
		propertiesFileHandler.writeConnectionProperties(file, profile.getProperties());
	}
}
