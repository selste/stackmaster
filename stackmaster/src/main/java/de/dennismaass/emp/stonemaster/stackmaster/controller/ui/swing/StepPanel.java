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

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.util.Constants;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

public class StepPanel extends JPanel {

	private static final long serialVersionUID = -2489089765486689116L;

	private static Logger LOGGER = Logger.getLogger(StepPanel.class);

	private ComCommunicator communicator;
	private ComConnectionProperties properties;

	public Font actualFont = new Font("Arial", Font.PLAIN, 20);
	private double lastManStep = 0.001;

	private boolean reverseStep = false;

	private long sleepMovementMirror = 1000, sleepMirrorPicture = 1000, sleepWhileMove = 1000,
			sleepPictureMovement = 1000, pulseDuration = 1000;

	private boolean pause, stop = false;

	private JLabel autoStepsizeSumLb, autoCountOfDoneRepeatsLb, stateLine, geschaetzteDauerLb, geschaetzteDauerValueLb,
	lblErfolderlicherWeg, lblMm, label_6;

	private JButton resetBT, executionBT, stopBT, btnPause;
	private JCheckBox mirrorCB;
	private JSpinner stepsizeTF, autoCountOfPicturesTF;

	private DecimalFormat df = new DecimalFormat("0.0000");

	public StepPanel(ComConnectionProperties properties, final JLabel stateLine) {

		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);
		setLayout(new MigLayout("", "[][][grow]", "[][][][][grow 200][][][][grow 50][][]"));

		setVariablesFromProperties(properties);

		JLabel autoStepSizeTitle = new JLabel("Schrittgröße [mm]");
		autoStepSizeTitle.setFont(actualFont);
		add(autoStepSizeTitle, "cell 0 0,alignx trailing");

