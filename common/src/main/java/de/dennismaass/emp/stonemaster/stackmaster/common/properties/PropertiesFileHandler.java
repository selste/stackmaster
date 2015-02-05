package de.dennismaass.emp.stonemaster.stackmaster.common.properties;

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
	private static Logger LOGGER = Logger.getLogger(PropertiesFileHandler.class);

	protected void writeProperties(File file, Properties properties) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry("application.properties");
			zos.putNextEntry(ze);
			properties.storeToXML(zos, "application properties", "UTF-8");

			zos.closeEntry();

			zos.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected Properties readProperties(File fileName) {
		Properties properties = new Properties();

		try {
			LOGGER.info("try loading properties");
			ZipFile zipFile = new ZipFile(fileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();

				InputStream inputStream = zipFile.getInputStream(zipEntry);
				properties.loadFromXML(inputStream);
				inputStream.close();
			}
			zipFile.close();
			LOGGER.info("properties loaded");
		} catch (IOException e) {
			LOGGER.error("Error before or while reading properties file", e);
		}

		return properties;
	}

}
