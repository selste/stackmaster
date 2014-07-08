package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComConnectionProperties {

	private int fastUpSpeed = 1500, middleUpSpeed = 500, slowUpSpeed = 10;
	private int slowDownSpeed = 10, middleDownSpeed = 500, fastDownSpeed = 1500;
	private boolean reverseRelativ = false;

	private int stepsPerMm = 66000;
	private boolean reverseSteps = false;
	private int microstepResolutionMode = 4;

	private long sleepMovementMirror = 1000;
	private long sleepMirrorPicture = 1000;
	private long sleepWhileMove = 1000;
	private long sleepPictureMovement = 1000;
	private long pulseDuration = 1000;

	private double stepSize = 0.01;
	private int pictureCount = 0;
	private boolean mirrorActivated = false;
	private String comConnectionName;

	public ComConnectionProperties() {
	}

	public ComConnectionProperties(ComConnectionProperties connectionProperties) {

		setComConnectionName(connectionProperties.getComConnectionName());
		setFastDownSpeed(connectionProperties.getFastDownSpeed());
		setFastUpSpeed(connectionProperties.getFastUpSpeed());
		setMicrostepResolutionMode(connectionProperties.getMicrostepResolutionMode());
		setMiddleDownSpeed(connectionProperties.getMiddleDownSpeed());
		setMiddleUpSpeed(connectionProperties.getMiddleUpSpeed());
		setPulseDuration(connectionProperties.getPulseDuration());
		setReverseSteps(connectionProperties.isReverseSteps());
		setSleepMirrorPicture(connectionProperties.getSleepMirrorPicture());
		setSleepMovementMirror(connectionProperties.getSleepMovementMirror());
		setSleepPictureMovement(connectionProperties.getSleepPictureMovement());
		setSleepWhileMove(connectionProperties.getSleepWhileMove());
		setSlowDownSpeed(connectionProperties.getSlowDownSpeed());
		setSlowUpSpeed(connectionProperties.getSlowUpSpeed());
		setStepSize(connectionProperties.getStepSize());
		setStepsPerMm(connectionProperties.getStepsPerMm());

	}

}
