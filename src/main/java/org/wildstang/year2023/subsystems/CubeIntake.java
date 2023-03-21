package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

import com.revrobotics.SparkMaxLimitSwitch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeIntake implements Subsystem {
    // inputs
    WsJoystickAxis leftTrigger;
    WsJoystickButton intake, outtake;
    WsJoystickButton cubeButton, coneButton;
    WsDigitalInput feedBB; //feed beam break sensor
    SparkMaxLimitSwitch clawLimit; // claw limit switch

    // outputs
    WsSparkMax roller;
    WsSparkMax feed;
    WsDoubleSolenoid cylinder;

    // states
    int state;
    double pathSpeed;
    int otbAmpLimit;
    boolean newLimit;
    boolean deploy;
    boolean in, out;
    boolean feedState;

    private static final double PATH_IN_SPEED = 0.8;
    private static final double PATH_OUT_SPEED = -0.8;


    @Override
    public void init() {
        initInputs();

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.INTAKE_MOTOR);
        feed = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.FEED);
        cylinder = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.INTAKE_SOLENOID);

        resetState();
    }

    private void initInputs(){
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_SHOULDER);
        cubeButton.addInputListener(this);
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_SHOULDER);
        coneButton.addInputListener(this);
        leftTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        
        feedBB = (WsDigitalInput) Core.getInputManager().getInput(WSInputs.FEED_SWITCH);
        feedBB.addInputListener(this);
        clawLimit = roller.getController().getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

    }

    @Override
    public void resetState() {
        state = 0;
        pathSpeed = 0;
        deploy = false;
        in = false;
        out = false;
        feedState = feedBB.getValue();
        newLimit = false;
        otbAmpLimit = 40;
        roller.setCurrentLimit(40, 40, 0);
    }

    @Override
    public void update() {
        // need to update these state machine transitions here because spark max limit switches don't currently have an input class to add a listener
        switch (state) {
            case 2:
            case 7:
            case 8:
                if (clawLimit.isPressed()){
                    state = 3;
                }
                break;
        }
        switch (state) {
            case 0:
                pathSpeed = 0;
                break;
            case 1:
                pathSpeed = PATH_IN_SPEED;
                break;
            case 2:
                break;
            case 3:
                pathSpeed = 0;
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                pathSpeed = PATH_OUT_SPEED;
                break;
            case 7:
                break;
            case 8:
                break;
        }

        if (roller.getTemperature() < 50) {
            if (otbAmpLimit != 80){
                otbAmpLimit = 80;
                newLimit = true;
            }
        } else if (roller.getTemperature() < 75) {
            if (otbAmpLimit != 60){
                otbAmpLimit = 60;
                newLimit = true;
            }
        } else if (roller.getTemperature() < 100) {
            if (otbAmpLimit != 50){
                otbAmpLimit = 50;
                newLimit = true;
            }
        } else if (roller.getTemperature() < 120) {
            if (otbAmpLimit != 40){
                otbAmpLimit = 40;
                newLimit = true;
            }
        } else {
            pathSpeed = 0;
        }

        if (newLimit) {
            roller.setCurrentLimit(otbAmpLimit, otbAmpLimit, 0);
            newLimit = false;
        }

        roller.setValue(pathSpeed);
        feed.setValue(-pathSpeed);

        if (deploy){
            cylinder.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
        } else {
            cylinder.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        }
    }

    @Override
    public void inputUpdate(Input source) {
        switch (state) {
            case 0:
                if (source == intake && intake.getValue()){
                    state = 1;
                } else if (source == coneButton && coneButton.getValue()){
                    state = 7;
                } else if (source == cubeButton && cubeButton.getValue()){
                    state = 8;
                }
                break;
            case 1:
                if (source == feedBB && feedBB.getValue()){
                    state = 2;
                } else if (source == intake && !intake.getValue()){
                    state = 0;
                }
                break;
            case 3:
                if (source == cubeButton && cubeButton.getValue()) {
                    state = 4;
                } else if(source == leftTrigger && Math.abs(leftTrigger.getValue()) > 0) {
                    state = 5;
                }
                break;
            case 4:
                if (source == cubeButton && !cubeButton.getValue()) {
                    state = 0;
                }
                break;
            case 5:
                if(source == leftTrigger && Math.abs(leftTrigger.getValue()) == 0) {
                    state = 0;
                }
                break;
            case 6:
                if (source == outtake && !outtake.getValue()) {
                    state = 0;
                }
                break;
            case 7:
                if(source == coneButton && !coneButton.getValue()){
                    state = 0;
                }
                break;
            case 8:
                if (source == cubeButton && !cubeButton.getValue()){
                    state = 0;
                }
                break;
            }
        // Outtake overrides all other states
        if (source == outtake && outtake.getValue()) {
            state = 6;
        }

        if ((source == intake && intake.getValue()) || (source == outtake && outtake.getValue())){
            deploy = true;
        } else if ((source == intake && !intake.getValue()) || (source == outtake && !outtake.getValue()) || (source == feedBB && feedBB.getValue() && intake.getValue())){
            deploy = false;
        }

        SmartDashboard.putBoolean("feed beam break", feedBB.getValue());
        SmartDashboard.putNumber("ball path state", state);
    }    
    
    // public void autoIntake(boolean open){
    //     if(open == true){
    //         pathSpeed = PATH_IN_SPEED;
    //         deploy = true;
    //     }
    //     else if(open == false){
    //         pathSpeed = 0;
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

    public void setSpeed(double pathSpeed){
        this.pathSpeed = pathSpeed;
    }

}