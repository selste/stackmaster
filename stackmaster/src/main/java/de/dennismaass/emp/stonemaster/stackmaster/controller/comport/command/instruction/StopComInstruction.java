package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.instruction;

import de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command.ComInstructionID;

public class StopComInstruction extends ComInstruction {

	public StopComInstruction(int address, int motor) {
		super(address, ComInstructionID.MOTOR_STOP, 0, motor, 0);
	}

}