		stepsizeTF = new JSpinner();
		stepsizeTF.setFont(actualFont);
		stepsizeTF.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.0001, 250.0, 0.0001));
		((JSpinner.NumberEditor) stepsizeTF.getEditor()).getFormat().setMaximumFractionDigits(4);
		add(stepsizeTF, "cell 2 0,growx");
		stepsizeTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
			}
		});

		JLabel autoCountOfRepeatsTitle = new JLabel("Anzahl Bilder");
		autoCountOfRepeatsTitle.setFont(actualFont);
		add(autoCountOfRepeatsTitle, "cell 0 1,alignx trailing");

		autoCountOfPicturesTF = new JSpinner();
		autoCountOfPicturesTF.setFont(actualFont);
		autoCountOfPicturesTF.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		autoCountOfPicturesTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Integer value = (Integer) autoCountOfPicturesTF.getValue();

				refreshComponents(value);
			}

		});
		add(autoCountOfPicturesTF, "cell 2 1,growx");

		mirrorCB = new JCheckBox("Spiegel vorauslösung");
		mirrorCB.setFont(actualFont);
		mirrorCB.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
				refreshSleep();
			}
		});

		add(mirrorCB, "cell 2 2,alignx center");

		executionBT = new JButton("ausführen");
		executionBT.setFont(actualFont);
		add(executionBT, "flowx,cell 2 3,alignx center");
		executionBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (pause) {
					pause = false;
					executionBT.setText("ausführen");
					executionBT.setEnabled(false);
					btnPause.setEnabled(true);
				} else {
					// TODO:
					// communicator.setMaxSpeed(fastDownSpeed);

					boolean correctValue = false;
					try {
						double stepSize = (double) stepsizeTF.getValue();

						LOGGER.info("click on execute step with size: " + stepSize);

						correctValue = validate(stepSize);
						if (correctValue) {
							stepsizeTF.setBackground(Color.white);

							if (communicator != null) {
								lastManStep = stepSize;
								int countOfPictures = (int) autoCountOfPicturesTF.getValue();

								if (countOfPictures > 1) {

									Thread job = createJob(stepSize, countOfPictures, sleepMovementMirror,
											sleepMirrorPicture, sleepPictureMovement, sleepWhileMove, pulseDuration);
									job.start();

								}
								if (countOfPictures == 1) {
									auslösen(pulseDuration);
									refreshAutoCountOfStepsLabel();
								}
								// else {
								// double moveStep = stepSize;
								// if (reverseStep) {
								// moveStep *= -1;
								// }
								// move(moveStep);
								//
								// refreshCountUi();
								// }

							}
						} else {
							stepsizeTF.setBackground(Color.red);
						}
					} catch (NumberFormatException ex) {
						stepsizeTF.setBackground(Color.red);
						stateLine.setText("Falsche Eingabe!");
					}
				}
			}

		});

		stopBT = new JButton("stop");
		stopBT.setFont(actualFont);
		stopBT.setEnabled(false);
		stopBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				communicator.stop();
				stop = true;
				if (pause) {
					pause = false;
					executionBT.setText("ausführen");
					executionBT.setEnabled(false);
					btnPause.setEnabled(true);
				}
			}
		});
		add(stopBT, "cell 2 3");

		btnPause = new JButton("pause");
		btnPause.setFont(actualFont);
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pause = true;

			}
		});
		btnPause.setEnabled(false);
		add(btnPause, "cell 2 3");

		JLabel autoCountOfDoneRepeatsTitle = new JLabel("Anzahl getätigter Bilder");
		autoCountOfDoneRepeatsTitle.setFont(actualFont);
		add(autoCountOfDoneRepeatsTitle, "cell 0 5,alignx right");

		autoCountOfDoneRepeatsLb = new JLabel("0");
		autoCountOfDoneRepeatsLb.setFont(actualFont);
		add(autoCountOfDoneRepeatsLb, "cell 2 5");

		JLabel autoStepsizeSumTitle = new JLabel("bisher zurückgelegter Weg[mm]");
		autoStepsizeSumTitle.setFont(actualFont);
		add(autoStepsizeSumTitle, "cell 0 6,alignx trailing");

		autoStepsizeSumLb = new JLabel("0.0");
		autoStepsizeSumLb.setFont(actualFont);
		add(autoStepsizeSumLb, "cell 2 6");

		resetBT = new JButton("reset");
		resetBT.setFont(actualFont);
		add(resetBT, "cell 2 7");

		resetBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetAutoCountOfRepeats();
				resetAutoSum();

			}
		});

		label_6 = new JLabel(" ");
		add(label_6, "cell 0 8");

		geschaetzteDauerLb = new JLabel("geschätzte Dauer");
		geschaetzteDauerLb.setFont(actualFont);
		add(geschaetzteDauerLb, "cell 0 9,alignx trailing");

		geschaetzteDauerValueLb = new JLabel("0 ms");
		geschaetzteDauerValueLb.setFont(actualFont);
		add(geschaetzteDauerValueLb, "cell 2 9");

		lblErfolderlicherWeg = new JLabel("erforderlicher Weg");
		lblErfolderlicherWeg.setFont(actualFont);
		add(lblErfolderlicherWeg, "cell 0 10,alignx trailing");

		lblMm = new JLabel("0.0 mm");
		lblMm.setFont(actualFont);
		add(lblMm, "cell 2 10");

		refreshComponents((Integer) autoCountOfPicturesTF.getValue());
	}

	public void refreshComponents(Integer value) {
		if (value != null) {
			if (value == 1) {
				mirrorCB.setEnabled(false);
				stepsizeTF.setEnabled(false);
			} else if (value > 1) {
				mirrorCB.setEnabled(true);
				stepsizeTF.setEnabled(true);
			}

			refreshSleep();
			refreshDistance();
		}
	}

	public void refreshDistance() {
		double distanceSum = ((int) autoCountOfPicturesTF.getValue() - 1) * (double) stepsizeTF.getValue();
		// double roundedDistanceSum = Math.rint(distanceSum * 10000) / 10000;
		lblMm.setText(df.format(distanceSum) + " mm");
		// lblMm.setText(Double.toString(roundedDistanceSum) + " mm");
	}

	public void refreshSleep() {
		long sleepSum = properties.getSleepPictureMovement() + properties.getSleepMovementMirror()
				+ (long) (properties.getSleepWhileMove() * (double) stepsizeTF.getValue())
				+ properties.getPulseDuration();
		if (mirrorCB.isSelected()) {
			sleepSum += properties.getSleepMirrorPicture() + properties.getPulseDuration();
		}
		long sleepSumMs = sleepSum * (int) autoCountOfPicturesTF.getValue();
		long sleepSumSec = sleepSumMs / 1000;
		long sleepSumMin = sleepSumSec / 60;
		long restsecs = sleepSumSec % 60;
		long sleepSumHours = sleepSumMin / 60;

		String text = "";
		if (sleepSumHours <= 0) {
			text = sleepSumMin % 60 + ":" + restsecs + "." + sleepSumMs % 1000;
		} else {
			text = sleepSumHours + ":" + sleepSumMin % 60 + ":" + restsecs + "." + sleepSumMs % 1000;
		}
		geschaetzteDauerValueLb.setText(text);
	}

	protected void checkSleepButton() {
		while (pause) {
			executionBT.setText("weiter");
			executionBT.setEnabled(true);
			btnPause.setEnabled(false);
			pause(100);
		}
	}

	protected Thread createJob(final double stepSize, final int countOfPictures, final long sleepMovementMirrow,
			final long sleepMirrowPicture, final long sleepPictureMovement, final long sleepWhileMove,
			final long pulseDuration) {

		Thread job = new Thread() {
			@Override
			public void run() {

				setAllComponentsDisableState(true);
				setStopEnable(true);

				for (int i = 0; i < countOfPictures - 1; i++) {
					// Picture
					if (stop) {
						break;
					}
					auslösen(pulseDuration);
					refreshAutoCountOfStepsLabel();

					if (mirrorCB.isSelected()) {
						pause(sleepMirrowPicture);
						if (stop) {
							break;
						}
						auslösen(pulseDuration);
					}
					checkSleepButton();

					// Move
					pause(sleepPictureMovement);
					if (stop) {
						break;
					}

					double moveStep = stepSize;
					if (reverseStep) {
						moveStep *= -1;
					}
					move(moveStep);

					refreshAutoSumLabel();

					pause((int) (sleepWhileMove * Math.abs(moveStep)) + sleepMovementMirrow);

				}
				// Picture
				if (!stop) {
					auslösen(pulseDuration);
					refreshAutoCountOfStepsLabel();

					if (mirrorCB.isSelected()) {
						pause(sleepMirrowPicture);
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

	private void setStopEnable(boolean value) {
		stopBT.setEnabled(value);
		btnPause.setEnabled(value);

	}

	protected void move(double stepSize) {
		communicator.move(stepSize);
	}

	protected void pause(long autoSleep) {
		try {
			Thread.sleep(autoSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void auslösen(long pulseDuration) {
		communicator.setSIO(2, true);
		try {
			Thread.sleep(pulseDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		communicator.setSIO(2, false);
	};

	// ---refreshAutoLabel
	protected void refreshAutoCountOfStepsLabel() {
		int countOfRepeatsInt = Integer.parseInt(autoCountOfDoneRepeatsLb.getText());
		int value = countOfRepeatsInt + 1;
		setAutoCountOfRepeatsLabel(value);
	}

	protected void setAutoCountOfRepeatsLabel(int value) {
		autoCountOfDoneRepeatsLb.setText(Integer.toString(value));
	}

	protected void refreshAutoSumLabel() {

		double stepDouble = (double) stepsizeTF.getValue();
		double sumDouble = Double.parseDouble(autoStepsizeSumLb.getText());

		double value = sumDouble + stepDouble;
		double multiplicateValue = value * Constants.ROUNDER;
		double roundedValue = Math.round(multiplicateValue);
		double roundedDividedValue = roundedValue / Constants.ROUNDER;

		setAutoSumLabel(roundedDividedValue);
	}

	protected void setAutoSumLabel(double value) {
		autoStepsizeSumLb.setText(Double.toString(value));
	}

	protected void resetAutoSum() {
		autoStepsizeSumLb.setText("0.0");
	}

	protected void resetAutoCountOfRepeats() {
		autoCountOfDoneRepeatsLb.setText("0");
	}

	public void setVariablesFromProperties(ComConnectionProperties properties) {
		lastManStep = properties.getStepSize();

		reverseStep = properties.isReverseSteps();
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();

	}

	protected void setPropertiesFromVariables(ComConnectionProperties properties) {
		properties.setStepSize(lastManStep);
	}

	public void setAllComponentsDisableState(boolean disableState) {
		if (!disableState) {
			LOGGER.info("set all components enable");
		} else {
			LOGGER.info("set all components disable");
		}

		executionBT.setEnabled(!disableState);
		resetBT.setEnabled(!disableState);

		stepsizeTF.setEnabled(!disableState);
		autoCountOfPicturesTF.setEnabled(!disableState);

		mirrorCB.setEnabled(!disableState);

	}

	protected boolean validate(double stepSize) {
		LOGGER.info("validation given step size : " + stepSize);
		if (stepSize > Constants.MIN_STEP && stepSize < Constants.MAX_STEP) {
			LOGGER.info("stepSize is between " + Constants.MIN_STEP + " and " + Constants.MAX_STEP + " and is valid");
			return true;
		}
		return false;
	}

	public ComCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}

	public JLabel getStateLine() {
		return stateLine;
	}

	public void setStateLine(JLabel stateLine) {
		this.stateLine = stateLine;
	}

}
