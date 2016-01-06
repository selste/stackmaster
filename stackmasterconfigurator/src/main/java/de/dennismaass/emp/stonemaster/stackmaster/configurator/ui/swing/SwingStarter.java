package de.dennismaass.emp.stonemaster.stackmaster.configurator.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationPropertiesFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.PropertiesValidator;
import de.dennismaass.emp.stonemaster.stackmaster.common.util.Constants;
import de.dennismaass.emp.stonemaster.stackmaster.common.util.PathConstants;
import de.dennismaass.emp.stonemaster.stackmaster.configurator.ui.utils.UiConstants;
import net.miginfocom.swing.MigLayout;

public class SwingStarter extends JFrame {
	private final static long serialVersionUID = -6032729218147398086L;

	private static Logger LOGGER = Logger.getLogger(SwingStarter.class);

	public static final int MIN_HARDWARE_MODE = 1;
	public static final int MAX_HARDWARE_MODE = 8;
	public static final int MIN_HARDWARE_SPEED = 1;
	public static final int MAX_HARDWARE_SPEED = 800;

	public static double MIN_STEP = -250.0, MAX_STEP = 250.0;
	public static long MIN_SLEEP = 1;
	public static long MAX_SLEEP = Long.MAX_VALUE;



	public Font font = new Font(Constants.FONT_NAME, Font.PLAIN, 20);

	private Profile defaultProfile;
	private File defaultFile = new File(PathConstants.DEFAULT_PROFILE_NAME);

	private ComConnectionProperties defaultConnectionProperties;

	private ProfileFileHandler propertiesHandler;

	private JTextField firstNameTF, lastNameTF, comConnectionNameTF;

	private JSpinner fastUpSpeedTF, middleUpSpeedTF, slowUpSpeedTF, fastDownSpeedTF, middleDownSpeedTF,
	slowDownSpeedTF,maxSpeedTF;

	private JSpinner countOfStepPerMmTF;
	private JComboBox<Integer> connectionModeCB;
	private JCheckBox reverseCB;
	private JSpinner sleepMirrorPictureTF, sleepMovementMirrorTF, sleepPictureMovementTF, pulseDurationTF;

	private PropertiesValidator validator = new PropertiesValidator();

	private static ApplicationProperties applicationProperties;
	private File applicationPropertyFile = new File("application.properties");
	private ApplicationPropertiesFileHandler applicationPropertiesFileHandler = new ApplicationPropertiesFileHandler();;

	private JCheckBox checkBoxFirstUse;
	private JComboBox<Integer> checkBoxFontSize;

	private JLabel lblModus, lblNewLabel_15, lblFirstUse, lblSchriftgre, lblNewLabel, lblNewLabel_1, lblNewLabel_2,
	lblNewLabel_3, lblNewLabel_4, lblNewLabel_6, lblNewLabel_7, lblNewLabel_8, lblNewLabel_9, lblNewLabel_10,
	lblNewLabel_11, lblNewLabel_12, lblNewLabel_14, lblSchritteProMillimeter, lblNewLabel_13, lblNewLabel_5,
	label_2;

	private JTabbedPane tabbedPane;

	private JMenu menu;
	private JMenuItem mntmExit, mntmSpeichern;

	private JLabel lblTranslate,lblMaxSpeed,lblMaxAcceleration;

	private JSpinner translationTF,maxAccelerationTF;

	public SwingStarter() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setTitle(UiConstants.TITLE);
		propertiesHandler = new ProfileFileHandler();

