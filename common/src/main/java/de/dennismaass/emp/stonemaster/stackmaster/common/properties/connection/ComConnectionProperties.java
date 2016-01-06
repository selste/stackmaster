package de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComConnectionProperties {

	private int fastUpSpeed = 600, middleUpSpeed = 300, slowUpSpeed = 100;
	private int slowDownSpeed = 100, middleDownSpeed = 300, fastDownSpeed = 600;
	private boolean reverseRelativ = false;

	private int stepsPerMm = 6403;
	private boolean reverseSteps = false;
	private int microstepResolutionMode = 4;

	private long sleepMovementMirror = 1000;
	private long sleepMirrorPicture = 1000;
	private long sleepWhileMove = 1000;
	private long sleepPictureMovement = 1000;
	private long pulseDuration = 1000;

	private double stepSize = 0.01;
	private double translation = 0.05;

	private int pictureCount = 0;
	private boolean mirrorActivated = false;
	private String comConnectionName;

	private int maxSpeed = 800;
	private int maxAcceleration=200;

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
		setTranslation(connectionProperties.getTranslation());
		setMaxSpeed(connectionProperties.getMaxSpeed());
		setMaxAcceleration(connectionProperties.getMaxAcceleration());
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("ConnectionProperties: ");
		stringBuilder.append("fastUpSpeed=" + fastUpSpeed);
		stringBuilder.append(", middleUpSpeed=" + middleUpSpeed);
		stringBuilder.append(", slowUpSpeed=" + slowUpSpeed);

		stringBuilder.append(", fastDownSpeed=" + fastDownSpeed);
		stringBuilder.append(", slowDownSpeed=" + slowDownSpeed);
		stringBuilder.append(", middleDownSpeed=" + middleDownSpeed);

		stringBuilder.append(", stepsPerMm=" + stepsPerMm);
		stringBuilder.append(", reverseSteps=" + reverseSteps);
		stringBuilder.append(", microstepResolutionMode=" + microstepResolutionMode);

		stringBuilder.append(", sleepMovementMirror=" + sleepMovementMirror);
		stringBuilder.append(", sleepMirrorPicture=" + sleepMirrorPicture);
		stringBuilder.append(", sleepWhileMove=" + sleepWhileMove);
		stringBuilder.append(", sleepPictureMovement=" + sleepPictureMovement);
		stringBuilder.append(", pulseDuration=" + pulseDuration);

		stringBuilder.append(", stepSize=" + stepSize);
		stringBuilder.append(", pictureCount=" + pictureCount);
		stringBuilder.append(", mirrorActivated=" + mirrorActivated);
		stringBuilder.append(", comConnectionName=" + comConnectionName);
		stringBuilder.append(", translation=" + translation);
		stringBuilder.append(", maxSpeed=" + maxSpeed);
		stringBuilder.append(", maxAcceleration=" + maxAcceleration);

		return stringBuilder.toString();
	}

}
