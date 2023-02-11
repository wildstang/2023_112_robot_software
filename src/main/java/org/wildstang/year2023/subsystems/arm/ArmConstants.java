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
    public static final double HIGH_POS = .2;
    public static final double MID_POS = 1.1;
    public static final double LOW_POS = 2.8;
    public static final double STOW_POS = -OFFSET/RATIO*2*Math.PI;
    public static final double POS_DEADBAND = 0.1;
    public static final double SOFT_STOP_LOW = -OFFSET;
    public static final double SOFT_STOP_HIGH = 2.85;

}