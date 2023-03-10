package org.wildstang.year2023.subsystems.swerve;

import com.ctre.phoenix.sensors.Pigeon2;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.inputs.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.swerve.SwerveDriveTemplate;
import org.wildstang.year2023.robot.CANConstants;
import org.wildstang.year2023.robot.WSInputs;
import org.wildstang.year2023.robot.WSOutputs;
// import org.wildstang.year2023.subsystems.targeting.AimHelper;
import org.wildstang.hardware.roborio.outputs.WsSparkMax;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**Class: SwerveDrive
 * inputs: driver left joystick x/y, right joystick x, right trigger, right bumper, select, face buttons all, gyro
 * outputs: four swerveModule objects
 * description: controls a swerve drive for four swerveModules through autonomous and teleoperated control
 */
public class SwerveDrive extends SwerveDriveTemplate {

    private AnalogInput leftStickX;//translation joystick x
    private AnalogInput leftStickY;//translation joystick y
    private AnalogInput rightStickX;//rot joystick
    // private AnalogInput rightStickY;//rot joystick for steering wheel mode
    private AnalogInput rightTrigger;//thrust
    private AnalogInput leftTrigger;//derate
    private DigitalInput rightBumper;//robot centric control
    private DigitalInput leftBumper;//intake
    // private DigitalInput rightStick;//steering wheel mode
    private DigitalInput select;//gyro reset
    private DigitalInput start;//snake mode
    private DigitalInput faceUp;//rotation lock 0 degrees
    private DigitalInput faceRight;//rotation lock 90 degrees
    private DigitalInput faceLeft;//rotation lock 270 degrees
    private DigitalInput faceDown;//rotation lock 180 degrees
    private DigitalInput dpadLeft;//defense mode
    
    private SwerveDrivePoseEstimator pose;

    private double xSpeed;
    private double ySpeed;
    private double rotSpeed;
    private double thrustValue;
    private double derateValue;
    private boolean rotLocked;
    private boolean isSnake;
    private boolean isFieldCentric;
    private double rotTarget;
    private double pathVel;
    private double pathHeading;
    private double pathTarget;

    private final Pigeon2 gyro = new Pigeon2(CANConstants.GYRO);
    public SwerveModule[] modules;
    private SwerveSignal swerveSignal;
    private WSSwerveHelper swerveHelper = new WSSwerveHelper();

    public enum driveType {TELEOP, AUTO, CROSS, LL};
    public driveType driveState;

