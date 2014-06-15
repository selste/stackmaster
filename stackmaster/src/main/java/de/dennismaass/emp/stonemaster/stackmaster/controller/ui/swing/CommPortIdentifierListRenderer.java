package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import gnu.io.CommPortIdentifier;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CommPortIdentifierListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1224386119871442113L;
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index,
			final boolean isSelected, final boolean cellHasFocus) {
		String label = "";

		if (value != null && value instanceof CommPortIdentifier) {
			final CommPortIdentifier commPortIdentifier = (CommPortIdentifier) value;
			label = commPortIdentifier.getName();
		}

		return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
	}

}
