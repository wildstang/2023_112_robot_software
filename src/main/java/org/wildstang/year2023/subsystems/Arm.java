package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

/**
 * Sample Subsystem that controls a motor with a joystick.
 * @author Liam
 */
public class Arm implements Subsystem {
    // inputs
    private WsJoystickAxis joystick;

    // outputs
    private WsSparkMax armMotor;

    // states
    double speed;


    @Override
    public void init() {

        joystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y);
        joystick.addInputListener(this);
        armMotor = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ARM);
        resetState();

    }

    @Override
    public void resetState() {
        speed = 0;
    }

    @Override
    public void update() {
        armMotor.setSpeed(speed);
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == joystick) {
            speed = joystick.getValue();
        }

        System.out.println(speed);
    }

    @Override
    public String getName() {
        return "Sample";
    }

    @Override
    public void selfTest() {
    }
}