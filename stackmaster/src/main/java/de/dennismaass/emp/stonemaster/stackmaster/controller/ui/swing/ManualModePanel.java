package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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
import de.dennismaass.emp.stonemaster.stackmaster.common.util.Constants;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import net.miginfocom.swing.MigLayout;

public class ManualModePanel extends JPanel {

	private static final long serialVersionUID = 5989898071317069721L;

	private static Logger LOGGER = Logger.getLogger(ManualModePanel.class);

	private JButton pauseButton,
	stopButton, resetButton, executeButton;

	public Font actualFont = new Font("Arial", Font.PLAIN, 20);

	private JLabel stateLine;

	private ComCommunicator communicator;

	private ComConnectionProperties properties ;

	private JCheckBox checkMirror;

	private JLabel relativeLabel, speedLabel, picCountLabel,
	stepSizeLabel, picsTakenLabel, pathTraveledLabel,
	assumedTimeLabel, nessecaryPathLengthLabel, picsNumberLabel,
	pathDistanceLabel, timeNumberLabel, pathLengthLabel;

	private JSpinner stepSpinner, picSpinner;

	private DecimalFormat df = new DecimalFormat("0.0000");

	protected boolean pause, stop, reverseStep = false;

	protected double lastManStep;

	private long sleepMovementMirror = 1000l, sleepMirrorPicture = 1000l, sleepWhileMove = 1000l,
			sleepPictureMovement = 1000l, pulseDuration = 1000l;

