package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection;

import gnu.io.CommPortIdentifier;

import java.util.EventObject;
import java.util.List;

public class CommPortIdentifierNotificationEvent extends EventObject {
	private static final long serialVersionUID = -4766066275663476550L;
	private final List<CommPortIdentifier> commPortIdentifierList;

	public CommPortIdentifierNotificationEvent(final Object source, final List<CommPortIdentifier> commPortIdentifierList) {
		super(source);
		this.commPortIdentifierList = commPortIdentifierList;
	}

	public List<CommPortIdentifier> getCommPortIdentifierList() {
		return commPortIdentifierList;
	}

}