		setSize(430, 770);
		getContentPane().setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		JPanel applicationPanel = new JPanel();
		tabbedPane.addTab("application.properties", null, applicationPanel, null);
		applicationPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));

		lblNewLabel_13 = new JLabel("Vorname");
		applicationPanel.add(lblNewLabel_13, "cell 0 0,alignx left");

		firstNameTF = new JTextField();
		applicationPanel.add(firstNameTF, "cell 1 0,growx");
		firstNameTF.setColumns(10);

		lblNewLabel_14 = new JLabel("Nachname");
		applicationPanel.add(lblNewLabel_14, "cell 0 1,alignx left");

		lastNameTF = new JTextField();
		applicationPanel.add(lastNameTF, "cell 1 1,growx");
		lastNameTF.setColumns(10);

		JLabel label = new JLabel(" ");
		applicationPanel.add(label, "cell 0 2");

		lblFirstUse = new JLabel("Erster Start");
		applicationPanel.add(lblFirstUse, "cell 0 6,alignx left");

		checkBoxFirstUse = new JCheckBox("");
		applicationPanel.add(checkBoxFirstUse, "cell 1 6");

		lblSchriftgre = new JLabel("Schriftgröße");
		applicationPanel.add(lblSchriftgre, "cell 0 7,alignx left");

		checkBoxFontSize = new JComboBox<Integer>();
		checkBoxFontSize.setFont(font);
		checkBoxFontSize.addItem(15);
		checkBoxFontSize.addItem(16);
		checkBoxFontSize.addItem(17);
		checkBoxFontSize.addItem(18);
		checkBoxFontSize.addItem(19);
		checkBoxFontSize.addItem(20);
		checkBoxFontSize.addItem(21);
		checkBoxFontSize.addItem(22);
		applicationPanel.add(checkBoxFontSize, "cell 1 7,growx");

		checkBoxFontSize.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int fontSize = (int) e.getItem();
					if (applicationProperties != null) {
						applicationProperties.setFontSize(fontSize);
					}
					setNewFont(fontSize);
				}
			}

		});

		JPanel defaultPanel = new JPanel();
		tabbedPane.addTab("default.stackmaster", null, defaultPanel, null);
		defaultPanel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][][][][][][][][]"));

		lblNewLabel = new JLabel("Hoch");
		defaultPanel.add(lblNewLabel, "cell 0 0,alignx left");

		lblNewLabel_1 = new JLabel("schnelle Geschw.");
		defaultPanel.add(lblNewLabel_1, "cell 0 1,alignx left");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_HARDWARE_SPEED),
				new Integer(100)));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(fastUpSpeedTF, "cell 1 1");

		lblNewLabel_2 = new JLabel("mittlere Geschw.");
		lblNewLabel_2.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_2, "cell 0 2,alignx left");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(MAX_HARDWARE_SPEED), new Integer(100)));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(middleUpSpeedTF, "cell 1 2");

		lblNewLabel_3 = new JLabel("langsame Geschw.");
		defaultPanel.add(lblNewLabel_3, "cell 0 3,alignx left");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_HARDWARE_SPEED),
				new Integer(100)));
		slowUpSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(slowUpSpeedTF, "cell 1 3");

		JLabel label_1 = new JLabel(" ");
		defaultPanel.add(label_1, "cell 0 4");

		lblNewLabel_4 = new JLabel("Runter");
		defaultPanel.add(lblNewLabel_4, "cell 0 5,alignx left");

		lblNewLabel_5 = new JLabel("schnelle Geschw.");
		defaultPanel.add(lblNewLabel_5, "cell 0 6,alignx left");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(MAX_HARDWARE_SPEED), new Integer(100)));
		fastDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(fastDownSpeedTF, "cell 1 6");

		lblNewLabel_6 = new JLabel("mittlere Geschw.");
		defaultPanel.add(lblNewLabel_6, "cell 0 7,alignx left");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(
				MAX_HARDWARE_SPEED), new Integer(100)));
		middleDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(middleDownSpeedTF, "cell 1 7");

		lblNewLabel_7 = new JLabel("langsame Geschw.");
		defaultPanel.add(lblNewLabel_7, "cell 0 8,alignx left");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(MAX_HARDWARE_SPEED), new Integer(100)));
		slowDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(slowDownSpeedTF, "cell 1 8");

		label_2 = new JLabel("");
		defaultPanel.add(label_2, "cell 0 9");

		lblNewLabel_8 = new JLabel("Richtung umdrehen");
		defaultPanel.add(lblNewLabel_8, "cell 0 10,alignx left");

		reverseCB = new JCheckBox("");
		defaultPanel.add(reverseCB, "cell 1 10");

		lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		defaultPanel.add(lblNewLabel_9, "cell 0 11,alignx left");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepMovementMirrorTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepMovementMirrorTF, "cell 1 11");

		lblNewLabel_10 = new JLabel("Pause (Spiegel - Bild) [ms]");
		defaultPanel.add(lblNewLabel_10, "cell 0 12,alignx left");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepMirrorPictureTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepMirrorPictureTF, "cell 1 12");

		lblNewLabel_11 = new JLabel("Pause (Bild - Bew.) [ms]");
		defaultPanel.add(lblNewLabel_11, "cell 0 13,alignx left");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepPictureMovementTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepPictureMovementTF, "cell 1 13");

		lblNewLabel_12 = new JLabel("Impulsdauer [ms]");
		defaultPanel.add(lblNewLabel_12, "cell 0 14,alignx left");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		pulseDurationTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(pulseDurationTF, "cell 1 14");

		lblNewLabel_15 = new JLabel("COM-Anschluss");
		defaultPanel.add(lblNewLabel_15, "cell 0 15,alignx left");

		comConnectionNameTF = new JTextField();
		defaultPanel.add(comConnectionNameTF, "cell 1 15,growx");
		comConnectionNameTF.setColumns(10);

		lblSchritteProMillimeter = new JLabel("Schritte pro Millimeter");
		defaultPanel.add(lblSchritteProMillimeter, "cell 0 16,alignx left");

		countOfStepPerMmTF = new JSpinner();
		countOfStepPerMmTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(countOfStepPerMmTF, "cell 1 16");

		lblModus = new JLabel("Modus");
		defaultPanel.add(lblModus, "cell 0 17,alignx left");

		connectionModeCB = new JComboBox<Integer>();
		connectionModeCB.addItem(1);
		connectionModeCB.addItem(2);
		connectionModeCB.addItem(3);
		connectionModeCB.addItem(4);
		connectionModeCB.addItem(5);
		connectionModeCB.addItem(6);
		connectionModeCB.addItem(7);
		connectionModeCB.addItem(8);
		defaultPanel.add(connectionModeCB, "cell 1 17,growx");

		lblTranslate = new JLabel("Motor-Übersetzung");
		defaultPanel.add(lblTranslate, "cell 0 18, alignx left");

		translationTF = new JSpinner();
		translationTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(translationTF, "cell 1 18");

		lblMaxSpeed = new JLabel("Maximale Geschwindigkeit");
		defaultPanel.add(lblMaxSpeed, "cell 0 19, alignx left");

		maxSpeedTF = new JSpinner();
		maxSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(MAX_HARDWARE_SPEED), new Integer(100)));
		maxSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(maxSpeedTF, "cell 1 19");

		lblMaxAcceleration = new JLabel("Maximale Beschleunigung");
		defaultPanel.add(lblMaxAcceleration, "cell 0 20, alignx left");

		maxAccelerationTF = new JSpinner();
		maxAccelerationTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
				new Integer(MAX_HARDWARE_SPEED), new Integer(100)));
		maxAccelerationTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(maxAccelerationTF, "cell 1 20");

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu = new JMenu("Datei");
		menuBar.add(menu);

		mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveProperties();
			}

		});
		menu.add(mntmSpeichern);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleExit();
			}

		});
		menu.add(mntmExit);

		if (!defaultFile.exists() && !applicationPropertyFile.exists()) {

			JOptionPane.showMessageDialog(this,
					"application.properties und default.stackmaster sind nicht im aufgerufenen Verzeichnis vorhanden.",
					"Fehler beim Starten", JOptionPane.OK_OPTION);
			System.exit(0);
		} else {
			if (applicationPropertyFile.exists()) {
				applicationProperties = applicationPropertiesFileHandler.read(applicationPropertyFile);
				setApplicationProperties(applicationProperties);
			} else {
				tabbedPane.setEnabledAt(0, false);
				tabbedPane.setSelectedIndex(1);
				LOGGER.error("No application.properties available!");
			}

			if (defaultFile.exists()) {
				defaultProfile = propertiesHandler.readProfile(defaultFile);
				defaultConnectionProperties = defaultProfile.getProperties();
			} else {
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setSelectedIndex(0);
				LOGGER.error("No default.stackmaster available!");
			}
		}

		setConnectionProperties(defaultConnectionProperties);

	}

	protected void handleExit() {
		LOGGER.info("Exit StackMaster");

		System.exit(0);
	}

	public void setConnectionProperties(ComConnectionProperties connectionProperties) {
		defaultConnectionProperties = connectionProperties;

		if (defaultProfile != null) {
			defaultProfile.setProperties(connectionProperties);
		}
		refreshConnectionProperties();
	}

	public void setApplicationProperties(ApplicationProperties connectionProperties) {
		applicationProperties = connectionProperties;
		refreshApplicationProperties();
	}

	public void changeFont(Component component, int fontSize) {
		Font f = font;
		component.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				changeFont(child, fontSize);
			}
		}
	}

	private void setNewFont(int fontSize) {
		changeFont(this, fontSize);

		Font f = getFont();
		mntmSpeichern.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmExit.setFont(new Font(f.getName(), f.getStyle(), fontSize));
	}

	public void refreshConnectionProperties() {
		comConnectionNameTF.setText(defaultConnectionProperties.getComConnectionName());
		connectionModeCB.setSelectedItem(defaultConnectionProperties.getMicrostepResolutionMode());
		countOfStepPerMmTF.setValue(defaultConnectionProperties.getStepsPerMm());

		fastUpSpeedTF.setValue(defaultConnectionProperties.getFastUpSpeed());
		middleUpSpeedTF.setValue(defaultConnectionProperties.getMiddleUpSpeed());
		slowUpSpeedTF.setValue(defaultConnectionProperties.getSlowUpSpeed());
		fastDownSpeedTF.setValue(defaultConnectionProperties.getFastDownSpeed());
		middleDownSpeedTF.setValue(defaultConnectionProperties.getMiddleDownSpeed());
		slowDownSpeedTF.setValue(defaultConnectionProperties.getSlowDownSpeed());
		reverseCB.setSelected(defaultConnectionProperties.isReverseSteps());
		sleepMirrorPictureTF.setValue(defaultConnectionProperties.getSleepMirrorPicture());
		sleepMovementMirrorTF.setValue(defaultConnectionProperties.getSleepMovementMirror());
		sleepPictureMovementTF.setValue(defaultConnectionProperties.getSleepPictureMovement());
		pulseDurationTF.setValue(defaultConnectionProperties.getPulseDuration());
		translationTF.setValue(defaultConnectionProperties.getTranslation());
		maxSpeedTF.setValue(defaultConnectionProperties.getMaxSpeed());
	}

	public void refreshApplicationProperties() {
		firstNameTF.setText(applicationProperties.getFirstName());
		lastNameTF.setText(applicationProperties.getLastName());
		checkBoxFirstUse.setSelected(applicationProperties.isFirstUse());

		int fontSize = applicationProperties.getFontSize();
		checkBoxFontSize.setSelectedItem(fontSize);
		setNewFont(fontSize);
	}

	protected void handleSaveProperties() {
		LOGGER.info("chosen file: " + defaultFile.getAbsolutePath());

		boolean allValid = validateUserSettings();
		LOGGER.info("try to save properties");
		LOGGER.info("properties are valid: " + allValid);
		if (allValid) {
			ComConnectionProperties newConnectionProperties = getNewConnectionProperties();
			setConnectionProperties(newConnectionProperties);
			propertiesHandler.writeProfile(defaultFile, defaultProfile);

			ApplicationProperties newApplicationProperties = getNewApplicationProperties();
			setApplicationProperties(newApplicationProperties);
			applicationPropertiesFileHandler.write(applicationPropertyFile, applicationProperties);
		}
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.error("SystemLookAndFeel not available", e);
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SwingStarter frame = new SwingStarter();
					frame.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Error in main-methode by creating SwingStarter object", e);
				}
			}
		});
	}

	public ApplicationProperties getNewApplicationProperties() {
		ApplicationProperties comConnectionProperties = new ApplicationProperties();

		String firstName = firstNameTF.getText();
		comConnectionProperties.setFirstName(firstName);

		String lastName = lastNameTF.getText();
		comConnectionProperties.setLastName(lastName);

		boolean firstUse = checkBoxFirstUse.isSelected();
		comConnectionProperties.setFirstUse(firstUse);

		int fontSize = (int) checkBoxFontSize.getSelectedItem();
		comConnectionProperties.setFontSize(fontSize);

		return comConnectionProperties;
	}

	public ComConnectionProperties getNewConnectionProperties() {
		ComConnectionProperties comConnectionProperties = new ComConnectionProperties();

		String comConnectionName = comConnectionNameTF.getText();
		comConnectionProperties.setComConnectionName(comConnectionName);

		int comConnectionMode = (int) connectionModeCB.getSelectedItem();
		comConnectionProperties.setMicrostepResolutionMode(comConnectionMode);

		int countOfStepsPerMm = (int) countOfStepPerMmTF.getValue();
		comConnectionProperties.setStepsPerMm(countOfStepsPerMm);

		double translation = (double) translationTF.getValue();
		comConnectionProperties.setTranslation(translation);

		int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		comConnectionProperties.setFastUpSpeed(fastUpSpeed);

		int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);

		int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		comConnectionProperties.setSlowUpSpeed(slowUpSpeed);

		int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		comConnectionProperties.setSlowDownSpeed(slowDownSpeed);

		int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);

		int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		comConnectionProperties.setFastDownSpeed(fastDownSpeed);

		long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);

		long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);

		long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);

		long pulseDuration = (long) pulseDurationTF.getValue();
		comConnectionProperties.setPulseDuration(pulseDuration);

		boolean reverseSteps = reverseCB.isSelected();
		comConnectionProperties.setReverseSteps(reverseSteps);

		int maxSpeed = (int) maxSpeedTF.getValue();
		comConnectionProperties.setMaxSpeed(maxSpeed);

		int maxAcceleration = (int) maxAccelerationTF.getValue();
		comConnectionProperties.setMaxAcceleration(maxAcceleration);

		return comConnectionProperties;

	}

	protected boolean validateUserSettings() {
		boolean allValid = true;

		int connectionMode = (int) connectionModeCB.getSelectedItem();
		allValid = validateMode(connectionMode, connectionModeCB);

		int countOfStepPerMm = (int) countOfStepPerMmTF.getValue();
		allValid = validateStepCount(countOfStepPerMm, countOfStepPerMmTF);

		//		double translation = (double) translationTF.getValue();
		//		allValid = validateTranslation(translation, tanslationTF);

		int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		allValid = validateSpeed(fastUpSpeed, fastUpSpeedTF);

		int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		allValid = validateSpeed(middleUpSpeed, middleUpSpeedTF);

		int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		allValid = validateSpeed(slowUpSpeed, slowUpSpeedTF);

		int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		allValid = validateSpeed(slowDownSpeed, slowDownSpeedTF);

		int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		allValid = validateSpeed(middleDownSpeed, middleDownSpeedTF);

		int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		allValid = validateSpeed(fastDownSpeed, fastDownSpeedTF);

		long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		allValid = validateSleep(sleepMovementMirror, sleepMovementMirrorTF);

		long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		allValid = validateSleep(sleepMirrorPicture, sleepMirrorPictureTF);

		long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		allValid = validateSleep(sleepPictureMovement, sleepPictureMovementTF);

		long pulseDuration = (long) pulseDurationTF.getValue();
		allValid = validateSleep(pulseDuration, pulseDurationTF);

		return allValid;

	}

	protected boolean validateSpeed(int speed, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidSpeed(speed)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateSleep(long sleep, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidSleep(sleep)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateMode(int mode, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidMicrostepResolutionMode(mode)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateStepCount(int stepCount, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidStepcount(stepCount)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateTranslation(double translation, JComponent component) {
		boolean allValid = false;
		//if() {
		//component.setBackground(Color.red);
		//LOGGER.error("error by parsing " + component.getName());
		//} else {
		//allValid = true;
		//component.setBackground(Color.white);
		//}
		return allValid;
	}

}
