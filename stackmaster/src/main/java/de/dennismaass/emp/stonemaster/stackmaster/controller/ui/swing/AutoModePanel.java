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

	private JButton saveStartButton, saveEndButton, startButton,
	stopButton, pauseButton, resetButton, upButton,
	downButton, moveStartButton;

	private JLabel stepSizeLabel, distanceLabel,
	picsMadeLabel, calculatedPicsLabel, calculatedPathLabel;

	private JSpinner stepSizeSpinner;

	private long sleepMovementMirror = 1000l, sleepMirrorPicture = 1000l, sleepWhileMove = 1000l,
			sleepPictureMovement = 1000l, pulseDuration = 1000l;

	protected ComCommunicator communicator;

	protected double startPos;
	protected double endPos;


	protected boolean stop = false;

	protected boolean pause = false;

	private int picsMax, picsCurrent = 0;

	private JLabel stateLine;

	public AutoModePanel(ComConnectionProperties properties, final JLabel stateLine, SwingStarter starter) {
		this.properties = properties;

		this.starter = starter;

		this.setLayout(new MigLayout("", "[] []30[]10[] []", "[]20[][][][][][][]"));

		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();

		mirrorCheckBox = new JCheckBox("Spiegelvorauslösung");
		mirrorCheckBox.setFont(actualFont);
		stepSizeSpinner = new JSpinner();
		stepSizeSpinner.setFont(actualFont);
		stepSizeSpinner.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.0001, 250.0, 0.0001));
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
		this.add(saveStartButton, "cell 3 1, wrap");
		this.add(saveEndButton, "cell 3 2, wrap");

		this.add(mirrorCheckBox, "cell 3 3, left, wrap");

		this.add(distanceLabel, "cell 2 4, right");
		this.add(calculatedPathLabel, "left, wrap");

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
		stepSizeLabel = new JLabel("Schrittgröße [mm]: ");
		stepSizeLabel.setFont(actualFont);
		picsMadeLabel = new JLabel("Bilder (ist/soll): ");
		picsMadeLabel.setFont(actualFont);
		calculatedPicsLabel = new JLabel("Y" +  " / " + "X");
		calculatedPicsLabel.setFont(actualFont);
		distanceLabel = new JLabel("Zurückgelegter Weg:");
		distanceLabel.setFont(actualFont);
		calculatedPathLabel = new JLabel("0 mm");
		calculatedPathLabel.setFont(actualFont);
	}

	private void assignListeners() {
		saveStartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.getPosition();
				try {
					Thread.sleep(300l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				startPos = starter.position;
				actualizePathLabel();
				initializePictureCountLabel();
			}
		});

		saveEndButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.getPosition();
				try {
					Thread.sleep(300l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				endPos = starter.position;
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

					communicator.moveTo(startPos);
					Thread job = createThread(stepSize);
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
				startPos = 0;
				endPos = 0;
				if (mirrorCheckBox.isSelected()) {
					mirrorCheckBox.setSelected(false);
				}
				stepSizeSpinner.setValue(properties.getStepSize());
				calculatedPathLabel.setText("0 mm");
				initializePictureCountLabel();
			}
		});

		stepSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if ((double) stepSizeSpinner.getValue() > ComCommunicator.MAX_STEP) {
					stepSizeSpinner.setValue(ComCommunicator.MAX_STEP);
				} else if((double) stepSizeSpinner.getValue() < ComCommunicator.MIN_STEP) {
					stepSizeSpinner.setValue(ComCommunicator.MIN_STEP);
				}
			}
		});
	}

	protected void actualizePathLabel() {
		if (startPos > endPos) {
			calculatedPathLabel.setText(df.format((startPos - endPos)) + " mm");
		} else {
			calculatedPathLabel.setText(df.format((endPos - startPos)) + " mm");
		}
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

	protected Thread createThread(final double stepSize) {
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
					Thread.sleep(2500l);
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
	}

	public void setProperties(ComConnectionProperties properties) {
		this.properties = properties;
		setVariablesFromProperties(properties);
	}
}

