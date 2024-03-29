package de.dennismaass.emp.stonemaster.stackmaster.common.properties.application;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesFileHandler;

public class ApplicationPropertiesFileHandler {
	private static Logger LOGGER = Logger.getLogger(ApplicationPropertiesFileHandler.class);
	private PropertiesFileHandler propertiesFileHandler = new PropertiesFileHandler();

	public ApplicationProperties read(File file) {
		LOGGER.info("Loading application.properties: " + file.getAbsolutePath());

		Properties properties = propertiesFileHandler.read(file);

		ApplicationProperties applicationProperties = new ApplicationProperties();

		try {
			int fontSize = Integer.parseInt(properties.getProperty("fontSize"));
			applicationProperties.setFontSize(fontSize);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing fontSize", e);
		}

		try {
			boolean firstUse = Boolean.parseBoolean(properties.getProperty("firstUse"));
			applicationProperties.setFirstUse(firstUse);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing firstUse", e);
		}

		try {
			String firstName = properties.getProperty("firstName");
			applicationProperties.setFirstName(firstName);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing firstName", e);
		}
		try {
			String lastName = properties.getProperty("lastName");
			applicationProperties.setLastName(lastName);
		} catch (NumberFormatException e) {
			LOGGER.error("error by parsing lastName", e);
		}

		LOGGER.info("no error by parsing applicationProperties");
		return applicationProperties;

	}
	
	
	public Properties toProperties(ApplicationProperties applicationProperties){
		Properties properties = new Properties();

		boolean firstUse = applicationProperties.isFirstUse();
		properties.setProperty("firstUse", Boolean.toString(firstUse));

		String firstName = applicationProperties.getFirstName();
		String lastName = applicationProperties.getLastName();
		if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
			properties.setProperty("firstName", firstName);
			properties.setProperty("lastName", lastName);
		}
		int fontSize = applicationProperties.getFontSize();
		properties.setProperty("fontSize", Integer.toString(fontSize));
		
		return properties;
	}

	public void write(File file, ApplicationProperties applicationProperties) {
		LOGGER.info("write applicationProperties to " + file.getAbsolutePath());
		propertiesFileHandler.write(file, toProperties(applicationProperties));
	}

}
