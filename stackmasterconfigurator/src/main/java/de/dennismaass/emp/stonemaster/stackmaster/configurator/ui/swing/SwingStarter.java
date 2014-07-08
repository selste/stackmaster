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
import java.util.Locale;

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

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationPropertiesFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.PropertiesValidator;

public class SwingStarter extends JFrame {
	private final static long serialVersionUID = -6032729218147398086L;

	private static final int MAX_SPEED = 2047;

	private static Logger LOGGER = Logger.getLogger(SwingStarter.class);

	private static String OSVERSION_PROPERTY_NAME = "os.version";
	private static String OSARCH_PROPERTY_NAME = "os.arch";
	private static String OSNAME_PROPERTY_NAME = "os.name";

	private static String TITLE = "StackMaster Configurator";

	private static String FONT_NAME = "Arial";
	public static Font FONT = new Font(FONT_NAME, Font.PLAIN, 20);

	private static String DEFAULT_PROFILE_NAME = "default.stackmaster";

	private Profile defaultProfile;
	private File defaultFile = new File(DEFAULT_PROFILE_NAME);

	private ComConnectionProperties defaultConnectionProperties;

	private ProfileFileHandler propertiesHandler;

	private JTextField firstNameTF;
	private JTextField lastNameTF;

	private JMenuItem mntmSpeichern;

	public static String OS_NAME = System.getProperty(OSNAME_PROPERTY_NAME).toLowerCase(Locale.US);
	public static String OS_ARCH = System.getProperty(OSARCH_PROPERTY_NAME).toLowerCase(Locale.US);
	public static String OS_VERSION = System.getProperty(OSVERSION_PROPERTY_NAME).toLowerCase(Locale.US);

	private JTextField comConnectionNameTF;

	private JSpinner fastUpSpeedTF;
	private JSpinner middleUpSpeedTF;
	private JSpinner slowUpSpeedTF;
	private JSpinner fastDownSpeedTF;
	private JSpinner middleDownSpeedTF;
	private JSpinner slowDownSpeedTF;
	private JSpinner countOfStepPerMmTF;
	private JComboBox<Integer> connectionModeCB;
	private JCheckBox reverseCB;
	private JSpinner sleepMirrorPictureTF;
	private JSpinner sleepMovementMirrorTF;
	private JSpinner sleepPictureMovementTF;
	private JSpinner pulseDurationTF;

	private PropertiesValidator validator = new PropertiesValidator();

	private static ApplicationProperties applicationProperties;
	private File applicationPropertyFile = new File("application.properties");
	private ApplicationPropertiesFileHandler applicationPropertiesFileHandler = new ApplicationPropertiesFileHandler();;

	private JCheckBox checkBoxFirstUse;

	private JComboBox<Integer> checkBoxFontSize;

	private JMenuItem mntmExit;

	private JLabel lblModus;

	private JLabel lblNewLabel_15;

	private JLabel lblFirstUse;

	private JLabel lblSchriftgre;

	private JLabel lblNewLabel;

	private JLabel lblNewLabel_1;

	private JLabel lblNewLabel_2;

	private JLabel lblNewLabel_3;

	private JLabel lblNewLabel_4;

	private JLabel lblNewLabel_6;

	private JLabel lblNewLabel_7;

	private JLabel lblNewLabel_8;

	private JLabel lblNewLabel_9;

	private JLabel lblNewLabel_10;

	private JLabel lblNewLabel_11;

	private JLabel lblNewLabel_12;

	private JMenu menu;

	private JLabel lblNewLabel_14;

	private JLabel lblSchritteProMillimeter;

	private JLabel lblNewLabel_13;

	private JLabel lblNewLabel_5;

	private JTabbedPane tabbedPane;
	private JLabel label_2;

	public SwingStarter() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setTitle(TITLE);
		propertiesHandler = new ProfileFileHandler();

