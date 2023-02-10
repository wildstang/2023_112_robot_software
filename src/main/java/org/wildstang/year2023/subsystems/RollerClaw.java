package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.inputs.WsJoystickToggleButton;
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
private boolean direction;
private double rollerSpeed;
private double deadband;
private boolean deploy;
private WsDoubleSolenoid gripper;

    @Override
    public void init() {

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.CLAW);
        rightTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_TRIGGER);
        rightTrigger.addInputListener(this);
        leftTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_SHOULDER);
        coneButton.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_SHOULDER);
        cubeButton.addInputListener(this);
        gripper = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.GRIPPER_SOLENOID);

    }

    @Override
    public void resetState(){
        direction = true;
        rollerSpeed = 0;
        deadband = 0.05;
        deploy = false;
    }

    @Override
    public void inputUpdate(Input source) {

        if (source == rightTrigger && (Math.abs(rightTrigger.getValue()) > deadband)){
            direction = true;
            rollerSpeed = Math.abs(rightTrigger.getValue());
        }

        else if (source == leftTrigger && (Math.abs(leftTrigger.getValue()) > deadband)){
            direction = false;
            rollerSpeed = Math.abs(leftTrigger.getValue());
        }

        else {
            direction = true;
            rollerSpeed = 0;
        }

        if (source == coneButton && coneButton.getValue()){
            deploy = false;
        } else if (source == cubeButton && cubeButton.getValue()){
            deploy = true;
        }


    }
    @Override
    public void update() {
        if (direction == true){
            roller.setValue(rollerSpeed);
        }
        if (direction == false){
            roller.setValue(-rollerSpeed);
        }
        if (deploy){
            gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        } else{
            gripper.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        }
    }

    @Override
    public String getName() {
        return "Roller Claw";
    }

    @Override
    public void selfTest() {
    }
}
