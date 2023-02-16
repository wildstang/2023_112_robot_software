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
    WsJoystickButton intake;
    WsJoystickButton outtake;

    // outputs
    WsSparkMax roller;
    WsDoubleSolenoid cylinder;

    // states
    double speed;
    boolean deploy;

    int i;

    private static final double IN_SPEED = 0.5;
    private static final double OUT_SPEED = -0.5;


    @Override
    public void init() {
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);

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
        if (i >= 25) {
            roller.setValue(speed);
        } else {
            roller.setValue(0);
        }
        if (deploy){
            cylinder.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        } else {
            cylinder.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }
        i ++;
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == intake){
            if (intake.getValue()){
                i = 0;
                deploy = true;
                speed = IN_SPEED;
            } else {
                i = 25;
                deploy = false;
                speed = 0;
            }
        } else if (source == outtake){
            if (outtake.getValue()){
                i = 0;
                deploy = true;
                speed = OUT_SPEED;
            } else {
                i = 25;
                deploy = false;
                speed = 0;
            }
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