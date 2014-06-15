package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.bytemessage;

public interface ByteMessageListener {

	public void handleByteMessageEvent(final ByteMessageEvent event);
}
