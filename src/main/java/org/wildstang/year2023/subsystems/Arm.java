package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.hardware.roborio.outputs.config.WsSparkMaxFollowerConfig;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class Arm implements Subsystem  {

private WsSparkMax motor;
private WsJoystickAxis joystick;
private WsDigitalInput buttonA;
private WsDigitalInput buttonB;
private WsDigitalInput buttonC;
private double motorSpeed;
private int position;
private boolean preset;


    @Override
    public void init() {

        motor = (WsSparkMax) WSOutputs.ARM.get();
        motor.initClosedLoop(0.5, 0, 0, 0);
        motor.setCurrentLimit(40, 40, 0);
        joystick = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y.get();
        buttonA = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_UP.get();
        buttonB = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_LEFT.get();
        buttonC = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_RIGHT.get();

    }

    @Override
    public void resetState() {
        position = 0;
        motorSpeed = 0;
        preset = false;
    }

    @Override
    public void inputUpdate(Input source) {

        if (source == joystick && (Math.abs(joystick.getValue()) > 0.10)) {

            motorSpeed = joystick.getValue();
            preset = false;

        }

        //presets
        if (source == buttonA) { 
            position = 1;
            preset = true;
        }
        if (source == buttonB) { 
            position = 2;
            preset = true;
        }
        if (source == buttonC) { 
            position = 3;
            preset = true;
        }


    }

    @Override
    public void update() {

        if(preset == false){
            motor.setValue(motorSpeed);
        }
        
        /* 
        if (position == 1 && preset == true){
            motor.setPosition(000);
        }
        if (position == 2 && preset == true){
            motor.setPosition(000);
        }
        if (position == 3 && preset == true){
            motor.setPosition(000);
        }
        */

    }

    @Override
    public String getName() {
        return "Arm";
    }

    @Override
    public void selfTest() {
    }
}
