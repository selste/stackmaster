package de.dennismaass.emp.stonemaster.stackmaster.configurator.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesValidator;

public class SwingStarter extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(SwingStarter.class);

	private static final String OSVERSION_PROPERTY_NAME = "os.version";
	private static final String OSARCH_PROPERTY_NAME = "os.arch";
	private static final String OSNAME_PROPERTY_NAME = "os.name";

	private static final String TITLE = "StackMaster";

	private static final String FONT_NAME = "Arial";
	public static final Font FONT = new Font(FONT_NAME, Font.PLAIN, 20);
	private static final String FILE_ENDING = ".stackmaster";
	private static final String DEFAULT_BACKUP_PROFILE_NAME = "default.stackmaster.backup";
	private static final String DEFAULT_PROFILE_NAME = "default.stackmaster";
	private final Profile defaultBackupProfile;
	private final File defaultFileBackup = new File(DEFAULT_BACKUP_PROFILE_NAME);

	private final Profile defaultProfile;
	private final File defaultFile = new File(DEFAULT_PROFILE_NAME);

	private final File actualOpenedProfileFileName = defaultFile;

	private final ComConnectionProperties defaultConnectionProperties;
	private ComConnectionProperties actualConnectionProperties;

	private final ProfileFileHandler propertiesHandler;

	private final JTextField firstNameTF;
	private final JTextField lastNameTF;

	private final JMenuItem mntmProfilSpeichern;
	// private final JMenuItem mntmProfilSpeichernAls;

	public static final String OS_NAME = System.getProperty(OSNAME_PROPERTY_NAME).toLowerCase(Locale.US);
	public static final String OS_ARCH = System.getProperty(OSARCH_PROPERTY_NAME).toLowerCase(Locale.US);
	public static final String OS_VERSION = System.getProperty(OSVERSION_PROPERTY_NAME).toLowerCase(Locale.US);

	private final JTextField comConnectionNameTF;

	private final JSpinner fastUpSpeedTF;
	private final JSpinner middleUpSpeedTF;
	private final JSpinner slowUpSpeedTF;
	private final JSpinner fastDownSpeedTF;
	private final JSpinner middleDownSpeedTF;
	private final JSpinner slowDownSpeedTF;
	private final JSpinner countOfStepPerMmTF;
	private final JComboBox<Integer> connectionModeCB;
	private final JCheckBox reverseCB;
	private final JSpinner sleepMirrorPictureTF;
	private final JSpinner sleepMovementMirrorTF;
	private final JSpinner sleepPictureMovementTF;
	private final JSpinner pulseDurationTF;

	private final PropertiesValidator validator = new PropertiesValidator();

	public SwingStarter() {
		propertiesHandler = new ProfileFileHandler();

		defaultProfile = propertiesHandler.readProfile(defaultFile);
		defaultBackupProfile = propertiesHandler.readProfile(defaultFileBackup);

		actualConnectionProperties = defaultProfile.getProperties();
		defaultConnectionProperties = defaultBackupProfile.getProperties();

		setSize(430, 420);
		getContentPane().setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		final JPanel generalPanel = new JPanel();
		tabbedPane.addTab("Allgemein", null, generalPanel, null);
		generalPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		final JLabel lblNewLabel_13 = new JLabel("Vorname");
		lblNewLabel_13.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_13, "cell 0 0,alignx left");

		firstNameTF = new JTextField();
		firstNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(firstNameTF, "cell 1 0,growx");
		firstNameTF.setColumns(10);

		final JLabel lblNewLabel_14 = new JLabel("Nachname");
		lblNewLabel_14.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_14, "cell 0 1,alignx left");

		lastNameTF = new JTextField();
		lastNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lastNameTF, "cell 1 1,growx");
		lastNameTF.setColumns(10);

		final JLabel label = new JLabel(" ");
		generalPanel.add(label, "cell 0 2");

		final JLabel lblSchritteProMillimeter = new JLabel("Schritte pro Millimeter");
		lblSchritteProMillimeter.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lblSchritteProMillimeter, "cell 0 3,alignx left");

		countOfStepPerMmTF = new JSpinner();
		countOfStepPerMmTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		countOfStepPerMmTF.setMinimumSize(new Dimension(100, 22));
		generalPanel.add(countOfStepPerMmTF, "cell 1 3");

		final JLabel lblModus = new JLabel("Modus");
		lblModus.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lblModus, "cell 0 4,alignx left");

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
		generalPanel.add(connectionModeCB, "cell 1 4,growx");

		final JLabel lblNewLabel_15 = new JLabel("COM-Anschluss");
		lblNewLabel_15.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_15, "cell 0 5,alignx left");

		comConnectionNameTF = new JTextField();
		comConnectionNameTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		generalPanel.add(comConnectionNameTF, "cell 1 5,growx");
		comConnectionNameTF.setColumns(10);

		final JPanel relativPanel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, relativPanel, null);
		relativPanel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][]"));

		final JLabel lblNewLabel = new JLabel("Hoch");
		lblNewLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel, "cell 0 0");

		final JLabel lblNewLabel_1 = new JLabel("schnelle Geschw.");
		lblNewLabel_1.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_1, "cell 0 1");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		relativPanel.add(fastUpSpeedTF, "cell 1 1");

		final JLabel lblNewLabel_2 = new JLabel("mittlere Geschw.");
		lblNewLabel_2.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_2, "cell 0 2");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		relativPanel.add(middleUpSpeedTF, "cell 1 2");

		final JLabel lblNewLabel_3 = new JLabel("langsame Geschw.");
		lblNewLabel_3.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_3, "cell 0 3");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		slowUpSpeedTF.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(slowUpSpeedTF, "cell 1 3");

		final JLabel label_1 = new JLabel(" ");
		relativPanel.add(label_1, "cell 0 4");

		final JLabel lblNewLabel_4 = new JLabel("Runter");
		lblNewLabel_4.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_4, "cell 0 5");

		final JLabel lblNewLabel_5 = new JLabel("schnelle Geschw.");
		lblNewLabel_5.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_5, "cell 0 6");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		fastDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(fastDownSpeedTF, "cell 1 6");

		final JLabel lblNewLabel_6 = new JLabel("mittlere Geschw.");
		lblNewLabel_6.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_6, "cell 0 7");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		middleDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(middleDownSpeedTF, "cell 1 7");

		final JLabel lblNewLabel_7 = new JLabel("langsame Geschw.");
		lblNewLabel_7.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_7, "cell 0 8");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		slowDownSpeedTF.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(slowDownSpeedTF, "cell 1 8");

		final JPanel stepPanel = new JPanel();
		tabbedPane.addTab("Step-Panel", null, stepPanel, null);
		stepPanel.setLayout(new MigLayout("", "[][]", "[][][][][]"));

		final JLabel lblNewLabel_8 = new JLabel("Richtung umdrehen");
		lblNewLabel_8.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_8, "cell 0 0");

		reverseCB = new JCheckBox("");
		stepPanel.add(reverseCB, "cell 1 0");

		final JLabel lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		lblNewLabel_9.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_9, "cell 0 1");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepMovementMirrorTF.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(sleepMovementMirrorTF, "cell 1 1");

		final JLabel lblNewLabel_10 = new JLabel("Pause (Spiegel - Bild) [ms]");
		lblNewLabel_10.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_10, "cell 0 2");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepMirrorPictureTF.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(sleepMirrorPictureTF, "cell 1 2");

		final JLabel lblNewLabel_11 = new JLabel("Pause (Bild - Bew.) [ms]");
		lblNewLabel_11.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_11, "cell 0 3");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		sleepPictureMovementTF.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(sleepPictureMovementTF, "cell 1 3");

		final JLabel lblNewLabel_12 = new JLabel("Impulsdauer [ms]");
		lblNewLabel_12.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_12, "cell 0 4");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		pulseDurationTF.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(pulseDurationTF, "cell 1 4");

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu menu = new JMenu("Datei");
		menu.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		menuBar.add(menu);

		// final JMenuItem mntmLaden = new JMenuItem("Profil laden");
		// mntmLaden.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		// mntmLaden.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(final ActionEvent e) {
		// handleLoadProperties();
		// }
		//
		// });
		// menu.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSaveProperties();
			}

		});
		// mntmProfilSpeichern.setEnabled(false);
		menu.add(mntmProfilSpeichern);

		// mntmProfilSpeichernAls = new JMenuItem("Profil speichern unter...");
		// mntmProfilSpeichernAls.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(final ActionEvent e) {
		// handleSaveAsProperties();
		// }<S
		//
		// });
		// mntmProfilSpeichernAls.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		// menu.add(mntmProfilSpeichernAls);

		final JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleExit();
			}

		});
		menu.add(mntmExit);

		setConnectionProperties(actualConnectionProperties);
	}

	protected void handleExit() {
		LOGGER.info("Exit StackMaster");

		System.exit(0);
	}

	public void setConnectionProperties(final ComConnectionProperties connectionProperties) {
		actualConnectionProperties = connectionProperties;
		refresh();
	}

	private void cancel() {
		firstNameTF.setText(actualConnectionProperties.getFirstName());
		lastNameTF.setText(actualConnectionProperties.getLastName());
		comConnectionNameTF.setText(actualConnectionProperties.getComConnectionName());
		connectionModeCB.setSelectedItem(actualConnectionProperties.getMicrostepResolutionMode());
		countOfStepPerMmTF.setValue(actualConnectionProperties.getStepsPerMm());

		fastUpSpeedTF.setValue(actualConnectionProperties.getFastUpSpeed());
		middleUpSpeedTF.setValue(actualConnectionProperties.getMiddleUpSpeed());
		slowUpSpeedTF.setValue(actualConnectionProperties.getSlowUpSpeed());
		fastDownSpeedTF.setValue(actualConnectionProperties.getFastDownSpeed());
		middleDownSpeedTF.setValue(actualConnectionProperties.getMiddleDownSpeed());
		slowDownSpeedTF.setValue(actualConnectionProperties.getSlowDownSpeed());
		reverseCB.setSelected(actualConnectionProperties.isReverseSteps());
		sleepMirrorPictureTF.setValue(actualConnectionProperties.getSleepMirrorPicture());
		sleepMovementMirrorTF.setValue(actualConnectionProperties.getSleepMovementMirror());
		sleepPictureMovementTF.setValue(actualConnectionProperties.getSleepPictureMovement());
		pulseDurationTF.setValue(actualConnectionProperties.getPulseDuration());
	}

	public void refresh() {
		cancel();
	}

	protected void handleSaveProperties() {
		LOGGER.info("chosen file: " + actualOpenedProfileFileName.getAbsolutePath());

		final boolean allValid = validateUserSettings();
		LOGGER.info("try to save properties");
		LOGGER.info("properties are valid: " + allValid);
		if (allValid) {
			final ComConnectionProperties newConnectionProperties = getNewConnectionProperties();
			setConnectionProperties(newConnectionProperties);

			propertiesHandler.writeProfile(actualOpenedProfileFileName, defaultProfile);
		}
	}

	// protected void handleLoadProperties() {
	// final JFileChooser chooser = new PropertiesFileChooser();
	//
	// final int returnVal = chooser.showOpenDialog(null);
	// if (returnVal == JFileChooser.APPROVE_OPTION) {
	// final File selectedFile = chooser.getSelectedFile();
	// if (selectedFile != null) {
	// LOGGER.info("chosen file: " + selectedFile);
	// actualOpenedProfileFileName = selectedFile;
	// final Profile loadedConnectionProperties = propertiesHandler.readProfile(actualOpenedProfileFileName);
	// setConnectionProperties(loadedConnectionProperties.getProperties());
	// setTitle(TITLE + " - " + selectedFile.getAbsolutePath());
	// mntmProfilSpeichern.setEnabled(true);
	// }
	// }
	// }

	// protected void handleSaveAsProperties() {
	// final JFileChooser chooser = new PropertiesFileChooser();
	//
	// final int returnVal = chooser.showSaveDialog(null);
	// if (returnVal == JFileChooser.APPROVE_OPTION) {
	// File selectedFile = chooser.getSelectedFile();
	//
	// if (selectedFile != null) {
	// String absolutePath = selectedFile.getAbsolutePath();
	//
	// if (!absolutePath.endsWith(FILE_ENDING)) {
	// absolutePath = absolutePath + FILE_ENDING;
	// selectedFile = new File(absolutePath);
	// }
	// LOGGER.info("chosen file: " + absolutePath);
	// actualOpenedProfileFileName = selectedFile;
	// propertiesHandler.writeProfile(actualOpenedProfileFileName, defaultProfile);
	// setTitle(TITLE + " - " + absolutePath);
	// mntmProfilSpeichern.setEnabled(true);
	// }
	// }
	// }

	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {
			LOGGER.error("SystemLookAndFeel not available", e);
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final SwingStarter frame = new SwingStarter();
					frame.setVisible(true);
				} catch (final Exception e) {
					LOGGER.error("Error in main-methode by creating SwingStarter object", e);
					e.printStackTrace();
				}
			}
		});
	}

	public ComConnectionProperties getNewConnectionProperties() {
		final ComConnectionProperties comConnectionProperties = new ComConnectionProperties();

		final int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		comConnectionProperties.setFastUpSpeed(fastUpSpeed);

		final int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);

		final int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		comConnectionProperties.setSlowUpSpeed(slowUpSpeed);

		final int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		comConnectionProperties.setSlowDownSpeed(slowDownSpeed);

		final int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);

		final int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		comConnectionProperties.setFastDownSpeed(fastDownSpeed);

		final long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);

		final long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);

		final long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);

		final long pulseDuration = (long) pulseDurationTF.getValue();
		comConnectionProperties.setPulseDuration(pulseDuration);

		final boolean reverseSteps = reverseCB.isSelected();
		comConnectionProperties.setReverseSteps(reverseSteps);

		return comConnectionProperties;

	}

	protected boolean validateUserSettings() {
		boolean allValid = true;

		final int fastUpSpeed = (int) fastUpSpeedTF.getValue();

		if (!validator.isValidSpeed(fastUpSpeed)) {
			allValid = false;
			fastUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastUpSpeed from Spinner");
		} else {
			fastUpSpeedTF.setBackground(Color.white);
		}

		final int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		if (!validator.isValidSpeed(middleUpSpeed)) {
			allValid = false;
			middleUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleUpSpeed from Spinner");
		} else {
			middleUpSpeedTF.setBackground(Color.white);
		}

		final int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		if (!validator.isValidSpeed(slowUpSpeed)) {
			allValid = false;
			slowUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowUpSpeed from Spinner");
		} else {
			slowUpSpeedTF.setBackground(Color.white);
		}

		final int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		if (!validator.isValidSpeed(slowDownSpeed)) {
			allValid = false;
			slowDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowDownSpeed from Spinner");
		} else {
			slowDownSpeedTF.setBackground(Color.white);
		}

		final int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		if (!validator.isValidSpeed(middleDownSpeed)) {
			allValid = false;
			middleDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleDownSpeed from Spinner");
		} else {
			middleDownSpeedTF.setBackground(Color.white);
		}

		final int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		if (!validator.isValidSpeed(fastDownSpeed)) {
			allValid = false;
			fastDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastDownSpeed from Spinner");
		} else {
			fastDownSpeedTF.setBackground(Color.white);
		}

		final long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		if (!validator.isValidSleep(sleepMovementMirror)) {
			allValid = false;
			sleepMovementMirrorTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMovementMirror Spinner");
		} else {
			sleepMovementMirrorTF.setBackground(Color.white);
		}

		final long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		if (!validator.isValidSleep(sleepMirrorPicture)) {
			allValid = false;
			sleepMirrorPictureTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMirrorPicture from Spinner");
		} else {
			sleepMirrorPictureTF.setBackground(Color.white);
		}

		final long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		if (!validator.isValidSleep(sleepPictureMovement)) {
			allValid = false;
			sleepPictureMovementTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepPictureMovement from Spinner");
		} else {
			sleepPictureMovementTF.setBackground(Color.white);
		}

		final long pulseDuration = (long) pulseDurationTF.getValue();
		if (!validator.isValidSleep(pulseDuration)) {
			allValid = false;
			pulseDurationTF.setBackground(Color.red);
			LOGGER.error("error by parsing pulseDuration from Spinner");
		} else {
			pulseDurationTF.setBackground(Color.white);
		}

		return allValid;

	}

}
