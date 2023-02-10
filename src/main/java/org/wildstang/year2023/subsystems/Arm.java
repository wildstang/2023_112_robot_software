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
private static final double pValue = 0.1;
private static final double iValue = 0;
private static final double dValue = 0;
private static final double pos1 = 90;
private static final double pos2 = 180;
private static final double pos3 = 270;



    @Override
    public void init() {

        motor = (WsSparkMax) WSOutputs.ARM.get();
        motor.initClosedLoop(0.5, 0, 0, 0);
        motor.setCurrentLimit(40, 40, 0);
        motor.initClosedLoop(pValue, iValue, dValue, 0);
        joystick = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y.get();
        buttonA = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_UP.get();
        buttonB = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_LEFT.get();
        buttonC = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_RIGHT.get();

    }

    @Override
    public void resetState() {
        position = 0;
        motorSpeed = 0;
    }

    @Override
    public void inputUpdate(Input source) {

        if (source == joystick && (Math.abs(joystick.getValue()) > 0.10)) {

            motorSpeed = 1;

        }

        //presets
        else if (source == buttonA) { 
            position = 1;
        }
        else if (source == buttonB) { 
            position = 2;
        }
        else if (source == buttonC) { 
            position = 3;
        }


    }

    @Override
    public void update() {

            motor.setValue(motorSpeed);
        
        /* 
        if (position == 1){
            setPosition(pos1);
        }
        if (position == 2){
            setPosition(pos2);
        }
        if (position == 3){
            setPosition(pos3);
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
