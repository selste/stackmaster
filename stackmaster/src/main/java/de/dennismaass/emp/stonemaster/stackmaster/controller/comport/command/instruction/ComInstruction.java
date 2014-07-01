package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.instruction;

import lombok.Getter;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.AComCommand;

@Getter
public class ComInstruction extends AComCommand {
	protected int type;
	protected int motor;

	public ComInstruction(int address, int instruction, int type, int motor, int value) {
		super(address, instruction, value);

		this.type = type;
		this.motor = motor;
	}

	public byte getChecksum() {
		return (byte) (address + instruction + type + motor + (byte) (value >> 24 & 0xff) + (byte) (value >> 16 & 0xff)
				+ (byte) (value >> 8 & 0xff) + (byte) (value & 0xff));
	}

	@Override
	public byte[] toByteMessage() {
		byte[] bytes = new byte[9];
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
