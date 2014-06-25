package de.dennismaass.emp.stonemaster.stackmaster.common.ui.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PropertiesFileChooser extends JFileChooser {
	private static final long serialVersionUID = -4209490203324629746L;

	public PropertiesFileChooser() {
		init();
	}

	public PropertiesFileChooser(final File currentDirectory) {
		super(currentDirectory);
		init();
	}

	protected void init() {
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("StackMaster Dateien", "stackmaster");
		setFileFilter(filter);
	}
}
