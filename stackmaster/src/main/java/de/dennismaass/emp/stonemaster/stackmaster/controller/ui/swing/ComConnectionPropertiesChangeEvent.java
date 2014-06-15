package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.util.EventObject;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.ComConnectionProperties;

public class ComConnectionPropertiesChangeEvent extends EventObject {
	private static final long serialVersionUID = -4766066275663476550L;

	private final ComConnectionProperties connectionProperties;

	public ComConnectionPropertiesChangeEvent(final Object source, final ComConnectionProperties connectionProperties) {
		super(source);
		this.connectionProperties = connectionProperties;
	}

	public ComConnectionProperties getConnectionProperties() {
		return connectionProperties;
	}

}
