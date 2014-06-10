package de.dennismaass.stonemaster.stackmaster.comport.command;

public final class ComInstructionID {

	// Opcodes of all TMCL commands that can be used in direct mode
	public static final int ROTATE_RIGHT = 1;
	public static final int ROTATE_LEFT = 2;
	public static final int MOTOR_STOP = 3;
	public static final int MOVE_POSITION = 4;

	public static final int STORE_AXIS_PARAMETER = 7;
	public static final int RSAP = 8;
	public static final int SGP = 9;
	public static final int GGP = 10;
	public static final int STGP = 11;
	public static final int RSGP = 12;
	public static final int RFS = 13;
	public static final int SIO = 14;
	public static final int GIO = 15;
	public static final int SCO = 30;
	public static final int GCO = 31;
	public static final int CCO = 32;

	public static final int ENABLE_INTERRUPTS = 25;
	public static final int ENABLE_INTERRUPTS_POSITION_REACHED = 3;

	// Opcodes of TMCL control functions (to be used to run or abort a TMCL
	// program in the module)
	public static final int APPLICATION_STOP = 128;
	public static final int APPLICATION_RUN = 129;
	public static final int APPLICATION_RESET = 131;

	// Options for SAP commandds
	public static final int SET_AXIS_PARAMETER = 5;
	public static final int SET_AXIS_PARAMETER_MAX_SPEED = 4;
	public static final int SET_AXIS_PARAMETER_POSITION_REACHED = 8;
	public static final int SET_AXIS_PARAMETER_MICROSTEP_RESOLUTION = 140;

	// Options for GAP commandds
	public static final int GET_AXIS_PARAMETER = 6;
	public static final int GET_AXIS_PARAMETER_ACTUAL_POSITION = 1;
	public static final int GET_AXIS_PARAMETER_ACTUAL_SPEED = 3;

	// Options for MVP commandds
	public static final int MOVE_POSITION_ABSOLUTE = 0;
	public static final int MOVE_POSITION_RELATIVE = 1;
	public static final int MOVE_POSITION_COORDINATE = 2;

	// Options for RFS command
	public static final int RFS_START = 0;
	public static final int RFS_STOP = 1;
	public static final int RFS_STATUS = 2;

	// Options for RFS command
	public static final int SIO_DOUT0 = 0;
	public static final int SIO_DOUT1 = 1;
	public static final int SIO_DOUT2 = 2;
	public static final int SIO_DOUT3 = 3;
	public static final int SIO_DOUT4 = 4;

	// Result codes for GetResult
	public static final int RESULT_OK = 0;
	public static final int RESULT_NOT_READY = 1;
	public static final int RESULT_CHECKSUM_ERROR = 2;

	private ComInstructionID() {
	}
}
