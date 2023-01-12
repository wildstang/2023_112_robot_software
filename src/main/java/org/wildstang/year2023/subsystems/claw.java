package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsPhoenix;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class Claw implements Subsystem {

AnalogInput joystick;
WsSparkMax motor;
double motorSpeed;

@Override
 public void init() {

    joystick = (AnalogInput) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y);
    joystick.addInputListener(this);
    motor = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
    motorSpeed = 0;

 }

 @Override
    public void resetState() {
        motorSpeed = 0;
    }

@Override
public void update() {

    motor.speed(motorSpeed);
        
    }

@Override
 public void inputUpdate(Input source) {

    if (source == joystick) {
        motorSpeed = joystick.getValue();
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