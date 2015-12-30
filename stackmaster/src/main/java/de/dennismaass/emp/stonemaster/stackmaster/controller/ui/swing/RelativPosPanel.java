package de.dennismaass.emp.stonemaster.stackmaster.controller.ui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;
import de.dennismaass.emp.stonemaster.stackmaster.controller.util.ImageUtils;
import net.miginfocom.swing.MigLayout;

public class RelativPosPanel extends JPanel {

	private static final long serialVersionUID = 287824350037456067L;

	private static Logger LOGGER = Logger.getLogger(RelativPosPanel.class);

	protected static String upSlowTooltip = "langsam [hoch]";
	protected static String upMiddleTooltip = "mittel [hoch]";
	protected static String upFastTooltip = "schnell [hoch]";

	protected static String downSlowTooltip = "langsam [runter]";
	protected static String downMiddleTooltip = "mittel [runter]";
	protected static String downFastTooltip = "schnell [runter]";

	private JButton doubleUpButton;
	private JButton upBigButton;
	private JButton upSmallButton;
	private JButton doubleDownButton, downBigButton, downSmallButton;

	private ImageIcon doubleUpIcon;
	private ImageIcon upBigIcon;
	private ImageIcon upSmallIcon;
	private ImageIcon downSmallIcon;
	private ImageIcon downBigIcon;
	private ImageIcon doubleDownIcon;

	private ComCommunicator communicator;

	private ComConnectionProperties properties;
	private int fastUpSpeed = 600, middleUpSpeed = 300, slowUpSpeed = 100;
	private int slowDownSpeed = 100, middleDownSpeed = 300, fastDownSpeed = 600;

	private boolean reverseRelativ = false;

	private static String IMAGES = "/images/";

	private JLabel stateLine;
	private JPanel panel;

	public RelativPosPanel(ComConnectionProperties properties, JLabel stateLine) {
		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);

