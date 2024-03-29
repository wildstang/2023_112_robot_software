package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDPadButton;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;
// import org.wildstang.year2023.robot.WSSubsystems;
// import org.wildstang.year2023.subsystems.arm.Arm;

public class RollerClaw implements Subsystem {
    
private WsSparkMax roller;
private WsJoystickAxis rightTrigger;
private WsJoystickAxis leftTrigger;
private WsJoystickButton coneButton;
private WsJoystickButton cubeButton;
private WsJoystickButton intake;
private WsJoystickButton outtake;
private WsDPadButton stow;
private double rollerSpeed;
private boolean deploy;
private WsDoubleSolenoid gripper;
// private boolean reclamp;

private int i, j;

private static final double IN_SPEED = 1.0;
private static final double OUT_SPEED = -0.4;
private static final double HOLD_SPEED = 0.05;

    @Override
    public void init() {

        roller = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.CLAW);
        gripper = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.GRIPPER_SOLENOID);
        initInputs();

    }

    private void initInputs(){
        
        rightTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_TRIGGER);
        rightTrigger.addInputListener(this);
        leftTrigger = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_SHOULDER);
        coneButton.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_LEFT_SHOULDER);
        cubeButton.addInputListener(this);
        stow = (WsDPadButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_DPAD_RIGHT);
        stow.addInputListener(this);
        intake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_RIGHT);
        intake.addInputListener(this);
        outtake = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_FACE_UP);
        outtake.addInputListener(this);
    }

    @Override
    public void resetState(){
        rollerSpeed = 0;
        deploy = false;
    }

    @Override
    public void inputUpdate(Input source) {

        if (source == rightTrigger){
            rollerSpeed = Math.abs(rightTrigger.getValue());
        } else if(source == leftTrigger){
            rollerSpeed = -0.5*Math.abs(leftTrigger.getValue());
        } else{
            rollerSpeed = HOLD_SPEED;
        }

        if (source == coneButton && coneButton.getValue()){
            deploy = false;
            j = -1;
            rollerSpeed = IN_SPEED;
            i = 0;
        } else if (source == cubeButton && cubeButton.getValue()){
            deploy = true;
            j = -1;
            rollerSpeed = OUT_SPEED;
            i = 0;
        } else if (source == intake) {
            if (intake.getValue()){
                deploy = true;
                j = -1;
                rollerSpeed = 0.0;
            } else{
                deploy = false;
                j = -1;
            }
        } else if (source == outtake) {
            if (outtake.getValue()){
                deploy = false;
                j = -1;
                rollerSpeed = -0.67;
            } else{
                deploy = false;
                j = -1;
            }
        } else if (source == stow && stow.getValue()){
            deploy = false;
            j = 25;
            // reclamp = true;
        }

    }
    @Override
    public void update() {

        if (i > 25){ // delay to allow tap to open or close cylinder
            roller.setValue(rollerSpeed);
        } else {
            roller.setValue(0);
            i ++;
        }

        // if(reclamp && ((Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM)).isAtTarget()){
            
        //     reclamp = false;
        //     gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
        //     j = 18;
        //     deploy = false;
        // }

        if (j <= 0){  // delay closing gripper when going to stow
            if (deploy){
                gripper.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
            } else{
                gripper.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
            }
        } else {
            j --;
        }

    }

    @Override
    public String getName() {
        return "RollerClaw";
    }

    @Override
    public void selfTest() {
        
    }

    public void autoScore(String mode) {

        if (mode == "Cube High"){
            rollerSpeed = OUT_SPEED;
        }
        else if (mode == "Cube Mid"){
            deploy = true;
            rollerSpeed = OUT_SPEED;
        }
        else if (mode == "Cone Mid"){
            deploy = true;
        }
        else if (mode == "Hybrid"){
            deploy = true;
        }
        else if (mode == "Hold"){
            deploy = false;
            rollerSpeed = IN_SPEED;
        }

    }

    public void setGripper(boolean deploy){
        this.deploy = deploy;
    }

    public void setSpeed(double speed){
        this.rollerSpeed = speed;
    }
}
