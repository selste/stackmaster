package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.util.Constants;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

public class AutoModePanel extends JPanel {

	private static final String END_LBL_TXT = "Endposition: ";

	private static final String START_LBL_TXT = "Startposition: ";

	private static final long serialVersionUID = 1042974763111948238L;
	
	private static final Color PANELCOLOR = new Color(221, 236, 250);
	
	private static final Logger LOGGER = Logger.getLogger(AutoModePanel.class);
	
	private static final String IMAGES = "/images/";
	private static final String SLOW = "Langsam";
	private static final String NORMAL = "Normal";
	private static final String FAST = "Schnell";
	
	private final SwingStarter starter;
	
	public Font actualFont = new Font("Arial", Font.PLAIN, 20);

	private ComConnectionProperties properties;
	
	private ButtonGroup speed;
	
	private DecimalFormat df = new DecimalFormat("0.0000");
	
	private JCheckBox mirrorCheckBox;
	
	private JRadioButton slowRadioButton, normalRadioButton, fastRadioButton;
	
	private ImageIcon upImage, downImage;

	private JButton saveStartButton, saveEndButton, startButton,
				stopButton, pauseButton, resetButton, upButton,
				downButton, moveStartButton;
	
	private JLabel stateLine, relMoveLabel, autoPathLabel, 
				startPosLabel, endPosLabel, speedLabel,
				stepSizeLabel, distanceLabel, picsMadeLabel,
				calculatedPicsLabel, calculatedPathLabel;

	private JSpinner stepSizeSpinner;

	private long sleepMovementMirror = 1000l, sleepMirrorPicture = 1000l, sleepWhileMove = 1000l,
			sleepPictureMovement = 1000l, pulseDuration = 1000l;
	
	private int upSpeed, downSpeed;

	protected ComCommunicator communicator;
	
	protected double startPos, endPos;

	protected boolean stop = false;

	protected boolean pause = false;

	private int picsMax, picsCurrent = 0;

	public AutoModePanel(ComConnectionProperties properties, final JLabel stateLine, SwingStarter starter) {
		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);
		this.starter = starter;
		
		this.setLayout(new MigLayout("", "[] []30[]10[] []", "[]20[][][][][][][]"));
		
		upSpeed = properties.getMiddleUpSpeed();
		downSpeed = properties.getMiddleDownSpeed();
		
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();
		
