package de.dennismaass.emp.stonemaster.stackmaster.controller.calculator;

import lombok.Getter;

@Getter
public class Calculator {
	private double numerator;
	private double denominator;

	public Calculator(double numerator, double denominator) {
		this.numerator = numerator;
		this.denominator = denominator;

	}

	public double getCountOfDenominator(double numerator) {
		double factor = numerator / denominator;
		return numerator / factor;
	}

	public double getCountOfNumerator(double denominator) {
		double factor = numerator / denominator;
		return denominator * factor;
	}

}
