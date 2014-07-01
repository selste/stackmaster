package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

public class StepPanel extends JPanel {

	private static long serialVersionUID = -2489089765486689116L;

	private static Logger LOGGER = Logger.getLogger(StepPanel.class);

	private static double MIN_VALUE = -250.0, MAX_VALUE = 250.0;
	private static String DOT = ".";

	private static String COMA = ",";

	private static int ROUNDVALUE = 100000000;

	private static String UNIT = "mm";

	private ComConnectionProperties properties;
	private double lastManStep = 0.001;

	// TODO: einbauen
	private boolean reverseStep = false;
	private long sleepMovementMirror = 1000;
	private long sleepMirrorPicture = 1000;
	private long sleepWhileMove = 1000;
	private long sleepPictureMovement = 1000;
	private long pulseDuration = 1000;

	private boolean pause;
	private boolean stop = false;

	private JLabel autoStepsizeSumLb;
	private JLabel autoCountOfDoneRepeatsLb;

	private JLabel stateLine;

	private ComCommunicator communicator;

	private JButton resetBT;

	private JButton executionBT;

	private JCheckBox mirrorCB;
	private JButton stopBT;
	private JButton btnPause;
	private JSpinner stepsizeTF;
	private JSpinner autoCountOfRepeatsTF;
	private JLabel geschaetzteDauerLb;
	private JLabel geschaetzteDauerValueLb;
	private JLabel lblErfolderlicherWeg;
	private JLabel lblMm;
	private JLabel label_6;

	/**
	 * Create the panel.
	 * 
	 * @wbp.parser.constructor
	 */
	public StepPanel(ComConnectionProperties properties, final JLabel stateLine) {

		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);
		setLayout(new MigLayout("", "[][][grow]", "[][][][][grow 200][][][][grow 50][][]"));

		setVariablesFromProperties(properties);

		JLabel autoStepSizeTitle = new JLabel("Schrittgröße [mm]");
		autoStepSizeTitle.setFont(SwingStarter.actualFont);
		add(autoStepSizeTitle, "cell 0 0,alignx trailing");

