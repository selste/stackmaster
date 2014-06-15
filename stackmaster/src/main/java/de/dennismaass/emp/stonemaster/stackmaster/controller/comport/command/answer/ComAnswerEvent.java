package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer;

import java.util.EventObject;

public class ComAnswerEvent extends EventObject {
	private static final long serialVersionUID = 8359647564190790477L;

	private final ComAnswer answer;
	private final double valueInMillimeter;

	public ComAnswerEvent(final Object source, final ComAnswer answer, final double valueInMillimeter) {
		super(source);
		this.answer = answer;
		this.valueInMillimeter = valueInMillimeter;
	}

	public ComAnswer getAnswer() {
		return answer;
	}

	public int getAdress() {
		return answer.getAddress();
	}

	public double getValueInMillimeter() {
		return valueInMillimeter;
	}

	public int getHost() {
		return answer.getHost();
	}

	public int getValue() {
		return answer.getValue();
	}

	public int getStatus() {
		return answer.getStatus();
	}

	public int getInstruction() {
		return answer.getInstruction();
	}
}
