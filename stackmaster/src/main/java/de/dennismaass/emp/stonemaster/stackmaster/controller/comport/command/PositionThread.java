package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command;

import lombok.Getter;
import lombok.Setter;
import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.communicator.ComCommunicator;

@Getter
@Setter
public class PositionThread extends Thread {

	private ComCommunicator communicator;
	private boolean running = true;
	private long delay = 10;

	public PositionThread(ComCommunicator communicator) {
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

	protected void pause(long delayInMillis) {
		try {
			Thread.sleep(delayInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
