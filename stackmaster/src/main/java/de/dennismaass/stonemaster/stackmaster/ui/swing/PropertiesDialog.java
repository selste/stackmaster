package de.dennismaass.stonemaster.stackmaster.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.stonemaster.stackmaster.comport.communicator.ComCommunicator;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ComConnectionProperties;

//TODO: 
// Als "new default" setzen
// "reset default"
//"to default"
public class PropertiesDialog extends JDialog {
	private static final long serialVersionUID = 8974001888731772934L;
	private static Logger LOGGER = Logger.getLogger(PropertiesDialog.class);

	private final JPanel contentPanel = new JPanel();
	private final JCheckBox reverseCB;

	private final ComConnectionProperties defaultConnectionProperties;
	private ComConnectionProperties actualConnectionProperties;

	private final List<ComConnectionPropertiesListener> listenerList = new ArrayList<>();

	final JSpinner fastUpSpeedTF, middleUpSpeedTF, slowUpSpeedTF;
	final JSpinner slowDownSpeedTF, middleDownSpeedTF, fastDownSpeedTF;
	final JSpinner sleepMovementMirrorTF, sleepMirrorPictureTF, sleepPictureMovementTF;
	final JSpinner pulseDurationTF;

	boolean cancel = false;

	/**
	 * Create the dialog.
	 */
	public PropertiesDialog(final ComConnectionProperties actualConnectionProperties,
			final ComConnectionProperties defaultConnectionProperties) {
		this.actualConnectionProperties = actualConnectionProperties;
		this.defaultConnectionProperties = defaultConnectionProperties;

		setResizable(false);
		setType(Type.UTILITY);
		setModal(true);
		setTitle("Einstellungen");
		setBounds(100, 100, 300, 353);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(final WindowEvent e) {

			}

			@Override
			public void windowIconified(final WindowEvent e) {

			}

			@Override
			public void windowDeiconified(final WindowEvent e) {

			}

			@Override
			public void windowDeactivated(final WindowEvent e) {

				if (cancel) {
					cancel();
				}
			}

			@Override
			public void windowClosing(final WindowEvent e) {

			}

			@Override
			public void windowClosed(final WindowEvent e) {
				if (cancel) {
					cancel();
				}

			}

			@Override
			public void windowActivated(final WindowEvent e) {

			}
		});
		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPanel.add(tabbedPane);

		final JPanel panel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, panel, null);
		panel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][]"));

		final JLabel lblNewLabel_6 = new JLabel("Hoch");
		panel.add(lblNewLabel_6, "cell 0 0,grow");

		final JLabel lblNewLabel_5 = new JLabel("");
		panel.add(lblNewLabel_5, "cell 1 0,grow");

		final JLabel lblSchnelleGeschw = new JLabel("schnelle Geschw.");
		panel.add(lblSchnelleGeschw, "cell 0 1,grow");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastUpSpeedTF, "cell 1 1,growx");

		final JLabel lblNewLabel = new JLabel("mittlere Geschw.");
		panel.add(lblNewLabel, "cell 0 2,grow");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleUpSpeedTF, "cell 1 2,growx");

		final JLabel lblNewLabel_1 = new JLabel("langsame Geschw.");
		panel.add(lblNewLabel_1, "cell 0 3,grow");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		panel.add(slowUpSpeedTF, "cell 1 3,growx");

		final JLabel lblNewLabel_7 = new JLabel("");
		panel.add(lblNewLabel_7, "cell 1 4,grow");

		final JLabel lblNewLabel_8 = new JLabel("Runter");
		panel.add(lblNewLabel_8, "cell 0 5,grow");

		final JLabel lblNewLabel_2 = new JLabel("schnelle Geschw.");
		panel.add(lblNewLabel_2, "cell 0 6,grow");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastDownSpeedTF, "cell 1 6,growx");

		final JLabel lblNewLabel_3 = new JLabel("mittlere Geschw.");
		panel.add(lblNewLabel_3, "cell 0 7,grow");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleDownSpeedTF, "cell 1 7,growx");

		final JLabel lblNewLabel_4 = new JLabel("langsame Geschw.");
		panel.add(lblNewLabel_4, "cell 0 8,grow");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		slowDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(slowDownSpeedTF, "cell 1 8,growx");

		final JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Step-Panel", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		final JLabel lblNewLabel_12 = new JLabel("Richtung umdrehen");
		panel_1.add(lblNewLabel_12, "cell 0 0,grow");

		reverseCB = new JCheckBox();
		panel_1.add(reverseCB, "cell 1 0,grow");
		reverseCB.setSelected(actualConnectionProperties.isReverse());

		final JLabel lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		panel_1.add(lblNewLabel_9, "cell 0 1,alignx trailing");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMovementMirror()), new Long(1), null, new Long(500)));
		sleepMovementMirrorTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMovementMirrorTF, "cell 1 1,growx");

		final JLabel lblNewLabel_11 = new JLabel("Pause (Spiegel - Bild) [ms]");
		panel_1.add(lblNewLabel_11, "cell 0 2,alignx trailing");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMirrorPicture()), new Long(1), null, new Long(500)));
		sleepMirrorPictureTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMirrorPictureTF, "cell 1 2,growx");

		final JLabel lblNewLabel_10 = new JLabel("Pause (Bild - Bew.) [ms]");
		panel_1.add(lblNewLabel_10, "cell 0 3,alignx trailing");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepPictureMovement()), new Long(1), null, new Long(500)));
		sleepPictureMovementTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepPictureMovementTF, "cell 1 3,growx");

		final JLabel lblNewLabel_13 = new JLabel("Impulsdauer [ms]");
		panel_1.add(lblNewLabel_13, "cell 0 4,alignx trailing");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties.getPulseDuration()),
				new Long(1), null, new Long(500)));
		pulseDurationTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(pulseDurationTF, "cell 1 4,growx");

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton("Okay");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final ComConnectionProperties comConnectionProperties = setVariablesFromTF(actualConnectionProperties);
				final boolean allValid = areValidConnectionProperties(comConnectionProperties);
				LOGGER.info("try to save properties");
				LOGGER.info("properties are valid: " + allValid);
				if (allValid) {
					setBackgroundOfAllTextFields(Color.white);

					for (final ComConnectionPropertiesListener listener : listenerList) {
						final ComConnectionPropertiesChangeEvent event = new ComConnectionPropertiesChangeEvent(this,
								comConnectionProperties);
						listener.handleComConnectionPropertiesChangeEvent(event);
					}

					dispose();
				}

			}

			protected void setBackgroundOfAllTextFields(final Color color) {
				fastUpSpeedTF.setBackground(color);
				middleUpSpeedTF.setBackground(color);
				slowUpSpeedTF.setBackground(color);
				fastDownSpeedTF.setBackground(color);
				middleDownSpeedTF.setBackground(color);
				slowDownSpeedTF.setBackground(color);
				reverseCB.setBackground(color);
				sleepMirrorPictureTF.setBackground(color);
				sleepMovementMirrorTF.setBackground(color);
				sleepPictureMovementTF.setBackground(color);
				pulseDurationTF.setBackground(color);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setActionCommand("Cancel");

		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				cancel = true;
				dispose();
			}

		});

	}

	private void cancel() {
		fastUpSpeedTF.setValue(actualConnectionProperties.getFastUpSpeed());
		middleUpSpeedTF.setValue(actualConnectionProperties.getMiddleUpSpeed());
		slowUpSpeedTF.setValue(actualConnectionProperties.getSlowUpSpeed());
		fastDownSpeedTF.setValue(actualConnectionProperties.getFastDownSpeed());
		middleDownSpeedTF.setValue(actualConnectionProperties.getMiddleDownSpeed());
		slowDownSpeedTF.setValue(actualConnectionProperties.getSlowDownSpeed());
		reverseCB.setSelected(actualConnectionProperties.isReverse());
		sleepMirrorPictureTF.setValue(actualConnectionProperties.getSleepMirrorPicture());
		sleepMovementMirrorTF.setValue(actualConnectionProperties.getSleepMovementMirror());
		sleepPictureMovementTF.setValue(actualConnectionProperties.getSleepPictureMovement());
		pulseDurationTF.setValue(actualConnectionProperties.getPulseDuration());
	}

	public void refresh() {
		cancel();
	}

	protected boolean isValidSpeed(final int value) {
		if (value >= ComCommunicator.MIN_SPEED && value <= ComCommunicator.MAX_SPEED) {
			return true;
		}
		return false;
	}

	protected boolean isValidSleep(final long value) {
		if (value >= ComCommunicator.MIN_SLEEP && value <= ComCommunicator.MAX_SLEEP) {
			return true;
		}
		return false;
	}

	protected boolean isValidStepcount(final int value) {
		return true;
	}

	protected boolean isValidMicrostepResolutionMode(final int value) {
		if (value >= ComCommunicator.MIN_MODE && value <= ComCommunicator.MAX_MODE) {
			return true;
		}
		return false;
	}

	protected boolean areValidConnectionProperties(final ComConnectionProperties comConnectionProperties) {
		if (!isValidSpeed(comConnectionProperties.getFastUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getMiddleUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getSlowUpSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getFastDownSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getMiddleDownSpeed())) {
			return false;
		}
		if (!isValidSpeed(comConnectionProperties.getSlowDownSpeed())) {
			return false;
		}
		if (!isValidStepcount(comConnectionProperties.getStepsPerMm())) {
			return false;
		}
		if (!isValidMicrostepResolutionMode(comConnectionProperties.getMicrostepResolutionMode())) {
			return false;
		}
		// TODO: reverse, sleeps

		return true;
	}

	protected ComConnectionProperties setVariablesFromTF(final ComConnectionProperties connectionProperties) {

		final int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		if (!isValidSpeed(fastUpSpeed)) {
			fastUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastUpSpeed  from Spinner");
		} else {
			connectionProperties.setFastUpSpeed(fastUpSpeed);
			fastUpSpeedTF.setBackground(Color.white);
		}

		final int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		if (!isValidSpeed(middleUpSpeed)) {
			middleUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleUpSpeed  from Spinner");
		} else {
			connectionProperties.setMiddleUpSpeed(middleUpSpeed);
			middleUpSpeedTF.setBackground(Color.white);
		}

		final int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		if (!isValidSpeed(slowUpSpeed)) {
			slowUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowUpSpeed from Spinner");
		} else {
			connectionProperties.setSlowUpSpeed(slowUpSpeed);
			slowUpSpeedTF.setBackground(Color.white);
		}

		final int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		if (!isValidSpeed(slowDownSpeed)) {
			slowDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowDownSpeed from Spinner");
		} else {
			connectionProperties.setSlowDownSpeed(slowDownSpeed);
			slowDownSpeedTF.setBackground(Color.white);
		}

		final int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		if (!isValidSpeed(middleDownSpeed)) {
			middleDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleDownSpeed from Spinner");
		} else {
			connectionProperties.setMiddleDownSpeed(middleDownSpeed);
			middleDownSpeedTF.setBackground(Color.white);
		}

		final int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		if (!isValidSpeed(fastDownSpeed)) {
			fastDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastDownSpeed from Spinner");
		} else {
			connectionProperties.setFastDownSpeed(fastDownSpeed);
			fastDownSpeedTF.setBackground(Color.white);
		}

		connectionProperties.setReverse(reverseCB.isSelected());

		final long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		if (!isValidSleep(sleepMovementMirror)) {
			sleepMovementMirrorTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMovementMirror Spinner");
		} else {
			connectionProperties.setSleepMovementMirror(sleepMovementMirror);
			sleepMovementMirrorTF.setBackground(Color.white);
		}

		final long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		if (!isValidSleep(sleepMirrorPicture)) {
			sleepMirrorPictureTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMirrorPicture from Spinner");
		} else {
			connectionProperties.setSleepMirrorPicture(sleepMirrorPicture);
			sleepMirrorPictureTF.setBackground(Color.white);
		}

		final long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		if (!isValidSleep(sleepPictureMovement)) {
			sleepPictureMovementTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepPictureMovement from Spinner");
		} else {
			connectionProperties.setSleepPictureMovement(sleepPictureMovement);
			sleepPictureMovementTF.setBackground(Color.white);
		}

		final long pulseDuration = (long) pulseDurationTF.getValue();
		if (!isValidSleep(pulseDuration)) {
			pulseDurationTF.setBackground(Color.red);
			LOGGER.error("error by parsing pulseDuration from Spinner");
		} else {
			connectionProperties.setPulseDuration(pulseDuration);
			pulseDurationTF.setBackground(Color.white);
		}

		return connectionProperties;

	}

	public void addComConnectionPropertiesListener(final ComConnectionPropertiesListener listener) {
		listenerList.add(listener);
	}

	public void removeComConnectionPropertiesListener(final ComConnectionPropertiesListener listener) {
		listenerList.remove(listener);
	}

	public ComConnectionProperties getConnectionProperties() {
		return actualConnectionProperties;
	}

	public void setConnectionProperties(final ComConnectionProperties connectionProperties) {
		actualConnectionProperties = connectionProperties;
		refresh();
	}
}
