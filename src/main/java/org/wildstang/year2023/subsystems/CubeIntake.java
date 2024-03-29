package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class CubeIntake implements Subsystem {
    // inputs
    WsJoystickButton intake;
    WsJoystickButton outtake;

    // outputs
    WsSparkMax roller;
    WsSparkMax feed;
    WsDoubleSolenoid cylinder;

    // states
    double speed;
    boolean deploy;

    private static final double IN_SPEED = 0.7;
    private static final double OUT_SPEED = -0.5;


    @Override
    public void init() {
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.INTAKE_MOTOR);
        roller.setCurrentLimit(50, 50, 0);
        feed = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.FEED);
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
        feed.setValue(-speed);
        if (deploy){
            cylinder.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        } else {
            cylinder.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }
    }

    @Override
    public void inputUpdate(Input source) {
        if (source == intake){
            if (intake.getValue()){
                deploy = true;
                speed = IN_SPEED;
            } else {
                deploy = false;
                speed = 0;
            }
        } else if (source == outtake){
            if (outtake.getValue()){
                deploy = true;
                speed = OUT_SPEED;
            } else {
                deploy = false;
                speed = 0;
            }
        }
    }    
    
    // public void autoIntake(boolean open){
    //     if(open == true){
    //         speed = IN_SPEED;
    //         deploy = true;
    //     }
    //     else if(open == false){
    //         speed = 0;
    //         deploy = false;
    //     }
    // }

    @Override
    public String getName() {
        return "CubeIntake";
    }

    @Override
    public void selfTest() {
    }

    public void setAutoDeploy(boolean deploy){
        this.deploy = deploy;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }
}