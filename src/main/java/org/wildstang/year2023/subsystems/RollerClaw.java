package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDPadButton;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class RollerClaw implements Subsystem {
    
private WsSparkMax roller;
private WsJoystickAxis rightTrigger;
private WsJoystickAxis leftTrigger;
private WsJoystickButton coneButton;
private WsJoystickButton cubeButton;
private WsJoystickButton intake;
private WsJoystickButton outtake;
private WsDPadButton stow;
private double rollerSpeed;
private boolean deploy;
private WsDoubleSolenoid gripper;

private int i, j;

private static final double IN_SPEED = 1.0;
private static final double OUT_SPEED = -0.4;
private static final double HOLD_SPEED = 0.1;

    @Override
    public void init() {

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.CLAW);
        gripper = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.GRIPPER_SOLENOID);
        initInputs();

    }

    private void initInputs(){
        
        rightTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_TRIGGER);
        rightTrigger.addInputListener(this);
        leftTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_SHOULDER);
        coneButton.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_SHOULDER);
        cubeButton.addInputListener(this);
        stow = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_RIGHT);
        stow.addInputListener(this);
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);
    }

    @Override
    public void resetState(){
        rollerSpeed = 0;
        deploy = false;
    }

    @Override
    public void inputUpdate(Input source) {

        // if (forwardButton.getValue()){
        //     rollerSpeed = speedConstant;
        // }

        // else if (backwardButton.getValue()){
        //     rollerSpeed = -speedConstant;
        // }

        // else {
        //     rollerSpeed = 0;
        // }

        if (source == rightTrigger){
            rollerSpeed = Math.abs(rightTrigger.getValue());
        } else if(source == leftTrigger){
            rollerSpeed = -Math.abs(leftTrigger.getValue());
        } else{
            rollerSpeed = HOLD_SPEED;
        }

        if (source == coneButton && coneButton.getValue()){
            deploy = false;
            j = 25;
            rollerSpeed = IN_SPEED;
            i = 0;
        } else if (source == cubeButton && cubeButton.getValue()){
            deploy = true;
            j = 25;
            rollerSpeed = OUT_SPEED;
            i = 0;
        } else if (source == intake) {
            if (intake.getValue()){
                deploy = true;
                j = 25;
                rollerSpeed = 0.67;
            } else{
                deploy = false;
                j = 25;
            }
        } else if (source == outtake) {
            if (outtake.getValue()){
                deploy = true;
                j = 25;
                rollerSpeed = -0.67;
            } else{
                deploy = false;
                j = 25;
            }
        } else if (source == stow && stow.getValue()){
            deploy = false;
            j = 0;
        }

    }
    @Override
    public void update() {

        if (i > 25){ // delay to allow tap to open or close cylinder
            roller.setValue(rollerSpeed);
        } else {
            roller.setValue(0);
            i ++;
        }
        if (j > 25){
            if (deploy){
                gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
            } else{
                gripper.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
            }
        } else {
            j ++;
        }
    }

    @Override
    public String getName() {
        return "Roller Claw";
    }

    @Override
    public void selfTest() {
    }

    public void AutoScore(String mode) {

        if (mode == "Cube High"){
            roller.setValue(OUT_SPEED);
        }

        if (mode == "Cube Mid"){
            gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
            roller.setValue(OUT_SPEED);
        }
        if (mode == "Cone Mid"){
            gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }
        if (mode == "Hybrid"){
            gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }

    }

    public void setGripper(boolean deploy){
        this.deploy = deploy;
    }

    public void setSpeed(double speed){
        this.rollerSpeed = speed;
    }
}
