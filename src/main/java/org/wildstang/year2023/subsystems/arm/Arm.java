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
    double curPos, goalPos, curPosErr, prevPosErr;
    double curVel, goalVel, curVelErr, prevVelErr;
    double setpoint;
    double prop,integral,der,ff;
    double kP, kI, kD;
    double prevOut, curOut;

    double acc, torque;

    private enum mode {CLOSED_LOOP, OPEN_LOOP};
    private mode ctrlMode;


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
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        curVel = armMotor.getVelocity()/ArmConstants.RATIO*2*Math.PI/60;
        goalPos = curPos;
        setpoint = curPos;
        goalVel = 0;
        adj = 0;
        ctrlMode = mode.CLOSED_LOOP;
        SmartDashboard.putNumber("kP_DOWN", 0.2);
        SmartDashboard.putNumber("kD_DOWN", 0);

    }

    @Override
    public void update() {
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        curVel = -armMotor.getVelocity()/ArmConstants.RATIO*2*Math.PI/60;
        if(ctrlMode == mode.CLOSED_LOOP){
            setpoint += adj;
            setpoint = Math.min(setpoint,ArmConstants.SOFT_STOP_HIGH);
            setpoint = Math.max(setpoint,ArmConstants.SOFT_STOP_LOW);
            goalPos = setpoint; //goalPos = getPositionTarget(curPos, curVel, goalVel);
            curPosErr = goalPos-curPos;
            acc = getAccTarget(curVel, curPosErr);
            torque = ArmConstants.I * acc - Math.sin(curPos) * ArmConstants.ARM_TORQUE;
            ff = torque/ArmConstants.STALL_TORQUE + curVel/ArmConstants.MAX_VEL;
            curOut = ff; //+ curPosErr * ArmConstants.kP_UP + curVelErr * ArmConstants.VEL_P;
            goalVel = getVelocityTarget(goalVel, curPosErr);
            curVelErr = goalVel - curVel;
            ff = -Math.sin(curPos) * ArmConstants.ARM_TORQUE/ArmConstants.STALL_TORQUE; //7.5 lbs 20.25in/ (3.5 Nm * 117.67) * sin(curPos)
            curOut = ff + goalVel * ArmConstants.kV + curVelErr * ArmConstants.VEL_P;

            // if ((curPosErr > 0 && curPos < 0) || (curPosErr < 0 && curPos > 0)){
            //     kP = ArmConstants.kP_UP;
            //     kI = ArmConstants.kI_UP;
            //     kD = ArmConstants.kD_UP;
            //     SmartDashboard.putBoolean("arm state", false);
            // }else{
            //     kP = ArmConstants.kP_DOWN;
            //     kI = ArmConstants.kI_DOWN;
            //     kD = ArmConstants.kD_DOWN;
            //     SmartDashboard.putBoolean("arm state", true);
            // }
            // prop = curPosErr * kP;
            // if (Math.abs(curPosErr) < .8){integral += curPosErr*kI;}
            
            // if (integral>1){
            //     integral = 1;
            // } else if (integral<-1){
            //     integral = -1;
            // }
            // der = (curPosErr - prevPosErr) * kD;
            // curOut = prop+integral+der+ff;
            // if (Math.abs(curOut - prevOut) > ArmConstants.RAMP_LIMIT){
            //     curOut = prevOut + Math.signum(curOut - prevOut) * ArmConstants.RAMP_LIMIT;
            // }

            // set output to zero if we are near the goal and not moving. This ensures that the motor
            // controller actually enters brake mode
            // TODO: also need to either zero out or pause integral term
            if(Math.abs(curPosErr)<ArmConstants.POS_DB && Math.abs(curVel) < ArmConstants.VEL_DB){
                curOut = ff;
            }
        } else if (ctrlMode == mode.OPEN_LOOP) {
            curOut = -speed;
        }
        
        // Impose soft stop restrictions to avoid overdriving arm
        if (curPos <= ArmConstants.SOFT_STOP_LOW){
            curOut = Math.max(0, curOut);
        } else if (curPos > ArmConstants.SOFT_STOP_HIGH){
            curOut = Math.min(0, curOut);
        }
        armMotor.setSpeed(-curOut);
        
        prevPosErr = curPosErr;
        prevOut = curOut;
        SmartDashboard.putNumber("arm pos", curPos);
        SmartDashboard.putNumber("arm pos error", curPosErr);
        SmartDashboard.putNumber("arm goal pos", goalPos);
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
            if (ctrlMode == mode.CLOSED_LOOP){
                ctrlMode = mode.OPEN_LOOP;
            } else{
                ctrlMode = mode.CLOSED_LOOP;
                setpoint = curPos;
            }
        }

        if(source == joystick){
            if (ctrlMode == mode.CLOSED_LOOP){
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
        return "Sample";
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

    public void AutoPosition(String height) {

        if (height == "High") {
            armMotor.setPosition(ArmConstants.HIGH_POS);
        }

        if (height == "Mid") {
            armMotor.setPosition(ArmConstants.MID_POS);
        }

        if (height == "Low") {
            armMotor.setPosition(ArmConstants.LOW_POS);
        }

    }

    public boolean isAtTarget() {
        return curPosErr < ArmConstants.POS_DB && curVel < ArmConstants.VEL_DB;
    }
}