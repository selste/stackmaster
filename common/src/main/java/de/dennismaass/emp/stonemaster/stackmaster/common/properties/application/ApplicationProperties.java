package de.dennismaass.emp.stonemaster.stackmaster.common.properties.application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationProperties {
	private String firstName;
	private String lastName;
	private boolean firstUse = true;
	private int fontSize = 20;

	public ApplicationProperties() {
	}

	public ApplicationProperties(ApplicationProperties applicationProperties) {
		setFirstName(applicationProperties.getFirstName());
		setFirstUse(applicationProperties.isFirstUse());
		setLastName(applicationProperties.getLastName());
		setFontSize(applicationProperties.getFontSize());
	}

}
