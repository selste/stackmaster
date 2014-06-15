package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.List;

import de.dennismaass.emp.stonemaster.stackmaster.controller.util.RxtxUtils;

public class ConnectionThread extends Thread {
	private final List<CommPortIdentifier> portList = new ArrayList<>();
	private final List<CommPortIdentifierNotificationListener> commPortIdentifierListenerList = new ArrayList<>();

	private long delay = 1000;
	private boolean running = true;

	public ConnectionThread(final int delay) {
		this();
		this.delay = delay;
	}

	public ConnectionThread() {
		super();
	}

	@Override
	public void run() {
		while (isRunning()) {
			refreshConnections();

			try {
				Thread.sleep(getDelay());
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void refreshConnections() {
		final List<CommPortIdentifier> actualPortList = RxtxUtils.getCommPortIdentifier();

		if (!actualPortList.equals(portList)) {
			portList.clear();
			portList.addAll(actualPortList);
			sendEvent(portList);
		}
	}

	protected void sendEvent(final List<CommPortIdentifier> actualPortList) {
		for (final CommPortIdentifierNotificationListener listener : commPortIdentifierListenerList) {
			final CommPortIdentifierNotificationEvent event = new CommPortIdentifierNotificationEvent(this, actualPortList);
			listener.handleCommPortIdentifierNotificationEvent(event);
		}

	}

	public void addCommPortIdentifierListener(final CommPortIdentifierNotificationListener listener) {
		commPortIdentifierListenerList.add(listener);
	}

	public void removeCommPortIdentifierListener(final CommPortIdentifierNotificationListener listener) {
		commPortIdentifierListenerList.remove(listener);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(final boolean running) {
		this.running = running;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(final long delay) {
		this.delay = delay;
	}
}
