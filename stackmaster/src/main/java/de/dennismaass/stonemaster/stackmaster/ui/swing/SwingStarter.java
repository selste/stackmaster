package de.dennismaass.stonemaster.stackmaster.ui.swing;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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

import de.dennismaass.stonemaster.stackmaster.comport.command.answer.ComAnswerEvent;
import de.dennismaass.stonemaster.stackmaster.comport.command.answer.ComAnswerListener;
import de.dennismaass.stonemaster.stackmaster.comport.communicator.ComCommunicator;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ComConnectionProperties;
import de.dennismaass.stonemaster.stackmaster.comport.connection.CommPortIdentifierNotificationEvent;
import de.dennismaass.stonemaster.stackmaster.comport.connection.CommPortIdentifierNotificationListener;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ConnectionThread;
import de.dennismaass.stonemaster.stackmaster.util.PropertiesHandler;

//TODO:
//windows und mac profile in maven einbauen
//Validierung aller Textfelder
//Kein Absturz mehr nach Trennen vom USB-Anschluss
//Start-Ende (Rückkanal)
//Baukasten
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
	private final ComConnectionProperties connectionProperties;
	private final ComConnectionProperties defaultConnectionProperties;
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
	private boolean startPropertiesfileLoaded = true;
	private final File defaultFileBackup = new File("default.stackmaster.backup");
	private final File defaultFile = new File("default.stackmaster");
	private File actualOpenedPropertiesFileName = defaultFile;
	private final PropertiesHandler propertiesHandler;

	/**
	 * Create the frame.
	 */
	public SwingStarter() {
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

				stepPanel.setPropertiesFromVariables(connectionProperties);
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

		setBounds(100, 100, 550, 520);

		propertiesHandler = new PropertiesHandler();
		connectionProperties = propertiesHandler.loadConnectionProperties(defaultFile);
		defaultConnectionProperties = propertiesHandler.loadConnectionProperties(defaultFileBackup);
		propertiesDialog = createPropertiesDialog(connectionProperties, defaultConnectionProperties);
		setTitle(TITLE + " - defaults");

		initMenu();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));

		initConnectionPanel();

		initStatePanel();

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		relativPosPanel = new RelativPosPanel(connectionProperties, stateLine);
		tabbedPane.addTab("relativ", null, relativPosPanel, null);

		stepPanel = new StepPanel(connectionProperties, stateLine);
		tabbedPane.addTab("Schritte", null, stepPanel, null);

		connectThread = newConnectionThread();
		connectThread.start();

		setAllComponentsDisableState(true);

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
		connectionComboBox.setPreferredSize(new Dimension(100, 22));
		connectionComboBox.setMinimumSize(new Dimension(100, 22));
		connectionComboBox.setMaximumSize(new Dimension(100, 32767));
		connectionComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectionComboBox);

		connectionComboBox.setRenderer(new CommPortIdentifierListRenderer());

		connectButton = new JButton("Verbinden");
		connectButton.setEnabled(false);
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
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
					} catch (final PortInUseException e1) {
						e1.printStackTrace();
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
		menuBar.add(mnDatei);

		final URL deleteURL = getClass().getResource(IMAGES + "delete-icon.png");
		final ImageIcon deleteIcon = new ImageIcon(deleteURL);
		final ImageIcon resizedDeleteIcon = getResizedImage(deleteIcon, 15, 15);

		final JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(resizedDeleteIcon);
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				exit();
			}

		});

		final JMenuItem mntmLaden = new JMenuItem("Profil laden");
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
						actualOpenedPropertiesFileName = selectedFile;
						final ComConnectionProperties loadedConnectionProperties = propertiesHandler
								.loadConnectionProperties(actualOpenedPropertiesFileName);
						setNewConnectionProperties(loadedConnectionProperties);
						setTitle(TITLE + " - " + selectedFile.getAbsolutePath());
						startPropertiesfileLoaded = false;
						mntmProfilSpeichern.setEnabled(true);
					}
				}

			}
		});
		mnDatei.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setEnabled(false);
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				LOGGER.info("chosen file: " + actualOpenedPropertiesFileName.getAbsolutePath());
				propertiesHandler.writeConnectionProperties(actualOpenedPropertiesFileName, connectionProperties);
				// propertiesDialog.loadConnectionProperties(actualOpenedPropertiesFileName);
			}

		});
		mnDatei.add(mntmProfilSpeichern);

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
						actualOpenedPropertiesFileName = selectedFile;
						propertiesHandler.writeConnectionProperties(actualOpenedPropertiesFileName,
								connectionProperties);
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
		final ImageIcon resizedHelpIcon = getResizedImage(helpIcon, 15, 15);

		final JMenu mnEinstellungen = new JMenu("Extras");
		menuBar.add(mnEinstellungen);

		final JMenuItem mntmEinstellungen = new JMenuItem("Einstellungen");
		mnEinstellungen.add(mntmEinstellungen);
		mntmEinstellungen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				propertiesDialog.setVisible(true);
			}

		});

		final JMenuItem mntmberCusa = new JMenuItem("\u00DCber");
		mnEinstellungen.add(mntmberCusa);
		mntmberCusa.setIcon(resizedHelpIcon);
		mntmberCusa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(getFrame(), "stonemaster", "Über", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		/** Menu Ende */
	}

	protected PropertiesDialog createPropertiesDialog(final ComConnectionProperties connectionProperties,
			final ComConnectionProperties defaultConnectionProperties) {
		propertiesDialog = new PropertiesDialog(connectionProperties, defaultConnectionProperties);
		propertiesDialog.addComConnectionPropertiesListener(this);
		// propertiesDialog.loadConnectionProperties(defaultFile);
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

	}

	protected void exit() {
		LOGGER.info("Exit Cusa");

		if (communicator != null) {
			communicator.stop();
			communicator.disconnect();
		}
		System.exit(0);
	}

	// TODO: auslagern
	protected ImageIcon getResizedImage(ImageIcon icon, final int width, final int height) {
		LOGGER.info("resize icon to " + width + " x " + height);

		final Image img = icon.getImage();
		final Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		return icon;
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
			communicator.setReverse(connectionProperties.isReverse());
			communicator.setMicrostepResolution(connectionProperties.getMicrostepResolutionMode());
		}
	}

}
