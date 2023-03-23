package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDPadButton;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

import com.revrobotics.SparkMaxLimitSwitch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GPStateMachine implements Subsystem {
    // inputs
    WsJoystickAxis leftTrigger;
    WsJoystickButton intake, outtake;
    WsJoystickButton cubeButton, coneButton;
    private WsDPadButton stow;
    WsDigitalInput feedBB; //feed beam break sensor
    SparkMaxLimitSwitch clawLimit; // claw limit switch

    // outputs
    WsSparkMax roller;
    WsSparkMax feed;
    WsDoubleSolenoid intakeCylinder;
    private WsSparkMax claw;
    private WsDoubleSolenoid gripper;
    
    // states
    int state;
    double pathSpeed, clawSpeed;
    int otbAmpLimit;
    boolean newLimit;
    boolean deployOTB, deployClaw;
    boolean in, out;
    boolean feedState;
    private int i,j;

    private static final double PATH_IN_SPEED = 0.8;
    private static final double PATH_OUT_SPEED = -0.8;

    private static final double CLAW_IN_SPEED = 1.0;
    private static final double CLAW_OUT_SPEED = -0.4;
    private static final double CLAW_HOLD_SPEED = 0.05;

    @Override
    public void inputUpdate(Input source) {
        switch (state) {
            case 0:
                if (source == intake && intake.getValue()){
                    state = 1;
                } else if (source == coneButton && coneButton.getValue()){
                    state = 7;
                    i = 25;
                } else if (source == cubeButton && cubeButton.getValue()){
                    state = 8;
                    i = 25;
                }
                break;
            case 1:
                if (source == feedBB && feedBB.getValue()){
                    state = 2;
                } else if (source == intake && !intake.getValue()){
                    state = 0;
                }
                break;
            case 3:
                if (source == cubeButton && cubeButton.getValue()) {
                    state = 4;
                } else if(source == leftTrigger && Math.abs(leftTrigger.getValue()) > 0) {
                    state = 5;
                }
                break;
            case 4:
                if (source == cubeButton && !cubeButton.getValue()) {
                    state = 0;
                }
                break;
            case 5:
                if(source == leftTrigger && Math.abs(leftTrigger.getValue()) == 0) {
                    state = 0;
                }
                break;
            case 6:
                if (source == outtake && !outtake.getValue()) {
                    state = 0;
                }
                break;
            case 7:
                if(source == coneButton && !coneButton.getValue()){
                    state = 0;
                }
                break;
            case 8:
                if (source == cubeButton && !cubeButton.getValue()){
                    state = 0;
                }
                break;
            }
        // Outtake overrides all other states
        if (source == outtake && outtake.getValue()) {
            state = 6;
        }

        if (source == stow && stow.getValue()){
            deployClaw = false;
            j = 25;
            state = 0;
            // reclamp = true;
        }

        SmartDashboard.putBoolean("feed beam break", feedBB.getValue());
        
    }

    private void initInputs(){
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_SHOULDER);
        cubeButton.addInputListener(this);
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_SHOULDER);
        coneButton.addInputListener(this);
        leftTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        stow = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_RIGHT);
        stow.addInputListener(this);
        
        feedBB = (WsDigitalInput) Core.getInputManager().getInput(WSInputs.FEED_SWITCH);
        feedBB.addInputListener(this);
        clawLimit = roller.getController().getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

    }

    @Override
    public void init() {
        initInputs();

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.INTAKE_MOTOR);
        feed = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.FEED);
        intakeCylinder = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.INTAKE_SOLENOID);
        claw = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.CLAW);
        gripper = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.GRIPPER_SOLENOID);

        resetState();
        
    }

    @Override
    public void selfTest() {
    }

    @Override
    public void update() {
        // need to update these state machine transitions here because spark max limit switches don't currently have an input class to add a listener
        switch (state) {
            case 0:
            case 1:
            case 2:
            case 7:
            case 8:
                if (clawLimit.isPressed()){
                    state = 3;
                }
                break;
        }

        switch (state) {
            case 0:  // rest state, don't care about claw open or closed
                pathSpeed = 0;
                clawSpeed = 0;
                deployOTB = false;
                break;
            case 1:  // intake state
                pathSpeed = PATH_IN_SPEED;
                clawSpeed = CLAW_IN_SPEED;
                deployOTB = true;
                deployClaw = true;
                break;
            case 2:  // retract OTB after beam break detects cube
                deployOTB = false;
                break;
            case 3:  // hold game piece state
                pathSpeed = 0;
                clawSpeed = CLAW_HOLD_SPEED;
                deployClaw = false;
                break;
            case 4:  // drop score mid state
                clawSpeed = 0;
                deployClaw = true;
                break;
            case 5:  // cube score high state
                clawSpeed = CLAW_OUT_SPEED;
                break;
            case 6:  // outtake state
                pathSpeed = PATH_OUT_SPEED;
                clawSpeed = CLAW_OUT_SPEED;
                deployOTB = true;
                deployClaw = false;
                break;
            case 7:  // cone intake state
                clawSpeed = CLAW_IN_SPEED;
                deployClaw = false;
                break;
            case 8:  // cube intake state
                clawSpeed = CLAW_IN_SPEED;
                deployClaw = true;
                break;
        }

        // if (roller.getTemperature() < 50) {
        //     if (otbAmpLimit != 80){
        //         otbAmpLimit = 80;
        //         newLimit = true;
        //     }
        // } else if (roller.getTemperature() < 75) {
        //     if (otbAmpLimit != 60){
        //         otbAmpLimit = 60;
        //         newLimit = true;
        //     }
        // } else if (roller.getTemperature() < 100) {
        //     if (otbAmpLimit != 50){
        //         otbAmpLimit = 50;
        //         newLimit = true;
        //     }
        // } else if (roller.getTemperature() < 120) {
        //     if (otbAmpLimit != 40){
        //         otbAmpLimit = 40;
        //         newLimit = true;
        //     }
        // } else {
        //     pathSpeed = 0;
        // }

        // if (newLimit) {
        //     roller.setCurrentLimit(otbAmpLimit, otbAmpLimit, 0);
        //     newLimit = false;
        // }

        roller.setValue(pathSpeed);
        feed.setValue(-pathSpeed);

        if (deployOTB){
            intakeCylinder.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        } else {
            intakeCylinder.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }

        if (i <= 0){ // delay to allow tap to open or close cylinder
            claw.setValue(clawSpeed);
        } else {
            claw.setValue(0);
            i --;
        }

        
        if (j <= 0){  // delay closing gripper when going to stow
            if (deployClaw){
                gripper.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
            } else{
                gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
            }
        } else {
            j --;
        }

        SmartDashboard.putBoolean("claw limit", clawLimit.isPressed());
        SmartDashboard.putNumber("ball path state", state);
    }

    @Override
    public void resetState() {
        state = 0;
        pathSpeed = 0;
        deployOTB = false;
        in = false;
        out = false;
        feedState = feedBB.getValue();
        newLimit = false;
        otbAmpLimit = 40;
        roller.setCurrentLimit(40, 40, 0);
        
    }

    @Override
    public String getName() {
        return "Game Piece State Machine";
    }
    
}