    @Override
    public void inputUpdate(Input source) {
        //determine if we are in cross or teleop
        if (driveState != driveType.AUTO && dpadLeft.getValue()) {
            driveState = driveType.CROSS;
            for (int i = 0; i < modules.length; i++) {
                modules[i].setDriveBrake(true);
            }
            this.swerveSignal = new SwerveSignal(new double[]{0, 0, 0, 0 }, swerveHelper.setCross().getAngles());
        }
        else if (driveState != driveType.AUTO) {
            driveState = driveType.TELEOP;
            for (int i = 0; i < modules.length; i++) {
                modules[i].setDriveBrake(false);
            }
        }
        //get x and y speeds
        xSpeed = swerveHelper.scaleDeadband(leftStickX.getValue(), DriveConstants.DEADBAND);
        ySpeed = swerveHelper.scaleDeadband(leftStickY.getValue(), DriveConstants.DEADBAND);
        
        //reset gyro
        if (source == select && select.getValue()) {
            gyro.setYaw(0.0);
        }

        if (source == rightBumper && rightBumper.getValue()){
            isFieldCentric = !isFieldCentric;
        }

        //determine snake or pid locks
        // if (Math.sqrt(Math.pow(rightStickX.getValue(), 2)+Math.pow(rightStickY.getValue(),2)) > 0.9){
        //     rotTarget = swerveHelper.getDirection(rightStickX.getValue(), rightStickY.getValue());
        // }
        if (start.getValue() && (Math.abs(xSpeed) > 0.1 || Math.abs(ySpeed) > 0.1)) {
            rotLocked = true;
            isSnake = true;
            rotTarget = swerveHelper.getDirection(xSpeed, ySpeed);
        }
        else {
            isSnake = false;
        }
        if (source == faceUp && faceUp.getValue()){
            rotTarget = 0.0;
            rotLocked = true;
            isFieldCentric = true;
        }
        if (source == faceLeft && faceLeft.getValue()){
            rotTarget = 270.0;
            rotLocked = true;
            isFieldCentric = true;
        }
        if (source == faceDown && faceDown.getValue()){
            rotTarget = 180.0;
            rotLocked = true;
            isFieldCentric = true;
        }
        if (source == faceRight && faceRight.getValue()){
            rotTarget = 90.0;
            rotLocked = true;
            isFieldCentric = true;
        }

        //get rotational joystick
        rotSpeed = rightStickX.getValue()*Math.abs(rightStickX.getValue());
        rotSpeed = swerveHelper.scaleDeadband(rotSpeed, DriveConstants.DEADBAND);

        //if the rotational joystick is being used, the robot should not be auto tracking heading
        if (rotSpeed != 0) {
            rotLocked = false;
            rotTarget = 0.0;
        }
        // switch between rotation speed control and steering wheel rotation control
        // if (source == rightStick){
        //     if (rightStick.getValue()){
        //         rotLocked = !rotLocked;
        //     }
        // }
        
        //assign thrust
        thrustValue = 1 - DriveConstants.DRIVE_THRUST + DriveConstants.DRIVE_THRUST * Math.abs(rightTrigger.getValue());
        derateValue = (DriveConstants.DRIVE_DERATE * Math.abs(leftTrigger.getValue()) + 1);
        xSpeed *= thrustValue/derateValue;
        ySpeed *= thrustValue/derateValue;
        rotSpeed *= thrustValue;
        
    }
 
    @Override
    public void init() {
        initInputs();
        initOutputs();
        initPoseEstimator(new Pose2d());
        resetState();
        gyro.setYaw(0.0);
    }

