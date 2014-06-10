package de.dennismaass.stonemaster.stackmaster.comport.command.instruction;

import de.dennismaass.stonemaster.stackmaster.comport.command.ComInstructionID;

public class StopComInstruction extends ComInstruction {

	public StopComInstruction(final int address, final int motor) {
		super(address, ComInstructionID.MOTOR_STOP, 0, motor, 0);
	}

}
