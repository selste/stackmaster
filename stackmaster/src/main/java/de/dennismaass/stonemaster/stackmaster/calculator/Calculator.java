package de.dennismaass.stonemaster.stackmaster.calculator;

public class Calculator {
	private final double factor;
	private final double numerator;
	private final double denominator;

	public Calculator(final double numerator, final double denominator) {
		this.numerator = numerator;
		this.denominator = denominator;

		factor = numerator / denominator;
	}

	public double getCountOfDenominator(final double numerator) {
		return numerator / factor;
	}

	public double getCountOfNumerator(final double denominator) {
		return denominator * factor;
	}

	public double getFactor() {
		return factor;
	}

	public double getNumerator() {
		return numerator;
	}

	public double getDenominator() {
		return denominator;
	}

}
