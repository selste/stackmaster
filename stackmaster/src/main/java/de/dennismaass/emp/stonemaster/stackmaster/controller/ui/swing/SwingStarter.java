package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.profile.ProfileFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.application.ApplicationPropertiesFileHandler;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionPropertiesChangeEvent;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionPropertiesListener;
import de.dennismaass.emp.stonemaster.stackmaster.common.ui.swing.PropertiesFileChooser;
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
//- Validierung aller Textfelder
//- Kein Absturz mehr nach Trennen vom USB-Anschluss
//- spinner problem
//- nur property event wenn sich auch echt was geändert worden ist

//Features:
//- schriftgrößen einstellbar
//- windows und mac profile in maven einbauen
//- Start-Ende (Rückkanal)
//- Baukasten
//- Mehrsprachigkeit
public class SwingStarter extends JFrame implements ComAnswerListener, CommPortIdentifierNotificationListener,
		ComConnectionPropertiesListener {
	private static final long serialVersionUID = 4155209335768313320L;

	private static Logger LOGGER = Logger.getLogger(SwingStarter.class);

	private static String DELETE_ICON_NAME = "delete-icon.png";

	private static String FILE_ENDING = ".stackmaster";

	/* Konstanten */

	private static String TITLE = "StackMaster";

	private static String IMAGES = "/images/";

	/* Stepper */
	private ComCommunicator communicator;

	/* UI */
	private JPanel contentPane;

	private ConnectionThread connectThread;
	private JButton connectButton;
	private JComboBox<CommPortIdentifier> connectionComboBox;

	private JLabel stateLine;

	/* Properties */
	private Profile defaultProfile;
	private Profile defaultBackupProfile;
	private int microstepResolutionMode = 4;

	private int stepsPerMm = 64025;

	private RelativPosPanel relativPosPanel;
	private StepPanel stepPanel;

	public static String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
	public static String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.US);
	public static String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);

	private JMenuItem mntmProfilSpeichern;;
	private JMenuItem mntmProfilSpeichernAls;

	private PropertiesDialog propertiesDialog;

	private File defaultFileBackup = new File("default.stackmaster.backup");
	private File defaultFile = new File("default.stackmaster");
	private File actualOpenedProfileFile = defaultFile;
	private ProfileFileHandler propertiesHandler;

	public static Font actualFont = new Font("Arial", Font.PLAIN, 20);

	private JLabel welcomeLabel;

	private JLabel secondLineLabel;

	private static ApplicationProperties applicationProperties;
	private File applicationPropertyFile = new File("application.profile");
	private ApplicationPropertiesFileHandler applicationPropertiesFileHandler = new ApplicationPropertiesFileHandler();
	@Getter
	@Setter
	private boolean unsavedChanges;

	private JMenuItem mntmExit;

	private JMenuItem mntmLaden;

	private JMenuItem mntmberCusa;

	private JMenuItem mntmEinstellungen;

	private JMenu mnEinstellungen;

	private JMenu mnDatei;

	/**
	 * Create the frame.
	 */
	public SwingStarter() {
		applicationProperties = applicationPropertiesFileHandler.read(applicationPropertyFile);

		LOGGER.info("Start SwingStarter");

		LOGGER.info("OS_ARCH " + OS_ARCH);
		LOGGER.info("OS_NAME " + OS_NAME);
		LOGGER.info("OS_VERSION " + OS_VERSION);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOGGER.info("window closed");
				handleExit();
			}
		});

		LOGGER.info("try building user interface");

		URL logo32URL = getClass().getResource(IMAGES + "stackmaster_32x32.png");
		URL logo64URL = getClass().getResource(IMAGES + "stackmaster_64x64.png");

		List<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(logo32URL).getImage());
		icons.add(new ImageIcon(logo64URL).getImage());
		setIconImages(icons);

		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 650, 620);

		propertiesHandler = new ProfileFileHandler();
		defaultProfile = propertiesHandler.readProfile(defaultFile);
		defaultBackupProfile = propertiesHandler.readProfile(defaultFileBackup);

		propertiesDialog = createPropertiesDialog(defaultProfile.getProperties(), defaultBackupProfile.getProperties());
		setTitle(TITLE + " - defaults");

		initMenu();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));

		initConnectionPanel();

		initStatePanel();

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		// tabbedPane.setFont(SwingStarter.actualFont);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel welcome = new JPanel();

		tabbedPane.addTab("Start", null, welcome, null);
		welcome.setLayout(new MigLayout("", "[grow,center]", "[center][][]"));

		welcomeLabel = new JLabel();
		// welcomeLabel.setFont(SwingStarter.actualFont);
		welcome.add(welcomeLabel, "cell 0 0,alignx center");

		JLabel platzhalter1 = new JLabel("");
		welcome.add(platzhalter1, "cell 0 1");

		secondLineLabel = new JLabel();
		// secondLineLabel.setFont(SwingStarter.actualFont);
		welcome.add(secondLineLabel, "cell 0 2,alignx left");

		relativPosPanel = new RelativPosPanel(defaultProfile.getProperties(), stateLine);
		tabbedPane.addTab("relativ", null, relativPosPanel, null);

		stepPanel = new StepPanel(defaultProfile.getProperties(), stateLine);
		tabbedPane.addTab("Schritte", null, stepPanel, null);

		connectThread = newConnectionThread();
		connectThread.start();

		setAllComponentsDisableState(true);

		setConnectionProperties(defaultProfile.getProperties());
		LOGGER.info("user interface builded");

		setNewFont(applicationProperties.getFontSize());
	}

	protected void initStatePanel() {
		JPanel statePanel = new JPanel();
		statePanel.setPreferredSize(new Dimension(10, 20));
		statePanel.setMinimumSize(new Dimension(10, 20));
		contentPane.add(statePanel, BorderLayout.SOUTH);

		stateLine = new JLabel("");
		statePanel.add(stateLine);
	}

	protected void initConnectionPanel() {
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		Box.createHorizontalBox();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		connectionComboBox = new JComboBox<>();
		connectionComboBox.setFont(SwingStarter.actualFont);
		connectionComboBox.setPreferredSize(new Dimension(100, 22));
		connectionComboBox.setMinimumSize(new Dimension(100, 22));
		connectionComboBox.setMaximumSize(new Dimension(100, 32767));
		connectionComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectionComboBox);

		connectionComboBox.setRenderer(new CommPortIdentifierListRenderer());

		connectButton = new JButton("Verbinden");
		connectButton.setFont(SwingStarter.actualFont);
		connectButton.setEnabled(false);
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				LOGGER.info("click connect button");

				if (communicator == null) {

					Object selectedItem = connectionComboBox.getSelectedItem();
					if (selectedItem instanceof CommPortIdentifier) {

						CommPortIdentifier commPortIdentifier = (CommPortIdentifier) selectedItem;
						boolean currentlyOwned = commPortIdentifier.isCurrentlyOwned();
						if (currentlyOwned) {
							stateLine.setText(commPortIdentifier.getName()
									+ " wird von einem anderen Programm verwendet");
						} else {
							stateLine.setText("Verbindung wird hergstellt...");
							ComCommunicator communicator = createCommunicator(commPortIdentifier.getName());

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
					} catch (PortInUseException e) {
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

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}

	private void setAllComponentsDisableState(boolean disableState) {
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

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnDatei = new JMenu("Datei");
		// mnDatei.setFont(SwingStarter.actualFont);
		menuBar.add(mnDatei);

		URL deleteURL = getClass().getResource(IMAGES + DELETE_ICON_NAME);
		ImageIcon deleteIcon = new ImageIcon(deleteURL);
		ImageIcon resizedDeleteIcon = ImageUtils.getResizedImage(deleteIcon, 15, 15);

		mntmExit = new JMenuItem("Exit");
		// mntmExit.setFont(SwingStarter.actualFont);
		mntmExit.setIcon(resizedDeleteIcon);
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleExit();
			}

		});

		mntmLaden = new JMenuItem("Profil laden");
		// mntmLaden.setFont(SwingStarter.actualFont);
		mntmLaden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleLoadProperties();
			}
		});
		mnDatei.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		// mntmProfilSpeichern.setFont(SwingStarter.actualFont);
		mntmProfilSpeichern.setEnabled(false);
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveProperties();
			}

		});
		mnDatei.add(mntmProfilSpeichern);

		mntmProfilSpeichernAls = new JMenuItem("Profil speichern unter...");
		// mntmProfilSpeichernAls.setFont(SwingStarter.actualFont);
		mntmProfilSpeichernAls.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveAsProperties();

			}
		});
		mnDatei.add(mntmProfilSpeichernAls);
		mnDatei.add(mntmExit);

		URL helpURL = getClass().getResource(IMAGES + "info-icon.png");
		ImageIcon helpIcon = new ImageIcon(helpURL);

		ImageIcon resizedHelpIcon = ImageUtils.getResizedImage(helpIcon, 15, 15);

		mnEinstellungen = new JMenu("Extras");
		// mnEinstellungen.setFont(SwingStarter.actualFont);
		menuBar.add(mnEinstellungen);

		mntmEinstellungen = new JMenuItem("Einstellungen");
		// mntmEinstellungen.setFont(SwingStarter.actualFont);
		mnEinstellungen.add(mntmEinstellungen);
		mntmEinstellungen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				propertiesDialog.setVisible(true);
			}

		});

		mntmberCusa = new JMenuItem("\u00DCber");
		// mntmberCusa.setFont(SwingStarter.actualFont);
		mnEinstellungen.add(mntmberCusa);
		mntmberCusa.setIcon(resizedHelpIcon);
		mntmberCusa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleOverUs();
			}

		});
		/** Menu Ende */
	}

	protected PropertiesDialog createPropertiesDialog(ComConnectionProperties connectionProperties,
			ComConnectionProperties defaultConnectionProperties) {
		propertiesDialog = new PropertiesDialog(connectionProperties, defaultConnectionProperties);
		propertiesDialog.addComConnectionPropertiesListener(this);
		return propertiesDialog;
	}

	protected Component getFrame() {
		return this;
	}

	protected ComCommunicator createCommunicator(String comPortName) {
		ComCommunicator communicator = ComCommunicator.getInstance(comPortName, stepsPerMm);
		communicator.addAnswerListener(this);
		return communicator;
	}

	protected ConnectionThread newConnectionThread() {
		LOGGER.info("build connection thread");
		ConnectionThread connectThread = new ConnectionThread();
		connectThread.addCommPortIdentifierListener(this);

		return connectThread;
	}

	protected void refreshConnectionComboBox(List<CommPortIdentifier> commPortIdentifierList) {

		List<CommPortIdentifier> items = new ArrayList<>();

		int numOfMenus = connectionComboBox.getItemCount();
		for (int i = 0; i < numOfMenus; i++) {
			CommPortIdentifier connectionComboBoxEntry = connectionComboBox.getItemAt(i);
			items.add(connectionComboBoxEntry);
		}

		for (CommPortIdentifier commPortIdentifier : commPortIdentifierList) {
			if (!items.contains(commPortIdentifier)) {
				LOGGER.info("add Connection " + commPortIdentifier.getName());
				connectionComboBox.addItem(commPortIdentifier);
			}
		}

		for (CommPortIdentifier commPortIdentifier : items) {
			if (!commPortIdentifierList.contains(commPortIdentifier)) {
				LOGGER.info("remove Connection " + commPortIdentifier.getName());
				connectionComboBox.removeItem(commPortIdentifier);
			}
		}

		String comConnectionName = defaultProfile.getProperties().getComConnectionName();
		if (comConnectionName != null) {
			int itemCount = connectionComboBox.getItemCount();
			for (int i = 0; i < itemCount; i++) {
				CommPortIdentifier connectionComboBoxEntry = connectionComboBox.getItemAt(i);
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
	public void handleCommPortIdentifierNotificationEvent(CommPortIdentifierNotificationEvent event) {
		refreshConnectionComboBox(event.getCommPortIdentifierList());

	}

	@Override
	public void handleAnswer(ComAnswerEvent e) {
		if (e.getInstruction() == ComInstructionID.MOVE_POSITION) {
			System.out.println(e.getValueInMillimeter());
		}
	}

	protected void handleExit() {
		LOGGER.info("Exit StackMaster");

		if (communicator != null) {
			communicator.stop();
			communicator.disconnect();
		}
		applicationProperties.setFirstUse(false);

		if (isUnsavedChanges()) {
			JDialog.setDefaultLookAndFeelDecorated(true);

			int response = JOptionPane.showConfirmDialog(null, "Wollen Sie Ihre Einstellungen speichern?",
					"Einstellungen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				if (actualOpenedProfileFile.equals(defaultFile)) {
					handleSaveAsProperties();
				} else {
					handleSaveProperties();
				}
			}

			setUnsavedChanges(false);

		}

		System.exit(0);
	}

	protected void handleOverUs() {
		JOptionPane
				.showMessageDialog(
						getFrame(),
						"\"StackMaster\" ist ein Produkt aus der Produktreihe \"stonemaster\" \n der Firma E-mP Ernst-mechanische Produkte",
						"Über", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Launch the application.
	 */
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

	@Override
	public void handleComConnectionPropertiesChangeEvent(ComConnectionPropertiesChangeEvent event) {
		ComConnectionProperties connectionProperties = event.getConnectionProperties();
		setConnectionProperties(connectionProperties);

		setUnsavedChanges(true);
	}

	public void setConnectionProperties(ComConnectionProperties connectionProperties) {
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

		defaultProfile.setProperties(connectionProperties);

		String text = "Willkommen ";
		String firstName = applicationProperties.getFirstName();
		if (StringUtils.isNotEmpty(firstName)) {
			text += " " + firstName;

			String lastName = applicationProperties.getLastName();
			if (StringUtils.isNotEmpty(lastName)) {
				text += " " + lastName;
			}
		}
		welcomeLabel.setText(text);

		String text2 = "";
		if (!applicationProperties.isFirstUse()) {
			text2 += "Ihre letzten Einstellungen waren: ";
		} else {
			text2 += "Wir wünschen Ihnen viel Spass mit dem Stackmaster!";
		}
		secondLineLabel.setText(text2);

	}

	protected void handleSaveProperties() {
		LOGGER.info("chosen file: " + actualOpenedProfileFile.getAbsolutePath());
		applicationProperties.setFirstUse(false);
		propertiesHandler.writeProfile(actualOpenedProfileFile, defaultProfile);
		setUnsavedChanges(false);
	}

	protected void handleLoadProperties() {
		JFileChooser chooser = new PropertiesFileChooser();

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (selectedFile != null) {
				LOGGER.info("chosen file: " + selectedFile);
				actualOpenedProfileFile = selectedFile;
				Profile loadedConnectionProperties = propertiesHandler.readProfile(actualOpenedProfileFile);
				setConnectionProperties(loadedConnectionProperties.getProperties());
				setTitle(TITLE + " - " + selectedFile.getAbsolutePath());
				mntmProfilSpeichern.setEnabled(true);
			}
		}
	}

	protected void handleSaveAsProperties() {
		JFileChooser chooser = new PropertiesFileChooser();

		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();

			if (selectedFile != null) {
				boolean acceptable = false;
				if (selectedFile.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						acceptable = true;
					}
				}
				if (acceptable) {
					String absolutePath = selectedFile.getAbsolutePath();

					if (!absolutePath.endsWith(FILE_ENDING)) {
						absolutePath = absolutePath + FILE_ENDING;
						selectedFile = new File(absolutePath);
					}
					LOGGER.info("chosen file: " + absolutePath);
					actualOpenedProfileFile = selectedFile;
					applicationProperties.setFirstUse(false);
					propertiesHandler.writeProfile(actualOpenedProfileFile, defaultProfile);
					setTitle(TITLE + " - " + absolutePath);
					mntmProfilSpeichern.setEnabled(true);
				}
			}
		}
	}

	public void changeFont(Component component, int fontSize) {
		Font f = actualFont;
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
		mntmProfilSpeichern.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmProfilSpeichernAls.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmLaden.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmExit.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mnDatei.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmberCusa.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mntmEinstellungen.setFont(new Font(f.getName(), f.getStyle(), fontSize));
		mnEinstellungen.setFont(new Font(f.getName(), f.getStyle(), fontSize));
	}

}