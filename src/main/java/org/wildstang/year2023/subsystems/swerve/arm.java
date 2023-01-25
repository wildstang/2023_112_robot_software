package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class arm implements Subsystem  {

WsSparkMax motor;
WsJoystickAxis joystick;
WsDigitalInput buttonA;
WsDigitalInput buttonB;
WsDigitalInput buttonC;
double motorSpeed;
int position;


    @Override
    public void init() {

        motor = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        joystick = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y.get();
        buttonA = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_UP.get();
        buttonB = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_LEFT.get();
        buttonC = (WsDigitalInput) WSInputs.MANIPULATOR_DPAD_RIGHT.get();

    }

    @Override
    public void resetState() {
        position = 0;
    }

    @Override
    public void inputUpdate(Input source) {

        if (source == joystick) {

            motorSpeed = joystick.getValue();

        }

        //presets
        if (source == buttonA) { 
            position = 1;
        }
        if (source == buttonB) { 
            position = 2;
        }
        if (source == buttonC) { 
            position = 3;
        }


    }

    @Override
    public void update() {

        motor.setValue(motorSpeed);

        if (position == 1){
            motor.setPosition(000);
        }
        if (position == 2){
            motor.setPosition(000);
        }
        if (position == 3){
            motor.setPosition(000);
        }

    }

    @Override
    public String getName() {
        return "arm";
    }

    @Override
    public void selfTest() {
    }
}
