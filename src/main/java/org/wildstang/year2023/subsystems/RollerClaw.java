package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsAnalogInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickToggleButton;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.hardware.roborio.outputs.config.WsSparkMaxFollowerConfig;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class RollerClaw implements Subsystem {
    
private WsSparkMax roller;
private WsAnalogInput rightTrigger;
private WsAnalogInput leftTrigger;
private boolean direction;
private double rollerSpeed;
private double deadband;

    @Override
    public void init() {

        roller = (WsSparkMax) WSOutputs.CLAW.get();
        rightTrigger = (WsAnalogInput) WSInputs.MANIPULATOR_RIGHT_TRIGGER.get();
        leftTrigger = (WsAnalogInput) WSInputs.MANIPULATOR_LEFT_TRIGGER.get();

    }

    @Override
    public void resetState(){

        direction = true;
        rollerSpeed = 0;
        deadband = 0.05;

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


    }
    @Override
    public void update() {
    
        if (direction == true){
            roller.setValue(rollerSpeed);
        }
        if (direction == false){
            roller.setValue(-rollerSpeed);
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
