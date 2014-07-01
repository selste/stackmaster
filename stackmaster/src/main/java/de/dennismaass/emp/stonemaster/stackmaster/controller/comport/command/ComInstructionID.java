package de.dennismaass.emp.stonemaster.stackmaster.controller.comport.command;

public class ComInstructionID {

	// Opcodes of all TMCL commands that can be used in direct mode
	public static int ROTATE_RIGHT = 1;
	public static int ROTATE_LEFT = 2;
	public static int MOTOR_STOP = 3;
	public static int MOVE_POSITION = 4;

	public static int STORE_AXIS_PARAMETER = 7;
	public static int RSAP = 8;
	public static int SGP = 9;
	public static int GGP = 10;
	public static int STGP = 11;
	public static int RSGP = 12;
	public static int RFS = 13;
	public static int SIO = 14;
	public static int GIO = 15;
	public static int SCO = 30;
	public static int GCO = 31;
	public static int CCO = 32;

	public static int ENABLE_INTERRUPTS = 25;
	public static int ENABLE_INTERRUPTS_POSITION_REACHED = 3;

	// Opcodes of TMCL control functions (to be used to run or abort a TMCL
	// program in the module)
	public static int APPLICATION_STOP = 128;
	public static int APPLICATION_RUN = 129;
	public static int APPLICATION_RESET = 131;

	// Options for SAP commandds
	public static int SET_AXIS_PARAMETER = 5;
	public static int SET_AXIS_PARAMETER_MAX_SPEED = 4;
	public static int SET_AXIS_PARAMETER_POSITION_REACHED = 8;
	public static int SET_AXIS_PARAMETER_MICROSTEP_RESOLUTION = 140;

	// Options for GAP commandds
	public static int GET_AXIS_PARAMETER = 6;
	public static int GET_AXIS_PARAMETER_ACTUAL_POSITION = 1;
	public static int GET_AXIS_PARAMETER_ACTUAL_SPEED = 3;

	// Options for MVP commandds
	public static int MOVE_POSITION_ABSOLUTE = 0;
	public static int MOVE_POSITION_RELATIVE = 1;
	public static int MOVE_POSITION_COORDINATE = 2;

	// Options for RFS command
	public static int RFS_START = 0;
	public static int RFS_STOP = 1;
	public static int RFS_STATUS = 2;

	// Options for RFS command
	public static int SIO_DOUT0 = 0;
	public static int SIO_DOUT1 = 1;
	public static int SIO_DOUT2 = 2;
	public static int SIO_DOUT3 = 3;
	public static int SIO_DOUT4 = 4;

	// Result codes for GetResult
	public static int RESULT_OK = 0;
	public static int RESULT_NOT_READY = 1;
	public static int RESULT_CHECKSUM_ERROR = 2;

	private ComInstructionID() {
	}
}