    public void initInputs() {
        leftStickX = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_LEFT_JOYSTICK_X);
        leftStickX.addInputListener(this);
        leftStickY = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_LEFT_JOYSTICK_Y);
        leftStickY.addInputListener(this);
        rightStickX = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_JOYSTICK_X);
        rightStickX.addInputListener(this);
        // rightStickY = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_JOYSTICK_X);
        // rightStickY.addInputListener(this);
        rightTrigger = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_TRIGGER);
        rightTrigger.addInputListener(this);
        leftTrigger = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRIVER_LEFT_TRIGGER);
        leftTrigger.addInputListener(this);
        rightBumper = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_SHOULDER);
        rightBumper.addInputListener(this);
        leftBumper = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_LEFT_SHOULDER);
        leftBumper.addInputListener(this);
        // rightStick = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_RIGHT_JOYSTICK_BUTTON);
        // rightBumper.addInputListener(this);
        select = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_SELECT);
        select.addInputListener(this);
        start = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_START);
        start.addInputListener(this);
        faceUp = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_FACE_UP);
        faceUp.addInputListener(this);
        faceLeft = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_FACE_LEFT);
        faceLeft.addInputListener(this);
        faceRight = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_FACE_RIGHT);
        faceRight.addInputListener(this);
        faceDown = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_FACE_DOWN);
        faceDown.addInputListener(this);
        dpadLeft = (DigitalInput) Core.getInputManager().getInput(WSInputs.DRIVER_DPAD_LEFT);
        dpadLeft.addInputListener(this);
    }

    public void initOutputs() {
        //create four swerve modules
        modules = new SwerveModule[]{
            new SwerveModule((WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.DRIVE1), 
                (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ANGLE1), DriveConstants.FRONT_LEFT_OFFSET),
            new SwerveModule((WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.DRIVE2), 
                (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ANGLE2), DriveConstants.FRONT_RIGHT_OFFSET),
            new SwerveModule((WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.DRIVE3), 
                (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ANGLE3), DriveConstants.REAR_LEFT_OFFSET),
            new SwerveModule((WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.DRIVE4), 
                (WsSparkMax) Core.getOutputManager().getOutput(WSOutputs.ANGLE4), DriveConstants.REAR_RIGHT_OFFSET)
        };
        //create default swerveSignal
        swerveSignal = new SwerveSignal(new double[]{0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 0.0, 0.0});
        //limelight = (AimHelper) Core.getSubsystemManager().getSubsystem(WSSubsystems.AIM_HELPER);
    }

    private void initPoseEstimator(Pose2d initialPos){
        // Locations for the swerve drive modules relative to the robot center.
        Translation2d m_frontLeftLocation = new Translation2d(-DriveConstants.ROBOT_WIDTH/2*.0254, DriveConstants.ROBOT_LENGTH/2*.0254);
        Translation2d m_frontRightLocation = new Translation2d(DriveConstants.ROBOT_WIDTH/2*.0254, DriveConstants.ROBOT_LENGTH/2*.0254);
        Translation2d m_backLeftLocation = new Translation2d(-DriveConstants.ROBOT_WIDTH/2*.0254, -DriveConstants.ROBOT_LENGTH/2*.0254);
        Translation2d m_backRightLocation = new Translation2d(DriveConstants.ROBOT_WIDTH/2*.0254, -DriveConstants.ROBOT_LENGTH/2*.0254);

        // Creating my kinematics object using the module locations
        SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
        m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
        );
        pose = new SwerveDrivePoseEstimator(m_kinematics, new Rotation2d(getGyroAngle()*Math.PI/180), 
        new SwerveModulePosition[] {
            modules[0].getSwerveModulePosition(),
            modules[1].getSwerveModulePosition(),
            modules[2].getSwerveModulePosition(),
            modules[3].getSwerveModulePosition()
        }, initialPos );
    }

    private void updateOdometry() {
        pose.update(
            new Rotation2d(getGyroAngle()*Math.PI/180),
            new SwerveModulePosition[] {
                modules[0].getSwerveModulePosition(),
                modules[1].getSwerveModulePosition(),
                modules[2].getSwerveModulePosition(),
                modules[3].getSwerveModulePosition()
            });

        //TODO: add vision odometry
        // var resultTimestamp = pipelineResult.getTimestampSeconds();
        // pose.addVisionMeasurement(visionMeasurement.toPose2d(), resultTimestamp);

    }

    public void resetOdometry(Pose2d newPose) {
        resetDriveEncoders();
        this.pose.resetPosition(new Rotation2d(getGyroAngle()), 
                                new SwerveModulePosition[] {
                                    modules[0].getSwerveModulePosition(),
                                    modules[1].getSwerveModulePosition(),
                                    modules[2].getSwerveModulePosition(),
                                    modules[3].getSwerveModulePosition()
                                }, newPose);
    }
    
    @Override
    public void selfTest() {
    }

    @Override
    public void update() {
        updateOdometry();
        if (driveState == driveType.CROSS) {
            //set to cross - done in inputupdate
            this.swerveSignal = swerveHelper.setCross();
            drive();
        }
        if (driveState == driveType.TELEOP) {
            if (rotLocked){
                //if rotation tracking, replace rotational joystick value with controller generated one
                rotSpeed = swerveHelper.getRotControl(rotTarget, getGyroAngle());
                if (isSnake) {
                    if (Math.abs(rotSpeed) < 0.05) {
                        rotSpeed = 0;
                    }
                    else {
                        rotSpeed *= 4;
                        if (Math.abs(rotSpeed) > 1) rotSpeed = 1.0 * Math.signum(rotSpeed);
                    }
                    
                } 
            }
            this.swerveSignal = swerveHelper.setDrive(xSpeed, ySpeed, rotSpeed, getGyroAngle());
            SmartDashboard.putNumber("FR signal", swerveSignal.getSpeed(0));
            drive();
        }
        if (driveState == driveType.AUTO) {
            //get controller generated rotation value
            rotSpeed = Math.max(-0.2, Math.min(0.2, swerveHelper.getRotControl(pathTarget, getGyroAngle())));
            //ensure rotation is never more than 0.2 to prevent normalization of translation from occuring
            
            //update where the robot is, to determine error in path
            this.swerveSignal = swerveHelper.setAuto(swerveHelper.getAutoPower(pathVel), pathHeading, rotSpeed, getGyroAngle());
            drive();
        }
        if (driveState == driveType.LL) {}

        SmartDashboard.putNumber("Gyro Reading", getGyroAngle());
        SmartDashboard.putNumber("X speed", xSpeed);
        SmartDashboard.putNumber("Y speed", ySpeed);
        SmartDashboard.putNumber("rotSpeed", rotSpeed);
        SmartDashboard.putString("Drive mode", driveState.toString());
        SmartDashboard.putBoolean("rotLocked", rotLocked);
        SmartDashboard.putNumber("Auto velocity", pathVel);
        SmartDashboard.putNumber("Auto translate direction", pathHeading);
        SmartDashboard.putNumber("Auto rotation target", pathTarget);
        SmartDashboard.putNumber("Gyro Pitch", getGyroPitch());
        SmartDashboard.putString("swerve odometery", pose.getEstimatedPosition().toString());
    }
    
    @Override
    public void resetState() {
        xSpeed = 0;
        ySpeed = 0;
        rotSpeed = 0;
        setToTeleop();
        rotLocked = false;
        rotTarget = 0.0;
        pathVel = 0.0;
        pathHeading = 0.0;
        pathTarget = 0.0;
        

        isFieldCentric = true;
        isSnake = false;
    }

    @Override
    public String getName() {
        return "SwerveDrive";
    }

    /** resets the drive encoders on each module */
    public void resetDriveEncoders() {
        for (int i = 0; i < modules.length; i++) {
            modules[i].resetDriveEncoders();
        }
    }

    /** sets the drive to teleop/cross, and sets drive motors to coast */
    public void setToTeleop() {
        driveState = driveType.TELEOP;
        for (int i = 0; i < modules.length; i++) {
            modules[i].setDriveBrake(false);
        }
        rotSpeed = 0;
        xSpeed = 0;
        ySpeed = 0;
        pathHeading = 0;
        pathVel = 0;
        rotLocked = false;
        swerveHelper.setRotSpeedConst(DriveConstants.ROTATION_SPEED);
    }

    /**sets the drive to autonomous */
    public void setToAuto() {
        driveState = driveType.AUTO;
        // resetDriveEncoders();
        swerveHelper.setRotSpeedConst(1);
    }

    /**drives the robot at the current swerveSignal, and displays information for each swerve module */
    private void drive() {
        if (driveState == driveType.CROSS) {
            for (int i = 0; i < modules.length; i++) {
                modules[i].runCross(swerveSignal.getSpeed(i), swerveSignal.getAngle(i));
                modules[i].displayNumbers(DriveConstants.POD_NAMES[i]);
            }
        }
        else {
            for (int i = 0; i < modules.length; i++) {
                modules[i].run(swerveSignal.getSpeed(i), swerveSignal.getAngle(i));
                modules[i].displayNumbers(DriveConstants.POD_NAMES[i]);
            }
        }
    }

    /**sets autonomous values from the path data file */
    public void setAutoValues(double velocity, double heading) {
        driveState = driveType.AUTO;
        pathVel = velocity;
        pathHeading = heading;
    }

    public void setCross() {
        driveState = driveType.CROSS;
    }

    /**sets the autonomous heading controller to a new target */
    public void setAutoHeading(double headingTarget) {
        pathTarget = headingTarget;
    }

    public void setAiming() {
        driveState = driveType.LL;
    }

    /**
     * Resets the gyro, and sets it the input number of degrees
     * Used for starting the match at a non-0 angle
     * @param degrees the current value the gyro should read
     */
    public void setGyro(double degrees) {
        // resetState();
        gyro.setYaw(degrees);
    }

    public double getGyroAngle() {
        if (!isFieldCentric) return 0;
        //limelight.setGyroValue((gyro.getYaw() + 360)%360);
        return (359.99 - gyro.getYaw()+360)%360;
    }  

    public double getGyroPitch() {
        return gyro.getRoll();
    }
}