		setSize(430, 694);
		getContentPane().setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		JPanel applicationPanel = new JPanel();
		tabbedPane.addTab("application.properties", null, applicationPanel, null);
		applicationPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));

		lblNewLabel_13 = new JLabel("Vorname");
		lblNewLabel_13.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		applicationPanel.add(lblNewLabel_13, "cell 0 0,alignx left");

		firstNameTF = new JTextField();
		firstNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		applicationPanel.add(firstNameTF, "cell 1 0,growx");
		firstNameTF.setColumns(10);

		lblNewLabel_14 = new JLabel("Nachname");
		lblNewLabel_14.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		applicationPanel.add(lblNewLabel_14, "cell 0 1,alignx left");

		lastNameTF = new JTextField();
		lastNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		applicationPanel.add(lastNameTF, "cell 1 1,growx");
		lastNameTF.setColumns(10);

		JLabel label = new JLabel(" ");
		applicationPanel.add(label, "cell 0 2");

		lblFirstUse = new JLabel("Erster Start");
		lblFirstUse.setFont(new Font("Arial", Font.PLAIN, 20));
		applicationPanel.add(lblFirstUse, "cell 0 6,alignx left");

		checkBoxFirstUse = new JCheckBox("");
		applicationPanel.add(checkBoxFirstUse, "cell 1 6");

		lblSchriftgre = new JLabel("Schriftgröße");
		lblSchriftgre.setFont(new Font("Arial", Font.PLAIN, 20));
		applicationPanel.add(lblSchriftgre, "cell 0 7,alignx left");

		checkBoxFontSize = new JComboBox<Integer>();
		checkBoxFontSize.setFont(FONT);
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
		defaultPanel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][][][][][][][]"));

		lblNewLabel = new JLabel("Hoch");
		lblNewLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel, "cell 0 0,alignx left");

		lblNewLabel_1 = new JLabel("schnelle Geschw.");
		lblNewLabel_1.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_1, "cell 0 1,alignx left");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		fastUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(fastUpSpeedTF, "cell 1 1");

		lblNewLabel_2 = new JLabel("mittlere Geschw.");
		lblNewLabel_2.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_2, "cell 0 2,alignx left");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		middleUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(middleUpSpeedTF, "cell 1 2");

		lblNewLabel_3 = new JLabel("langsame Geschw.");
		lblNewLabel_3.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_3, "cell 0 3,alignx left");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		slowUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		slowUpSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(slowUpSpeedTF, "cell 1 3");

		JLabel label_1 = new JLabel(" ");
		defaultPanel.add(label_1, "cell 0 4");

		lblNewLabel_4 = new JLabel("Runter");
		lblNewLabel_4.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_4, "cell 0 5,alignx left");

		lblNewLabel_5 = new JLabel("schnelle Geschw.");
		lblNewLabel_5.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_5, "cell 0 6,alignx left");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		fastDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		fastDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(fastDownSpeedTF, "cell 1 6");

		lblNewLabel_6 = new JLabel("mittlere Geschw.");
		lblNewLabel_6.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_6, "cell 0 7,alignx left");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		middleDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		middleDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(middleDownSpeedTF, "cell 1 7");

		lblNewLabel_7 = new JLabel("langsame Geschw.");
		lblNewLabel_7.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_7, "cell 0 8,alignx left");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(MAX_SPEED),
				new Integer(100)));
		slowDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		slowDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(slowDownSpeedTF, "cell 1 8");

		label_2 = new JLabel("");
		defaultPanel.add(label_2, "cell 0 9");

		lblNewLabel_8 = new JLabel("Richtung umdrehen");
		lblNewLabel_8.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_8, "cell 0 10,alignx left");

		reverseCB = new JCheckBox("");
		defaultPanel.add(reverseCB, "cell 1 10");

		lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		lblNewLabel_9.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_9, "cell 0 11,alignx left");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepMovementMirrorTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepMovementMirrorTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepMovementMirrorTF, "cell 1 11");

		lblNewLabel_10 = new JLabel("Pause (Spiegel - Bild) [ms]");
		lblNewLabel_10.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_10, "cell 0 12,alignx left");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepMirrorPictureTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepMirrorPictureTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepMirrorPictureTF, "cell 1 12");

		lblNewLabel_11 = new JLabel("Pause (Bild - Bew.) [ms]");
		lblNewLabel_11.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_11, "cell 0 13,alignx left");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		sleepPictureMovementTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepPictureMovementTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(sleepPictureMovementTF, "cell 1 13");

		lblNewLabel_12 = new JLabel("Impulsdauer [ms]");
		lblNewLabel_12.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_12, "cell 0 14,alignx left");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setModel(new SpinnerNumberModel(new Long(1), new Long(1), null, new Long(500)));
		pulseDurationTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		pulseDurationTF.setPreferredSize(new Dimension(100, 22));
		defaultPanel.add(pulseDurationTF, "cell 1 14");

		lblNewLabel_15 = new JLabel("COM-Anschluss");
		lblNewLabel_15.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblNewLabel_15, "cell 0 15,alignx left");

		comConnectionNameTF = new JTextField();
		comConnectionNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(comConnectionNameTF, "cell 1 15,growx");
		comConnectionNameTF.setColumns(10);

		lblSchritteProMillimeter = new JLabel("Schritte pro Millimeter");
		lblSchritteProMillimeter.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(lblSchritteProMillimeter, "cell 0 16,alignx left");

		countOfStepPerMmTF = new JSpinner();
		countOfStepPerMmTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		countOfStepPerMmTF.setMinimumSize(new Dimension(100, 22));
		defaultPanel.add(countOfStepPerMmTF, "cell 1 16");

		lblModus = new JLabel("Modus");
		lblModus.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
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
		connectionModeCB.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		defaultPanel.add(connectionModeCB, "cell 1 17,growx");

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu = new JMenu("Datei");
		menu.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		menuBar.add(menu);

		mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSpeichern.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		mntmSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveProperties();
			}

		});
		menu.add(mntmSpeichern);

		mntmExit = new JMenuItem("Exit");
		mntmExit.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
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
				if (defaultProfile != null) {
					defaultConnectionProperties = defaultProfile.getProperties();
				}
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
		Font f = FONT;
		component.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				changeFont(child, fontSize);
			}
		}
	}

	private void setNewFont(int fontSize) {
		changeFont(this, fontSize);
		// firstNameTF.setFont(font);
		// lastNameTF.setFont(font);
		// checkBoxFirstUse.setFont(font);
		//
		// comConnectionNameTF.setFont(font);
		// connectionModeCB.setFont(font);
		//
		// JSpinner.NumberEditor countOfStepPerMm = (JSpinner.NumberEditor) countOfStepPerMmTF.getEditor();
		// countOfStepPerMm.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor fastUpSpeed = (JSpinner.NumberEditor) fastUpSpeedTF.getEditor();
		// fastUpSpeed.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor middleUpSpeed = (JSpinner.NumberEditor) middleUpSpeedTF.getEditor();
		// middleUpSpeed.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor slowUpSpeed = (JSpinner.NumberEditor) slowUpSpeedTF.getEditor();
		// slowUpSpeed.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor fastDownSpeed = (JSpinner.NumberEditor) fastDownSpeedTF.getEditor();
		// fastDownSpeed.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor middleDownSpeed = (JSpinner.NumberEditor) middleDownSpeedTF.getEditor();
		// middleDownSpeed.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor slowDownSpeed = (JSpinner.NumberEditor) slowDownSpeedTF.getEditor();
		// slowDownSpeed.getTextField().setFont(font);
		//
		// reverseCB.setFont(font);
		//
		// JSpinner.NumberEditor sleepMirrorPicture = (JSpinner.NumberEditor) sleepMirrorPictureTF.getEditor();
		// sleepMirrorPicture.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor sleepMovementMirror = (JSpinner.NumberEditor) sleepMovementMirrorTF.getEditor();
		// sleepMovementMirror.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor sleepPictureMovement = (JSpinner.NumberEditor) sleepPictureMovementTF.getEditor();
		// sleepPictureMovement.getTextField().setFont(font);
		//
		// JSpinner.NumberEditor pulseDuration = (JSpinner.NumberEditor) pulseDurationTF.getEditor();
		// pulseDuration.getTextField().setFont(font);
		//
		Font f = getFont();
		mntmSpeichern.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmExit.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		// checkBoxFontSize.setFont(font);
		// menu.setFont(font);
		// lblModus.setFont(font);
		// lblNewLabel_15.setFont(font);
		// lblFirstUse.setFont(font);
		// lblSchriftgre.setFont(font);
		// lblNewLabel.setFont(font);
		// lblNewLabel_1.setFont(font);
		// lblNewLabel_2.setFont(font);
		// lblNewLabel_3.setFont(font);
		// lblNewLabel_4.setFont(font);
		// lblNewLabel_5.setFont(font);
		// lblNewLabel_6.setFont(font);
		// lblNewLabel_7.setFont(font);
		// lblNewLabel_8.setFont(font);
		// lblNewLabel_9.setFont(font);
		// lblNewLabel_10.setFont(font);
		// lblNewLabel_11.setFont(font);
		// lblNewLabel_12.setFont(font);
		// lblNewLabel_14.setFont(font);
		// lblSchritteProMillimeter.setFont(font);
		// lblNewLabel_13.setFont(font);
		//
		// tabbedPane.setFont(font);
	}

	public void refreshConnectionProperties() {
		if (defaultConnectionProperties != null) {
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
		}
	}

	public void refreshApplicationProperties() {

		if (applicationProperties != null) {
			firstNameTF.setText(applicationProperties.getFirstName());
			lastNameTF.setText(applicationProperties.getLastName());
			checkBoxFirstUse.setSelected(applicationProperties.isFirstUse());

			int fontSize = applicationProperties.getFontSize();
			checkBoxFontSize.setSelectedItem(fontSize);
			setNewFont(fontSize);
		}
	}

	protected void handleSaveProperties() {
		LOGGER.info("chosen file: " + defaultFile.getAbsolutePath());

		boolean allValid = validateUserSettings();
		LOGGER.info("try to save properties");
		LOGGER.info("properties are valid: " + allValid);
		if (allValid) {

			if (defaultProfile != null) {
				ComConnectionProperties newConnectionProperties = getNewConnectionProperties();
				setConnectionProperties(newConnectionProperties);
				propertiesHandler.writeProfile(defaultFile, defaultProfile);
			}

			if (applicationProperties != null) {
				ApplicationProperties newApplicationProperties = getNewApplicationProperties();
				setApplicationProperties(newApplicationProperties);
				applicationPropertiesFileHandler.write(applicationPropertyFile, applicationProperties);
			}

		}
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.error("SystemLookAndFeel not available", e);
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SwingStarter frame = new SwingStarter();
					frame.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Error in main-methode by creating SwingStarter object", e);
					e.printStackTrace();
				}
			}
		});
	}

	public ApplicationProperties getNewApplicationProperties() {
		ApplicationProperties comConnectionProperties = new ApplicationProperties();

		String firstName = firstNameTF.getText();
		comConnectionProperties.setFirstName(firstName);

		System.out.println(firstName);

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

		return comConnectionProperties;

	}

	protected boolean validateUserSettings() {
		boolean allValid = true;

		int connectionMode = (int) connectionModeCB.getSelectedItem();
		allValid = validateMode(connectionMode, connectionModeCB);

		int countOfStepPerMm = (int) countOfStepPerMmTF.getValue();
		allValid = validateStepCount(countOfStepPerMm, countOfStepPerMmTF);

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

}
