package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Font;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;
import net.miginfocom.swing.MigLayout;

/**
 * Klasse für ein Panel, welche die Automatische Steuerung eines
 * Kameraschlittens mit Hilfe von zwei Positionen und einer vorgegebenen
 * Bilderzahl übernimmt.
 * 
 * @author Johannes
 *
 */
public class PictureModePanel extends JPanel {

	private static final long serialVersionUID = -2137933567033802574L;

	private static final Logger LOGGER = Logger.getLogger(PictureModePanel.class);

	private static final String IMAGES = "/images/";
	private static final String START_LBL_TXT = "Startposition: ";
	private static final String END_LBL_TXT = "Endposition: ";

	private SwingStarter starter;

	private double startPos, endPos;

	private JSpinner picSpinner;

	private JButton upButton, downButton, saveStartButton, saveEndButton, moveStartButton, startButton, stopButton,
			pauseButton, resetButton;

	private JRadioButton fast, normal, slow;
	private ButtonGroup speeds;

	private JLabel stateLine, relativLabel, autoLabel, startPosLabel, endPosLabel, picCountLabel, picsLabel,
			stepSizeDescLabel, stepSizeLabel, maxPicsLabel, speedLabel;

	private ComConnectionProperties properties;

	public Font actualFont = new Font("Arial", Font.PLAIN, 20);

	private ImageIcon upIcon, downIcon;

	private JCheckBox mirrorCheck;

	public PictureModePanel(ComConnectionProperties properties, final JLabel stateLine, SwingStarter starter) {
		this.properties = properties;
		setStateLine(stateLine);
		this.starter = starter;

		this.setLayout(new MigLayout("", "[] []30[] [] []", "[]20[][][][][][]"));

		picSpinner = new JSpinner();
		picSpinner.setFont(actualFont);
		picSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));

		mirrorCheck = new JCheckBox("Spiegelvorauslösung");
		mirrorCheck.setFont(actualFont);

		createLabels();
		initIcons();
		defineButtons();
		defineRadio();
		assignListeners();

		buildLayout();
	}

	private void initIcons() {
		URL upURL = getClass().getResource(IMAGES + "upBlue_2.png");
		URL downURL = getClass().getResource(IMAGES + "downBlue_2.png");

		upIcon = new ImageIcon(upURL);
		upIcon = ImageUtils.getResizedImage(upIcon, 30, 30);
		downIcon = new ImageIcon(downURL);
		downIcon = ImageUtils.getResizedImage(downIcon, 30, 30);
	}

	private void buildLayout() {
		// TODO Auto-generated method stub

	}

	private void assignListeners() {
		// TODO Auto-generated method stub

	}

	private void defineRadio() {
		fast = new JRadioButton("schnell");
		normal = new JRadioButton("normal");
		normal.setSelected(true);
		slow = new JRadioButton("langsam");

		speeds = new ButtonGroup();
		speeds.add(fast);
		speeds.add(normal);
		speeds.add(slow);
	}

	private void createLabels() {
		relativLabel = new JLabel("Relative Bewegung");
		relativLabel.setFont(actualFont);
		autoLabel = new JLabel("Automatische Bewegung durch Bildzahl");
		autoLabel.setFont(actualFont);
		startPosLabel = new JLabel(START_LBL_TXT + "leer");
		startPosLabel.setFont(actualFont);
		endPosLabel = new JLabel(END_LBL_TXT + "leer");
		endPosLabel.setFont(actualFont);
		picCountLabel = new JLabel("X / X");
		picCountLabel.setFont(actualFont);
		picsLabel = new JLabel("Gemachte Bilder: ");
		picsLabel.setFont(actualFont);
		stepSizeDescLabel = new JLabel("Berechnete Schrittgröße: ");
		stepSizeDescLabel.setFont(actualFont);
		stepSizeLabel = new JLabel("0");
		stepSizeLabel.setFont(actualFont);
		maxPicsLabel = new JLabel("Maximale Bildzahl: ");
		maxPicsLabel.setFont(actualFont);
		speedLabel = new JLabel("Geschwindigkeit");
		speedLabel.setFont(actualFont);
	}

	private void defineButtons() {
		saveStartButton = new JButton("Startpunkt speichern");
		saveStartButton.setFont(actualFont);
		saveEndButton = new JButton("Endpunkt speichern");
		saveEndButton.setFont(actualFont);
		startButton = new JButton("Start");
		startButton.setFont(actualFont);
		stopButton = new JButton("Stop");
		stopButton.setFont(actualFont);
		pauseButton = new JButton("Pause");
		pauseButton.setFont(actualFont);
		resetButton = new JButton("Reset");
		resetButton.setFont(actualFont);
		moveStartButton = new JButton("Zum Startpunkt");
		moveStartButton.setFont(actualFont);
		upButton = new JButton(upIcon);
		downButton = new JButton(downIcon);
	}

	private void setStateLine(JLabel stateLine) {
		this.stateLine = stateLine;
	}

	private void initializePictureCountLabel() {

	}

	private void actualizePictureCountLabel() {

	}
}
