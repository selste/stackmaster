package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

import java.util.EventObject;

import lombok.Getter;

public class ComConnectionPropertiesChangeEvent extends EventObject {
	private final static long serialVersionUID = -4766066275663476550L;

	@Getter
	private ComConnectionProperties connectionProperties;

	public ComConnectionPropertiesChangeEvent(Object source, ComConnectionProperties connectionProperties) {
		super(source);
		this.connectionProperties = connectionProperties;
	}

}
