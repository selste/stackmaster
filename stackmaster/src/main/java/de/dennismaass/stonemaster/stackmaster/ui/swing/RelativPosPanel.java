package de.dennismaass.stonemaster.stackmaster.ui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import de.dennismaass.stonemaster.stackmaster.comport.communicator.ComCommunicator;
import de.dennismaass.stonemaster.stackmaster.comport.connection.ComConnectionProperties;

public class RelativPosPanel extends JPanel {

	private static final long serialVersionUID = 287824350037456067L;

	private static final Logger LOGGER = Logger.getLogger(RelativPosPanel.class);

	protected static final String upSlowTooltip = "langsam [hoch]";
	protected static final String upMiddleTooltip = "mittel [hoch]";
	protected static final String upFastTooltip = "schnell [hoch]";

	protected static final String downSlowTooltip = "langsam [runter]";
	protected static final String downMiddleTooltip = "mittel [runter]";
	protected static final String downFastTooltip = "schnell [runter]";

	private final JButton doubleUpButton;
	private final JButton upBigButton;
	private final JButton upSmallButton;
	private final JButton doubleDownButton, downBigButton, downSmallButton;

	private ImageIcon doubleUpIcon;
	private ImageIcon upBigIcon;
	private ImageIcon upSmallIcon;
	private ImageIcon downSmallIcon;
	private ImageIcon downBigIcon;
	private ImageIcon doubleDownIcon;

	private ComCommunicator communicator;

	private final ComConnectionProperties properties;
	private int fastUpSpeed = 1000, middleUpSpeed = 500, slowUpSpeed = 100;
	private int slowDownSpeed = 100, middleDownSpeed = 500, fastDownSpeed = 1000;

	private boolean reverseRelativ = false;

	private static final String IMAGES = "/images/";

	private JLabel stateLine;

	/**
	 * Create the panel.
	 */
	public RelativPosPanel(final ComConnectionProperties properties, final JLabel stateLine) {
		this.properties = properties;
		setVariablesFromProperties(properties);
		setStateLine(stateLine);

		initIcons();

		/** Relativ Tab */
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		doubleUpButton = new JButton(doubleUpIcon);
		doubleUpButton.setToolTipText(upFastTooltip);
		doubleUpButton.setOpaque(false);
		doubleUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		doubleUpButton.setPreferredSize(new Dimension(100, 70));
		doubleUpButton.setMinimumSize(new Dimension(10, 10));
		doubleUpButton.setMaximumSize(new Dimension(100, 70));
		add(doubleUpButton);
		doubleUpButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + fastUpSpeed);
					communicator.rotateRight(fastUpSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});

		add(Box.createVerticalStrut(2));

		upBigButton = new JButton(upBigIcon);
		upBigButton.setToolTipText(upMiddleTooltip);
		upBigButton.setOpaque(false);
		upBigButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		upBigButton.setPreferredSize(new Dimension(100, 50));
		upBigButton.setMinimumSize(new Dimension(10, 10));
		upBigButton.setMaximumSize(new Dimension(100, 50));
		add(upBigButton);
		upBigButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + middleUpSpeed);
					communicator.rotateRight(middleUpSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}

			}
		});

		add(Box.createVerticalStrut(2));

		upSmallButton = new JButton(upSmallIcon);
		upSmallButton.setToolTipText(upSlowTooltip);
		upSmallButton.setOpaque(false);
		upSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		upSmallButton.setMaximumSize(new Dimension(100, 30));
		upSmallButton.setMinimumSize(new Dimension(10, 10));
		upSmallButton.setPreferredSize(new Dimension(100, 30));
		upSmallButton.setSize(new Dimension(0, 50));
		add(upSmallButton);
		upSmallButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate right command with speed: " + slowUpSpeed);
					communicator.rotateRight(slowUpSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});

		add(Box.createVerticalStrut(20));

		downSmallButton = new JButton(downSmallIcon);
		downSmallButton.setToolTipText(downSlowTooltip);
		downSmallButton.setOpaque(false);
		downSmallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		downSmallButton.setPreferredSize(new Dimension(100, 30));
		downSmallButton.setMinimumSize(new Dimension(10, 10));
		downSmallButton.setMaximumSize(new Dimension(100, 30));
		add(downSmallButton);
		downSmallButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + slowDownSpeed);
					communicator.rotateLeft(slowDownSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});

		add(Box.createVerticalStrut(2));

		downBigButton = new JButton(downBigIcon);
		downBigButton.setToolTipText(downMiddleTooltip);
		downBigButton.setOpaque(false);
		downBigButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		downBigButton.setPreferredSize(new Dimension(100, 50));
		downBigButton.setMinimumSize(new Dimension(10, 10));
		downBigButton.setMaximumSize(new Dimension(100, 50));
		add(downBigButton);
		downBigButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + middleDownSpeed);
					communicator.rotateLeft(middleDownSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				if (communicator != null) {
					if (communicator.isActiv()) {
						LOGGER.info("stop motor");
						communicator.stop();
					}
				}
			}
		});

		add(Box.createVerticalStrut(2));

		doubleDownButton = new JButton(doubleDownIcon);
		doubleDownButton.setToolTipText(downFastTooltip);
		doubleDownButton.setOpaque(false);
		add(doubleDownButton);
		doubleDownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		doubleDownButton.setMaximumSize(new Dimension(100, 70));
		doubleDownButton.setMinimumSize(new Dimension(10, 10));
		doubleDownButton.setPreferredSize(new Dimension(100, 70));
		doubleDownButton.setSize(new Dimension(50, 50));
		doubleDownButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("send rotate left command with speed: " + fastDownSpeed);
					communicator.rotateLeft(fastDownSpeed);
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (communicator != null) {
					LOGGER.info("stop motor");
					communicator.stop();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
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

	public void setVariablesFromProperties(final ComConnectionProperties properties) {
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
		final URL doubleUpURL = getClass().getResource(IMAGES + "up_blue2.png");
		doubleUpIcon = new ImageIcon(doubleUpURL);
		doubleUpIcon = getResizedImage(doubleUpIcon, 70, 70);

		final URL upBigURL = getClass().getResource(IMAGES + "up_blue2.png");
		upBigIcon = new ImageIcon(upBigURL);
		upBigIcon = getResizedImage(upBigIcon, 50, 50);

		final URL upSmallURL = getClass().getResource(IMAGES + "up_blue2.png");
		upSmallIcon = new ImageIcon(upSmallURL);
		upSmallIcon = getResizedImage(upSmallIcon, 30, 30);

		final URL downSmallURL = getClass().getResource(IMAGES + "down_blue2.png");
		downSmallIcon = new ImageIcon(downSmallURL);
		downSmallIcon = getResizedImage(downSmallIcon, 30, 30);

		final URL downBigURL = getClass().getResource(IMAGES + "down_blue2.png");
		downBigIcon = new ImageIcon(downBigURL);
		downBigIcon = getResizedImage(downBigIcon, 50, 50);

		final URL doubleDownURL = getClass().getResource(IMAGES + "down_blue2.png");
		doubleDownIcon = new ImageIcon(doubleDownURL);
		doubleDownIcon = getResizedImage(doubleDownIcon, 70, 70);
		/** Icons Ende */
	}

	// TODO: auslagern
	protected ImageIcon getResizedImage(ImageIcon icon, final int width, final int height) {
		LOGGER.info("resize icon to " + width + "x" + height);

		final Image img = icon.getImage();
		final Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		return icon;
	}

	public void setAllComponentsDisableState(final boolean disableState) {
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
