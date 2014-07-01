package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection;

import gnu.io.CommPortIdentifier;

import java.util.EventObject;
import java.util.List;

public class CommPortIdentifierNotificationEvent extends EventObject {
	private static long serialVersionUID = -4766066275663476550L;
	private List<CommPortIdentifier> commPortIdentifierList;

	public CommPortIdentifierNotificationEvent(Object source, List<CommPortIdentifier> commPortIdentifierList) {
		super(source);
		this.commPortIdentifierList = commPortIdentifierList;
	}

	public List<CommPortIdentifier> getCommPortIdentifierList() {
		return commPortIdentifierList;
	}

}
