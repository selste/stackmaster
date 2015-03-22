package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import net.miginfocom.swing.MigLayout;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

public class AutoModePanel extends JPanel {

	private static final long serialVersionUID = 1042974763111948238L;
	
	private static final Logger LOGGER = Logger.getLogger(AutoModePanel.class);
	
	private static final String IMAGES = "/images/";
	private static final String SLOW = "Langsam";
	private static final String NORMAL = "Normal";
	private static final String FAST = "Schnell";
	
	private final SwingStarter starter;

	private ComConnectionProperties properties;
	
	private ButtonGroup speed;
	
	private JCheckBox mirrorCheckBox;
	
	private JRadioButton slowRadioButton, normalRadioButton, fastRadioButton;
	
	private ImageIcon upImage, downImage;

	private JButton saveStartButton, saveEndButton, startButton,
				stopButton, pauseButton, resetButton, upButton,
				downButton;
	
	private JLabel stateLine, relMoveLabel, autoPathLabel, 
				startPosLabel, endPosLabel, speedLabel,
				stepSizeLabel;

	private JSpinner stepSizeSpinner;

	private JLabel picsMadeLabel;

	private JLabel calculatedPicsLabel;

	private long sleepMovementMirror = 1000l, sleepMirrorPicture = 1000l, sleepWhileMove = 1000l,
			sleepPictureMovement = 1000l, pulseDuration = 1000l;
	
	private int upSpeed, downSpeed;

	protected ComCommunicator communicator;
	
	protected Double startPos, endPos;

	public AutoModePanel(ComConnectionProperties properties, final JLabel stateLine, SwingStarter starter) {
		this.properties = properties;
		setStateLine(stateLine);
		this.starter = starter;
		
		this.setLayout(new MigLayout("debug", "[] []30[] [] []", "[]20[][][][][][]"));
		
		upSpeed = properties.getMiddleUpSpeed();
		downSpeed = properties.getMiddleDownSpeed();
		
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();
		
		mirrorCheckBox = new JCheckBox("Spiegelvorauslösung");
		stepSizeSpinner = new JSpinner();
		startPos = new Double(0.0);
		endPos = new Double(0.0);
		
		initIcons();
		defineLabels();
		defineButtons();
		createRadioGroup();
		assignListeners();
		
		buildLayout();
	}

	private void createRadioGroup() {
		slowRadioButton = new JRadioButton(SLOW);
		normalRadioButton = new JRadioButton(NORMAL);
		normalRadioButton.setSelected(true);
		fastRadioButton = new JRadioButton(FAST);
		
		speed = new ButtonGroup();
		speed.add(slowRadioButton);
		speed.add(normalRadioButton);
		speed.add(fastRadioButton);
	}

	private void buildLayout() {
		this.add(relMoveLabel, "center, span 2");
		this.add(autoPathLabel, "center, span 3, wrap");
		
		this.add(startPosLabel, "cell 2 1");
		this.add(saveStartButton, "wrap");
		
		this.add(speedLabel, "left");
		this.add(endPosLabel, "cell 2 2");
		this.add(saveEndButton, "wrap");
		
		this.add(slowRadioButton, "left");
		this.add(upButton);
		this.add(mirrorCheckBox, "left, wrap");
		
		this.add(normalRadioButton, "left");
		this.add(stepSizeLabel, "cell 2 4");
		this.add(stepSizeSpinner, "right, span 2, pushx, growx, wrap");
		
		this.add(fastRadioButton, "left");
		this.add(downButton);
		this.add(startButton, "split 3");
		this.add(stopButton);
		this.add(pauseButton, "center, wrap");
		
		this.add(picsMadeLabel, "cell 2 6, center");
		this.add(calculatedPicsLabel, "left");
		this.add(resetButton, "left, wrap");
	}

	private void defineLabels() {
		relMoveLabel = new JLabel("relative Bewegung");
		autoPathLabel = new JLabel("Steuerung Automatik, Streckenbasiert");
		startPosLabel = new JLabel("Startposition: " + "leer");
		endPosLabel = new JLabel("Endposition: " + "leer");
		speedLabel = new JLabel("Bewegungsgeschwindigkeit");
		stepSizeLabel = new JLabel("Schrittgröße [mm]: ");
		picsMadeLabel = new JLabel("Gemachte Bilder / Maximale Anzahl: ");
		calculatedPicsLabel = new JLabel("Y" +  " / " + "X");
	}

	private void assignListeners() {
		
		slowRadioButton.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (slowRadioButton.isSelected()) {
					upSpeed = properties.getSlowUpSpeed();
					downSpeed = properties.getSlowDownSpeed();
				}
			}
		});
		
		normalRadioButton.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (normalRadioButton.isSelected()) {
					upSpeed = properties.getMiddleUpSpeed();
					downSpeed = properties.getMiddleDownSpeed();
				}
			}
		});
		
		fastRadioButton.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (fastRadioButton.isSelected()) {
					upSpeed = properties.getFastUpSpeed();
					downSpeed = properties.getFastDownSpeed();
				}
			}
		});
		
		upButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stopping motor");
						communicator.stop();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("rotate left with speed: " + upSpeed);
					communicator.rotateLeft(upSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping motor");
					communicator.stop();
				}
			}
		});
		
		downButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stopping motor");
						communicator.stop();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("rotating right with speed: " + downSpeed);
					communicator.rotateRight(downSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping Motor");
					communicator.stop();
				}
			}	
		});
		
		saveStartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startPos = Double.parseDouble("" + starter.position);
			}
		});
		
		saveEndButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				endPos = Double.parseDouble("" + starter.position);
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//1. Zu startposition bewegen
				//2. in Schritten zu endposition bewegen
				//3. nach jedem Schritt Foto machen
				// nach letztem Foto beenden
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//bewegung und Fotosession beenden
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Aktuelle Aktion pausieren
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startPos = Double.parseDouble("0.0");
				endPos = Double.parseDouble("0.0");
				if (!normalRadioButton.isSelected()) {
					normalRadioButton.setSelected(true);
				}
			}
		});
		
		stepSizeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				//TODO
				
			}
		});
	}

	private void initIcons() {
		URL upUrl = getClass().getResource(IMAGES + "up_blue2.png");
		URL downUrl = getClass().getResource(IMAGES + "down_blue2.png");
		
		upImage = new ImageIcon(upUrl);
		upImage = ImageUtils.getResizedImage(upImage, 30, 30);
		downImage = new ImageIcon(downUrl);
		downImage = ImageUtils.getResizedImage(downImage, 30, 30);
	}

	private void defineButtons() {
		saveStartButton = new JButton("Startpunkt Speichern");
		saveEndButton = new JButton("Endpunkt Speichern");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		pauseButton = new JButton("Pause");
		resetButton = new JButton("reset");
		upButton = new JButton(upImage);
		downButton = new JButton(downImage);
	}

	private void setStateLine(JLabel stateLine) {
		this.stateLine = stateLine;
	}

	public ComCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}

}

