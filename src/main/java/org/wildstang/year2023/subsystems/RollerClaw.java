package org.wildstang.year2023.subsystems;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.roborio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.roborio.outputs.WsSolenoid;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickToggleButton;
import org.wildstang.hardware.roborio.inputs.config.WsJSButtonInputConfig;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.hardware.roborio.outputs.config.WsSparkMaxFollowerConfig;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

import edu.wpi.first.wpilibj.DigitalInput;

public class RollerClaw implements Subsystem {
    
private WsSparkMax roller;
private WsDigitalInput forwardButton;
private WsDigitalInput backwardButton;
private WsDigitalInput retract;
private WsDigitalInput extend;
private WsSolenoid solenoid;
private double rollerSpeed;
private boolean piston;
private static final double speedConstant = 1;

    @Override
    public void init() {

        roller = (WsSparkMax) WSOutputs.CLAW.get();
        forwardButton = (WsDigitalInput) WSInputs.MANIPULATOR_RIGHT_SHOULDER.get();
        backwardButton = (WsDigitalInput) WSInputs.MANIPULATOR_LEFT_SHOULDER.get();
        retract = (WsDigitalInput) WSInputs.MANIPULATOR_FACE_LEFT.get();
        extend = (WsDigitalInput) WSInputs.MANIPULATOR_FACE_RIGHT.get();
        solenoid = (WsSolenoid)WSOutputs.CLAW_SOLENOID.get();

    }

    @Override
    public void resetState(){

        rollerSpeed = 0;
        piston = false;

    }

    @Override
    public void inputUpdate(Input source) {

        if (forwardButton.getValue()){
            rollerSpeed = speedConstant;
        }

        else if (backwardButton.getValue()){
            rollerSpeed = -speedConstant;
        }

        else {
            rollerSpeed = 0;
        }


        if (extend.getValue()){

            piston = true;

        }

        if (retract.getValue()){

            piston = false;

        }

    }
    @Override
    public void update() {
    
        roller.setValue(rollerSpeed);
        
        solenoid.setValue(piston);

    }

    public void AutoScore(String mode) {

        if (mode == "Cube High"){
            rollerSpeed = -speedConstant;
        }

        else if (mode == "Cube Mid"){
            piston = true;
            rollerSpeed = -speedConstant;
        }
        else if (mode == "Cone Mid"){
            piston = true;
        }
        else if (mode == "Hybrid"){
            piston = true;
        }
        else if (mode == "Hold"){
            piston = false;
            rollerSpeed = speedConstant;
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
