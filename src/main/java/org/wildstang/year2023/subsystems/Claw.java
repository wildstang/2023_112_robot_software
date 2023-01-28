package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
//import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
//import org.wildstang.hardware.roborio.outputs.WsPhoenix;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;
import com.revrobotics.AnalogInput;


public class Claw implements Subsystem {

    AnalogInput leftBumper;
    AnalogInput rightBumper;
    WsSparkMax motor;
    double speed;

    @Override
    public void init() {
        
        leftBumper = (AnalogInput) WSInputs.DRIVER_LEFT_SHOULDER.get();
        rightBumper = (AnalogInput) WSInputs.DRIVER_RIGHT_SHOULDER.get();
        motor = (WsSparkMax) WSOutputs.CLAW.get();
        speed = 0;

    }

    @Override
    public void resetState() {
        speed = 0;
    }

    @Override
    public void update() {
        motor.setValue(speed);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == leftBumper) {
            speed = leftBumper.getPosition();
        } else if (source == rightBumper) {
            speed = (rightBumper.getPosition() * -1);
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