		initIcons();
		setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));

		panel = new JPanel();
		add(panel, "cell 0 0,alignx center,aligny center");
		panel.setLayout(new MigLayout("", "[100px]", "[70px][50px][30px][30px][50px][70px]"));

		doubleUpButton = new JButton(doubleUpIcon);
		panel.add(doubleUpButton, "cell 0 0,alignx center,aligny center");
		doubleUpButton.setToolTipText(upFastTooltip);
		doubleUpButton.setOpaque(false);
		doubleUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		doubleUpButton.setPreferredSize(new Dimension(100, 70));
		doubleUpButton.setMinimumSize(new Dimension(10, 10));
		doubleUpButton.setMaximumSize(new Dimension(100, 70));

		upBigButton = new JButton(upBigIcon);
		panel.add(upBigButton, "cell 0 1,alignx center,aligny center");
		upBigButton.setToolTipText(upMiddleTooltip);
		upBigButton.setOpaque(false);
		upBigButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		upBigButton.setPreferredSize(new Dimension(100, 50));
		upBigButton.setMinimumSize(new Dimension(10, 10));
		upBigButton.setMaximumSize(new Dimension(100, 50));

		upSmallButton = new JButton(upSmallIcon);
		panel.add(upSmallButton, "cell 0 2,alignx center,aligny center");
		upSmallButton.setToolTipText(upSlowTooltip);
		upSmallButton.setOpaque(false);
		upSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		upSmallButton.setMaximumSize(new Dimension(100, 30));
		upSmallButton.setMinimumSize(new Dimension(10, 10));
		upSmallButton.setPreferredSize(new Dimension(100, 30));
		upSmallButton.setSize(new Dimension(0, 50));

		downSmallButton = new JButton(downSmallIcon);
		panel.add(downSmallButton, "cell 0 3,alignx center,aligny center");
		downSmallButton.setToolTipText(downSlowTooltip);
		downSmallButton.setOpaque(false);
		downSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		downSmallButton.setPreferredSize(new Dimension(100, 30));
		downSmallButton.setMinimumSize(new Dimension(10, 10));
		downSmallButton.setMaximumSize(new Dimension(100, 30));

		downBigButton = new JButton(downBigIcon);
		panel.add(downBigButton, "cell 0 4,alignx center,aligny center");
		downBigButton.setToolTipText(downMiddleTooltip);
		downBigButton.setOpaque(false);
		downBigButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		downBigButton.setPreferredSize(new Dimension(100, 50));
		downBigButton.setMinimumSize(new Dimension(10, 10));
		downBigButton.setMaximumSize(new Dimension(100, 50));

		doubleDownButton = new JButton(doubleDownIcon);
		panel.add(doubleDownButton, "cell 0 5,alignx center,aligny center");
		doubleDownButton.setToolTipText(downFastTooltip);
		doubleDownButton.setOpaque(false);
		doubleDownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		doubleDownButton.setMaximumSize(new Dimension(100, 70));
		doubleDownButton.setMinimumSize(new Dimension(10, 10));
		doubleDownButton.setPreferredSize(new Dimension(100, 70));
		doubleDownButton.setSize(new Dimension(50, 50));
		doubleDownButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + fastDownSpeed);
					communicator.rotateLeft(fastDownSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});
		downBigButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + middleDownSpeed);
					communicator.rotateLeft(middleDownSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});
		downSmallButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + slowDownSpeed);
					communicator.rotateLeft(slowDownSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});
		upSmallButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + slowUpSpeed);
					communicator.rotateRight(slowUpSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});
		upBigButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + middleUpSpeed);
					communicator.rotateRight(middleUpSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}

			}
		});
		doubleUpButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + fastUpSpeed);
					communicator.rotateRight(fastUpSpeed);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});
		/** Relativ Tab Ende */
	}

	public void setVariablesFromProperties(ComConnectionProperties properties) {
		fastUpSpeed = properties.getFastUpSpeed();
		middleUpSpeed = properties.getMiddleUpSpeed();
		slowUpSpeed = properties.getSlowUpSpeed();
		slowDownSpeed = properties.getSlowDownSpeed();
		middleDownSpeed = properties.getMiddleDownSpeed();
		fastDownSpeed = properties.getFastDownSpeed();
		reverseRelativ = properties.isReverseSteps();
	}

	protected void initIcons() {
		/** Icons */
		URL doubleUpURL = getClass().getResource(IMAGES + "up_blue2.png");
		doubleUpIcon = new ImageIcon(doubleUpURL);
		doubleUpIcon = ImageUtils.getResizedImage(doubleUpIcon, 70, 70);

		URL upBigURL = getClass().getResource(IMAGES + "up_blue2.png");
		upBigIcon = new ImageIcon(upBigURL);
		upBigIcon = ImageUtils.getResizedImage(upBigIcon, 50, 50);

		URL upSmallURL = getClass().getResource(IMAGES + "up_blue2.png");
		upSmallIcon = new ImageIcon(upSmallURL);
		upSmallIcon = ImageUtils.getResizedImage(upSmallIcon, 30, 30);

		URL downSmallURL = getClass().getResource(IMAGES + "down_blue2.png");
		downSmallIcon = new ImageIcon(downSmallURL);
		downSmallIcon = ImageUtils.getResizedImage(downSmallIcon, 30, 30);

		URL downBigURL = getClass().getResource(IMAGES + "down_blue2.png");
		downBigIcon = new ImageIcon(downBigURL);
		downBigIcon = ImageUtils.getResizedImage(downBigIcon, 50, 50);

		URL doubleDownURL = getClass().getResource(IMAGES + "down_blue2.png");
		doubleDownIcon = new ImageIcon(doubleDownURL);
		doubleDownIcon = ImageUtils.getResizedImage(doubleDownIcon, 70, 70);
		/** Icons Ende */
	}

	public void setAllComponentsDisableState(boolean disableState) {
		if (!disableState) {
			LOGGER.info("set all components enable");
		} else {
			LOGGER.info("set all components disable");
		}

		upBigButton.setEnabled(!disableState);
		upSmallButton.setEnabled(!disableState);
		doubleUpButton.setEnabled(!disableState);
		doubleDownButton.setEnabled(!disableState);
		downBigButton.setEnabled(!disableState);
		downSmallButton.setEnabled(!disableState);
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
