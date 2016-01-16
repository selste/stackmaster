package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection;

import java.util.ArrayList;
import java.util.List;

import de.dennismaass.emp.stonemaster.stackmaster.controller.util.RxtxUtils;
import gnu.io.CommPortIdentifier;

public class ConnectionThread extends Thread {
	private List<CommPortIdentifier> portList = new ArrayList<>();
	private List<CommPortIdentifierNotificationListener> commPortIdentifierListenerList = new ArrayList<>();

	private long delay = 500;
	private boolean running = true;

	public ConnectionThread(int delay) {
		this.delay = delay;
	}

	public ConnectionThread() {}

	@Override
	public void run() {
		while (isRunning()) {
			refreshConnections();

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void refreshConnections() {
		List<CommPortIdentifier> actualPortList = RxtxUtils.getCommPortIdentifier();

		if (!actualPortList.equals(portList)) {
			portList.clear();
			portList.addAll(actualPortList);
			sendEvent(portList);
		}
	}

	protected void sendEvent(List<CommPortIdentifier> actualPortList) {
		for (CommPortIdentifierNotificationListener listener : commPortIdentifierListenerList) {
			CommPortIdentifierNotificationEvent event = new CommPortIdentifierNotificationEvent(this, actualPortList);
			listener.handleCommPortIdentifierNotificationEvent(event);
		}

	}

	public void addCommPortIdentifierListener(CommPortIdentifierNotificationListener listener) {
		commPortIdentifierListenerList.add(listener);
	}

	public void removeCommPortIdentifierListener(CommPortIdentifierNotificationListener listener) {
		commPortIdentifierListenerList.remove(listener);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
}
