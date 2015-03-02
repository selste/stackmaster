package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Component;
import java.awt.LayoutManager;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import net.miginfocom.swing.MigLayout;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

public class AutoModePanel extends JPanel {

	private static final long serialVersionUID = 1042974763111948238L;
	
	private static final String IMAGES = "/images/";
	private static final String SLOW = "Langsam";
	private static final String NORMAL = "Normal";
	private static final String FAST = "Schnell";

	private ComConnectionProperties properties;
	
	private ButtonGroup speed;
	
	private JCheckBox mirrorCheckBox;
	
	private JRadioButton slowRadioButton, normalRadioButton, fastRadioButton;
	
	private ImageIcon upImage, downImage;

	private JButton saveStartButton, saveEndButton, startButton,
				stopButton, pauseButton, resetButton, upButton,
				downButton;
	
	private JLabel stateLine, relMoveLabel, autoPathLabel, 
				startPosLabel, endPosLabel, speedLabel,
				stepSizeLabel;

	private JSpinner stepSizeSpinner;

	private JLabel picsMadeLabel;

	private JLabel calculatedPicsLabel;

	



	public AutoModePanel(ComConnectionProperties properties, final JLabel stateLine) {
		this.properties = properties;
		setStateLine(stateLine);
		
		this.setLayout(new MigLayout("debug", "[] []30[] [] []", "[]20[][][][][][]"));
		
		mirrorCheckBox = new JCheckBox("Spiegelvorauslösung");
		stepSizeSpinner = new JSpinner();
		
		initIcons();
		defineButtons();
		assignListeners();
		defineLabels();
		createRadioGroup();
		
		buildLayout();
	}

	private void createRadioGroup() {
		slowRadioButton = new JRadioButton(SLOW);
		normalRadioButton = new JRadioButton(NORMAL);
		normalRadioButton.setSelected(true);
		fastRadioButton = new JRadioButton(FAST);
		
		speed = new ButtonGroup();
		speed.add(slowRadioButton);
		speed.add(normalRadioButton);
		speed.add(fastRadioButton);
	}

	private void buildLayout() {
		this.add(relMoveLabel, "center, span 2");
		this.add(autoPathLabel, "center, span 3, wrap");
		
		this.add(startPosLabel, "cell 2 1");
		this.add(saveStartButton, "wrap");
		
		this.add(speedLabel, "left");
		this.add(endPosLabel, "cell 2 2");
		this.add(saveEndButton, "wrap");
		
		this.add(slowRadioButton, "left");
		this.add(upButton);
		this.add(mirrorCheckBox, "left, wrap");
		
		this.add(normalRadioButton, "left");
		this.add(stepSizeLabel, "cell 2 4");
		this.add(stepSizeSpinner, "right, span 2, pushx, growx, wrap");
		
		this.add(fastRadioButton, "left");
		this.add(downButton);
		this.add(startButton, "split 3");
		this.add(stopButton);
		this.add(pauseButton, "center, wrap");
		
		this.add(picsMadeLabel, "cell 2 6, center");
		this.add(calculatedPicsLabel, "left");
		this.add(resetButton, "left, wrap");
	}

	private void defineLabels() {
		relMoveLabel = new JLabel("relative Bewegung");
		autoPathLabel = new JLabel("Steuerung Automatik, Streckenbasiert");
		startPosLabel = new JLabel("Startposition: " + "leer");
		endPosLabel = new JLabel("Endposition: " + "leer");
		speedLabel = new JLabel("Bewegungsgeschwindigkeit");
		stepSizeLabel = new JLabel("Schrittgröße [mm]: ");
		picsMadeLabel = new JLabel("Gemachte Bilder / Maximale Anzahl: ");
		calculatedPicsLabel = new JLabel("Y" +  " / " + "X");
	}

	private void assignListeners() {
		// TODO Auto-generated method stub
		
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
		saveStartButton = new JButton("Anfangspunkt Speichern");
		saveEndButton = new JButton("Endpunkt Speichern");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		pauseButton = new JButton("Pause");
		resetButton = new JButton("reset");
		upButton = new JButton(upImage);
		downButton = new JButton(downImage);
	}

	private void setStateLine(JLabel stateLine) {
		this.stateLine = stateLine;
	}

	

}

