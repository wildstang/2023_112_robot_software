package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class claw implements Subsystem {

AnalogInput joystick;
WsSparkMax motor;
double motorSpeed;

@Override
 public void init() {

    joystick = (AnalogInput) WSInputs.DRIVER_LEFT_JOYSTICK_Y.get();
    motor = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
    motorSpeed = 0;

 }

 @Override
    public void resetState() {
        motorSpeed = 0;
    }

@Override
public void update() {

    motor.setValue(motorSpeed);
        
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