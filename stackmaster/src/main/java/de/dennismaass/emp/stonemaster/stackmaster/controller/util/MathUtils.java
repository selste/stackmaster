package de.dennismaass.emp.stonemaster.stackmaster.controller.util;

public class MathUtils {

	private MathUtils() {

	}

	public static double round(double value, int countOfDecimals) {
		double factor = Math.pow(10, countOfDecimals);
		return Math.rint(value * factor) / factor;
	}

}
