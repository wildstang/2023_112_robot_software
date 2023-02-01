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

public class roller_claw implements Subsystem {
    
private WsSparkMax roller;
private WsAnalogInput rightTrigger;
private WsAnalogInput leftTrigger;
private boolean direction;
private double rollerSpeed;

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

    }

    @Override
    public void inputUpdate(Input source) {

        if (source == rightTrigger && (Math.abs(rightTrigger.getValue()) > 0.05)){
            direction = true;
            rollerSpeed = Math.abs(rightTrigger.getValue());
        }

        else if (source == leftTrigger && (Math.abs(leftTrigger.getValue()) > 0.05)){
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
        return "roller_claw";
    }

    @Override
    public void selfTest() {
    }
}
