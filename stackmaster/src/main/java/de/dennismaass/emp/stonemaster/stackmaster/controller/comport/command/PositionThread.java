package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

public class PositionThread extends Thread {

	private final ComCommunicator communicator;

	private boolean running = true;
	private long delay = 10;

	public PositionThread(final ComCommunicator communicator) {
		this.communicator = communicator;
	}

	@Override
	public void run() {
		super.run();

		while (isRunning()) {
			getCommunicator().getPosition();
			pause(100);
			// TODO
			getCommunicator().storePosition(0);
			pause(100);
		}
	}

	protected void pause(final long delayInMillis) {
		try {
			Thread.sleep(delayInMillis);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(final long delay) {
		this.delay = delay;
	}

	public ComCommunicator getCommunicator() {
		return communicator;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(final boolean running) {
		this.running = running;
	}

}
