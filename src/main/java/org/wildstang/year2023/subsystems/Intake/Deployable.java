package org.wildstang.year2023.subsystems.Intake;

import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsSolenoid;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class Deployable implements Subsystem {

    private WsSparkMax intake1;
    private WsJoystickButton deployRevIntake;
    private WsSolenoid deployable1;
    private WsSolenoid deployable2;

    //All variables used
    private boolean intakeCall;
   
    

    @Override
    public void inputUpdate(Input source) {
        // TODO Auto-generated method stub
        
        if (source == deployRevIntake){
            intakeCall = !(intakeCall);

        }
    }
    
    @Override
    public void init() {
        // TODO Auto-generated method stub
        intake1 = (WsSparkMax) WSOutputs.INTAKE1.get();

        deployRevIntake = (WsJoystickButton) WSInputs.MANIPULATOR_DPAD_DOWN.get();
        deployRevIntake.addInputListener(this);

        deployable1 = (WsSolenoid) WSOutputs.DEPLOYABLE1.get();
        deployable2 = (WsSolenoid) WSOutputs.DEPLOYABLE2.get();

        intakeCall = false; 

    }

    @Override
    public void selfTest() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        if (intakeCall){
            intake1.setValue(1); 
            deployable1.setValue(true);
            deployable2.setValue(true);
        } else {
            intake1.setValue(0);
            deployable1.setValue(false);
            deployable2.setValue(false);
        }
    }

    @Override
    public void resetState() {
        // TODO Auto-generated method stub
        intakeCall = false; 
        
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "Deployable";
    }


    
}
