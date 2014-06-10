package de.dennismaass.stonemaster.stackmaster.util;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class RxtxUtils {

	private RxtxUtils() {

	}

	public static CommPortIdentifier getCommPortIdentifier(final String portName) {
		CommPortIdentifier serialPortId = null;

		final Enumeration<?> enumComm = CommPortIdentifier.getPortIdentifiers();
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (serialPortId.getName().contentEquals(portName)) {
				break;
			}
		}

		return serialPortId;
	}

	public static boolean isSerial(final CommPortIdentifier portIdentifier) {
		if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			return true;
		}

		return false;
	}

	public static List<CommPortIdentifier> getCommPortIdentifier() {
		final Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		final List<?> actualObjectList = Collections.list(ports);

		final List<CommPortIdentifier> actualPortList = new ArrayList<>();

		CommPortIdentifier portIdentifier = null;
		for (final Object object : actualObjectList) {
			if (object instanceof CommPortIdentifier) {
				portIdentifier = (CommPortIdentifier) object;
				actualPortList.add(portIdentifier);
			}
		}
		return actualPortList;
	}

}
