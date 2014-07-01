package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command;

import lombok.Getter;

@Getter
public abstract class AComCommand {

	protected int address;
	protected int instruction;
	protected int value;

	public AComCommand(int address, int instruction, int value) {
		super();
		this.address = address;
		this.instruction = instruction;
		this.value = value;
	}

	public abstract byte[] toByteMessage();

}
