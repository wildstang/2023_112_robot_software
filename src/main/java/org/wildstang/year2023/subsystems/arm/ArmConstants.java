package org.wildstang.year2023.subsystems.arm;

public final class ArmConstants{
    public static final double kP_UP = 0.6;
    public static final double kI_UP = 0.002;//.003
    public static final double kD_UP = 0.18; //0.2

    public static final double kP_DOWN = 0.5;
    public static final double kI_DOWN = 0.003;//.003
    public static final double kD_DOWN = 0.05; //0.2

    public static final double RAMP_LIMIT = .04;
    
    public static final double MASS = 7.5 * 4.448;  // Newtons
    public static final double COM = 20.25 * .0254; // arm center of mass, meters
    public static final double ARM_TORQUE = MASS * COM;  // Nm
    public static final double RATIO = 180;  // arm reduction
    public static final double STALL_TORQUE = 3.5 * RATIO;  // total driving stall torque, Nm
    public static final double OFFSET = 66;  //arm offset
    public static final double HIGH_POS = .9;
    public static final double MID_POS = 1.1;
    public static final double LOW_POS = 2.8;
    public static final double CONE_RIGHTING_POS = 2.45;
    public static final double SUBSTATION_POS = .95;
    public static final double STOW_POS = -OFFSET/RATIO*2*Math.PI;
    public static final double POS_DEADBAND = 0.1;
    public static final double SOFT_STOP_LOW = -OFFSET;
    public static final double SOFT_STOP_HIGH = 2.85;

    public static final double MAX_VEL = 5676 / RATIO *2*Math.PI/60.0 * (1-(ARM_TORQUE/STALL_TORQUE));  // max arm speed, 3.3 rad/s
    public static final double MAX_ACC = (STALL_TORQUE-ARM_TORQUE) / (MASS * Math.pow(COM, 2));  // maximum acceleration, 69.4 rad/s/s
    public static final double MAX_DEC = -MAX_ACC;  // maximum deceleration
    public static final double kV = (5676 / RATIO *2*Math.PI/60.0) / 12;  // motor kV adjusted for units and gear ratio, (rad/s)/volt
    public static final double VEL_P = 0.0;
}