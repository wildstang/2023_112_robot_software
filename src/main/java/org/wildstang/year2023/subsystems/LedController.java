package org.wildstang.year2023.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.year2023.robot.WSInputs;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LedController implements Subsystem{

    private WsJoystickButton coneButton;
    private WsJoystickButton cubeButton;
    private WsJoystickButton tieDyeButton;
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;

    private int port = 9;//port
    private int length = 45;//length
    private int initialHue = 0;
    private boolean auto; 
    private int[] autoColorsR = {52, 33, 11, 255};
    private int[] autoColorsG = {161, 79, 2, 255};
    private int[] autoColorsB = {236, 194, 97, 255};
    private int timer = 0;
    private int color = 0;
    private boolean isRainbow = true;
    private int speed = 1;

    @Override
    public void inputUpdate(Input source) {
        if (source == coneButton) {
            coneDisplay();
            isRainbow = false;
        } else if (source == cubeButton) {
            cubeDisplay();
            isRainbow = false;
        } else if (source == tieDyeButton) {
            isRainbow = true;
        }
        
    }

    @Override
    public void init() {
        coneButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_START);
        coneButton.addInputListener(this);
        cubeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_SELECT);
        cubeButton.addInputListener(this);
        tieDyeButton = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.MANIPULATOR_RIGHT_JOYSTICK_BUTTON);
        tieDyeButton.addInputListener(this);

        led = new AddressableLED(port);
        ledBuffer = new AddressableLEDBuffer(length);
        led.setLength(ledBuffer.getLength());
        led.setData(ledBuffer);
        led.start();

        resetState();
        
    }

    @Override
    public void selfTest() {
    }

    @Override
    public void update() {
        if (isRainbow){
            rainbow();
        } 

        if (auto){
            // isRainbow = false; 
            showTime();
        }
        SmartDashboard.putNumber("color", color);
    }

    @Override
    public void resetState() {
        isRainbow = true;
        auto = false;
    }

    @Override
    public String getName() {
        return "LedController";
    }

    public void cubeDisplay(){
        for (var i = 0; i < length; i++) {
            ledBuffer.setRGB(i, 255, 0, 255);
        }
        led.setData(ledBuffer);
    }

    public void coneDisplay(){
        for (var i = 0; i < length; i++) {
            ledBuffer.setRGB(i, 255, 255, 0);
        }
        led.setData(ledBuffer);
    }

    public void showTime(){

        if (timer >= 13){
            for (int i = 0; i < length; i++){
                ledBuffer.setRGB(i, autoColorsR[color], autoColorsG[color], autoColorsB[color]);
            }
            led.setData(ledBuffer);
            timer = 0;
            color = (color + 1) % autoColorsR.length;
        }
        timer++;
    }

    public void setAuto(boolean on){
        auto = on;
        isRainbow = false;
    }

    private void rainbow(){
        for (int i = 0; i < ledBuffer.getLength(); i++){
            ledBuffer.setRGB(i, (initialHue + (i*255/(ledBuffer.getLength()/3)))%255, (initialHue + (i*255/(ledBuffer.getLength()/3)))%255, 255);
        }
        if(DriverStation.isTeleop()){ speed = 3;}
        else if (DriverStation.isAutonomous()){ speed = 6;}
        else if(DriverStation.isDisabled()){ speed = 1;}
        
        
        initialHue = (initialHue + speed) % 255;
        led.setData(ledBuffer);
    } 
    
}
