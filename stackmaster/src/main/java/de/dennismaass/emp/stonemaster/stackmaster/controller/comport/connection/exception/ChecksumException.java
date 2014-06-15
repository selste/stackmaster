package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.exception;

public class ChecksumException extends Exception {

	public ChecksumException(final String message) {
		super(message);
	}

	public ChecksumException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
