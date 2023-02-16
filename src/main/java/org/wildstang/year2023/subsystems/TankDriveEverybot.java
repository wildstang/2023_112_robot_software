package org.wildstang.year2023.subsystems;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

public class TankDriveEverybot implements Subsystem
{
    WsSparkMax motor;
    WsSparkMax motor2;
    WsJoystickAxis forwardTrigger;
    WsJoystickAxis backTrigger;
    double motorSpeed;
    @Override
    public void inputUpdate(Input sourceInput) {
        // TODO Auto-generated method stub
        if (sourceInput == forwardTrigger){
            if (Math.abs(forwardTrigger.getValue()) > 0.2){
            motorSpeed = 0.8;
         }
            else {
            motorSpeed = 0;
          }} 
            
        else if (sourceInput == backTrigger){
            if (Math.abs(backTrigger.getValue()) < 0.2){
                motorSpeed = -0.8;
            }
            else {
                motorSpeed = 0;
            }
        }
    }
    @Override
    public void init() {
        // TODO Auto-generated method stub
        forwardTrigger = (WsJoystickAxis) WSInputs.MANIPULATOR_RIGHT_TRIGGER.get();
        backTrigger = (WsJoystickAxis) WSInputs.MANIPULATOR_LEFT_TRIGGER.get();
        forwardTrigger.addInputListener(this);
        backTrigger.addInputListener(this);
        motor = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        motor2 = (WsSparkMax) WSOutputs.TEST_MOTOR.get();
        motorSpeed = 0;
    }
    @Override
    public void selfTest() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void update() {
        // TODO Auto-generated method stub
        motor.setValue(motorSpeed);
            if (motorSpeed == 0){
                motor.setBrake();
            }
            else {
                motor.setCoast();
            }
            if (motorSpeed == 0){
                motor2.setBrake();
            }
            else {
                motor.setCoast();
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
    }

}
