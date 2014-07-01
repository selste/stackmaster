package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import gnu.io.CommPortIdentifier;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CommPortIdentifierListRenderer extends DefaultListCellRenderer {

	private static long serialVersionUID = 1224386119871442113L;
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		String label = "";

		if (value != null && value instanceof CommPortIdentifier) {
			CommPortIdentifier commPortIdentifier = (CommPortIdentifier) value;
			label = commPortIdentifier.getName();
		}

		return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
	}

}
