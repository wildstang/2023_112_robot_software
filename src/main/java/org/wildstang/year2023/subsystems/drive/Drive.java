package org.wildstang.year2023.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.pid.PIDConstants;
import org.wildstang.framework.subsystems.drive.PathFollowingDrive;
import org.wildstang.hardware.roborio.inputs.WsAnalogInput;
import org.wildstang.hardware.roborio.inputs.WsDigitalInput;
import org.wildstang.hardware.roborio.inputs.WsJoystickAxis;
import org.wildstang.hardware.roborio.inputs.WsJoystickButton;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;

import edu.wpi.first.wpilibj.I2C;

public class Drive extends PathFollowingDrive {

    public enum DriveState{ TELEOP, AUTO, BASELOCK;}

    private WsSparkMax left, right;
    private WsJoystickAxis throttleJoystick, headingJoystick;
    private WsJoystickButton baseLock;
    private DriveState state;

    private double heading;
    private double throttle;
    private DriveSignal signal;

    private WSDriveHelper helper = new WSDriveHelper();

    @Override
    public void init() {
        left = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.LEFT_DRIVE);
        right = (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.RIGHT_DRIVE);
        motorSetUp(left);
        motorSetUp(right);
        throttleJoystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_JOYSTICK_X);
        throttleJoystick.addInputListener(this);
        headingJoystick = (WsJoystickAxis) Core.getInputManager().getInput(WSInputs.DRIVER_LEFT_JOYSTICK_Y);
        headingJoystick.addInputListener(this);
        baseLock = (WsJoystickButton) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_SHOULDER);
        baseLock.addInputListener(this);
        resetState();
    }

    @Override
    public void selfTest() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        switch (state){
            case TELEOP: 
                signal = helper.teleopDrive(throttle, heading);
                drive(signal);
                break;
            case BASELOCK:
                left.setPosition(left.getPosition());
                right.setPosition(right.getPosition());
                break;
            case AUTO:
                break;
        }

    }

    @Override
    public void resetState() {
        state = DriveState.TELEOP;
        throttle = 0.0;
        heading = 0.0;
        signal = new DriveSignal(0.0, 0.0);
    }

    @Override
    public String getName() {
        return "Drive";
    }

    @Override
    public void inputUpdate(Input source) {
        heading = headingJoystick.getValue();
        throttle = throttleJoystick.getValue();
        if (baseLock.getValue()){
            state = DriveState.BASELOCK;
        } else {
            state = DriveState.TELEOP;
        }

    }

    @Override
    public void setBrakeMode(boolean brake) {
        if (brake) {
            left.setBrake();
            right.setBrake();
        }
        else {
            left.setCoast();
            right.setCoast();
        }
    }

    @Override
    public void resetEncoders() {
        left.resetEncoder();
        right.resetEncoder();
    }
    
    @Override
    public void startPathFollower() {
        state = DriveState.AUTO;
    }
    
    @Override
    public void stopPathFollower() {
        state = DriveState.TELEOP;
        drive(new DriveSignal(0.0, 0.0));
    }

    @Override
    public void updatePathFollower(double[] trajectoryInfo) {

    }

    public void drive(DriveSignal commandSignal){
        left.setSpeed(commandSignal.leftMotor);
        right.setSpeed(commandSignal.rightMotor);
    }

    private void motorSetUp(WsSparkMax setupMotor){
        PIDConstants constants = DrivePID.BASE_LOCK.getConstants();
        setupMotor.initClosedLoop(constants.p, constants.i, constants.d, constants.f);
        setupMotor.setCurrentLimit(80, 20, 10000);
        setupMotor.enableVoltageCompensation();
    }

    public void setGyro(double degrees){
    }
}
