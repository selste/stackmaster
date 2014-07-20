package de.dennismaass.emp.stonemaster.stackmaster.common.util;

public final class Constants {

	public static final String FONT_NAME = "Arial";

	public static final String OS_NAME = System.getProperty("os.name");
	public static final String OS_ARCH = System.getProperty("os.arch");
	public static final String OS_VERSION = System.getProperty("os.version");
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String JAVA_HOME = System.getProperty("java.home");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor");

	public static final int ROUNDER = 100000000;
	public static final String UNIT = "mm";

	public static final double MIN_STEP = -250.0, MAX_STEP = 250.0;
	public static final int MAX_SPEED = 2047;

	private Constants() {
	}
}
