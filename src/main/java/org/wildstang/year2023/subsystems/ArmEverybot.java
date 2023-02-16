package org.wildstang.year2023.subsystems;

import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D.VerticalDirection;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsDPadButton;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSAutoPrograms;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class ArmEverybot implements Subsystem 
{
    WsSparkMax m1;
    WsSparkMax m2;
    WsJoystickAxis lefttrigger;
    WsJoystickAxis righttrigger;
    WsDPadButton dPadButton1;
    WsDPadButton dPadButton2;
    double mSpeed;
    double mSpeed2;


    @Override
    public void inputUpdate(Input sourceInput) {
            if (sourceInput == lefttrigger) {
                if (Math.abs(lefttrigger.getValue()) < 0.3){
                    mSpeed = -0.8;
                }
                else {
                    mSpeed = 0;
                }
            }
            else if (sourceInput == righttrigger)
{
                if (Math.abs(righttrigger.getValue()) > 0.3){
                    mSpeed = 0.8;
                }
                else {
                    mSpeed = 0;
                };
            if (sourceInput == dPadButton1){
                if (dPadButton1.getValue()){
                    mSpeed2 = 0.8;
                }
                if (sourceInput == dPadButton2){
                    if (dPadButton2.getValue()){
                        mSpeed2 = 0.8;
                    }
                }                    
            }
            else {

            }

            };
}        // TODO Auto-generated method stub
    


    @Override
    public void init() {
        // TODO Auto-generated method stub
        lefttrigger = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_JOYSTICK_BUTTON.get();
        righttrigger = (WsJoystickAxis) WSInputs.MANIPULATOR_RIGHT_JOYSTICK_BUTTON.get();
        lefttrigger.addInputListener(this);
        righttrigger.addInputListener(this);
        m1 = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        m2 = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        mSpeed = 0;
        dPadButton1 = (WsDPadButton) WSInputs.MANIPULATOR_DPAD_UP.get();
        dPadButton2 = (WsDPadButton) WSInputs.MANIPULATOR_DPAD_DOWN.get();
        dPadButton1.addInputListener(this);
        dPadButton2.addInputListener(this);
        mSpeed2 = 0;
        }

        @Override
        public void selfTest() {
            // TODO Auto-generated method stub
            
        };
    @Override
    public void update() {
        // TODO Auto-generated method stub
        m1.setValue(mSpeed);
        if (mSpeed == 0){
            m1.setBrake();
        }
        else {
            m1.setCoast();
        }
        m2.setValue(mSpeed2);
        if (mSpeed2 == 0) {
            m2.setBrake();
        }
        else {
            m2.setCoast();
        }
    };


    @Override
    public void resetState() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    };
    //The controls would be LT to intake cargo, RT to score cargo, LB to intake 

    //cones, RB to score cones, and Dpad up and down to raise and lower the arm
}