	public ManualModePanel(ComConnectionProperties properties, final JLabel stateLine) {
		this.properties = properties;

		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();

		this.setLayout(new MigLayout("", "[] []30[] [] []", "[]20[] [] [] [] [] [] []10[] []"));

		checkMirror = new JCheckBox("Spiegelvorauslösung");
		checkMirror.setFont(actualFont);
		stepSpinner = new JSpinner();
		stepSpinner.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.0001, 250.1, 0.0001));
		((JSpinner.NumberEditor) stepSpinner.getEditor()).getFormat().setMaximumFractionDigits(4);
		stepSpinner.setFont(actualFont);
		picSpinner = new JSpinner();
		picSpinner.setFont(actualFont);
		picSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));

		createLabels();
		declareButtons();
		assignListeners();

		setVariablesFromProperties(properties);
		setStateLine(stateLine);

		buildLayout();
		setAllComponentsDisableState(true);
	}

	private void buildLayout() {
		add(stepSizeLabel, "right, cell 2 1");
		add(stepSpinner, "span 2,pushx, growx, wrap");

		add(picCountLabel, "right, cell 2 2");
		add(picSpinner, "span 2, pushx, growx, wrap");

		add(checkMirror, "cell 3 3, wrap");

		add(executeButton, "cell 3 4, split 3");
		add(stopButton);
		stopButton.setEnabled(false);
		add(pauseButton, "wrap");
		pauseButton.setEnabled(false);

		add(picsTakenLabel, "cell 2 5, right");
		add(picsNumberLabel, "left, wrap");

		add(pathTraveledLabel, "cell 2 6, right");
		add(pathDistanceLabel, "left, wrap");

		add(resetButton, "cell 3 7, left, wrap");

		add(assumedTimeLabel, "cell 2 8, right");
		add(timeNumberLabel, "wrap");

		add(nessecaryPathLengthLabel, "cell 2 9, right");
		add(pathLengthLabel, "wrap");

	}

	private void createLabels() {
		relativeLabel = new JLabel("Relative Bewegung");
		relativeLabel.setFont(actualFont);
		speedLabel = new JLabel("Geschwindigkeit:");
		speedLabel.setFont(actualFont);
		picCountLabel = new JLabel("Anzahl Bilder:");
		picCountLabel.setFont(actualFont);
		stepSizeLabel = new JLabel("Schrittgröße [mm]:");
		stepSizeLabel.setFont(actualFont);
		picsTakenLabel = new JLabel("Getätigte Bilder: ");
		picsTakenLabel.setFont(actualFont);
		picsNumberLabel = new JLabel("0");
		picsNumberLabel.setFont(actualFont);
		pathTraveledLabel = new JLabel("Zurückgelegter Weg: ");
		pathTraveledLabel.setFont(actualFont);
		pathDistanceLabel = new JLabel("0.0 mm");
		pathDistanceLabel.setFont(actualFont);
		assumedTimeLabel = new JLabel("Geschätze Dauer: ");
		assumedTimeLabel.setFont(actualFont);
		timeNumberLabel = new JLabel("0");
		timeNumberLabel.setFont(actualFont);
		nessecaryPathLengthLabel = new JLabel("erforderlicher Weg: ");
		nessecaryPathLengthLabel.setFont(actualFont);
		pathLengthLabel = new JLabel("0 mm");
		pathLengthLabel.setFont(actualFont);
	}

	private void declareButtons() {
		LOGGER.debug("creating normal Buttons.");

		pauseButton = new JButton("pause");
		pauseButton.setFont(actualFont);
		stopButton = new JButton("stop");
		stopButton.setFont(actualFont);
		resetButton = new JButton("reset");
		resetButton.setFont(actualFont);
		executeButton = new JButton("ausführen");
		executeButton.setFont(actualFont);
	}

	private void assignListeners() {
		LOGGER.debug("assigning Listeners");

		stepSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
			}

		});

		picSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Integer value = (Integer) picSpinner.getValue();
				refreshComponents(value);
			}
		});

		checkMirror.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
				refreshSleep();
			}
		});

		executeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) {
					pause = false;
					executeButton.setText("ausführen");
					executeButton.setEnabled(false);
					pauseButton.setEnabled(true);
				} else {

					boolean correctValue = false;
					try {
						double stepSize = (double) stepSpinner.getValue();

						LOGGER.info("click on execute step with size: " + stepSize);

						correctValue = validate(stepSize);
						if (correctValue) {
							stepSpinner.setBackground(Color.white);

							if (communicator != null) {
								lastManStep = stepSize;
								int countOfPictures = (int) picSpinner.getValue();

								if (countOfPictures > 1) {

									Thread job = createJob(stepSize, countOfPictures, sleepMovementMirror,
											sleepMirrorPicture, sleepPictureMovement, sleepWhileMove, pulseDuration);
									job.start();
								}

								if (countOfPictures == 1) {
									auslösen(pulseDuration);
									refreshAutoCountOfStepsLabel();
								}
							}
						} else {
							stepSpinner.setBackground(Color.red);
						}
					} catch (NumberFormatException numEx) {
						stepSpinner.setBackground(Color.red);
						stateLine.setText("Falsche Eingabe");
					}
				}
			}
		});

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.stop();
				stop = true;
				if (pause) {
					pause = false;
					executeButton.setText("ausführen");
					executeButton.setEnabled(false);
					pauseButton.setEnabled(true);
				}
			}
		});

		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pause = true;
			}
		});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetAutoCountOfRepeats();
				resetAutoSum();
			}
		});

	}

	protected void resetAutoSum() {
		pathDistanceLabel.setText("0.0 mm");
	}

	protected void resetAutoCountOfRepeats() {
		picsNumberLabel.setText("0");
	}

	protected Thread createJob(final double stepSize, final int countOfPictures, final long sleepMovementMirror,
			final long sleepMirrorPicture, final long sleepPictureMovement, final long sleepWhileMove,
			final long pulseDuration) {
		Thread job = new Thread() {
			@Override
			public void run() {

				setAllComponentsDisableState(true);
				setStopEnable(true);

				for (int i = 0; i < countOfPictures - 1; i++) {
					//Picture
					if (stop) {
						break;
					}
					auslösen(pulseDuration);
					refreshAutoCountOfStepsLabel();

					if (checkMirror.isSelected()) {
						pause(sleepMirrorPicture);
						if (stop) {
							break;
						}
						auslösen(pulseDuration);
					}
					checkSleepButton();

					//movement
					pause(sleepPictureMovement);
					if (stop) {
						break;
					}

					double moveStep = stepSize;
					if(reverseStep) {
						moveStep *= -1;
					}
					move(moveStep);

					refreshAutoSumLabel();

					pause((int) (sleepWhileMove * Math.abs(moveStep)) + sleepMovementMirror);
				}
				//picture
				if (!stop) {
					auslösen(pulseDuration);
					refreshAutoCountOfStepsLabel();

					if (checkMirror.isSelected()) {
						pause(sleepMirrorPicture);
						auslösen(pulseDuration);
					}
				}

				stop = false;
				setStopEnable(false);
				setAllComponentsDisableState(false);
			}
		};
		return job;
	}

	protected void refreshAutoSumLabel() {
		double stepDouble = (double) stepSpinner.getValue();
		String[] val = pathDistanceLabel.getText().split(" ", 2);
		double sumDouble = Double.parseDouble(val[0]);

		double value = sumDouble + stepDouble;
		double multiplicateValue = value * Constants.ROUNDER;
		double roundedValue = Math.round(multiplicateValue);
		double roundedDividedValue = roundedValue / Constants.ROUNDER;

		setAutoSumLabel(roundedDividedValue);
	}

	protected void setAutoSumLabel(double value) {
		pathDistanceLabel.setText(Double.toString(value) + " mm");
	}

	protected void move(double moveStep) {
		communicator.move(moveStep);
	}

	protected void checkSleepButton() {
		while (pause) {
			executeButton.setText("weiter");
			executeButton.setEnabled(true);
			pauseButton.setEnabled(false);
			pause(100);
		}
	}

	protected void pause(long pauseTime) {
		try {
			Thread.sleep(pauseTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setStopEnable(boolean state) {
		stopButton.setEnabled(state);
		pauseButton.setEnabled(state);
	}

	public void setAllComponentsDisableState(boolean disableState) {
		if (!disableState) {
			LOGGER.info("set all Components enable");
		} else {
			LOGGER.info("set all Components disable");
		}

		executeButton.setEnabled(!disableState);
		resetButton.setEnabled(!disableState);

		stepSpinner.setEnabled(!disableState);
		picSpinner.setEnabled(!disableState);

		checkMirror.setEnabled(!disableState);
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

	protected void refreshAutoCountOfStepsLabel() {
		int happenedRepeats = Integer.parseInt(picsNumberLabel.getText());
		int value = happenedRepeats+1;
		setPicCountLabel(value);
	}

	protected void setPicCountLabel(int value) {
		picsNumberLabel.setText(Integer.toString(value));
	}

	protected boolean validate(double stepSize) {
		LOGGER.info("validation given step size: " + stepSize);
		if (stepSize > ComCommunicator.MIN_STEP && stepSize < ComCommunicator.MAX_STEP) {
			LOGGER.info("stepSize is between " + ComCommunicator.MIN_STEP + "and " + ComCommunicator.MAX_STEP + "and is valid");
			return true;
		}
		LOGGER.debug("stepSize is not in the valid range");
		return false;
	}

	public void refreshComponents(Integer value) {
		if (value != null) {
			if (value == 1) {
				checkMirror.setEnabled(false);
				stepSpinner.setEnabled(false);
			} else if(value > 1) {
				checkMirror.setEnabled(true);
				stepSpinner.setEnabled(true);
			}
			refreshSleep();
			refreshDistance();
		}
	}

	public void refreshSleep() {
		long sleepSum = properties.getSleepPictureMovement() + properties.getSleepMovementMirror()
		+ (long) (properties.getSleepWhileMove() * (double) stepSpinner.getValue())
		+ properties.getPulseDuration();
		if (checkMirror.isSelected()) {
			sleepSum += properties.getSleepMirrorPicture() + properties.getPulseDuration();
		}
		long sleepSumMs = sleepSum * (int) picSpinner.getValue();
		long sleepSumSec = sleepSumMs / 1000;
		long sleepSumMin = sleepSumSec / 60;
		long restSecs = sleepSumSec % 60;
		long sleepSumHours = sleepSumMin / 60;

		String text;
		if (sleepSumHours <= 0) {
			text = sleepSumMin % 60 + ":" + restSecs + "." + sleepSumMs % 1000;
		} else {
			text = sleepSumHours + ":" + sleepSumMin % 60 + ":" + restSecs + "." + sleepSumMs % 1000;
		}
		timeNumberLabel.setText(text);
	}

	public void refreshDistance() {
		double distanceSum = ((int) picSpinner.getValue() - 1) * (double) stepSpinner.getValue();
		pathLengthLabel.setText(df.format(distanceSum) + " mm");
	}

	public ComCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}

	public void setStateLine(JLabel stateLine) {
		this.stateLine = stateLine;
	}

	public void setVariablesFromProperties(ComConnectionProperties properties) {
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();
		reverseStep = properties.isReverseSteps();
	}

	public void setProperties(ComConnectionProperties properties) {
		this.properties = properties;
		setVariablesFromProperties(properties);
	}

}

