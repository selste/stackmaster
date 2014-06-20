package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionPropertiesChangeEvent;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionPropertiesListener;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.UiProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.ComInstructionID;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.CommPortIdentifierNotificationEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.CommPortIdentifierNotificationListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.ConnectionThread;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

//TODO:

//Bugs:
//Validierung aller Textfelder
//Kein Absturz mehr nach Trennen vom USB-Anschluss

//Features:
//windows und mac profile in maven einbauen
//Start-Ende (Rückkanal)
//Baukasten
//Mehrsprachigkeit
public class SwingStarter extends JFrame implements ComAnswerListener, CommPortIdentifierNotificationListener,
		ComConnectionPropertiesListener {

	private static final String FILE_ENDING = ".stackmaster";

	/* Konstanten */
	private static final long serialVersionUID = -811339745631798835L;

	private static final Logger LOGGER = Logger.getLogger(SwingStarter.class);
	private static final String TITLE = "StackMaster";

	private static final String IMAGES = "/images/";

	/* Stepper */
	private ComCommunicator communicator;

	/* UI */
	private final JPanel contentPane;

	private ConnectionThread connectThread;
	private JButton connectButton;
	private JComboBox<CommPortIdentifier> connectionComboBox;

	private JLabel stateLine;

	/* Properties */
	private final Profile profile;
	private final Profile defaultProfile;
	private final int microstepResolutionMode = 4;

	private final int stepsPerMm = 64025;

	private final RelativPosPanel relativPosPanel;
	private final StepPanel stepPanel;

	public static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
	public static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.US);
	public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);

	private JMenuItem mntmProfilSpeichern;;
	private JMenuItem mntmProfilSpeichernAls;

	private PropertiesDialog propertiesDialog;

	private final File defaultFileBackup = new File("default.stackmaster.backup");
	private final File defaultFile = new File("default.stackmaster");
	private File actualOpenedProfileFileName = defaultFile;
	private final ProfileFileHandler propertiesHandler;

	public static final Font FONT = new Font("Arial", Font.PLAIN, 20);

	/**
	 * Create the frame.
	 */
	public SwingStarter() {
		setFont(SwingStarter.FONT);
		LOGGER.info("Start SwingStarter");

		LOGGER.info("OS_ARCH " + OS_ARCH);
		LOGGER.info("OS_NAME " + OS_NAME);
		LOGGER.info("OS_VERSION " + OS_VERSION);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(final WindowEvent event) {
				LOGGER.info("window closed");
				if (communicator != null) {
					communicator.stop();
					communicator.disconnect();
				}

				stepPanel.setPropertiesFromVariables(profile.getProperties());
			}
		});

		LOGGER.info("try building user interface");

		final URL logo32URL = getClass().getResource(IMAGES + "stackmaster_32x32.png");
		final URL logo64URL = getClass().getResource(IMAGES + "stackmaster_64x64.png");

		final List<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(logo32URL).getImage());
		icons.add(new ImageIcon(logo64URL).getImage());
		setIconImages(icons);

		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 650, 620);

		propertiesHandler = new ProfileFileHandler();
		profile = propertiesHandler.readProfile(defaultFile);
		defaultProfile = propertiesHandler.readProfile(defaultFileBackup);

		propertiesDialog = createPropertiesDialog(profile.getProperties(), defaultProfile.getProperties());
		setTitle(TITLE + " - defaults");

		initMenu();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));

		initConnectionPanel();

		initStatePanel();

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(SwingStarter.FONT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		relativPosPanel = new RelativPosPanel(profile.getProperties(), stateLine);
		tabbedPane.addTab("relativ", null, relativPosPanel, null);

		stepPanel = new StepPanel(profile.getProperties(), stateLine);
		tabbedPane.addTab("Schritte", null, stepPanel, null);

		connectThread = newConnectionThread();
		connectThread.start();

		setAllComponentsDisableState(true);

		setNewConnectionProperties(defaultProfile.getProperties());
		LOGGER.info("user interface builded");
	}

	protected void initStatePanel() {
		final JPanel statePanel = new JPanel();
		statePanel.setPreferredSize(new Dimension(10, 20));
		statePanel.setMinimumSize(new Dimension(10, 20));
		contentPane.add(statePanel, BorderLayout.SOUTH);

		stateLine = new JLabel("");
		statePanel.add(stateLine);
	}

	protected void initConnectionPanel() {
		final JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		Box.createHorizontalBox();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		connectionComboBox = new JComboBox<>();
		connectionComboBox.setFont(SwingStarter.FONT);
		connectionComboBox.setPreferredSize(new Dimension(100, 22));
		connectionComboBox.setMinimumSize(new Dimension(100, 22));
		connectionComboBox.setMaximumSize(new Dimension(100, 32767));
		connectionComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectionComboBox);

		connectionComboBox.setRenderer(new CommPortIdentifierListRenderer());

		connectButton = new JButton("Verbinden");
		connectButton.setFont(SwingStarter.FONT);
		connectButton.setEnabled(false);
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent actionEvent) {
				LOGGER.info("click connect button");

				if (communicator == null) {

					final Object selectedItem = connectionComboBox.getSelectedItem();
					if (selectedItem instanceof CommPortIdentifier) {

						final CommPortIdentifier commPortIdentifier = (CommPortIdentifier) selectedItem;
						final boolean currentlyOwned = commPortIdentifier.isCurrentlyOwned();
						if (currentlyOwned) {
							stateLine.setText(commPortIdentifier.getName()
									+ " wird von einem anderen Programm verwendet");
						} else {
							stateLine.setText("Verbindung wird hergstellt...");
							final ComCommunicator communicator = createCommunicator(commPortIdentifier.getName());

							setCommunicator(communicator);

							relativPosPanel.setCommunicator(communicator);
							stepPanel.setCommunicator(communicator);
						}
					}
				}
				if (!communicator.isConnected()) {
					LOGGER.info("no connection ready, try to build a connection");

					boolean connect = false;
					try {
						connect = communicator.connect();
					} catch (final PortInUseException e) {
						e.printStackTrace();
						stateLine.setText(communicator.getComPort() + " wird von einem anderen Programm verwendet");
					}
					if (connect) {
						connectButton.setText("Trennen");
						stateLine.setText("Verbindung mit " + communicator.getComPort() + " hergestellt");
						setAllComponentsDisableState(false);
						connectionComboBox.setEnabled(false);

						connectThread.setRunning(false);

						communicator.setMicrostepResolution(microstepResolutionMode);
					}
				} else {
					LOGGER.info("connection ready, try to disconnect");

					communicator.stop();
					communicator.disconnect();
					connectButton.setText("Verbinden");
					stateLine.setText("Verbindung mit " + communicator.getComPort() + " getrennt");
					setAllComponentsDisableState(true);
					connectionComboBox.setEnabled(true);

					connectThread = newConnectionThread();
					connectThread.start();
				}

			}

		});

	}

	public void setCommunicator(final ComCommunicator communicator) {
		this.communicator = communicator;
	}

	private void setAllComponentsDisableState(final boolean disableState) {
		if (!disableState) {
			LOGGER.info("set all components enable");
		} else {
			LOGGER.info("set all components disable");
		}
		stepPanel.setAllComponentsDisableState(disableState);
		relativPosPanel.setAllComponentsDisableState(disableState);
	}

	protected void initMenu() {

		/** Menu */

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnDatei = new JMenu("Datei");
		mnDatei.setFont(SwingStarter.FONT);
		menuBar.add(mnDatei);

		final URL deleteURL = getClass().getResource(IMAGES + "delete-icon.png");
		final ImageIcon deleteIcon = new ImageIcon(deleteURL);
		final ImageIcon resizedDeleteIcon = ImageUtils.getResizedImage(deleteIcon, 15, 15);

		final JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setFont(SwingStarter.FONT);
		mntmExit.setIcon(resizedDeleteIcon);
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				exit();
			}

		});

		final JMenuItem mntmLaden = new JMenuItem("Profil laden");
		mntmLaden.setFont(SwingStarter.FONT);
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
						setNewConnectionProperties(loadedConnectionProperties.getProperties());
						setTitle(TITLE + " - " + selectedFile.getAbsolutePath());
						mntmProfilSpeichern.setEnabled(true);
					}
				}

			}
		});
		mnDatei.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setFont(SwingStarter.FONT);
		mntmProfilSpeichern.setEnabled(false);
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				LOGGER.info("chosen file: " + actualOpenedProfileFileName.getAbsolutePath());
				propertiesHandler.writeProfile(actualOpenedProfileFileName, profile);
				// propertiesDialog.loadConnectionProperties(actualOpenedPropertiesFileName);
			}

		});
		mnDatei.add(mntmProfilSpeichern);

		mntmProfilSpeichernAls = new JMenuItem("Profil speichern unter...");
		mntmProfilSpeichernAls.setFont(SwingStarter.FONT);
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
		mnDatei.add(mntmProfilSpeichernAls);
		mnDatei.add(mntmExit);

		final URL helpURL = getClass().getResource(IMAGES + "info-icon.png");
		final ImageIcon helpIcon = new ImageIcon(helpURL);

		final ImageIcon resizedHelpIcon = ImageUtils.getResizedImage(helpIcon, 15, 15);

		final JMenu mnEinstellungen = new JMenu("Extras");
		mnEinstellungen.setFont(SwingStarter.FONT);
		menuBar.add(mnEinstellungen);

		final JMenuItem mntmEinstellungen = new JMenuItem("Einstellungen");
		mntmEinstellungen.setFont(SwingStarter.FONT);
		mnEinstellungen.add(mntmEinstellungen);
		mntmEinstellungen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				propertiesDialog.setVisible(true);
			}

		});

		final JMenuItem mntmberCusa = new JMenuItem("\u00DCber");
		mntmberCusa.setFont(SwingStarter.FONT);
		mnEinstellungen.add(mntmberCusa);
		mntmberCusa.setIcon(resizedHelpIcon);
		mntmberCusa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								getFrame(),
								"\"StackMaster\" ist ein Produkt aus der Produktreihe \"stonemaster\" \n der Firma E-mP Ernst-mechanische Produkte",
								"Über", JOptionPane.PLAIN_MESSAGE);
			}
		});
		/** Menu Ende */
	}

	protected PropertiesDialog createPropertiesDialog(final UiProperties connectionProperties,
			final UiProperties defaultConnectionProperties) {
		propertiesDialog = new PropertiesDialog(connectionProperties, defaultConnectionProperties);
		propertiesDialog.addComConnectionPropertiesListener(this);
		return propertiesDialog;
	}

	protected Component getFrame() {
		return this;
	}

	protected ComCommunicator createCommunicator(final String comPortName) {
		final ComCommunicator communicator = ComCommunicator.getInstance(comPortName, stepsPerMm);
		communicator.addAnswerListener(this);
		return communicator;
	}

	protected ConnectionThread newConnectionThread() {
		LOGGER.info("build connection thread");
		final ConnectionThread connectThread = new ConnectionThread();
		connectThread.addCommPortIdentifierListener(this);

		return connectThread;
	}

	protected void refreshConnectionComboBox(final List<CommPortIdentifier> commPortIdentifierList) {

		final List<CommPortIdentifier> items = new ArrayList<>();

		final int numOfMenus = connectionComboBox.getItemCount();
		for (int i = 0; i < numOfMenus; i++) {
			final CommPortIdentifier connectionComboBoxEntry = connectionComboBox.getItemAt(i);
			items.add(connectionComboBoxEntry);
		}

		for (final CommPortIdentifier commPortIdentifier : commPortIdentifierList) {
			if (!items.contains(commPortIdentifier)) {
				LOGGER.info("add Connection " + commPortIdentifier.getName());
				connectionComboBox.addItem(commPortIdentifier);
			}
		}

		for (final CommPortIdentifier commPortIdentifier : items) {
			if (!commPortIdentifierList.contains(commPortIdentifier)) {
				LOGGER.info("remove Connection " + commPortIdentifier.getName());
				connectionComboBox.removeItem(commPortIdentifier);
			}
		}

		final String comConnectionName = profile.getProperties().getComConnectionName();
		if (comConnectionName != null) {
			final int itemCount = connectionComboBox.getItemCount();
			for (int i = 0; i < itemCount; i++) {
				final CommPortIdentifier connectionComboBoxEntry = connectionComboBox.getItemAt(i);
				if (connectionComboBoxEntry.getName().equals(comConnectionName)) {
					connectionComboBox.setSelectedItem(connectionComboBoxEntry);
				}
			}

		}

		LOGGER.info("count of connections " + connectionComboBox.getItemCount());
		if (connectionComboBox.getItemCount() > 0) {
			connectionComboBox.setSelectedIndex(0);
			connectButton.setEnabled(true);
		}
	}

	@Override
	public void handleCommPortIdentifierNotificationEvent(final CommPortIdentifierNotificationEvent event) {
		refreshConnectionComboBox(event.getCommPortIdentifierList());

	}

	@Override
	public void handleAnswer(final ComAnswerEvent e) {
		if (e.getInstruction() == ComInstructionID.MOVE_POSITION) {
			System.out.println(e.getValueInMillimeter());
		}
	}

	protected void exit() {
		LOGGER.info("Exit StackMaster");

		if (communicator != null) {
			communicator.stop();
			communicator.disconnect();
		}
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

	@Override
	public void handleComConnectionPropertiesChangeEvent(final ComConnectionPropertiesChangeEvent event) {
		final ComConnectionProperties connectionProperties = event.getConnectionProperties();
		setNewConnectionProperties(connectionProperties);

	}

	public void setNewConnectionProperties(final ComConnectionProperties connectionProperties) {
		stepPanel.setVariablesFromProperties(connectionProperties);
		stepPanel.refreshDistance();
		stepPanel.refreshSleep();
		relativPosPanel.setVariablesFromProperties(connectionProperties);
		propertiesDialog.setConnectionProperties(connectionProperties);

		if (communicator != null) {
			communicator.setStepsPerMm(connectionProperties.getStepsPerMm());
			communicator.setReverse(connectionProperties.isReverseSteps());
			communicator.setMicrostepResolution(connectionProperties.getMicrostepResolutionMode());
		}
	}

}
