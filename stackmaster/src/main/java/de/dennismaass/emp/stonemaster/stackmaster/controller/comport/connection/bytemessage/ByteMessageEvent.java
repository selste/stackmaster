package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage;

import java.util.EventObject;

public class ByteMessageEvent extends EventObject {
	private static final long serialVersionUID = -4766066275663476550L;

	/** Equals the byte message **/
	private final byte[] byteArray;

	public ByteMessageEvent(final Object source, final byte[] byteArray) {
		super(source);
		this.byteArray = byteArray;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

}
