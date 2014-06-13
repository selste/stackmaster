package de.dennismaass.stonemaster.stackmaster.ui.swing;

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

import de.dennismaass.stonemaster.stackmaster.comport.communicator.ComCommunicator;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ComConnectionProperties;

public class StepPanel extends JPanel {

	private static final long serialVersionUID = -2489089765486689116L;

	private static final Logger LOGGER = Logger.getLogger(StepPanel.class);

	private static final double MIN_VALUE = -250.0, MAX_VALUE = 250.0;
	private static final String DOT = ".";

	private static final String COMA = ",";

	private static final int ROUNDVALUE = 100000000;

	private static final String UNIT = "mm";

	private final ComConnectionProperties properties;
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

	private final JLabel autoStepsizeSumLb;
	private final JLabel autoCountOfDoneRepeatsLb;

	private JLabel stateLine;

	private ComCommunicator communicator;

	private final JButton resetBT;

	private final JButton executionBT;

	private final JCheckBox mirrorCB;
	private final JButton stopBT;
	private final JButton btnPause;
	private final JSpinner stepsizeTF;
	private final JSpinner autoCountOfRepeatsTF;
	private final JLabel geschaetzteDauerLb;
	private final JLabel geschaetzteDauerValueLb;
	private final JLabel lblErfolderlicherWeg;
	private final JLabel lblMm;

