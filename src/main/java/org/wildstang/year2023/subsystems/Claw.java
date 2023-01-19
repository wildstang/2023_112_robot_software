package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class Claw implements Subsystem {

    WsJoystickAxis forwardButton;
    WsJoystickAxis reverseButton;
    WsSparkMax motor;
    double motorSpeed;

    @Override
    public void init() {
        forwardButton = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_TRIGGER.get();
        forwardButton.addInputListener(this);
        reverseButton = (WsJoystickAxis) WSInputs.MANIPULATOR_RIGHT_TRIGGER.get();
        reverseButton.addInputListener(this);
        motor = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        motorSpeed = 0;
    }

    @Override
    public void resetState() {
        motorSpeed = 0;
    }

    @Override
    public void update() {
        if (motorSpeed == 0)
        {
            motor.setBrake();
        }
        else
        {
            motor.setCoast();
        }
        motor.setValue(motorSpeed);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == forwardButton) {
            if (Math.abs(forwardButton.getValue()) > 0.3)
            {
                motorSpeed = -0.8;
            }
            else
            {
                motorSpeed = 0;
            }
        }
        else if (source == reverseButton) {
            if (Math.abs(reverseButton.getValue()) > 0.3)
            {
                motorSpeed = 0.8;
            }
            else
            {
                motorSpeed = 0;
            }
        }
    }

    @Override
    public void selfTest() {
    }

    @Override
    public String getName() {
        return "Claw";
    }
}
