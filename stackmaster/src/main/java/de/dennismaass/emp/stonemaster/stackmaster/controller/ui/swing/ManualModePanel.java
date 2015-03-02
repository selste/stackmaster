package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

public class ManualModePanel extends JPanel {

	private static final long serialVersionUID = 5989898071317069721L;

	private static final String IMAGES = "/images/";
	private static final String SLOW = "Langsam";
	private static final String NORMAL = "Normal";
	private static final String FAST = "Schnell";
	
	private static Logger LOGGER = Logger.getLogger(ManualModePanel.class);
	
	private ButtonGroup speed;

	private JRadioButton fast, normal, slow;
	
	private JButton upButton, downButton, pauseButton,
				stopButton, resetButton, executeButton;
	
	private ImageIcon upIcon, downIcon;
	
	private JLabel stateLine;
	
	private ComCommunicator communicator;
	
	private ComConnectionProperties properties ;

	private JCheckBox checkMirror;

	private JLabel relativeLabel, stepLabel, speedLabel, picCountLabel,
				stepSizeLabel, picsTakenLabel, pathTraveledLabel,
				assumedTimeLabel, nessecaryPathLengthLabel, picsNumberLabel,
				pathDistanceLabel, timeNumberLabel, pathLengthLabel;

	private JSpinner stepSpinner, picSpinner;
	
	private DecimalFormat df = new DecimalFormat("0.0000");
	

	public ManualModePanel(ComConnectionProperties properties, JLabel stateLine) {
		this.properties = properties;
		//setVariablesFromProperties(properties);
		setStateLine(stateLine);
		
		this.setLayout(new MigLayout("debug", "[] []30[] [] []", "[]20[] [] [] []20[] [] []20[] []"));
		
		checkMirror = new JCheckBox("Spiegelvorauslösung");
		stepSpinner = new JSpinner();
		picSpinner = new JSpinner();
		
		createLabels();
		declareRadioButtons();
		groupRadioButtons();
		initIcons();
		declareButtons();
		assignListeners();
		
		buildLayout();
	}
	
	private void buildLayout() {
		this.add(relativeLabel, "center, span 2");
		this.add(stepLabel, "center, span 3, wrap");
		
		this.add(stepSizeLabel, "right, cell 2 1");
		this.add(stepSpinner, "span 2,pushx, growx, wrap");
		
		this.add(picCountLabel, "right, cell 2 2");
		this.add(picSpinner, "span 2, pushx, growx, wrap");
		
		this.add(checkMirror, "cell 3 3, wrap");
		
		this.add(executeButton, "cell 3 4, split 3");
		this.add(stopButton);
		this.add(pauseButton, "wrap");
		
		this.add(picsTakenLabel, "cell 2 5, right");
		this.add(picsNumberLabel, "left, wrap");
		
		this.add(pathTraveledLabel, "cell 2 6, right");
		this.add(pathDistanceLabel, "left, wrap");
		
		this.add(resetButton, "cell 3 7, left, wrap");
		
		this.add(assumedTimeLabel, "cell 2 8, right");
		this.add(timeNumberLabel, "wrap");
		
		this.add(nessecaryPathLengthLabel, "cell 2 9, right");
		this.add(pathLengthLabel, "wrap");
		
	}

	private void createLabels() {
		relativeLabel = new JLabel("Relative Bewegung");
		stepLabel = new JLabel("Steuerung durch Schritte");
		speedLabel = new JLabel("Geschwindigkeit:");
		picCountLabel = new JLabel("Anzahl Bilder:");
		stepSizeLabel = new JLabel("Schrittgröße [mm]:");
		picsTakenLabel = new JLabel("Anzahl getätigter Bilder: ");
		picsNumberLabel = new JLabel("0");
		pathTraveledLabel = new JLabel("Bisher zurückgelegter Weg [mm]: ");
		pathDistanceLabel = new JLabel("0");
		assumedTimeLabel = new JLabel("Geschätze Dauer: ");
		timeNumberLabel = new JLabel("0");
		nessecaryPathLengthLabel = new JLabel("erforderlicher Weg: ");
		pathLengthLabel = new JLabel("0 mm");
	}
	
	private void initIcons(){
		URL upURL = getClass().getResource(IMAGES + "up_blue2.png");
		URL downURL = getClass().getResource(IMAGES + "down_blue2.png");
		
		upIcon = new ImageIcon(upURL);
		upIcon = ImageUtils.getResizedImage(upIcon, 30, 30);
		downIcon = new ImageIcon(downURL);
		downIcon = ImageUtils.getResizedImage(downIcon, 30, 30);
	}

	private void declareButtons() {
		LOGGER.debug("creating normal Buttons.");
		
		upButton = new JButton(upIcon);
		downButton = new JButton(downIcon);
		pauseButton = new JButton("pause");
		stopButton = new JButton("stop");
		resetButton = new JButton("reset");
		executeButton = new JButton("ausführen");
		
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
				// TODO Auto-generated method stub
				
			}
		});
		
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

	private void groupRadioButtons() {
		LOGGER.debug("grouping Radio Buttons.");
		
		speed = new ButtonGroup();
		speed.add(fast);
		speed.add(normal);
		speed.add(slow);
	}

	private void declareRadioButtons() {
		LOGGER.debug("creating Radio Buttons.");
		
		fast = new JRadioButton(FAST);
		fast.setActionCommand(FAST);
		normal = new JRadioButton(NORMAL);
		normal.setActionCommand(NORMAL);
		normal.setSelected(true);
		slow = new JRadioButton(SLOW);
		slow.setActionCommand(SLOW);
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

