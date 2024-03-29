package de.dennismaass.emp.stonemaster.stackmaster.controller.calculator;

import lombok.Getter;

@Getter
public class Calculator {
	private double factor;
	private double numerator;
	private double denominator;

	public Calculator(double numerator, double denominator) {
		this.numerator = numerator;
		this.denominator = denominator;

		factor = numerator / denominator;
	}

	public double getCountOfDenominator(double numerator) {
		return numerator / factor;
	}

	public double getCountOfNumerator(double denominator) {
		return denominator * factor;
	}

}