		stepsizeTF = new JSpinner();
		stepsizeTF.setFont(SwingStarter.actualFont);
		stepsizeTF.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.001, 250.0, 0.001));
		add(stepsizeTF, "cell 2 0,growx");
		stepsizeTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
			}
		});

		JLabel autoCountOfRepeatsTitle = new JLabel("Anzahl Bilder");
		autoCountOfRepeatsTitle.setFont(SwingStarter.actualFont);
		add(autoCountOfRepeatsTitle, "cell 0 1,alignx trailing");

		autoCountOfRepeatsTF = new JSpinner();
		autoCountOfRepeatsTF.setFont(SwingStarter.actualFont);
		autoCountOfRepeatsTF.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		autoCountOfRepeatsTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshSleep();
				refreshDistance();
			}

		});
		add(autoCountOfRepeatsTF, "cell 2 1,growx");

		mirrorCB = new JCheckBox("Spiegel vorauslösung");
		mirrorCB.setFont(SwingStarter.actualFont);
		mirrorCB.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshDistance();
				refreshSleep();
			}
		});
		add(mirrorCB, "cell 2 2,alignx center");

		executionBT = new JButton("ausführen");
		executionBT.setFont(SwingStarter.actualFont);
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
								int countOfSteps = (int) autoCountOfRepeatsTF.getValue();

								if (countOfSteps > 0) {

									Thread job = createJob(stepSize, countOfSteps, sleepMovementMirror,
											sleepMirrorPicture, sleepPictureMovement, sleepWhileMove, pulseDuration);
									job.start();

								} else {
									double moveStep = stepSize;
									if (reverseStep) {
										moveStep *= -1;
									}
									move(moveStep);

									refreshCountUi();
								}

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
		stopBT.setFont(SwingStarter.actualFont);
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
		btnPause.setFont(SwingStarter.actualFont);
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pause = true;

			}
		});
		btnPause.setEnabled(false);
		add(btnPause, "cell 2 3");

		JLabel autoCountOfDoneRepeatsTitle = new JLabel("Anzahl getätigter Fahrten");
		autoCountOfDoneRepeatsTitle.setFont(SwingStarter.actualFont);
		add(autoCountOfDoneRepeatsTitle, "cell 0 5,alignx right");

		autoCountOfDoneRepeatsLb = new JLabel("0");
		autoCountOfDoneRepeatsLb.setFont(SwingStarter.actualFont);
		add(autoCountOfDoneRepeatsLb, "cell 2 5");

		JLabel autoStepsizeSumTitle = new JLabel("bisher zurückgelegter Weg[mm]");
		autoStepsizeSumTitle.setFont(SwingStarter.actualFont);
		add(autoStepsizeSumTitle, "cell 0 6,alignx trailing");

		autoStepsizeSumLb = new JLabel("0.0");
		autoStepsizeSumLb.setFont(SwingStarter.actualFont);
		add(autoStepsizeSumLb, "cell 2 6");

		resetBT = new JButton("reset");
		resetBT.setFont(SwingStarter.actualFont);
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
		geschaetzteDauerLb.setFont(SwingStarter.actualFont);
		add(geschaetzteDauerLb, "cell 0 9,alignx trailing");

		geschaetzteDauerValueLb = new JLabel("0 ms");
		geschaetzteDauerValueLb.setFont(SwingStarter.actualFont);
		add(geschaetzteDauerValueLb, "cell 2 9");

		lblErfolderlicherWeg = new JLabel("erfolderlicher Weg");
		lblErfolderlicherWeg.setFont(SwingStarter.actualFont);
		add(lblErfolderlicherWeg, "cell 0 10,alignx trailing");

		lblMm = new JLabel("0.0 mm");
		lblMm.setFont(SwingStarter.actualFont);
		add(lblMm, "cell 2 10");
	}

	public void refreshDistance() {
		double distanceSum = (int) autoCountOfRepeatsTF.getValue() * (double) stepsizeTF.getValue();
		double roundedDistanceSum = Math.rint(distanceSum * 1000) / 1000;
		lblMm.setText(Double.toString(roundedDistanceSum) + " mm");
	}

	public void refreshSleep() {
		long sleepSum = properties.getSleepPictureMovement() + properties.getSleepMovementMirror()
				+ (long) (properties.getSleepWhileMove() * (double) stepsizeTF.getValue())
				+ properties.getPulseDuration();
		if (mirrorCB.isSelected()) {
			sleepSum += properties.getSleepMirrorPicture() + properties.getPulseDuration();
		}
		double sleepSumMs = sleepSum * (int) autoCountOfRepeatsTF.getValue();
		double sleepSumMin = sleepSumMs / 60000;
		double roundedSleepSumMin = Math.rint(sleepSumMin * 1000) / 1000;
		geschaetzteDauerValueLb.setText(Double.toString(roundedSleepSumMin) + " min");
	}

	protected void checkSleepButton() {
		while (pause) {
			executionBT.setText("weiter");
			executionBT.setEnabled(true);
			btnPause.setEnabled(false);
			pause(100);
		}
	}

	protected Thread createJob(final double stepSize, final int countOfSteps, final long sleepMovementMirrow,
			final long sleepMirrowPicture, final long sleepPictureMovement, final long sleepWhileMove,
			final long pulseDuration) {

		Thread job = new Thread() {
			@Override
			public void run() {

				setAllComponentsDisableState(true);
				setStopEnable(true);

				for (int i = 0; i < countOfSteps; i++) {
					// Picture
					if (stop) {
						break;
					}
					auslösen(pulseDuration);

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

					refreshCountUi();

					pause((int) (sleepWhileMove * moveStep) + sleepMovementMirrow);

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

	protected void refreshCountUi() {
		refreshAutoCountOfStepsLabel();
		refreshAutoSumLabel();
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
		double multiplicateValue = value * ROUNDVALUE;
		double roundedValue = Math.round(multiplicateValue);
		double roundedDividedValue = roundedValue / ROUNDVALUE;

		// TODO formatierung
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
		autoCountOfRepeatsTF.setEnabled(!disableState);

		mirrorCB.setEnabled(!disableState);

	}

	protected boolean validate(double stepSize) {
		LOGGER.info("validation given step size : " + stepSize);
		if (stepSize > MIN_VALUE && stepSize < MAX_VALUE) {
			LOGGER.info("stepSize is between " + MIN_VALUE + " and " + MAX_VALUE + " and is valid");
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
