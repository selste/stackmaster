package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;
import net.miginfocom.swing.MigLayout;

public class AutoModePanel extends JPanel {

	private static final long serialVersionUID = 1042974763111948238L;

	private static final Logger LOGGER = Logger.getLogger(AutoModePanel.class);

	private static final String IMAGES = "/images/";


	private final SwingStarter starter;

	public Font actualFont = new Font("Arial", Font.PLAIN, 20);

	private ComConnectionProperties properties;


	private DecimalFormat df = new DecimalFormat("0.0000");

	private JCheckBox mirrorCheckBox;

	private ImageIcon upImage, downImage;

	private JToggleButton saveStartButton, saveEndButton;

	private JButton startButton,
	stopButton, pauseButton, resetButton, moveStartButton;

	private JLabel stepSizeLabel, distanceLabel,
	picsMadeLabel, calculatedPicsLabel, calculatedPathLabel;

	private JSpinner stepSizeSpinner;

	private long sleepMovementMirror = 1000;
	private long sleepMirrorPicture = 1000;
	private long sleepOneMmWhileMoveWithThousand = 600;
	private long sleepPictureMovement = 1000;
	private long pulseDuration = 1000;

	protected ComCommunicator communicator;

	protected double startPos;
	protected double endPos;


	protected boolean stop = false;

	protected boolean pause = false;

	private int picsMax, picsCurrent = 0;

	private JLabel stateLine;
	private JLabel startLabel;
	private JLabel endLabel;

