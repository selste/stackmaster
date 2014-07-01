package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage;

import java.util.EventObject;

import lombok.Getter;

@Getter
public class ByteMessageEvent extends EventObject {
	private final static long serialVersionUID = -4766066275663476550L;

	/** Equals the byte message **/
	private byte[] byteArray;

	public ByteMessageEvent(Object source, byte[] byteArray) {
		super(source);
		this.byteArray = byteArray;
	}

}
