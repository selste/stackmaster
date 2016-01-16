package de.dennismaass.emp.stonemaster.stackmaster.controller.calculator;

import lombok.Getter;

@Getter
public class Calculator {
	private double numerator;
	private double denominator;
	private double factor;

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
