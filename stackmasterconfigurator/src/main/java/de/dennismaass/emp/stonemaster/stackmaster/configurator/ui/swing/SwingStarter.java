package de.dennismaass.emp.stonemaster.stackmaster.configurator.ui.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

public class SwingStarter extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(SwingStarter.class);

	public SwingStarter() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		final JPanel generalPanel = new JPanel();
		tabbedPane.addTab("Allgemein", null, generalPanel, null);
		generalPanel.setLayout(new MigLayout("", "[]", "[]"));

		final JPanel relativPanel = new JPanel();
		tabbedPane.addTab("Relativ-Panel", null, relativPanel, null);
		relativPanel.setLayout(new MigLayout("", "[]", "[]"));

		final JPanel stepPanel = new JPanel();
		tabbedPane.addTab("Step-Panel", null, stepPanel, null);
		stepPanel.setLayout(new MigLayout("", "[]", "[]"));
	}

	private static final String FILE_ENDING = ".stackmaster";

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {
			LOGGER.error("SystemLookAndFeel not available", e);
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final SwingStarter frame = new SwingStarter();
					frame.setVisible(true);
				} catch (final Exception e) {
					LOGGER.error("Error in main-methode by creating SwingStarter object", e);
					e.printStackTrace();
				}
			}
		});
	}

}
