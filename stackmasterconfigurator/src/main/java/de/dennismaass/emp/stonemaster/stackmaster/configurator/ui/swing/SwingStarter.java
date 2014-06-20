package de.dennismaass.emp.stonemaster.stackmaster.configurator.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionProperties;

public class SwingStarter extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(SwingStarter.class);

	private final Profile profile;
	private final Profile defaultProfile;

	private final File defaultFileBackup = new File("default.stackmaster.backup");
	private final File defaultFile = new File("default.stackmaster");
	private File actualOpenedProfileFileName = defaultFile;
	private final ProfileFileHandler propertiesHandler;

	private static final String FILE_ENDING = ".stackmaster";
	private final JTextField textField;
	private final JTextField textField_1;

	public static final Font FONT = new Font("Arial", Font.PLAIN, 20);
	private static final String TITLE = "StackMaster";

	private final JMenuItem mntmProfilSpeichern;;
	private final JMenuItem mntmProfilSpeichernAls;

	public static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
	public static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.US);
	public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);

	private final ComConnectionProperties defaultConnectionProperties;
	private ComConnectionProperties actualConnectionProperties;
	private final JTextField textField_2;

	public SwingStarter() {
		propertiesHandler = new ProfileFileHandler();
		profile = propertiesHandler.readProfile(defaultFile);
		defaultProfile = propertiesHandler.readProfile(defaultFileBackup);
		actualConnectionProperties = profile.getProperties();
		defaultConnectionProperties = defaultProfile.getProperties();

		setSize(430, 420);
		getContentPane().setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(new Font("Arial", Font.PLAIN, 20));
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		final JPanel generalPanel = new JPanel();
		tabbedPane.addTab("Allgemein", null, generalPanel, null);
		generalPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		final JLabel lblNewLabel_13 = new JLabel("Vorname");
		lblNewLabel_13.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_13, "cell 0 0,alignx left");

		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(textField, "cell 1 0,growx");
		textField.setColumns(10);

		final JLabel lblNewLabel_14 = new JLabel("Nachname");
		lblNewLabel_14.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_14, "cell 0 1,alignx left");

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(textField_1, "cell 1 1,growx");
		textField_1.setColumns(10);

		final JLabel label = new JLabel(" ");
		generalPanel.add(label, "cell 0 2");

		final JLabel lblSchritteProMillimeter = new JLabel("Schritte pro Millimeter");
		lblSchritteProMillimeter.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(lblSchritteProMillimeter, "cell 0 3,alignx left");

		final JSpinner spinner_10 = new JSpinner();
		spinner_10.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_10.setMinimumSize(new Dimension(100, 22));
		generalPanel.add(spinner_10, "cell 1 3");

		final JLabel lblModus = new JLabel("Modus");
		lblModus.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(lblModus, "cell 0 4,alignx left");

		final JComboBox<Integer> comboBox = new JComboBox<Integer>();
		comboBox.addItem(1);
		comboBox.addItem(2);
		comboBox.addItem(3);
		comboBox.addItem(4);
		comboBox.addItem(5);
		comboBox.addItem(6);
		comboBox.addItem(7);
		comboBox.addItem(8);
		comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(comboBox, "cell 1 4,growx");

		final JLabel lblNewLabel_15 = new JLabel("COM-Anschluss");
		lblNewLabel_15.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(lblNewLabel_15, "cell 0 5,alignx left");

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Arial", Font.PLAIN, 20));
		generalPanel.add(textField_2, "cell 1 5,growx");
		textField_2.setColumns(10);

		final JPanel relativPanel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, relativPanel, null);
		relativPanel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][]"));

		final JLabel lblNewLabel = new JLabel("Hoch");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel, "cell 0 0");

		final JLabel lblNewLabel_1 = new JLabel("schnelle Geschw.");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_1, "cell 0 1");

		final JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner.setMinimumSize(new Dimension(100, 22));
		relativPanel.add(spinner, "cell 1 1");

		final JLabel lblNewLabel_2 = new JLabel("mittlere Geschw.");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_2, "cell 0 2");

		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_1.setMinimumSize(new Dimension(100, 22));
		relativPanel.add(spinner_1, "cell 1 2");

		final JLabel lblNewLabel_3 = new JLabel("langsame Geschw.");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_3, "cell 0 3");

		final JSpinner spinner_2 = new JSpinner();
		spinner_2.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_2.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(spinner_2, "cell 1 3");

		final JLabel label_1 = new JLabel(" ");
		relativPanel.add(label_1, "cell 0 4");

		final JLabel lblNewLabel_4 = new JLabel("Runter");
		lblNewLabel_4.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_4, "cell 0 5");

		final JLabel lblNewLabel_5 = new JLabel("schnelle Geschw.");
		lblNewLabel_5.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_5, "cell 0 6");

		final JSpinner spinner_3 = new JSpinner();
		spinner_3.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_3.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(spinner_3, "cell 1 6");

		final JLabel lblNewLabel_6 = new JLabel("mittlere Geschw.");
		lblNewLabel_6.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_6, "cell 0 7");

		final JSpinner spinner_4 = new JSpinner();
		spinner_4.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_4.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(spinner_4, "cell 1 7");

		final JLabel lblNewLabel_7 = new JLabel("langsame Geschw.");
		lblNewLabel_7.setFont(new Font("Arial", Font.PLAIN, 20));
		relativPanel.add(lblNewLabel_7, "cell 0 8");

		final JSpinner spinner_5 = new JSpinner();
		spinner_5.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_5.setPreferredSize(new Dimension(100, 22));
		relativPanel.add(spinner_5, "cell 1 8");

		final JPanel stepPanel = new JPanel();
		tabbedPane.addTab("Step-Panel", null, stepPanel, null);
		stepPanel.setLayout(new MigLayout("", "[][]", "[][][][][]"));

		final JLabel lblNewLabel_8 = new JLabel("Richtung umdrehen");
		lblNewLabel_8.setFont(new Font("Arial", Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_8, "cell 0 0");

		final JCheckBox chckbxNewCheckBox = new JCheckBox("");
		stepPanel.add(chckbxNewCheckBox, "cell 1 0");

		final JLabel lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		lblNewLabel_9.setFont(new Font("Arial", Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_9, "cell 0 1");

		final JSpinner spinner_6 = new JSpinner();
		spinner_6.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_6.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(spinner_6, "cell 1 1");

		final JLabel lblNewLabel_10 = new JLabel("Pause (Spiegel - Bild) [ms]");
		lblNewLabel_10.setFont(new Font("Arial", Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_10, "cell 0 2");

		final JSpinner spinner_7 = new JSpinner();
		spinner_7.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_7.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(spinner_7, "cell 1 2");

		final JLabel lblNewLabel_11 = new JLabel("Pause (Bild - Bew.) [ms]");
		lblNewLabel_11.setFont(new Font("Arial", Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_11, "cell 0 3");

		final JSpinner spinner_8 = new JSpinner();
		spinner_8.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_8.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(spinner_8, "cell 1 3");

		final JLabel lblNewLabel_12 = new JLabel("Impulsdauer [ms]");
		lblNewLabel_12.setFont(new Font("Arial", Font.PLAIN, 20));
		stepPanel.add(lblNewLabel_12, "cell 0 4");

		final JSpinner spinner_9 = new JSpinner();
		spinner_9.setFont(new Font("Arial", Font.PLAIN, 20));
		spinner_9.setPreferredSize(new Dimension(100, 22));
		stepPanel.add(spinner_9, "cell 1 4");

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu menu = new JMenu("Datei");
		menu.setFont(new Font("Arial", Font.PLAIN, 20));
		menuBar.add(menu);

		final JMenuItem mntmLaden = new JMenuItem("Profil laden");
		mntmLaden.setFont(new Font("Arial", Font.PLAIN, 20));
		mntmLaden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser chooser = new JFileChooser();
				final FileNameExtensionFilter filter = new FileNameExtensionFilter("StackMaster Dateien", "stackmaster");
				chooser.setFileFilter(filter);
				final int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File selectedFile = chooser.getSelectedFile();
					if (selectedFile != null) {
						LOGGER.info("chosen file: " + selectedFile);
						actualOpenedProfileFileName = selectedFile;
						final Profile loadedConnectionProperties = propertiesHandler
								.readProfile(actualOpenedProfileFileName);
						setConnectionProperties(loadedConnectionProperties.getProperties());
						setTitle(TITLE + " - " + selectedFile.getAbsolutePath());
						mntmProfilSpeichern.setEnabled(true);
					}
				}

			}
		});
		menu.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setFont(new Font("Arial", Font.PLAIN, 20));
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				LOGGER.info("chosen file: " + actualOpenedProfileFileName.getAbsolutePath());
				propertiesHandler.writeProfile(actualOpenedProfileFileName, profile);
				// propertiesDialog.loadConnectionProperties(actualOpenedPropertiesFileName);
			}

		});
		mntmProfilSpeichern.setEnabled(false);
		menu.add(mntmProfilSpeichern);

		mntmProfilSpeichernAls = new JMenuItem("Profil speichern unter...");
		mntmProfilSpeichernAls.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser chooser = new JFileChooser();
				final FileNameExtensionFilter filter = new FileNameExtensionFilter("StackMaster Dateien", "stackmaster");
				chooser.setFileFilter(filter);
				final int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					if (selectedFile != null) {
						String absolutePath = selectedFile.getAbsolutePath();

						if (!absolutePath.endsWith(FILE_ENDING)) {
							absolutePath = absolutePath + FILE_ENDING;
							selectedFile = new File(absolutePath);
						}
						LOGGER.info("chosen file: " + absolutePath);
						actualOpenedProfileFileName = selectedFile;
						propertiesHandler.writeProfile(actualOpenedProfileFileName, profile);
						setTitle(TITLE + " - " + absolutePath);
						mntmProfilSpeichern.setEnabled(true);
					}
				}

			}
		});
		mntmProfilSpeichernAls.setFont(new Font("Arial", Font.PLAIN, 20));
		menu.add(mntmProfilSpeichernAls);

		final JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setFont(new Font("Arial", Font.PLAIN, 20));
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				exit();
			}

		});
		menu.add(mntmExit);
	}

	protected void exit() {
		LOGGER.info("Exit StackMaster");

		System.exit(0);
	}

	/**
	 * Launch the application.
	 */
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

	public void setConnectionProperties(final ComConnectionProperties connectionProperties) {
		actualConnectionProperties = connectionProperties;
		refresh();
	}

	private void cancel() {
		// fastUpSpeedTF.setValue(actualConnectionProperties.getFastUpSpeed());
		// middleUpSpeedTF.setValue(actualConnectionProperties.getMiddleUpSpeed());
		// slowUpSpeedTF.setValue(actualConnectionProperties.getSlowUpSpeed());
		// fastDownSpeedTF.setValue(actualConnectionProperties.getFastDownSpeed());
		// middleDownSpeedTF.setValue(actualConnectionProperties.getMiddleDownSpeed());
		// slowDownSpeedTF.setValue(actualConnectionProperties.getSlowDownSpeed());
		// reverseCB.setSelected(actualConnectionProperties.isReverseSteps());
		// sleepMirrorPictureTF.setValue(actualConnectionProperties.getSleepMirrorPicture());
		// sleepMovementMirrorTF.setValue(actualConnectionProperties.getSleepMovementMirror());
		// sleepPictureMovementTF.setValue(actualConnectionProperties.getSleepPictureMovement());
		// pulseDurationTF.setValue(actualConnectionProperties.getPulseDuration());
	}

	public void refresh() {
		cancel();
	}

}
