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
    private WsJoystickButton righting;
    private WsJoystickButton substation;
    private WsJoystickButton modeSwitch;

    // outputs
    private WsSparkMax armMotor;

    // states
    double speed,adj;
    double curPos, curPosErr, prevPosErr;
    double curVel, goalVel, curVelErr, prevVelErr;
    double setpoint;
    double prop,integral,der,ff;
    double kP, kI, kD;
    double prevOut, curOut;

    double acc, torque;

    public enum HEIGHT {LOW, MID, HIGH, STOW};

    private enum MODE {CLOSED_LOOP, OPEN_LOOP};
    private MODE ctrlMode;


    @Override
    public void init() {
        initInputs();
        armMotor = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ARM);
        resetState();

    }

    public void initInputs(){

        joystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y);
        joystick.addInputListener(this);
        highGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_UP);
        highGoal.addInputListener(this);
        midGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_LEFT);
        midGoal.addInputListener(this);
        lowGoal = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_DOWN);
        lowGoal.addInputListener(this);
        stow = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_RIGHT);
        stow.addInputListener(this);
        righting = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_DOWN);
        righting.addInputListener(this);
        substation = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_LEFT);
        substation.addInputListener(this);
        modeSwitch = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_JOYSTICK_BUTTON);
        modeSwitch.addInputListener(this);
    }

    @Override
    public void resetState() {
        speed = 0;
        integral = 0;
        curPos =  -armMotor.getPosition() / ArmConstants.RATIO * 2 * Math.PI - ArmConstants.OFFSET;
        curVel = armMotor.getVelocity()/ArmConstants.RATIO*2*Math.PI/60;
        setpoint = curPos;
        goalVel = 0;
        adj = 0;
        ctrlMode = MODE.CLOSED_LOOP;

    }

    @Override
    public void update() {
        curPos = -armMotor.getPosition() / ArmConstants.RATIO * 2 * Math.PI - ArmConstants.OFFSET;
        curVel = -armMotor.getVelocity()/ArmConstants.RATIO*2*Math.PI/60;
        if(ctrlMode == MODE.CLOSED_LOOP){
            setpoint += adj;
            setpoint = Math.min(setpoint,ArmConstants.SOFT_STOP_HIGH);  // don't command a position higher than the soft stop
            setpoint = Math.max(setpoint,ArmConstants.SOFT_STOP_LOW);  // don't command a position lower than the soft stop
            curPosErr = setpoint-curPos;

            // TODO: test below code in place of current code
            // acc = getAccTarget(curVel, curPosErr);
            // torque = ArmConstants.I * acc - Math.sin(curPos) * ArmConstants.ARM_TORQUE;
            // ff = torque/ArmConstants.STALL_TORQUE - curVel/ArmConstants.MAX_VEL;
            // curOut = ff; //+ curPosErr * ArmConstants.kP_UP + curVelErr * ArmConstants.VEL_P;

            goalVel = getVelocityTarget(goalVel, curPosErr);
            curVelErr = goalVel - curVel;
            ff = -Math.sin(curPos) * ArmConstants.ARM_TORQUE/ArmConstants.STALL_TORQUE; //7.5 lbs 20.25in/ (3.5 Nm * 117.67) * sin(curPos)
            curOut = ff + goalVel * ArmConstants.kV + curVelErr * ArmConstants.VEL_P;

            // TODO: also need to either zero out or pause integral term
            if(isAtTarget()){
                curOut = ff;
            }
        } else if (ctrlMode == MODE.OPEN_LOOP) {
            curOut = -speed;
        }
        
        // Impose soft stop restrictions to avoid driving arm into hardstops
        if (curPos <= ArmConstants.SOFT_STOP_LOW){
            curOut = Math.max(0, curOut);
        } else if (curPos > ArmConstants.SOFT_STOP_HIGH){
            curOut = Math.min(0, curOut);
        }
        armMotor.setSpeed(-curOut);
        
        prevPosErr = curPosErr;
        prevOut = curOut;
        SmartDashboard.putBoolean("arm at target", isAtTarget());
        SmartDashboard.putNumber("arm pos", curPos);
        SmartDashboard.putNumber("arm pos error", curPosErr);
        SmartDashboard.putNumber("arm setpoint", setpoint);
        SmartDashboard.putNumber("arm output", curOut);
        SmartDashboard.putNumber("arm velocity", curVel);
        SmartDashboard.putNumber("arm goal vel", goalVel);
        SmartDashboard.putNumber("arm vel error", curVelErr);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == highGoal){
            setpoint = ArmConstants.HIGH_POS;
        } else if (source == midGoal){
            setpoint = ArmConstants.MID_POS;
        } else if (source == lowGoal){
            setpoint = ArmConstants.LOW_POS;
        } else if (source == stow){
            setpoint = ArmConstants.STOW_POS;
        } else if (source == righting){
            setpoint = ArmConstants.CONE_RIGHTING_POS;
        } else if (source == substation){
            setpoint = ArmConstants.SUBSTATION_POS;
        }

        if (source == modeSwitch && modeSwitch.getValue()){
            if (ctrlMode == MODE.CLOSED_LOOP){
                ctrlMode = MODE.OPEN_LOOP;
            } else{
                ctrlMode = MODE.CLOSED_LOOP;
                setpoint = curPos;
            }
        }

        if(source == joystick){
            if (ctrlMode == MODE.CLOSED_LOOP){
                if (Math.abs(joystick.getValue()) > 0.1){
                    adj = -joystick.getValue() * .01;
                } else {
                    adj = 0;
                }
            } else {
                if (Math.abs(joystick.getValue()) > .05){
                    speed = joystick.getValue();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Arm";
    }

    @Override
    public void selfTest() {
    }

    public double getVelocityTarget(double curVel, double curPosErr){
        if (curPosErr > 0){
            if (curPosErr <= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return curVel - (ArmConstants.MAX_ACC*.02);
            } else if (curVel < ArmConstants.MAX_VEL){
                return Math.min(curVel + ArmConstants.MAX_ACC * .02, ArmConstants.MAX_VEL);
            } else{
                return ArmConstants.MAX_VEL;
            }
        } else {
            if (curPosErr >= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return curVel + (ArmConstants.MAX_ACC*.02);
            } else if (curVel > -ArmConstants.MAX_VEL){
                return Math.max(curVel - ArmConstants.MAX_ACC * .02, -ArmConstants.MAX_VEL);
            } else{
                return -ArmConstants.MAX_VEL;
            }
        }
    }

    public double getAccTarget(double curVel, double curPosErr){
        if (curPosErr > 0){
            if (curPosErr <= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return - (ArmConstants.MAX_ACC);
            } else if (curVel < ArmConstants.MAX_VEL){
                if (curVel + ArmConstants.MAX_ACC * .02 < ArmConstants.MAX_VEL){
                    return ArmConstants.MAX_ACC;
                } else {
                    return (ArmConstants.MAX_VEL - curVel)/.02;
                }
            } else{
                return 0;
            }
        } else {
            if (curPosErr >= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return (ArmConstants.MAX_ACC);
            } else if (curVel > -ArmConstants.MAX_VEL){
                if (curVel - ArmConstants.MAX_ACC * .02 > -ArmConstants.MAX_VEL){
                    return -ArmConstants.MAX_ACC;
                } else {
                    return (-ArmConstants.MAX_VEL + curVel)/.02;
                }
            } else{
                return 0;
            }
        }
    }

    public double getPositionTarget(double curPos, double prevVel, double nextVel){
        return curPos + (prevVel + nextVel) / 2 * .02;  // current position plus average velocity for next timestep times timestep
    }

    public void setPosTarget(double target){
        this.setpoint = target;
    }

    public void autoPosition(HEIGHT height) {

        if (height == HEIGHT.HIGH) {
            setpoint = ArmConstants.HIGH_POS;
        }

        else if (height == HEIGHT.MID) {
            setpoint = ArmConstants.MID_POS;
        }

        else if (height == HEIGHT.LOW) {
            setpoint = ArmConstants.LOW_POS;
        }

        else if (height == HEIGHT.STOW) {
            setpoint = ArmConstants.STOW_POS;
        }

    }

    public boolean isAtTarget() {
        curPosErr = setpoint-curPos;
        return Math.abs(curPosErr) < ArmConstants.POS_DB && Math.abs(curVel) < ArmConstants.VEL_DB;
    }
}