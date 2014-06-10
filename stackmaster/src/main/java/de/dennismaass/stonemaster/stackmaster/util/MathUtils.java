package de.dennismaass.stonemaster.stackmaster.util;

public final class MathUtils {

	private MathUtils() {

	}

	public static double round(final double value, final int countOfDecimals) {
		final double factor = Math.pow(10, countOfDecimals);
		return Math.rint(value * factor) / factor;
	}

}
