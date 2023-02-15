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
    double speed;
    double curPos, goalPos, curErr, prevErr;
    double curVel, goalVel, curVelErr, prevVelErr;
    double setpoint;
    double p,i,d,f;
    double kP, kI, kD;
    double prevOut, curOut;

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
        // curPos = 0;
        i = 0;
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        goalPos = curPos;
        ctrlMode = mode.CLOSED_LOOP;
        SmartDashboard.putNumber("kP_DOWN", 0.2);
        SmartDashboard.putNumber("kD_DOWN", 0);

    }

    @Override
    public void update() {
        curPos = -(armMotor.getPosition()+ArmConstants.OFFSET)/ArmConstants.RATIO*2*Math.PI;
        curVel = armMotor.getVelocity()/ArmConstants.RATIO*2*Math.PI/60;
        goalVel = getVelocityTarget(curVel, curErr);
        // curVelErr = goalVel - curVel;
        // curOut = goalVel * ArmConstants.kV + curVelErr * ArmConstants.VEL_P;
        //f = -Math.sin(curPos) * ArmConstants.ARM_TORQUE/ArmConstants.STALL_TORQUE; //7.5 lbs 20.25in/ (3.5 Nm * 117.67) * sin(curPos)
        curErr = goalPos-curPos;

        if ((curErr > 0 && curPos < 0) || (curErr < 0 && curPos > 0)){
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
        curOut = p+i+d+f;
        if (Math.abs(curOut - prevOut) > ArmConstants.RAMP_LIMIT){
            curOut = prevOut + Math.signum(curOut - prevOut) * ArmConstants.RAMP_LIMIT;
        }
        if (ctrlMode == mode.OPEN_LOOP) {
            curOut = -speed;
        }
        if (curPos <= ArmConstants.SOFT_STOP_LOW && curOut < 0){
            curOut = 0;
        } else if (curPos > ArmConstants.SOFT_STOP_HIGH && curOut > 0){
            curOut = 0;
        }

        // set output to zero if we are near the goal and not moving. This ensures that the motor
        // controller actually enters brake mode
        // TODO: also need to either zero out or pause integral term
        // if(Math.abs(curErr)<ArmConstants.POS_DB && Math.abs(curVel) < ArmConstants.VEL_DB){
        //     curOut = 0;
        // }
        armMotor.setSpeed(-curOut);
        
        prevErr = curErr;
        prevOut = curOut;
        SmartDashboard.putNumber("arm pos", curPos);
        SmartDashboard.putNumber("arm pos error", curErr);
        SmartDashboard.putNumber("arm goal pos", goalPos);
        SmartDashboard.putNumber("arm output", curOut);
        SmartDashboard.putNumber("arm velocity", curVel);
        SmartDashboard.putNumber("arm goal vel", goalVel);
        SmartDashboard.putNumber("arm vel error", curVelErr);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == highGoal){
            goalPos = ArmConstants.HIGH_POS;
        } else if (source == midGoal){
            goalPos = ArmConstants.MID_POS;
        } else if (source == lowGoal){
            goalPos = ArmConstants.LOW_POS;
        } else if (source == stow){
            goalPos = ArmConstants.STOW_POS;
        } else if (source == righting){
            goalPos = ArmConstants.CONE_RIGHTING_POS;
        } else if (source == substation){
            goalPos = ArmConstants.SUBSTATION_POS;
        }

        if (source == modeSwitch && modeSwitch.getValue()){
            if (ctrlMode == mode.CLOSED_LOOP){
                ctrlMode = mode.OPEN_LOOP;
            } else{
                ctrlMode = mode.CLOSED_LOOP;
                goalPos = curPos;
            }
        }

        if(source == joystick){
            if (ctrlMode == mode.CLOSED_LOOP){
                goalPos -= joystick.getValue() * .01;
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

    public double getVelocityTarget(double curVel, double curErr){
        if (curErr > 0){
            if (curErr <= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return curVel - (ArmConstants.MAX_ACC*.02);
            } else if (curVel < ArmConstants.MAX_VEL){
                return Math.min(curVel + ArmConstants.MAX_ACC * .02, ArmConstants.MAX_VEL);
            } else{
                return ArmConstants.MAX_VEL;
            }
        } else {
            if (curErr >= curVel * Math.abs(curVel/ArmConstants.MAX_ACC) * .5){
                return curVel + (ArmConstants.MAX_ACC*.02);
            } else if (curVel > -ArmConstants.MAX_VEL){
                return Math.max(curVel - ArmConstants.MAX_ACC * .02, -ArmConstants.MAX_VEL);
            } else{
                return -ArmConstants.MAX_VEL;
            }
        }
    }
}