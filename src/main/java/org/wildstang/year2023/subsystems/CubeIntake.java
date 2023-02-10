package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
//import org.wildstang.hardware.roborio.outputs.WsPhoenix;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

/**
 * Sample Subsystem that controls a motor with a joystick.
 * @author Liam
 */
public class CubeIntake implements Subsystem {
    // inputs
    WsJoystickAxis joystick;
    WsJoystickButton deployIntake;

    // outputs
    WsSparkMax roller;
    WsDoubleSolenoid cylinder;

    // states
    double speed;
    boolean deploy;


    @Override
    public void init() {
        joystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_JOYSTICK_Y);
        joystick.addInputListener(this);
        deployIntake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        deployIntake.addInputListener(this);

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.INTAKE_MOTOR);
        cylinder = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.INTAKE_SOLENOID);

        resetState();
    }

    @Override
    public void resetState() {
        speed = 0;
        deploy = false;
    }

    @Override
    public void update() {
        roller.setValue(speed);
        if (deploy){
            cylinder.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        } else {
            cylinder.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == joystick) {
            speed = joystick.getValue();
        }
        if (source == deployIntake){
            deploy = deployIntake.getValue();
        }
    }

    @Override
    public String getName() {
        return "Sample";
    }

    @Override
    public void selfTest() {
    }
}