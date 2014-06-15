package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.AComCommand;

public class ComAnswer extends AComCommand {
	private final int host;
	private final int status;
	private final int checksum;

	public ComAnswer(final int host, final int address, final int instruction, final int status, final int value,
			final int checksum) {
		super(address, instruction, value);

		this.host = host;
		this.status = status;
		this.checksum = checksum;
	}

	public int getChecksum() {
		return checksum;
	}

	public int getHost() {
		return host;
	}

	public int getStatus() {
		return status;
	}

	@Override
	public byte[] toByteMessage() {
		final int value = getValue();

		final byte[] bytes = new byte[9];
		bytes[0] = (byte) (getAddress() & 0xff);
		bytes[1] = (byte) (getInstruction() & 0xff);
		bytes[2] = (byte) (getStatus() & 0xff);
		bytes[3] = (byte) (getInstruction() & 0xff);
		bytes[4] = (byte) (value >> 24 & 0xff);
		bytes[5] = (byte) (value >> 16 & 0xff);
		bytes[6] = (byte) (value >> 8 & 0xff);
		bytes[7] = (byte) (value & 0xff);
		bytes[8] = (byte) (getChecksum() & 0xff);

		return bytes;
	}

	@Override
	public String toString() {
		return "TMCLReply(" + (getHost() & 0xff) + ", " + (getAddress() & 0xff) + ", " + (getStatus() & 0xff) + ", "
				+ (getInstruction() & 0xff) + ", " + getValue() + ", " + (getChecksum() & 0xff) + ")";
	}

	public static ComAnswer build(final byte[] bytes) {
		ComAnswer answer = null;
		if (bytes != null && bytes.length == 9) {
			final byte checksum = bytes[8];
			final byte host = bytes[0];
			final byte address = bytes[1];
			final byte status = bytes[2];
			final byte instruction = bytes[3];
			final int value = (bytes[4] & 0xff) << 24 | (bytes[5] & 0xff) << 16 | (bytes[6] & 0xff) << 8
					| (bytes[7] & 0xff) << 0;
			answer = new ComAnswer(host, address, instruction, status, value, checksum);

		}
		return answer;
	}

}