	public AutoModePanel(ComConnectionProperties properties, final JLabel stateLine, SwingStarter starter) {
		this.properties = properties;

		this.starter = starter;

		this.setLayout(new MigLayout("", "[] []30[]10[] []", "[]20[][][][][][][]"));

		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepOneMmWhileMoveWithThousand = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();

		mirrorCheckBox = new JCheckBox("Spiegelvorauslösung");
		mirrorCheckBox.setFont(actualFont);
		stepSizeSpinner = new JSpinner();
		stepSizeSpinner.setFont(actualFont);

		stepSizeSpinner.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.0001, ComCommunicator.MAX_STEP, 0.0001));
		((JSpinner.NumberEditor) stepSizeSpinner.getEditor()).getFormat().setMaximumFractionDigits(4);

		initIcons();
		defineLabels();
		defineButtons();
		assignListeners();

		setVariablesFromProperties(properties);
		this.stateLine=stateLine;

		buildLayout();
		disableAllComponents(true);
	}

	private void buildLayout() {
		this.add(startLabel, "cell 2 1, right");
		this.add(saveStartButton, "cell 3 1, wrap");
		this.add(endLabel, "cell 2 2, right");
		this.add(saveEndButton, "cell 3 2, wrap");

		this.add(mirrorCheckBox, "cell 3 3, left, wrap");

		this.add(distanceLabel, "cell 2 4, right");
		this.add(calculatedPathLabel, "left, wrap");

		this.add(stepSizeLabel, "cell 2 5, right");
		this.add(stepSizeSpinner, "right, span 2, pushx, growx, wrap");

		this.add(moveStartButton, "cell 2 6, center");
		moveStartButton.setEnabled(false);
		this.add(startButton, "split 3");
		startButton.setEnabled(false);
		this.add(stopButton);
		stopButton.setEnabled(false);
		this.add(pauseButton, "center, wrap");
		pauseButton.setEnabled(false);

		this.add(picsMadeLabel, "cell 2 7, right");
		this.add(calculatedPicsLabel, "left");
		this.add(resetButton, "left, wrap");
		resetButton.setEnabled(false);
	}

	private void defineLabels() {
		startLabel = new JLabel("Startpunkt");
		startLabel.setFont(actualFont);

		endLabel = new JLabel("Endpunkt");
		endLabel.setFont(actualFont);

		stepSizeLabel = new JLabel("Schrittgröße [mm]:");
		stepSizeLabel.setFont(actualFont);
		picsMadeLabel = new JLabel("Bilder (ist/soll):");
		picsMadeLabel.setFont(actualFont);
		calculatedPicsLabel = new JLabel("0" +  " / " + "0");
		calculatedPicsLabel.setFont(actualFont);
		distanceLabel = new JLabel("Weg Startpunkt - Endpunkt:");
		distanceLabel.setFont(actualFont);
		calculatedPathLabel = new JLabel("0,0000 mm");
		calculatedPathLabel.setFont(actualFont);
	}

	private void assignListeners() {
		saveStartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.getPosition();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				startPos = starter.position;

				if(saveEndButton.isSelected() && saveStartButton.isSelected()){
					actualizePathLabel();
					initializePictureCountLabel();

					moveStartButton.setEnabled(true);
					startButton.setEnabled(true);
					resetButton.setEnabled(true);
				}else{
					calculatedPathLabel.setText("0,0000 mm");
					calculatedPicsLabel.setText(0 + " / " + 0);

					moveStartButton.setEnabled(false);
					startButton.setEnabled(false);
					resetButton.setEnabled(false);
				}
			}
		});

		saveEndButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.getPosition();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				endPos = starter.position;

				if(saveEndButton.isSelected() && saveStartButton.isSelected()){
					actualizePathLabel();
					initializePictureCountLabel();

					moveStartButton.setEnabled(true);
					startButton.setEnabled(true);
					resetButton.setEnabled(true);

				}else{
					calculatedPathLabel.setText("0,0000 mm");
					calculatedPicsLabel.setText(0 + " / " + 0);

					moveStartButton.setEnabled(false);
					startButton.setEnabled(false);
					resetButton.setEnabled(false);
				}

			}
		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				initializePictureCountLabel();


				double stepSize = (double) stepSizeSpinner.getValue();
				boolean correctValue = validate(stepSize);
				if (correctValue) {
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					pauseButton.setEnabled(true);

					communicator.moveTo(startPos);
					Thread job = createThread(stepSize,properties.getMaxSpeed());
					job.start();
				}
			}
		});

		moveStartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				initializePictureCountLabel();
				double pos;
				if (endPos > startPos && startPos != starter.position) {
					pos = startPos - 1.0;
					communicator.moveTo(pos);
				} else if (endPos < startPos && starter.position != startPos){
					pos = startPos + 1.0;
					communicator.moveTo(pos);
				}
				communicator.getPosition();
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
				reset();
			}


		});

		stepSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				initializePictureCountLabel();
			}
		});
	}

	private void reset() {
		startPos = 0;
		endPos = 0;

		calculatedPathLabel.setText("0,0000 mm");
		calculatedPicsLabel.setText(0 + " / " + 0);

		saveStartButton.setSelected(false);
		saveEndButton.setSelected(false);
	}

	protected void actualizePathLabel() {
		calculatedPathLabel.setText(df.format(Math.abs(startPos - endPos)) + " mm");
	}

	protected boolean validate(double stepSize) {
		LOGGER.info("Validating size of steps: " + stepSize);
		if (stepSize > ComCommunicator.MIN_STEP && stepSize < ComCommunicator.MAX_STEP) {
			LOGGER.info("stepSize validated. Range is correct");
			return true;
		}
		LOGGER.info("stepSize validated. Range is incorrect");
		return false;
	}

	protected Thread createThread(final double stepSize,final double maxSpeed) {
		Thread job = new Thread() {

			@Override
			public void run() {
				disableAllComponents(true);
				setEnableStopAndPause(true);

				boolean needMove = false;

				if (startPos != endPos) {
					needMove = true;
				}

				double step;
				double path;
				if (startPos > endPos) {
					step = stepSize * -1;
					path = startPos - endPos;
				} else {
					step = stepSize;
					path = endPos - startPos;
				}
				if (path < 0) {
					path *= -1;
				}

				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (needMove) {
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
					if (path > stepSize) {
						communicator.move(step);
						path = path - stepSize;
					} else {
						communicator.move(step);
						needMove = false;
					}
					pause((int) ((sleepOneMmWhileMoveWithThousand*1000/maxSpeed) * Math.abs(stepSize)) + sleepMovementMirror);

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

				moveStartButton.setEnabled(true);
				startButton.setEnabled(true);
				resetButton.setEnabled(true);
			}
		};
		return job;
	}

	protected void performPause() {
		while(pause) {
			try {
				Thread.sleep(500);
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

		saveStartButton.setSelected(false);
		saveStartButton.setEnabled(!disableState);
		saveEndButton.setSelected(false);
		saveEndButton.setEnabled(!disableState);

		//		moveStartButton.setEnabled(!disableState);

		//		upButton.setEnabled(!disableState);
		//		downButton.setEnabled(!disableState);

		//		startButton.setEnabled(!disableState);
		//		resetButton.setEnabled(!disableState);

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
		picsMax = 1;
		double path;
		double divisor = (double) stepSizeSpinner.getValue();
		if (startPos > endPos) {
			path = startPos - endPos;
		} else {
			path = endPos - startPos;
		}
		double max = path / divisor;
		if (max != 0) {
			picsMax = (int) max + picsMax + 1;
		}
		picsCurrent = 0;
		calculatedPicsLabel.setText(picsCurrent + " / " + picsMax);
	}

	protected void actualizePictureCountLabel() {
		picsCurrent += 1;
		calculatedPicsLabel.setText(picsCurrent + " / " + picsMax);
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
		saveStartButton = new JToggleButton("setzen");
		saveStartButton.setFont(actualFont);
		saveEndButton = new JToggleButton("setzen");
		saveEndButton.setFont(actualFont);
		startButton = new JButton("Start");
		startButton.setFont(actualFont);
		stopButton = new JButton("Stop");
		stopButton.setFont(actualFont);
		pauseButton = new JButton("Pause");
		pauseButton.setFont(actualFont);
		resetButton = new JButton("Reset");
		resetButton.setFont(actualFont);
		moveStartButton = new JButton("zum Startpunkt");
		moveStartButton.setFont(actualFont);
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
		sleepOneMmWhileMoveWithThousand = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();
	}

	public void setProperties(ComConnectionProperties properties) {
		this.properties = properties;
		setVariablesFromProperties(properties);
	}
}