	/**
	 * Create the panel.
	 * 
	 * @wbp.parser.constructor
	 */
	public StepPanel(final ComConnectionProperties properties, final JLabel stateLine) {

		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);
		setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));

		setVariablesFromProperties(properties);

		final JLabel autoStepSizeTitle = new JLabel("Schrittgröße [mm]");
		autoStepSizeTitle.setFont(SwingStarter.FONT);
		add(autoStepSizeTitle, "cell 0 0,alignx trailing");

		stepsizeTF = new JSpinner();
		stepsizeTF.setFont(SwingStarter.FONT);
		stepsizeTF.setModel(new SpinnerNumberModel(properties.getStepSize(), 0.001, 250.0, 0.001));
		add(stepsizeTF, "cell 1 0,growx");
		stepsizeTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				refreshDistance();
			}
		});

		final JLabel autoCountOfRepeatsTitle = new JLabel("Anzahl Bilder");
		autoCountOfRepeatsTitle.setFont(SwingStarter.FONT);
		add(autoCountOfRepeatsTitle, "cell 0 1,alignx trailing");

		autoCountOfRepeatsTF = new JSpinner();
		autoCountOfRepeatsTF.setFont(SwingStarter.FONT);
		autoCountOfRepeatsTF.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		autoCountOfRepeatsTF.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				refreshSleep();
				refreshDistance();
			}

		});
		add(autoCountOfRepeatsTF, "cell 1 1,growx");

		mirrorCB = new JCheckBox("Spiegel vorauslösung");
		mirrorCB.setFont(SwingStarter.FONT);
		mirrorCB.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				refreshDistance();
				refreshSleep();
			}
		});
		add(mirrorCB, "cell 1 2");

		executionBT = new JButton("ausführen");
		executionBT.setFont(SwingStarter.FONT);
		add(executionBT, "flowx,cell 1 3");
		executionBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
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
						final double stepSize = (double) stepsizeTF.getValue();

						LOGGER.info("click on execute step with size: " + stepSize);

						correctValue = validate(stepSize);
						if (correctValue) {
							stepsizeTF.setBackground(Color.white);

							if (communicator != null) {
								lastManStep = stepSize;
								final int countOfSteps = (int) autoCountOfRepeatsTF.getValue();

								if (countOfSteps > 0) {

									final Thread job = createJob(stepSize, countOfSteps, sleepMovementMirror,
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
					} catch (final NumberFormatException ex) {
						stepsizeTF.setBackground(Color.red);
						stateLine.setText("Falsche Eingabe!");
					}
				}
			}

		});

		final JLabel autoCountOfDoneRepeatsTitle = new JLabel("Anzahl getätigter Fahrten");
		autoCountOfDoneRepeatsTitle.setFont(SwingStarter.FONT);
		add(autoCountOfDoneRepeatsTitle, "cell 0 7,alignx right");

		autoCountOfDoneRepeatsLb = new JLabel("0");
		autoCountOfDoneRepeatsLb.setFont(SwingStarter.FONT);
		add(autoCountOfDoneRepeatsLb, "cell 1 7");

		final JLabel autoStepsizeSumTitle = new JLabel("bisher zurückgelegter Weg[mm]");
		autoStepsizeSumTitle.setFont(SwingStarter.FONT);
		add(autoStepsizeSumTitle, "cell 0 8,alignx trailing");

		autoStepsizeSumLb = new JLabel("0.0");
		autoStepsizeSumLb.setFont(SwingStarter.FONT);
		add(autoStepsizeSumLb, "cell 1 8");

		stopBT = new JButton("stop");
		stopBT.setFont(SwingStarter.FONT);
		stopBT.setEnabled(false);
		stopBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
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
		add(stopBT, "cell 1 3");

		btnPause = new JButton("pause");
		btnPause.setFont(SwingStarter.FONT);
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				pause = true;

			}
		});
		btnPause.setEnabled(false);
		add(btnPause, "cell 1 3");

		resetBT = new JButton("reset");
		resetBT.setFont(SwingStarter.FONT);
		add(resetBT, "cell 1 9");

		geschaetzteDauerLb = new JLabel("geschätzte Dauer");
		geschaetzteDauerLb.setFont(SwingStarter.FONT);
		add(geschaetzteDauerLb, "cell 0 11,alignx trailing");

		geschaetzteDauerValueLb = new JLabel("0 ms");
		geschaetzteDauerValueLb.setFont(SwingStarter.FONT);
		add(geschaetzteDauerValueLb, "cell 1 11");

		lblErfolderlicherWeg = new JLabel("erfolderlicher Weg");
		lblErfolderlicherWeg.setFont(SwingStarter.FONT);
		add(lblErfolderlicherWeg, "cell 0 12,alignx trailing");

		lblMm = new JLabel("0.0 mm");
		lblMm.setFont(SwingStarter.FONT);
		add(lblMm, "cell 1 12");

		resetBT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				resetAutoCountOfRepeats();
				resetAutoSum();

			}
		});
	}

	public void refreshDistance() {
		final double distanceSum = (int) autoCountOfRepeatsTF.getValue() * (double) stepsizeTF.getValue();
		final double roundedDistanceSum = Math.rint(distanceSum * 1000) / 1000;
		lblMm.setText(Double.toString(roundedDistanceSum) + " mm");
	}

	public void refreshSleep() {
		long sleepSum = properties.getSleepPictureMovement() + properties.getSleepMovementMirror()
				+ (long) (properties.getSleepWhileMove() * (double) stepsizeTF.getValue())
				+ properties.getPulseDuration();
		if (mirrorCB.isSelected()) {
			sleepSum += properties.getSleepMirrorPicture() + properties.getPulseDuration();
		}
		final double sleepSumMs = sleepSum * (int) autoCountOfRepeatsTF.getValue();
		final double sleepSumMin = sleepSumMs / 60000;
		final double roundedSleepSumMin = Math.rint(sleepSumMin * 1000) / 1000;
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

		final Thread job = new Thread() {
			@Override
			public void run() {

				setAllComponentsDisableState(true);
				setStopEnable(true);

				for (int i = 0; i < countOfSteps; i++) {

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
					checkSleepButton();

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

				}
				stop = false;
				setStopEnable(false);
				setAllComponentsDisableState(false);

			}

		};
		return job;
	}

	private void setStopEnable(final boolean value) {
		stopBT.setEnabled(value);
		btnPause.setEnabled(value);

	}

	protected void move(final double stepSize) {
		communicator.move(stepSize);
	}

	protected void refreshCountUi() {
		refreshAutoCountOfStepsLabel();
		refreshAutoSumLabel();
	}

	protected void pause(final long autoSleep) {
		try {
			Thread.sleep(autoSleep);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void auslösen(final long pulseDuration) {
		communicator.setSIO(2, true);
		try {
			Thread.sleep(pulseDuration);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		communicator.setSIO(2, false);
	};

	// ---refreshAutoLabel
	protected void refreshAutoCountOfStepsLabel() {
		final int countOfRepeatsInt = Integer.parseInt(autoCountOfDoneRepeatsLb.getText());
		final int value = countOfRepeatsInt + 1;
		setAutoCountOfRepeatsLabel(value);
	}

	protected void setAutoCountOfRepeatsLabel(final int value) {
		autoCountOfDoneRepeatsLb.setText(Integer.toString(value));
	}

	protected void refreshAutoSumLabel() {

		final double stepDouble = (double) stepsizeTF.getValue();
		final double sumDouble = Double.parseDouble(autoStepsizeSumLb.getText());

		final double value = sumDouble + stepDouble;
		final double multiplicateValue = value * ROUNDVALUE;
		final double roundedValue = Math.round(multiplicateValue);
		final double roundedDividedValue = roundedValue / ROUNDVALUE;

		// TODO formatierung
		setAutoSumLabel(roundedDividedValue);
	}

	protected void setAutoSumLabel(final double value) {
		autoStepsizeSumLb.setText(Double.toString(value));
	}

	protected void resetAutoSum() {
		autoStepsizeSumLb.setText("0.0");
	}

	protected void resetAutoCountOfRepeats() {
		autoCountOfDoneRepeatsLb.setText("0");
	}

	public void setVariablesFromProperties(final ComConnectionProperties properties) {
		lastManStep = properties.getStepSize();

		reverseStep = properties.isReverseSteps();
		sleepMovementMirror = properties.getSleepMovementMirror();
		sleepMirrorPicture = properties.getSleepMirrorPicture();
		sleepWhileMove = properties.getSleepWhileMove();
		sleepPictureMovement = properties.getSleepPictureMovement();
		pulseDuration = properties.getPulseDuration();

	}

	protected void setPropertiesFromVariables(final ComConnectionProperties properties) {
		properties.setStepSize(lastManStep);
	}

	public void setAllComponentsDisableState(final boolean disableState) {
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

	protected boolean validate(final double stepSize) {
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

	public void setCommunicator(final ComCommunicator communicator) {
		this.communicator = communicator;
	}

	public JLabel getStateLine() {
		return stateLine;
	}

	public void setStateLine(final JLabel stateLine) {
		this.stateLine = stateLine;
	}

}
