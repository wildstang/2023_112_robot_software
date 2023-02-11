package org.wildstang.year2023.subsystems.arm;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDPadButton;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Sample Subsystem that controls a motor with a joystick.
 * @author Liam
 */
public class Arm implements Subsystem {
    // inputs
    private WsJoystickAxis joystick;
    private WsDPadButton highGoal;
    private WsDPadButton midGoal;
    private WsDPadButton lowGoal;
    private WsDPadButton stow;

    // outputs
    private WsSparkMax armMotor;

    // states
    double speed;
    double curPos;
    double goalPos, curErr, prevErr, setpoint;
    double p,i,d,f;
    double kP, kI, kD;
    double prevOut, curOut;


    @Override
    public void init() {

        joystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_JOYSTICK_Y);
        joystick.addInputListener(this);
        highGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_UP);
        highGoal.addInputListener(this);
        midGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_LEFT);
        midGoal.addInputListener(this);
        lowGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_DOWN);
        lowGoal.addInputListener(this);
        stow = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_RIGHT);
        stow.addInputListener(this);
        armMotor = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ARM);
        resetState();

    }

    @Override
    public void resetState() {
        speed = 0;
        curPos = 0;
        i = 0;
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        goalPos = curPos;
        SmartDashboard.putNumber("kP_DOWN", 0.2);
        SmartDashboard.putNumber("kD_DOWN", 0);

    }

    @Override
    public void update() {
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        //f = -Math.sin(curPos) * ArmConstants.ARM_TORQUE/ArmConstants.STALL_TORQUE; //7.5 lbs 20.25in/ (3.5 Nm * 117.67) * sin(curPos)
        curErr = goalPos-curPos;

        if ((curErr > 0 && curPos < 0) ||(curErr < 0 && curPos > 0)){
            kP = ArmConstants.kP_UP;
            kI = ArmConstants.kI_UP;
            kD = ArmConstants.kD_UP;
            SmartDashboard.putBoolean("arm state", false);
        }else{
            kP = ArmConstants.kP_DOWN;
            kI = ArmConstants.kI_DOWN;
            kD = ArmConstants.kD_DOWN;
            SmartDashboard.putBoolean("arm state", true);
        }
        p = curErr * kP;
        if (Math.abs(curErr) < .4){i += curErr*kI;}
        
        if (i>1){
            i = 1;
        } else if (i<-1){
            i = -1;
        }
        d = (curErr - prevErr) * kD;
        setpoint = p+i+d+f;
        if (curPos < ArmConstants.SOFT_STOP_LOW && setpoint < 0){
            setpoint = 0;
        } else if (curPos > ArmConstants.SOFT_STOP_HIGH && setpoint > 0){
            setpoint = 0;
        }
        if (Math.abs(setpoint - prevOut) > ArmConstants.RAMP_LIMIT){
            curOut = prevOut + Math.signum(setpoint - prevOut) * ArmConstants.RAMP_LIMIT;
        } else{
            curOut = setpoint;
        }
        
        armMotor.setSpeed(-curOut);
        prevErr = curErr;
        prevOut = curOut;
        SmartDashboard.putNumber("arm encoder", curPos);
        SmartDashboard.putNumber("arm error", curErr);
        SmartDashboard.putNumber("arm P value", p);
        SmartDashboard.putNumber("arm F value", f);
        SmartDashboard.putNumber("arm goal pos", goalPos);
        SmartDashboard.putNumber("arm setpoint", setpoint);
        SmartDashboard.putNumber("arm D value", d);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == joystick) {
            if (Math.abs(joystick.getValue()) > .05){
                speed = -joystick.getValue();
            } else{
                speed = 0;
            }
        }
        if (source == highGoal){
            goalPos = ArmConstants.HIGH_POS;
        } else if (source == midGoal){
            goalPos = ArmConstants.MID_POS;
        } else if (source == lowGoal){
            goalPos = ArmConstants.LOW_POS;
        } else if (source == stow){
            goalPos = ArmConstants.STOW_POS;
        }
    }

    @Override
    public String getName() {
        return "Sample";
    }

    @Override
    public void selfTest() {
    }
}