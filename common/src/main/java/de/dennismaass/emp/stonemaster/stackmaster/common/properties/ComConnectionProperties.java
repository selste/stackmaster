package de.dennismaass.emp.stonemaster.stackmaster.common.properties;

public class ComConnectionProperties {
	private int fastUpSpeed = 1500, middleUpSpeed = 500, slowUpSpeed = 10;
	private int slowDownSpeed = 10, middleDownSpeed = 500, fastDownSpeed = 1500;
	private final boolean reverseRelativ = false;

	private int stepsPerMm = 66000;
	private boolean reverseSteps = false;
	private int microstepResolutionMode = 4;

	private long sleepMovementMirror = 1000;
	private long sleepMirrorPicture = 1000;
	private long sleepWhileMove = 1000;
	private long sleepPictureMovement = 1000;
	private long pulseDuration = 1000;

	private double stepSize = 0.01;
	private final int pictureCount = 0;
	private final boolean mirrorActivated = false;
	private int actualPos;
	private String comConnectionName;
	private String firstName;
	private String lastName;

	public int getFastUpSpeed() {
		return fastUpSpeed;
	}

	public void setFastUpSpeed(final int fastUpSpeed) {
		this.fastUpSpeed = fastUpSpeed;
	}

	public int getMiddleUpSpeed() {
		return middleUpSpeed;
	}

	public void setMiddleUpSpeed(final int middleUpSpeed) {
		this.middleUpSpeed = middleUpSpeed;
	}

	public int getSlowUpSpeed() {
		return slowUpSpeed;
	}

	public void setSlowUpSpeed(final int slowUpSpeed) {
		this.slowUpSpeed = slowUpSpeed;
	}

	public int getSlowDownSpeed() {
		return slowDownSpeed;
	}

	public void setSlowDownSpeed(final int slowDownSpeed) {
		this.slowDownSpeed = slowDownSpeed;
	}

	public int getMiddleDownSpeed() {
		return middleDownSpeed;
	}

	public void setMiddleDownSpeed(final int middleDownSpeed) {
		this.middleDownSpeed = middleDownSpeed;
	}

	public int getFastDownSpeed() {
		return fastDownSpeed;
	}

	public void setFastDownSpeed(final int fastDownSpeed) {
		this.fastDownSpeed = fastDownSpeed;
	}

	public int getStepsPerMm() {
		return stepsPerMm;
	}

	public void setStepsPerMm(final int stepsPerMm) {
		this.stepsPerMm = stepsPerMm;
	}

	public boolean isReverseSteps() {
		return reverseSteps;
	}

	public void setReverseSteps(final boolean reverseSteps) {
		this.reverseSteps = reverseSteps;
	}

	public int getMicrostepResolutionMode() {
		return microstepResolutionMode;
	}

	public void setMicrostepResolutionMode(final int microstepResolutionMode) {
		this.microstepResolutionMode = microstepResolutionMode;
	}

	public long getSleepMovementMirror() {
		return sleepMovementMirror;
	}

	public void setSleepMovementMirror(final long sleepMovementMirror) {
		this.sleepMovementMirror = sleepMovementMirror;
	}

	public long getSleepMirrorPicture() {
		return sleepMirrorPicture;
	}

	public void setSleepMirrorPicture(final long sleepMirrorPicture) {
		this.sleepMirrorPicture = sleepMirrorPicture;
	}

	public long getSleepWhileMove() {
		return sleepWhileMove;
	}

	public void setSleepWhileMove(final long sleepWhileMove) {
		this.sleepWhileMove = sleepWhileMove;
	}

	public long getSleepPictureMovement() {
		return sleepPictureMovement;
	}

	public void setSleepPictureMovement(final long sleepPictureMovement) {
		this.sleepPictureMovement = sleepPictureMovement;
	}

	public long getPulseDuration() {
		return pulseDuration;
	}

	public void setPulseDuration(final long pulseDuration) {
		this.pulseDuration = pulseDuration;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(final double stepSize) {
		this.stepSize = stepSize;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getComConnectionName() {
		return comConnectionName;
	}

	public void setComConnectionName(String comConnectionName) {
		this.comConnectionName = comConnectionName;
	}

}
