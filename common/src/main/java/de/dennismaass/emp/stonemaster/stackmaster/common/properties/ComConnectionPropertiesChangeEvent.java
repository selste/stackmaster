package de.dennismaass.emp.stonemaster.stackmaster.common.properties;

import java.util.EventObject;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionProperties;

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