		mirrorCheckBox = new JCheckBox("Spiegelvorauslösung");
		mirrorCheckBox.setFont(actualFont);
		mirrorCheckBox.setBackground(PANELCOLOR);
		stepSizeSpinner = new JSpinner();
		stepSizeSpinner.setFont(actualFont);
		stepSizeSpinner.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.0001, 250.0, 0.0001));
		((JSpinner.NumberEditor) stepSizeSpinner.getEditor()).getFormat().setMaximumFractionDigits(4);
		
		initIcons();
		defineLabels();
		defineButtons();
		createRadioGroup();
		assignListeners();
		
		buildLayout();
		disableAllComponents(true);
	}

	private void createRadioGroup() {
		slowRadioButton = new JRadioButton(SLOW);
		slowRadioButton.setFont(actualFont);
		slowRadioButton.setBackground(PANELCOLOR);
		normalRadioButton = new JRadioButton(NORMAL);
		normalRadioButton.setFont(actualFont);
		normalRadioButton.setSelected(true);
		normalRadioButton.setBackground(PANELCOLOR);
		fastRadioButton = new JRadioButton(FAST);
		fastRadioButton.setFont(actualFont);
		fastRadioButton.setBackground(PANELCOLOR);
		
		speed = new ButtonGroup();
		speed.add(slowRadioButton);
		speed.add(normalRadioButton);
		speed.add(fastRadioButton);
	}

	private void buildLayout() {
		this.add(relMoveLabel, "center, span 2");
		this.add(autoPathLabel, "center, span 3, wrap");
		
		this.add(startPosLabel, "cell 2 1, right");
		this.add(saveStartButton, "wrap");
		
		this.add(speedLabel, "left");
		this.add(endPosLabel, "cell 2 2, right");
		this.add(saveEndButton, "wrap");
		
		this.add(slowRadioButton, "left");
		this.add(upButton);
		this.add(mirrorCheckBox, "cell 3 3, left, wrap");
		
		this.add(normalRadioButton, "left");
		this.add(distanceLabel, "cell 2 4, right");
		this.add(calculatedPathLabel, "left, wrap");
			
		this.add(fastRadioButton, "left");
		this.add(downButton);
		this.add(stepSizeLabel, "cell 2 5, right");
		this.add(stepSizeSpinner, "right, span 2, pushx, growx, wrap");
		
		this.add(moveStartButton, "cell 2 6, center");
		this.add(startButton, "split 3");
		this.add(stopButton);
		stopButton.setEnabled(false);
		this.add(pauseButton, "center, wrap");
		pauseButton.setEnabled(false);
		
		this.add(picsMadeLabel, "cell 2 7, right");
		this.add(calculatedPicsLabel, "left");
		this.add(resetButton, "left, wrap");
	}

	private void defineLabels() {
		relMoveLabel = new JLabel("Relative Bewegung");
		relMoveLabel.setFont(actualFont);
		autoPathLabel = new JLabel("Steuerung Automatik, Streckenbasiert");
		autoPathLabel.setFont(actualFont);
		startPosLabel = new JLabel(START_LBL_TXT + "leer");
		startPosLabel.setFont(actualFont);
		endPosLabel = new JLabel(END_LBL_TXT + "leer");
		endPosLabel.setFont(actualFont);
		speedLabel = new JLabel("Geschwindigkeit:");
		speedLabel.setFont(actualFont);
		stepSizeLabel = new JLabel("Schrittgröße [mm]: ");
		stepSizeLabel.setFont(actualFont);
		picsMadeLabel = new JLabel("Bilder (ist/soll): ");
		picsMadeLabel.setFont(actualFont);
		calculatedPicsLabel = new JLabel("Y" +  " / " + "X");
		calculatedPicsLabel.setFont(actualFont);
		distanceLabel = new JLabel("Zurückgelegter Weg:");
		distanceLabel.setFont(actualFont);
		calculatedPathLabel = new JLabel("x");
		calculatedPathLabel.setFont(actualFont);
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
						communicator.getPosition();
						communicator.stop();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("rotate left with speed: " + upSpeed);
					communicator.rotateRight(upSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping motor");
					communicator.getPosition();
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
						communicator.getPosition();
						communicator.stop();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("rotating right with speed: " + downSpeed);
					communicator.rotateLeft(downSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping Motor");
					communicator.getPosition();
					communicator.stop();
				}
			}	
		});
		
		saveStartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startPos = starter.position;
				startPosLabel.setText(START_LBL_TXT + df.format(startPos));
				actualizePathLabel();
				initializePictureCountLabel();
			}
		});
		
		saveEndButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				endPos = starter.position;
				endPosLabel.setText(END_LBL_TXT + df.format(endPos));
				actualizePathLabel();
				initializePictureCountLabel();
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
					
					Thread job = createThread(stepSize);
					job.start();	
				}
			}
		});
		
		moveStartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.getPosition();
				double pos;
				if (endPos > startPos && startPos != starter.position) {
					pos = startPos - 2.0;
					communicator.moveTo(pos);
					communicator.moveTo(startPos);
				} else if (endPos < startPos && starter.position != startPos){
					pos = startPos + 2.0;
					communicator.moveTo(pos);
					communicator.moveTo(startPos);
				}
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.stop();
				stop = true;
				stopButton.setEnabled(false);
				pause = false;
				pauseButton.setEnabled(false);
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) {
					pause = false;
					pauseButton.setText("Pause");
				} else {
					pause = true;
					pauseButton.setText("Fortsetzen");
				}
			}
		});
		
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startPos = 0;
				endPos = 0;
				startPosLabel.setText(START_LBL_TXT + "leer");
				endPosLabel.setText(END_LBL_TXT + "leer");
				if (mirrorCheckBox.isSelected()) {
					mirrorCheckBox.setSelected(false);
				}
				if (!normalRadioButton.isSelected()) {
					normalRadioButton.setSelected(true);
				}
				stepSizeSpinner.setValue(properties.getStepSize());
				initializePictureCountLabel();
			}
		});
		
		stepSizeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((double) stepSizeSpinner.getValue() > Constants.MAX_STEP) {
					stepSizeSpinner.setValue(Constants.MAX_STEP);
					initializePictureCountLabel();
				} else if((double) stepSizeSpinner.getValue() < Constants.MIN_STEP) {
					stepSizeSpinner.setValue(Constants.MIN_STEP);
					initializePictureCountLabel();
				}
			}
		});
	}

	protected void actualizePathLabel() {
		if (startPos > endPos) {
			calculatedPathLabel.setText(df.format((startPos - endPos)) + "mm");
		} else {
			calculatedPathLabel.setText(df.format((endPos - startPos)) + "mm");
		}
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
				setEnableStopAndPause(true);
				
				communicator.getPosition();
				
				while (starter.position != endPos) {
					if (stop) {
						break;
					}
					auslösen(pulseDuration);
					actualizePictureCountLabel();
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
					if (pause) {
						performPause();
					}
					if (startPos > endPos) {
						if ((starter.position - endPos) > stepSize) {
							communicator.move((stepSize)*-1);
							communicator.getPosition();
						} else {
							communicator.move((stepSize)*-1);
							break;
						}
					} else {
						if ((endPos - starter.position) > stepSize) {
							communicator.move((stepSize));
							communicator.getPosition();
						} else {
							communicator.move(stepSize);
							break;
						}
					}
					pause((int) (sleepWhileMove * Math.abs(stepSize)) + sleepMovementMirror);
					
					if(pause){
						performPause();
					}
					
				}
				//letzes Bild
				if (!stop) {
					auslösen(pulseDuration);
					actualizePictureCountLabel();
					
					if (mirrorCheckBox.isSelected()) {
						pause(sleepMirrorPicture);
						auslösen(pulseDuration);
					}
				}
				
				stop = false;
				pause = false;
				setEnableStopAndPause(false);
				disableAllComponents(false);
			}	
		};
		return job;
	}
	
	protected void performPause() {
		while(pause) {
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		
		saveStartButton.setEnabled(!disableState);
		saveEndButton.setEnabled(!disableState);
		moveStartButton.setEnabled(!disableState);
		
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

	protected void initializePictureCountLabel() {
		picsMax = 0;
		if (startPos > endPos) {
			double maximum = (startPos - endPos) / (double) stepSizeSpinner.getValue();
			picsMax = (int) maximum;
			picsMax += 1;
			if((maximum - picsMax) > 0)
				picsMax += 1;
//			picsMax = (int) ((startPos - endPos) / (double) stepSizeSpinner.getValue());
//			if (((startPos - endPos) % (double) stepSizeSpinner.getValue()) > 0) {
//				picsMax += 1;
//			}
		} else if(endPos > startPos) {
			double maximum = (endPos - startPos) / (double) stepSizeSpinner.getValue();
			picsMax = (int) maximum;
			picsMax += 1;
			if ((maximum - picsMax) > 0)
				picsMax += 1;		
//			picsMax = (int) ((endPos - startPos) / (double) stepSizeSpinner.getValue());
//			if (((endPos - startPos) % (double) stepSizeSpinner.getValue() ) > 0) {
//				picsMax += 1;
//			}
		}
		picsCurrent = 0;
		calculatedPicsLabel.setText(picsCurrent + "/" + picsMax);
	}
	
	protected void actualizePictureCountLabel() {
		picsCurrent += 1;
		calculatedPicsLabel.setText(picsCurrent + "/" + picsMax);
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
		saveStartButton.setFont(actualFont);
		saveEndButton = new JButton("Endpunkt Speichern");
		saveEndButton.setFont(actualFont);
		startButton = new JButton("Start");
		startButton.setFont(actualFont);
		stopButton = new JButton("Stop");
		stopButton.setFont(actualFont);
		pauseButton = new JButton("Pause");
		pauseButton.setFont(actualFont);
		resetButton = new JButton("reset");
		resetButton.setFont(actualFont);
		upButton = new JButton(upImage);
		downButton = new JButton(downImage);
		moveStartButton = new JButton("Zum Startpunkt");
		moveStartButton.setFont(actualFont);
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

	public void setVariablesFromProperties(ComConnectionProperties properties) {
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();
		
		upSpeed = properties.getMiddleUpSpeed();
		downSpeed = properties.getMiddleDownSpeed();
	}
	
	public void setProperties(ComConnectionProperties properties) {
		this.properties = properties;
		setVariablesFromProperties(properties);
	}
}

