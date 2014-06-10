package de.dennismaass.stonemaster.stackmaster.comport.connection.bytemessage;

public interface ByteMessageListener {

	public void handleByteMessageEvent(final ByteMessageEvent event);
}
