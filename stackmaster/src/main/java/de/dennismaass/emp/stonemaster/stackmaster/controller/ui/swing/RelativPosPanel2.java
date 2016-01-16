package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;

public class RelativPosPanel2 extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(RelativPosPanel2.class);

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private static final String IMAGES = "/images/";
	private ImageIcon upIcon, downIcon;

	private JButton btnNewButton_1;

	private JButton btnNewButton;

	private JRadioButton middle;

	private JRadioButton fast;

	private JRadioButton slow;

	private ComConnectionProperties properties;

	private JLabel stateLine;

	private ComCommunicator communicator;

	public RelativPosPanel2(final ComConnectionProperties properties, final JLabel stateLine) {
		this.properties = properties;
		this.stateLine = stateLine;

		initIcons();

		setMinimumSize(new Dimension(150, 100));
		setPreferredSize(new Dimension(200, 200));
		setMaximumSize(new Dimension(200, 200));
		setLayout(new GridLayout(1, 2, 0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Geschwindigkeit");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.LEADING);
		panel_2.add(lblNewLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel_2.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		slow = new JRadioButton("langsam");
		panel.add(slow);
		buttonGroup.add(slow);

		middle = new JRadioButton("mittel");
		middle.setSelected(true);
		panel.add(middle);
		buttonGroup.add(middle);

		fast = new JRadioButton("schnell");
		panel.add(fast);
		buttonGroup.add(fast);

		JPanel panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new GridLayout(2, 1, 0, 0));

		btnNewButton_1 = new JButton(upIcon);
		panel_3.add(btnNewButton_1);

		btnNewButton = new JButton(downIcon);
		panel_3.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Richtung");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.LEADING);
		panel_1.add(lblNewLabel_1, BorderLayout.NORTH);

		btnNewButton_1.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping motor");
					communicator.stop();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					int upSpeed = getUpSpeed();
					LOGGER.info("rotate left command with speed: " + upSpeed);
					communicator.rotateRight(upSpeed);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stopping Motor");
						communicator.stop();
					}
				}
			}
		});

		btnNewButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stopping motor");
						communicator.stop();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					int downSpeed = getDownSpeed();
					LOGGER.info("rotating motor right with speed: " + downSpeed);
					communicator.rotateLeft(downSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stopping motor");
					communicator.stop();
				}
			}
		});
	}

	private int getUpSpeed() {
		int upSpeed = 0;
		if (slow.isSelected()) {
			upSpeed = properties.getSlowUpSpeed();
		} else if (middle.isSelected()) {
			upSpeed = properties.getMiddleUpSpeed();
		} else if (fast.isSelected()) {
			upSpeed = properties.getFastUpSpeed();
		}
		return upSpeed;
	}

	private int getDownSpeed() {
		int downSpeed = 0;
		if (slow.isSelected()) {
			downSpeed = properties.getSlowDownSpeed();
		} else if (middle.isSelected()) {
			downSpeed = properties.getMiddleDownSpeed();
		} else if (fast.isSelected()) {
			downSpeed = properties.getFastDownSpeed();
		}
		return downSpeed;
	}

	public void disableAllComponents(boolean disableState) {
		if (!disableState) {
			LOGGER.info("set all Components enable");
		} else {
			LOGGER.info("set all Components disable");
		}


		btnNewButton_1.setEnabled(!disableState);
		btnNewButton.setEnabled(!disableState);

		middle.setEnabled(!disableState);
		fast.setEnabled(!disableState);
		slow.setEnabled(!disableState);

	}

	private void initIcons() {
		URL upURL = getClass().getResource(IMAGES + "up_blue2.png");
		URL downURL = getClass().getResource(IMAGES + "down_blue2.png");

		upIcon = new ImageIcon(upURL);
		upIcon = ImageUtils.getResizedImage(upIcon, 30, 30);
		downIcon = new ImageIcon(downURL);
		downIcon = ImageUtils.getResizedImage(downIcon, 30, 30);
	}

	public ComCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ComCommunicator communicator) {
		this.communicator = communicator;
	}



	public void setProperties(ComConnectionProperties properties) {
		this.properties = properties;
	}

}
