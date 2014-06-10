package de.dennismaass.stonemaster.stackmaster.comport.command;

public abstract class AComCommand {

	protected final int address;
	protected final int instruction;
	protected final int value;

	public AComCommand(final int address, final int instruction, final int value) {
		super();
		this.address = address;
		this.instruction = instruction;
		this.value = value;
	}

	public int getAddress() {
		return address;
	}

	public int getInstruction() {
		return instruction;
	}

	public int getValue() {
		return value;
	}

	public abstract byte[] toByteMessage();

}
