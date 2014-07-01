package de.dennismaass.emp.stonemaster.stackmaster.common.ui.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PropertiesFileChooser extends JFileChooser {
	private static String STACK_MASTER_FILE_DESCRIPTION = "StackMaster Dateien";
	private static long serialVersionUID = -4209490203324629746L;
	private static String FILE_ENDING = "stackmaster";

	public PropertiesFileChooser() {
		init();
	}

	public PropertiesFileChooser(File currentDirectory) {
		super(currentDirectory);
		init();
	}

	protected void init() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(STACK_MASTER_FILE_DESCRIPTION, FILE_ENDING);
		setFileFilter(filter);
	}
}
