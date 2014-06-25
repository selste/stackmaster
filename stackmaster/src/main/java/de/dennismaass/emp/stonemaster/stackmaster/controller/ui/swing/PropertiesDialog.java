package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionPropertiesChangeEvent;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.ComConnectionPropertiesListener;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.PropertiesValidator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

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
	private final JTextField textField;
	private final JTextField textField_1;

	private final PropertiesValidator validator = new PropertiesValidator();

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
		setBounds(100, 100, 391, 431);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(final WindowEvent e) {
				if (cancel) {
					cancel();
				}
			}

			@Override
			public void windowClosed(final WindowEvent e) {
				if (cancel) {
					cancel();
				}
			}

		});
		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(SwingStarter.FONT);
		contentPanel.add(tabbedPane);

		final JPanel allgemein = new JPanel();
		tabbedPane.addTab("Allgemein", null, allgemein, null);
		allgemein.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

		final JLabel lblVorname = new JLabel("Vorname");
		lblVorname.setFont(SwingStarter.FONT);
		allgemein.add(lblVorname, "cell 0 0,alignx trailing");

		textField = new JTextField();
		textField.setFont(SwingStarter.FONT);
		allgemein.add(textField, "cell 1 0,growx");
		textField.setColumns(10);

		final JLabel lblNachname = new JLabel("Nachname");
		lblNachname.setFont(SwingStarter.FONT);
		allgemein.add(lblNachname, "cell 0 1,alignx trailing");

		textField_1 = new JTextField();
		textField_1.setFont(SwingStarter.FONT);
		allgemein.add(textField_1, "cell 1 1,growx");
		textField_1.setColumns(10);

		final JLabel label = new JLabel(" ");
		allgemein.add(label, "cell 0 2");

		final JPanel panel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, panel, null);
		panel.setLayout(new MigLayout("", "[][]", "[][][][][][][][][]"));

		final JLabel lblNewLabel_6 = new JLabel("Hoch");
		lblNewLabel_6.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_6, "cell 0 0,grow");

		final JLabel lblNewLabel_5 = new JLabel("");
		panel.add(lblNewLabel_5, "cell 1 0,grow");

		final JLabel lblSchnelleGeschw = new JLabel("schnelle Geschw.");
		lblSchnelleGeschw.setFont(SwingStarter.FONT);
		panel.add(lblSchnelleGeschw, "cell 0 1,grow");

		fastUpSpeedTF = new JSpinner();
		fastUpSpeedTF.setFont(SwingStarter.FONT);
		fastUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastUpSpeedTF, "cell 1 1,growx");

		final JLabel lblNewLabel = new JLabel("mittlere Geschw.");
		lblNewLabel.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel, "cell 0 2,grow");

		middleUpSpeedTF = new JSpinner();
		middleUpSpeedTF.setFont(SwingStarter.FONT);
		middleUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleUpSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleUpSpeedTF, "cell 1 2,growx");

		final JLabel lblNewLabel_1 = new JLabel("langsame Geschw.");
		lblNewLabel_1.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_1, "cell 0 3,grow");

		slowUpSpeedTF = new JSpinner();
		slowUpSpeedTF.setFont(SwingStarter.FONT);
		slowUpSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowUpSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		panel.add(slowUpSpeedTF, "cell 1 3,growx");

		final JLabel lblNewLabel_7 = new JLabel("");
		panel.add(lblNewLabel_7, "cell 1 4,grow");

		final JLabel lblNewLabel_8 = new JLabel("Runter");
		lblNewLabel_8.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_8, "cell 0 5,grow");

		final JLabel lblNewLabel_2 = new JLabel("schnelle Geschw.");
		lblNewLabel_2.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_2, "cell 0 6,grow");

		fastDownSpeedTF = new JSpinner();
		fastDownSpeedTF.setFont(SwingStarter.FONT);
		fastDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getFastDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		fastDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(fastDownSpeedTF, "cell 1 6,growx");

		final JLabel lblNewLabel_3 = new JLabel("mittlere Geschw.");
		lblNewLabel_3.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_3, "cell 0 7,grow");

		middleDownSpeedTF = new JSpinner();
		middleDownSpeedTF.setFont(SwingStarter.FONT);
		middleDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getMiddleDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		middleDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(middleDownSpeedTF, "cell 1 7,growx");

		final JLabel lblNewLabel_4 = new JLabel("langsame Geschw.");
		lblNewLabel_4.setFont(SwingStarter.FONT);
		panel.add(lblNewLabel_4, "cell 0 8,grow");

		slowDownSpeedTF = new JSpinner();
		slowDownSpeedTF.setFont(SwingStarter.FONT);
		slowDownSpeedTF.setModel(new SpinnerNumberModel(new Integer(actualConnectionProperties.getSlowDownSpeed()),
				new Integer(1), new Integer(ComCommunicator.MAX_SPEED), new Integer(100)));
		slowDownSpeedTF.setMinimumSize(new Dimension(100, 22));
		panel.add(slowDownSpeedTF, "cell 1 8,growx");

		final JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Step-Panel", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		final JLabel lblNewLabel_12 = new JLabel("Richtung umdrehen");
		lblNewLabel_12.setFont(SwingStarter.FONT);
		panel_1.add(lblNewLabel_12, "cell 0 0,alignx left");

		reverseCB = new JCheckBox();
		reverseCB.setFont(SwingStarter.FONT);
		panel_1.add(reverseCB, "cell 1 0,grow");
		reverseCB.setSelected(actualConnectionProperties.isReverseSteps());

		final JLabel lblNewLabel_9 = new JLabel("Pause (Bew. - Spiegel) [ms]");
		lblNewLabel_9.setFont(SwingStarter.FONT);
		panel_1.add(lblNewLabel_9, "cell 0 1,alignx left");

		sleepMovementMirrorTF = new JSpinner();
		sleepMovementMirrorTF.setFont(SwingStarter.FONT);
		sleepMovementMirrorTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMovementMirror()), new Long(1), null, new Long(500)));
		sleepMovementMirrorTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMovementMirrorTF, "cell 1 1,growx");

		final JLabel lblNewLabel_11 = new JLabel("Pause (Spiegel - Bild) [ms]");
		lblNewLabel_11.setFont(SwingStarter.FONT);
		panel_1.add(lblNewLabel_11, "cell 0 2,alignx left");

		sleepMirrorPictureTF = new JSpinner();
		sleepMirrorPictureTF.setFont(SwingStarter.FONT);
		sleepMirrorPictureTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepMirrorPicture()), new Long(1), null, new Long(500)));
		sleepMirrorPictureTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepMirrorPictureTF, "cell 1 2,growx");

		final JLabel lblNewLabel_10 = new JLabel("Pause (Bild - Bew.) [ms]");
		lblNewLabel_10.setFont(SwingStarter.FONT);
		panel_1.add(lblNewLabel_10, "cell 0 3,alignx left");

		sleepPictureMovementTF = new JSpinner();
		sleepPictureMovementTF.setFont(SwingStarter.FONT);
		sleepPictureMovementTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties
				.getSleepPictureMovement()), new Long(1), null, new Long(500)));
		sleepPictureMovementTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(sleepPictureMovementTF, "cell 1 3,growx");

		final JLabel lblNewLabel_13 = new JLabel("Impulsdauer [ms]");
		lblNewLabel_13.setFont(SwingStarter.FONT);
		panel_1.add(lblNewLabel_13, "cell 0 4,alignx left");

		pulseDurationTF = new JSpinner();
		pulseDurationTF.setFont(SwingStarter.FONT);
		pulseDurationTF.setModel(new SpinnerNumberModel(new Long(actualConnectionProperties.getPulseDuration()),
				new Long(1), null, new Long(500)));
		pulseDurationTF.setMinimumSize(new Dimension(100, 22));
		panel_1.add(pulseDurationTF, "cell 1 4,growx");

		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton("Okay");
		okButton.setFont(SwingStarter.FONT);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				handleOkayButton();

			}

		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		final JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setFont(SwingStarter.FONT);
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

	protected void handleOkayButton() {
		final boolean allValid = validateUserSettings();
		LOGGER.info("try to save properties");
		LOGGER.info("properties are valid: " + allValid);
		if (allValid) {
			final ComConnectionProperties newConnectionProperties = getNewConnectionProperties();
			setConnectionProperties(newConnectionProperties);

			for (final ComConnectionPropertiesListener listener : listenerList) {
				final ComConnectionPropertiesChangeEvent event = new ComConnectionPropertiesChangeEvent(this,
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
		final ComConnectionProperties comConnectionProperties = new ComConnectionProperties();

		final int fastUpSpeed = (int) fastUpSpeedTF.getValue();
		comConnectionProperties.setFastUpSpeed(fastUpSpeed);

		final int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		comConnectionProperties.setMiddleUpSpeed(middleUpSpeed);

		final int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		comConnectionProperties.setSlowUpSpeed(slowUpSpeed);

		final int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		comConnectionProperties.setSlowDownSpeed(slowDownSpeed);

		final int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		comConnectionProperties.setMiddleDownSpeed(middleDownSpeed);

		final int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		comConnectionProperties.setFastDownSpeed(fastDownSpeed);

		final long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		comConnectionProperties.setSleepMovementMirror(sleepMovementMirror);

		final long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		comConnectionProperties.setSleepMirrorPicture(sleepMirrorPicture);

		final long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		comConnectionProperties.setSleepPictureMovement(sleepPictureMovement);

		final long pulseDuration = (long) pulseDurationTF.getValue();
		comConnectionProperties.setPulseDuration(pulseDuration);

		final boolean reverseSteps = reverseCB.isSelected();
		comConnectionProperties.setReverseSteps(reverseSteps);

		return comConnectionProperties;

	}

	protected boolean validateUserSettings() {
		boolean allValid = true;

		final int fastUpSpeed = (int) fastUpSpeedTF.getValue();

		if (!validator.isValidSpeed(fastUpSpeed)) {
			allValid = false;
			fastUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastUpSpeed from Spinner");
		} else {
			fastUpSpeedTF.setBackground(Color.white);
		}

		final int middleUpSpeed = (int) middleUpSpeedTF.getValue();
		if (!validator.isValidSpeed(middleUpSpeed)) {
			allValid = false;
			middleUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleUpSpeed from Spinner");
		} else {
			middleUpSpeedTF.setBackground(Color.white);
		}

		final int slowUpSpeed = (int) slowUpSpeedTF.getValue();
		if (!validator.isValidSpeed(slowUpSpeed)) {
			allValid = false;
			slowUpSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowUpSpeed from Spinner");
		} else {
			slowUpSpeedTF.setBackground(Color.white);
		}

		final int slowDownSpeed = (int) slowDownSpeedTF.getValue();
		if (!validator.isValidSpeed(slowDownSpeed)) {
			allValid = false;
			slowDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing slowDownSpeed from Spinner");
		} else {
			slowDownSpeedTF.setBackground(Color.white);
		}

		final int middleDownSpeed = (int) middleDownSpeedTF.getValue();
		if (!validator.isValidSpeed(middleDownSpeed)) {
			allValid = false;
			middleDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing middleDownSpeed from Spinner");
		} else {
			middleDownSpeedTF.setBackground(Color.white);
		}

		final int fastDownSpeed = (int) fastDownSpeedTF.getValue();
		if (!validator.isValidSpeed(fastDownSpeed)) {
			allValid = false;
			fastDownSpeedTF.setBackground(Color.red);
			LOGGER.error("error by parsing fastDownSpeed from Spinner");
		} else {
			fastDownSpeedTF.setBackground(Color.white);
		}

		final long sleepMovementMirror = (long) sleepMovementMirrorTF.getValue();
		if (!validator.isValidSleep(sleepMovementMirror)) {
			allValid = false;
			sleepMovementMirrorTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMovementMirror Spinner");
		} else {
			sleepMovementMirrorTF.setBackground(Color.white);
		}

		final long sleepMirrorPicture = (long) sleepMirrorPictureTF.getValue();
		if (!validator.isValidSleep(sleepMirrorPicture)) {
			allValid = false;
			sleepMirrorPictureTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepMirrorPicture from Spinner");
		} else {
			sleepMirrorPictureTF.setBackground(Color.white);
		}

		final long sleepPictureMovement = (long) sleepPictureMovementTF.getValue();
		if (!validator.isValidSleep(sleepPictureMovement)) {
			allValid = false;
			sleepPictureMovementTF.setBackground(Color.red);
			LOGGER.error("error by parsing sleepPictureMovement from Spinner");
		} else {
			sleepPictureMovementTF.setBackground(Color.white);
		}

		final long pulseDuration = (long) pulseDurationTF.getValue();
		if (!validator.isValidSleep(pulseDuration)) {
			allValid = false;
			pulseDurationTF.setBackground(Color.red);
			LOGGER.error("error by parsing pulseDuration from Spinner");
		} else {
			pulseDurationTF.setBackground(Color.white);
		}

		return allValid;

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
		setConnectionPropertiesValues(actualConnectionProperties);
	}

	protected void setConnectionPropertiesValues(final ComConnectionProperties connectionProperties) {
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
