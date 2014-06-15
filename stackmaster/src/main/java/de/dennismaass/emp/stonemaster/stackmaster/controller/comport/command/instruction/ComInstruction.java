package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.instruction;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.AComCommand;

public class ComInstruction extends AComCommand {
	protected final int type;
	protected final int motor;

	public ComInstruction(final int address, final int instruction, final int type, final int motor, final int value) {
		super(address, instruction, value);

		this.type = type;
		this.motor = motor;
	}

	public byte getChecksum() {
		return (byte) (address + instruction + type + motor + (byte) (value >> 24 & 0xff) + (byte) (value >> 16 & 0xff)
				+ (byte) (value >> 8 & 0xff) + (byte) (value & 0xff));
	}

	public int getType() {
		return type;
	}

	public int getMotor() {
		return motor;
	}

	@Override
	public byte[] toByteMessage() {
		final byte[] bytes = new byte[9];
		bytes[0] = (byte) (address & 0xff);
		bytes[1] = (byte) (instruction & 0xff);
		bytes[2] = (byte) (type & 0xff);
		bytes[3] = (byte) (motor & 0xff);
		bytes[4] = (byte) (value >> 24 & 0xff);
		bytes[5] = (byte) (value >> 16 & 0xff);
		bytes[6] = (byte) (value >> 8 & 0xff);
		bytes[7] = (byte) (value & 0xff);
		bytes[8] = (byte) (getChecksum() & 0xff);

		return bytes;
	}

	@Override
	public String toString() {
		return "instruction(" + (address & 0xff) + ", " + (instruction & 0xff) + ", " + type + ", " + motor + ", "
				+ value + ", " + (getChecksum() & 0xff) + ")";
	}

}
