package de.dennismaass.emp.stonemaster.stackmaster.controller.util;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class RxtxUtils {

	private RxtxUtils() {

	}

	public static CommPortIdentifier getCommPortIdentifier(String portName) {
		CommPortIdentifier serialPortId = null;

		Enumeration<?> enumComm = CommPortIdentifier.getPortIdentifiers();
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (serialPortId.getName().contentEquals(portName)) {
				break;
			}
		}

		return serialPortId;
	}

	public static boolean isSerial(CommPortIdentifier portIdentifier) {
		if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			return true;
		}

		return false;
	}

	public static List<CommPortIdentifier> getCommPortIdentifier() {
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		List<?> actualObjectList = Collections.list(ports);

		List<CommPortIdentifier> actualPortList = new ArrayList<>();

		CommPortIdentifier portIdentifier = null;
		for (Object object : actualObjectList) {
			if (object instanceof CommPortIdentifier) {
				portIdentifier = (CommPortIdentifier) object;
				actualPortList.add(portIdentifier);
			}
		}
		return actualPortList;
	}

}
