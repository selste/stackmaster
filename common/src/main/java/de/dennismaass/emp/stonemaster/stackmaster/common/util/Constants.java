package de.dennismaass.emp.stonemaster.stackmaster.common.util;

import java.util.Locale;

public final class Constants {

	public static final String FONT_NAME = "Arial";

	public static final String OSVERSION_PROPERTY_NAME = "os.version";
	public static final String OSARCH_PROPERTY_NAME = "os.arch";
	public static final String OSNAME_PROPERTY_NAME = "os.name";

	public static final String OS_NAME = System.getProperty(OSNAME_PROPERTY_NAME).toLowerCase(Locale.US);
	public static final String OS_ARCH = System.getProperty(OSARCH_PROPERTY_NAME).toLowerCase(Locale.US);
	public static final String OS_VERSION = System.getProperty(OSVERSION_PROPERTY_NAME).toLowerCase(Locale.US);

	public static final int ROUNDER = 100000000;
	public static final String UNIT = "mm";

	public static final double MIN_STEP = -250.0, MAX_STEP = 250.0;
	public static final int MAX_SPEED = 2047;

	private Constants() {
	}
}
