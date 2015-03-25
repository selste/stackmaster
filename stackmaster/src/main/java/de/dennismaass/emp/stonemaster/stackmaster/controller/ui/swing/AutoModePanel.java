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
import de.dennismaass.emp.stonemaster.stackmaster.common.util.Constants;
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

	protected boolean stop = false;

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
		stopButton.setEnabled(false);
		this.add(pauseButton, "center, wrap");
		pauseButton.setEnabled(false);
		
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
				double stepSize = (double) stepSizeSpinner.getValue();
				boolean correctValue = validate(stepSize);
				if (correctValue) {
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					pauseButton.setEnabled(true);
					
					//1. Zu startposition bewegen
					if (startPos.doubleValue() > endPos.doubleValue()) {
						LOGGER.info("Startposition über Endposition, bewege zu start");
						communicator.moveTo(startPos.doubleValue() + 0.05);
						communicator.moveTo(startPos.doubleValue());
						//2. in Schritten zu endposition bewegen und Fotos machen
						Thread job = createThread(stepSize);
						job.start();
					} else if (startPos.doubleValue() < endPos.doubleValue()) {
						LOGGER.info("Startposition unter Endposition, bewege zu start");
						communicator.moveTo(startPos.doubleValue() - 0.05);
						communicator.moveTo(startPos.doubleValue());
						//2. in Schritten zu endposition bewegen und Fotos machen
						Thread job = createThread(stepSize);
						job.start();
					} else {
						LOGGER.info("Startposition gleich Endposition, bewege zu start");
						communicator.moveTo(startPos.doubleValue());
						Thread job = createThread(stepSize);
						job.start();
					}
				}
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.stop();
				stop = true;
				stopButton.setEnabled(false);
				
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseButton.setEnabled(false);
				
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

	protected boolean validate(double stepSize) {
		LOGGER.info("Validating size of steps: " + stepSize);
		if (stepSize > Constants.MIN_STEP && stepSize < Constants.MAX_STEP) {
			LOGGER.info("stepSize validated. Range is correct");
			return true;
		}
		LOGGER.info("stepSize validated. Range is incorrect");
		return false;
	}
	
	protected Thread createThread(final double stepSize) {
		Thread job = new Thread() {

			@Override
			public void run() {
				disableAllComponents(true);
				setEnableStopAndPause(false);
				
				while (startPos.doubleValue() != endPos.doubleValue()) {
					if (stop) {
						break;
					}
					auslösen(pulseDuration);
					if (mirrorCheckBox.isSelected()) {
						pause(sleepMirrorPicture);
						if (stop) {
							break;
						}
						auslösen(pulseDuration);
					}
					
					//bewegung
					pause(sleepPictureMovement);
					if (stop) {
						break;
					}
					if (startPos.doubleValue() > endPos.doubleValue()) {					
						if ((starter.position - endPos.doubleValue()) > stepSize) {
							communicator.move(stepSize);
						} else {
							communicator.moveTo(endPos.doubleValue());
						}
					} else {
						if ((endPos.doubleValue() - starter.position) > stepSize) {
							communicator.move((stepSize)*-1); 
						} else {
							communicator.moveTo(endPos.doubleValue());
						}
					}
				}
				//letzes Bild
				if (!stop) {
					auslösen(pulseDuration);
					
					if (mirrorCheckBox.isSelected()) {
						pause(sleepMirrorPicture);
						auslösen(pulseDuration);
					}
				}
				
				stop = false;
				setEnableStopAndPause(false);
				disableAllComponents(false);
			}	
		};
		return job;
	}
	
	protected void pause(long pauseTime) {
		try {
			Thread.sleep(pauseTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void disableAllComponents(boolean disableState) {
		if (!disableState) {
			LOGGER.info("enable all components");
		} else {
			LOGGER.info("disable all components");
		}
		
		upButton.setEnabled(!disableState);
		downButton.setEnabled(!disableState);
		
		slowRadioButton.setEnabled(!disableState);
		normalRadioButton.setEnabled(!disableState);
		fastRadioButton.setEnabled(!disableState);
		
		startButton.setEnabled(!disableState);
		resetButton.setEnabled(!disableState);
		
		mirrorCheckBox.setEnabled(!disableState);
		
		stepSizeSpinner.setEnabled(!disableState);
	}
	
	protected void setEnableStopAndPause(boolean state) {
		stopButton.setEnabled(state);
		pauseButton.setEnabled(state);
	}
	
	protected void auslösen(long pulseDuration) {
		communicator.setSIO(2, true);
		try {
			Thread.sleep(pulseDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		communicator.setSIO(2, false);
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

