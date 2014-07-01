package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer;

import java.util.EventObject;

import lombok.Getter;

@Getter
public class ComAnswerEvent extends EventObject {
	private final static long serialVersionUID = 8359647564190790477L;

	private ComAnswer answer;
	private double valueInMillimeter;

	public ComAnswerEvent(Object source, ComAnswer answer, double valueInMillimeter) {
		super(source);
		this.answer = answer;
		this.valueInMillimeter = valueInMillimeter;
	}

	public int getAdress() {
		return answer.getAddress();
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
