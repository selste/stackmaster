package de.dennismaass.emp.stonemaster.stackmaster.common.ui.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PropertiesFileChooser extends JFileChooser {
	private static final long serialVersionUID = 3124624998436602958L;
	
	private static String STACK_MASTER_FILE_DESCRIPTION = "StackMaster Dateien";
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
