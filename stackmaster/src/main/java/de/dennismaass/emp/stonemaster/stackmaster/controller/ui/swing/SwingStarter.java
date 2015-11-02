package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
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
import de.dennismaass.emp.stonemaster.stackmaster.common.util.PathConstants;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.answer.ComAnswerListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.CommPortIdentifierNotificationEvent;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.CommPortIdentifierNotificationListener;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.ConnectionThread;
import de.dennismaass.emp.stonemaster.stackmaster.controller.ui.utils.UiConstants;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.RxtxUtils;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

//TODO:

//Bugs:
//- Validierung aller Textfelder
//- Kein Absturz mehr nach Trennen vom USB-Anschluss
//- spinner problem

//Features:
//- Start-Ende (Rückkanal)
//- nur property event wenn sich auch echt was geändert worden ist
//- Baukasten
//- Mehrsprachigkeit
public class SwingStarter extends JFrame
		implements ComAnswerListener, CommPortIdentifierNotificationListener, ComConnectionPropertiesListener {

	private static final long serialVersionUID = 4155209335768313320L;

	private static final Color CONTENTCOLOR = new Color(0, 141, 212);
	private static final Color PANELCOLOR = new Color(221, 236, 250);
	public static final String FONT_NAME = "Arial";

	public static final String OS_NAME = System.getProperty("os.name");
	public static final String OS_ARCH = System.getProperty("os.arch");
	public static final String OS_VERSION = System.getProperty("os.version");
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String JAVA_HOME = System.getProperty("java.home");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor");

	public static final int ROUNDER = 100000000;
	public static final String UNIT = "mm";

	public static final double MIN_STEP = -250.0, MAX_STEP = 250.0;
	public static final int MAX_SPEED = 2047;

	private static Logger LOGGER = Logger.getLogger(SwingStarter.class);

	private ComCommunicator communicator;

	private Profile defaultProfile;
	private int microstepResolutionMode = 4, stepsPerMm = 64025;

	private ProfileFileHandler propertiesHandler;
	private ApplicationProperties applicationProperties;
	private ApplicationPropertiesFileHandler applicationPropertiesFileHandler = new ApplicationPropertiesFileHandler();

	private File defaultFile = new File(PathConstants.PATH_DEFAULT_STACKMASTER);
	private File applicationPropertyFile = new File(PathConstants.PATH_APPLICATION_PROPERTIES_FILENAME);
	private File actualOpenedProfileFile = defaultFile;

	@Getter
	@Setter
	private boolean unsavedChanges;

	private JPanel contentPane;
	private ConnectionThread connectThread;
	private JButton connectButton;
	private JComboBox<CommPortIdentifier> connectionComboBox;
	// private RelativPosPanel relativPosPanel;
	// private StepPanel stepPanel;
	private JMenuItem mntmProfilSpeichern, mntmProfilSpeichernAls;
	private PropertiesDialog propertiesDialog;
	public Font actualFont = new Font("Arial", Font.PLAIN, 20);
	private JLabel welcomeLabel, secondLineLabel, stateLine;
	private JMenuItem mntmExit, mntmLaden, mntmberCusa, mntmEinstellungen, mnEinstellungen, mnDatei;

	private AutoModePanel autoModePanel;

	private ManualModePanel manualModePanel;

	public double position;

	public SwingStarter() {

		List<CommPortIdentifier> actualPortList = RxtxUtils.getCommPortIdentifier();
		for (CommPortIdentifier commPortIdentifier : actualPortList) {
			LOGGER.info("name: " + commPortIdentifier.getName());
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		LOGGER.info("--------------------------------------------------------");
		LOGGER.info("os: name=" + OS_NAME + ", architecture=" + OS_ARCH + ", version=" + OS_VERSION);
		LOGGER.info("java: version=" + JAVA_VERSION + ", home=" + JAVA_HOME + ", vendor=" + JAVA_VENDOR);
		LOGGER.info("--------------------------------------------------------");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOGGER.info("closing window");
				handleExit();
			}
		});

		URL logo32URL = getClass().getResource(PathConstants.PATH_IMAGE + PathConstants.PATH_STACKMASTER_32X32_PNG);
		URL logo64URL = getClass().getResource(PathConstants.PATH_IMAGE + PathConstants.PATH_STACKMASTER_64X64_PNG);

		List<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(logo32URL).getImage());
		icons.add(new ImageIcon(logo64URL).getImage());
		setIconImages(icons);

		setTitle(UiConstants.TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 855, 640);

		propertiesHandler = new ProfileFileHandler();
		defaultProfile = propertiesHandler.read(defaultFile);

		propertiesDialog = createPropertiesDialog(defaultProfile.getProperties(), defaultProfile.getProperties());
		setTitle(UiConstants.TITLE + " - defaults");

		initMenu();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(CONTENTCOLOR);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));

		initConnectionPanel();

		initStatePanel();

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBackground(PANELCOLOR);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel welcome = new JPanel();

		tabbedPane.addTab("Start", null, welcome, null);
		welcome.setLayout(new MigLayout("", "[grow,center]", "[center][][][]"));
		welcome.setBackground(PANELCOLOR);

		welcomeLabel = new JLabel();
		welcome.add(welcomeLabel, "cell 0 0,alignx center");

		JLabel platzhalter1 = new JLabel("");
		welcome.add(platzhalter1, "cell 0 1");

		secondLineLabel = new JLabel("sdf");
		welcome.add(secondLineLabel, "cell 0 2,alignx left");

		// relativPosPanel = new RelativPosPanel(defaultProfile.getProperties(),
		// stateLine);
		// tabbedPane.addTab("relativ", null, relativPosPanel, null);
		//
		// stepPanel = new StepPanel(defaultProfile.getProperties(), stateLine);
		// tabbedPane.addTab("Schritte", null, stepPanel, null);

		manualModePanel = new ManualModePanel(defaultProfile.getProperties(), stateLine);
		manualModePanel.setBackground(PANELCOLOR);
		tabbedPane.addTab("Manuell", null, manualModePanel, null);

		autoModePanel = new AutoModePanel(defaultProfile.getProperties(), stateLine, this);
		autoModePanel.setBackground(PANELCOLOR);
		tabbedPane.addTab("Automatisch", null, autoModePanel, null);

		connectThread = createConnectionThread();
		connectThread.start();

		setAllComponentsDisableState(true);

		LOGGER.info("user interface builded");

		if (applicationPropertyFile.exists()) {

			applicationProperties = applicationPropertiesFileHandler.read(applicationPropertyFile);
			setNewFont(applicationProperties.getFontSize());
		} else {
			LOGGER.error("No application.properties available!");
		}
		setConnectionProperties(defaultProfile.getProperties());

	}

	protected void initStatePanel() {
		JPanel statePanel = new JPanel();
		statePanel.setBackground(PANELCOLOR);
		statePanel.setPreferredSize(new Dimension(10, 75));
		statePanel.setMinimumSize(new Dimension(10, 75));
		contentPane.add(statePanel, BorderLayout.SOUTH);

		JLabel logo1 = new JLabel();
		URL url1 = getClass().getResource("/images/Logo-kontur-transparent.png");
		ImageIcon icon1 = new ImageIcon(url1);
		icon1 = ImageUtils.getResizedImage(icon1, 270, 52);
		logo1.setIcon(icon1);
		logo1.setAlignmentX(Component.CENTER_ALIGNMENT);
		statePanel.add(logo1);

		JLabel spacer1 = new JLabel("           ");
		spacer1.setAlignmentX(Component.CENTER_ALIGNMENT);
		statePanel.add(spacer1);

		JLabel logo = new JLabel();
		URL url = getClass().getResource("/images/stackmaster_64x64.png");
		ImageIcon icon = new ImageIcon(url);
		logo.setIcon(icon);
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		statePanel.add(logo);
	}

	protected void initConnectionPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(PANELCOLOR);
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

		setFavoriteConnectionIfAvailable();

		connectButton = new JButton("Verbinden");
		connectButton.setEnabled(false);
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(connectButton);
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleConnect();
			}
		});

		stateLine = new JLabel("");
		panel.add(stateLine);

	}

	protected void setFavoriteConnectionIfAvailable() {
		String comConnectionName = defaultProfile.getProperties().getComConnectionName();
		CommPortIdentifier commPortIdentifier = getComPortName(comConnectionName);
		if (commPortIdentifier != null) {
			connectionComboBox.setSelectedItem(commPortIdentifier);
		}
	}

	private CommPortIdentifier getComPortName(String comConnectionName) {
		int itemCount = connectionComboBox.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			CommPortIdentifier commPortIdentifier = connectionComboBox.getItemAt(i);
			if (commPortIdentifier.getName().equals(comConnectionName)) {
				return commPortIdentifier;
			}
		}
		return null;
	}

	protected void handleConnect() {
		LOGGER.info("click connectbutton...");

		if (communicator == null) {
			LOGGER.info("communicator==null");

			Object selectedItem = connectionComboBox.getSelectedItem();
			if (selectedItem instanceof CommPortIdentifier) {
				CommPortIdentifier commPortIdentifier = (CommPortIdentifier) selectedItem;

				if (commPortIdentifier.isCurrentlyOwned()) {
					LOGGER.info(commPortIdentifier.getName() + " in use");
					stateLine.setText(commPortIdentifier.getName() + " wird von einem anderen Programm verwendet");
				} else {
					LOGGER.info(commPortIdentifier.getName() + "not in use");

					stateLine.setText("Verbindung wird hergestellt...");
					ComCommunicator communicator = createCommunicator(commPortIdentifier.getName());
					setCommunicator(communicator);

					// relativPosPanel.setCommunicator(communicator);
					// stepPanel.setCommunicator(communicator);
					manualModePanel.setCommunicator(communicator);
					autoModePanel.setCommunicator(communicator);
				}
			}
		}

		if (!communicator.isConnected()) {
			LOGGER.info("no connection etablished, try to build a connection");

			boolean connect = false;
			try {
				connect = communicator.connect();
			} catch (PortInUseException e) {
				e.printStackTrace();
				stateLine.setText(communicator.getComPort() + " wird von einem anderen Programm verwendet");
			}
			if (connect) {
				LOGGER.info("connection etablished with " + communicator.getComPort());

				connectButton.setText("Trennen");
				stateLine.setText("Verbindung mit " + communicator.getComPort() + " hergestellt");
				setAllComponentsDisableState(false);
				connectionComboBox.setEnabled(false);

				connectThread.setRunning(false);

				communicator.setMicrostepResolution(microstepResolutionMode);
			} else {
				LOGGER.info("connection not etablished with " + communicator.getComPort());
			}
		} else {
			LOGGER.info("connection always etablished, try to disconnect...");

			communicator.stop();
			communicator.disconnect();
			connectButton.setText("Verbinden");
			stateLine.setText("Verbindung mit " + communicator.getComPort() + " getrennt");
			setAllComponentsDisableState(true);
			connectionComboBox.setEnabled(true);

			connectThread = createConnectionThread();
			connectThread.start();
			communicator = null;
		}
	}

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}

	private void setAllComponentsDisableState(boolean disableState) {
		// stepPanel.setAllComponentsDisableState(disableState);
		// relativPosPanel.setAllComponentsDisableState(disableState);
		manualModePanel.setAllComponentsDisableState(disableState);
		autoModePanel.disableAllComponents(disableState);
	}

	protected void initMenu() {
		/** Menu */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		URL deleteURL = getClass().getResource(PathConstants.PATH_IMAGE + PathConstants.PATH_DELETE_ICON_NAME);
		ImageIcon deleteIcon = new ImageIcon(deleteURL);
		ImageIcon resizedDeleteIcon = ImageUtils.getResizedImage(deleteIcon, 15, 15);

		mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(resizedDeleteIcon);
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleExit();

			}
		});

		mntmLaden = new JMenuItem("Profil laden");
		mntmLaden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleLoadProperties();
			}
		});
		mnDatei.add(mntmLaden);

		mntmProfilSpeichern = new JMenuItem("Profil speichern");
		mntmProfilSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmProfilSpeichern.setEnabled(false);
		mntmProfilSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveProperties();

			}
		});
		mnDatei.add(mntmProfilSpeichern);

		mntmProfilSpeichernAls = new JMenuItem("Profil speichern unter...");
		mntmProfilSpeichernAls.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveAsProperties();
			}
		});
		mnDatei.add(mntmProfilSpeichernAls);
		mnDatei.add(mntmExit);

		URL helpURL = getClass().getResource(PathConstants.PATH_IMAGE + PathConstants.PATH_INFO_ICON_PNG);
		ImageIcon helpIcon = new ImageIcon(helpURL);

		ImageIcon resizedHelpIcon = ImageUtils.getResizedImage(helpIcon, 15, 15);

		mnEinstellungen = new JMenu("Extras");
		menuBar.add(mnEinstellungen);

		mntmEinstellungen = new JMenuItem("Einstellungen");
		mnEinstellungen.add(mntmEinstellungen);
		mntmEinstellungen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				propertiesDialog.setVisible(true);

			}
		});

		mntmberCusa = new JMenuItem("\u00DCber");
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

	protected ConnectionThread createConnectionThread() {
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
				LOGGER.info("add new Connection " + commPortIdentifier.getName());
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

		LOGGER.info(connectionComboBox.getItemCount() + " connections available");
		if (connectionComboBox.getItemCount() > 0) {
			connectionComboBox.setSelectedIndex(0);
			connectButton.setEnabled(true);
		}
	}

	@Override
	public void handleCommPortIdentifierNotificationEvent(CommPortIdentifierNotificationEvent event) {
		refreshConnectionComboBox(event.getCommPortIdentifierList());
		setFavoriteConnectionIfAvailable();
	}

	@Override
	public void handleAnswer(ComAnswerEvent e) {
		if (e.getInstruction() == 6) {
			LOGGER.info("Value in mm: " + e.getValueInMillimeter());
			position = e.getValueInMillimeter();
		}
	}

	protected void handleExit() {
		LOGGER.info("Exit");

		if (communicator != null) {
			communicator.stop();
			communicator.disconnect();
		}
		if (applicationProperties != null && applicationProperties.isFirstUse()) {
			applicationProperties.setFirstUse(false);
			applicationPropertiesFileHandler.write(applicationPropertyFile, applicationProperties);
		}

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
		JOptionPane.showMessageDialog(getFrame(),
				"\"StackMaster\" ist ein Produkt aus der Produktreihe \"stonemaster\" \n der Firma E-mP Ernst-mechanische Produkte.\n\n"
						+ "Das Programm zur Steuerung des Stackmasters wurde in Zusammenarbeit\nvon Dennis Maaß, Karlsruhe und Johannes Müller, Saarwellingen entwickelt.",
				"Über", JOptionPane.PLAIN_MESSAGE);
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

	@Override
	public void handleComConnectionPropertiesChangeEvent(ComConnectionPropertiesChangeEvent event) {
		ComConnectionProperties connectionProperties = event.getConnectionProperties();
		setConnectionProperties(connectionProperties);

		setUnsavedChanges(true);
	}

	public void setConnectionProperties(ComConnectionProperties connectionProperties) {
		LOGGER.info("set new connection properties: " + connectionProperties);

		// stepPanel.setVariablesFromProperties(connectionProperties);
		// stepPanel.refreshDistance();
		// stepPanel.refreshSleep();
		// relativPosPanel.setVariablesFromProperties(connectionProperties);
		autoModePanel.setProperties(connectionProperties);
		manualModePanel.setProperties(connectionProperties);
		propertiesDialog.setConnectionProperties(connectionProperties);

		if (communicator != null) {
			communicator.setStepsPerMm(connectionProperties.getStepsPerMm());
			communicator.setMicrostepResolution(connectionProperties.getMicrostepResolutionMode());
		}

		defaultProfile.setProperties(connectionProperties);

		if (applicationProperties != null) {
			String text = "Willkommen";
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
				text2 += "<html>" + "<br>" + "<br>" + "Ihre letzten Verbindung war mit ComAnschluss \""
						+ defaultProfile.getProperties().getComConnectionName() + "\"<br>" + "<br>" + "<br>"
						+ "Bei Problemen mit der Software schreiben sie uns unter support@stonemaster.eu." + "<br>"
						+ "<br>" + "Bei Ideen zur Weiterentwicklung unter ideen@stonemaster.eu." + "</html>";
			} else {
				text2 += "<html>Wir wünschen Ihnen viel Spass mit dem Stackmaster!</html>";
			}
			secondLineLabel.setText(text2);
		}
	}

	protected void handleSaveProperties() {
		LOGGER.info("chosen file: " + actualOpenedProfileFile.getAbsolutePath());
		applicationProperties.setFirstUse(false);
		propertiesHandler.write(actualOpenedProfileFile, defaultProfile);
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
				Profile loadedConnectionProperties = propertiesHandler.read(actualOpenedProfileFile);
				setConnectionProperties(loadedConnectionProperties.getProperties());
				setTitle(UiConstants.TITLE + " - " + selectedFile.getAbsolutePath());
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
				boolean acceptable = true;
				if (selectedFile.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION) {
						acceptable = false;
					}
				}
				if (acceptable) {
					String absolutePath = selectedFile.getAbsolutePath();

					if (!absolutePath.endsWith(PathConstants.FILE_ENDING_PROFILE)) {
						absolutePath = absolutePath + PathConstants.FILE_ENDING_PROFILE;
						selectedFile = new File(absolutePath);
					}
					LOGGER.info("chosen file: " + absolutePath);
					actualOpenedProfileFile = selectedFile;
					propertiesHandler.write(actualOpenedProfileFile, defaultProfile);
					setTitle(UiConstants.TITLE + " - " + absolutePath);
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
