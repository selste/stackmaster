package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
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

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionPropertiesChangeEvent;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionPropertiesListener;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.PropertiesValidator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

public class PropertiesDialog extends JDialog {
	private static long serialVersionUID = 8974001888731772934L;
	private static Logger LOGGER = Logger.getLogger(PropertiesDialog.class);

	private JPanel contentPanel = new JPanel();
	private JCheckBox reverseCB;

	private ComConnectionProperties defaultConnectionProperties;
	private ComConnectionProperties actualConnectionProperties;

	private List<ComConnectionPropertiesListener> listenerList = new ArrayList<>();

	private JSpinner fastUpSpeedTF, middleUpSpeedTF, slowUpSpeedTF;
	private JSpinner slowDownSpeedTF, middleDownSpeedTF, fastDownSpeedTF;
	private JSpinner sleepMovementMirrorTF, sleepMirrorPictureTF, sleepPictureMovementTF;
	private JSpinner pulseDurationTF;

	boolean cancel = false;

	private PropertiesValidator validator = new PropertiesValidator();
	public Font actualFont = new Font("Arial", Font.PLAIN, 20);

	public PropertiesDialog(ComConnectionProperties actualConnectionProperties,
			ComConnectionProperties defaultConnectionProperties) {
		this.actualConnectionProperties = actualConnectionProperties;
		this.defaultConnectionProperties = defaultConnectionProperties;

		setResizable(false);
		setType(Type.UTILITY);
		setModal(true);
		setTitle("Einstellungen");
		setBounds(100, 100, 391, 431);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				if (cancel) {
					cancel();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				if (cancel) {
					cancel();
				}
			}

		});
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(actualFont);
		contentPanel.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, panel, null);
		panel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][]"));

		JLabel lblNewLabel_6 = new JLabel("Hoch");
		lblNewLabel_6.setFont(actualFont);
		panel.add(lblNewLabel_6, "cell 0 0,grow");

		JLabel lblNewLabel_5 = new JLabel("");
		panel.add(lblNewLabel_5, "cell 1 0,grow");

		JLabel lblSchnelleGeschw = new JLabel("schnelle Geschw.");
		lblSchnelleGeschw.setFont(actualFont);
		panel.add(lblSchnelleGeschw, "cell 0 1,grow");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setFont(actualFont);
		fastUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastUpSpeedTF, "cell 1 1,growx");

		JLabel lblNewLabel = new JLabel("mittlere Geschw.");
		lblNewLabel.setFont(actualFont);
		panel.add(lblNewLabel, "cell 0 2,grow");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setFont(actualFont);
		middleUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleUpSpeedTF, "cell 1 2,growx");

		JLabel lblNewLabel_1 = new JLabel("langsame Geschw.");
		lblNewLabel_1.setFont(actualFont);
		panel.add(lblNewLabel_1, "cell 0 3,grow");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setFont(actualFont);
		slowUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		panel.add(slowUpSpeedTF, "cell 1 3,growx");

		JLabel lblNewLabel_7 = new JLabel("");
		panel.add(lblNewLabel_7, "cell 1 4,grow");

		JLabel lblNewLabel_8 = new JLabel("Runter");
		lblNewLabel_8.setFont(actualFont);
		panel.add(lblNewLabel_8, "cell 0 5,grow");

		JLabel lblNewLabel_2 = new JLabel("schnelle Geschw.");
		lblNewLabel_2.setFont(actualFont);
		panel.add(lblNewLabel_2, "cell 0 6,grow");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setFont(actualFont);
		fastDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastDownSpeedTF, "cell 1 6,growx");

		JLabel lblNewLabel_3 = new JLabel("mittlere Geschw.");
		lblNewLabel_3.setFont(actualFont);
		panel.add(lblNewLabel_3, "cell 0 7,grow");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setFont(actualFont);
		middleDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleDownSpeedTF, "cell 1 7,growx");

		JLabel lblNewLabel_4 = new JLabel("langsame Geschw.");
		lblNewLabel_4.setFont(actualFont);
		panel.add(lblNewLabel_4, "cell 0 8,grow");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setFont(actualFont);
		slowDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		slowDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(slowDownSpeedTF, "cell 1 8,growx");

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Step-Panel", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		JLabel lblNewLabel_12 = new JLabel("Richtung umdrehen");
		lblNewLabel_12.setFont(actualFont);
		panel_1.add(lblNewLabel_12, "cell 0 0,alignx left");

		reverseCB = new JCheckBox();
		reverseCB.setFont(actualFont);
		panel_1.add(reverseCB, "cell 1 0,grow");
		reverseCB.setSelected(actualConnectionProperties.isReverseSteps());

		JLabel lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		lblNewLabel_9.setFont(actualFont);
		panel_1.add(lblNewLabel_9, "cell 0 1,alignx left");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setFont(actualFont);
		sleepMovementMirrorTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMovementMirror()), new Long(1), null, new Long(500)));
		sleepMovementMirrorTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMovementMirrorTF, "cell 1 1,growx");

		JLabel lblNewLabel_11 = new JLabel("Pause (Spiegel - Bild) [ms]");
		lblNewLabel_11.setFont(actualFont);
		panel_1.add(lblNewLabel_11, "cell 0 2,alignx left");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setFont(actualFont);
		sleepMirrorPictureTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMirrorPicture()), new Long(1), null, new Long(500)));
		sleepMirrorPictureTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMirrorPictureTF, "cell 1 2,growx");

		JLabel lblNewLabel_10 = new JLabel("Pause (Bild - Bew.) [ms]");
		lblNewLabel_10.setFont(actualFont);
		panel_1.add(lblNewLabel_10, "cell 0 3,alignx left");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setFont(actualFont);
		sleepPictureMovementTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepPictureMovement()), new Long(1), null, new Long(500)));
		sleepPictureMovementTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepPictureMovementTF, "cell 1 3,growx");

		JLabel lblNewLabel_13 = new JLabel("Impulsdauer [ms]");
		lblNewLabel_13.setFont(actualFont);
		panel_1.add(lblNewLabel_13, "cell 0 4,alignx left");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setFont(actualFont);
		pulseDurationTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties.getPulseDuration()),
				new Long(1), null, new Long(500)));
		pulseDurationTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(pulseDurationTF, "cell 1 4,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Okay");
		okButton.setFont(actualFont);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				handleOkayButton();

			}

		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setFont(actualFont);
		cancelButton.setActionCommand("Cancel");

		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancel = true;
				dispose();
			}

		});

	}

	protected void handleOkayButton() {
		boolean allValid = validateUserSettings();
		LOGGER.info("try to save properties");
		LOGGER.info("properties are valid: " + allValid);
		if (allValid) {
			ComConnectionProperties newConnectionProperties = getNewConnectionProperties();
			setConnectionProperties(newConnectionProperties);

			for (ComConnectionPropertiesListener listener : listenerList) {
				ComConnectionPropertiesChangeEvent event = new ComConnectionPropertiesChangeEvent(this,
						newConnectionProperties);
				listener.handleComConnectionPropertiesChangeEvent(event);
			}

			dispose();
		}
	}

	private void cancel() {
		setConnectionPropertiesValues(actualConnectionProperties);
	}

	public ComConnectionProperties getNewConnectionProperties() {
		ComConnectionProperties comConnectionProperties = new ComConnectionProperties(actualConnectionProperties);

		int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		comConnectionProperties.setFastUpSpeed(fastUpSpeed);

		int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);

		int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		comConnectionProperties.setSlowUpSpeed(slowUpSpeed);

		int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		comConnectionProperties.setSlowDownSpeed(slowDownSpeed);

		int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);

		int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		comConnectionProperties.setFastDownSpeed(fastDownSpeed);

		long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);

		long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);

		long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);

		long pulseDuration = (long) pulseDurationTF.getValue();
		comConnectionProperties.setPulseDuration(pulseDuration);

		boolean reverseSteps = reverseCB.isSelected();
		comConnectionProperties.setReverseSteps(reverseSteps);

		return comConnectionProperties;

	}

	protected boolean validateUserSettings() {
		boolean allValid = true;

		int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		allValid = validateSpeed(fastUpSpeed, fastUpSpeedTF);

		int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		allValid = validateSpeed(middleUpSpeed, middleUpSpeedTF);

		int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		allValid = validateSpeed(slowUpSpeed, slowUpSpeedTF);

		int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		allValid = validateSpeed(slowDownSpeed, slowDownSpeedTF);

		int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		allValid = validateSpeed(middleDownSpeed, middleDownSpeedTF);

		int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		allValid = validateSpeed(fastDownSpeed, fastDownSpeedTF);

		long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		allValid = validateSleep(sleepMovementMirror, sleepMovementMirrorTF);

		long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		allValid = validateSleep(sleepMirrorPicture, sleepMirrorPictureTF);

		long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		allValid = validateSleep(sleepPictureMovement, sleepPictureMovementTF);

		long pulseDuration = (long) pulseDurationTF.getValue();
		allValid = validateSleep(pulseDuration, pulseDurationTF);

		return allValid;

	}

	protected boolean validateSpeed(int speed, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidSpeed(speed)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateSleep(long sleep, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidSleep(sleep)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateMode(int mode, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidMicrostepResolutionMode(mode)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	protected boolean validateStepCount(int stepCount, JComponent component) {
		boolean allValid = false;

		if (!validator.isValidStepcount(stepCount)) {
			component.setBackground(Color.red);
			LOGGER.error("error by parsing " + component.getName());
		} else {
			allValid = true;
			component.setBackground(Color.white);
		}
		return allValid;
	}

	public void addComConnectionPropertiesListener(ComConnectionPropertiesListener listener) {
		listenerList.add(listener);
	}

	public void removeComConnectionPropertiesListener(ComConnectionPropertiesListener listener) {
		listenerList.remove(listener);
	}

	public ComConnectionProperties getConnectionProperties() {
		return actualConnectionProperties;
	}

	public void setConnectionProperties(ComConnectionProperties connectionProperties) {
		actualConnectionProperties = connectionProperties;
		setConnectionPropertiesValues(actualConnectionProperties);
	}

	protected void setConnectionPropertiesValues(ComConnectionProperties connectionProperties) {
		fastUpSpeedTF.setValue(connectionProperties.getFastUpSpeed());
		middleUpSpeedTF.setValue(connectionProperties.getMiddleUpSpeed());
		slowUpSpeedTF.setValue(connectionProperties.getSlowUpSpeed());
		fastDownSpeedTF.setValue(connectionProperties.getFastDownSpeed());
		middleDownSpeedTF.setValue(connectionProperties.getMiddleDownSpeed());
		slowDownSpeedTF.setValue(connectionProperties.getSlowDownSpeed());
		reverseCB.setSelected(connectionProperties.isReverseSteps());
		sleepMirrorPictureTF.setValue(connectionProperties.getSleepMirrorPicture());
		sleepMovementMirrorTF.setValue(connectionProperties.getSleepMovementMirror());
		sleepPictureMovementTF.setValue(connectionProperties.getSleepPictureMovement());
		pulseDurationTF.setValue(connectionProperties.getPulseDuration());

	}
}
