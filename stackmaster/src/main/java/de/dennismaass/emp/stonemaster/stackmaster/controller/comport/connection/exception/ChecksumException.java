package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.connection.exception;

public class ChecksumException extends Exception {
	private static final long serialVersionUID = 8818147605966147233L;

	public ChecksumException(String message) {
		super(message);
	}

	public ChecksumException(String message, Throwable cause) {
		super(message, cause);
	}

}
