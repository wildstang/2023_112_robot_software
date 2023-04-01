package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoBalance extends AutoStep {

    private SwerveDrive swerve;
    // private double heading;
    // private boolean isBlue;
    private static final double kP = 0.8;
    private static final double kD = 0.0;
    private int i;
    private double error, prevErr;
    private double heading;
    private double dOut;
    private double output;

    public AutoBalance (){
        // this.isBlue = isBlue;
        // this.heading = heading;
    }

    @Override
    public void initialize() {
        swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        swerve.setToAuto();
        i = 0;
        
    }

    @Override
    public void update() {
        error = swerve.getGyroPitch();
        dOut = kD * (error - prevErr);
        output = kP*error + dOut;
        if(output<0){
            heading = 180;
        } else {
            heading = 0;
        }
        
        SmartDashboard.putNumber("gyro balance output", error);
        SmartDashboard.putNumber("gyro d term", dOut);

        if (Math.abs(error) < 3) {
            i ++;
        } else {
            i = 0;
        }

        SmartDashboard.putNumber("balance delay", i);
        
        if (DriverStation.getMatchTime() <= 0.1) { // 
            swerve.setToCross();
            setFinished();
        } else {
            swerve.setAutoValues(Math.abs(output), heading);
        }

        prevErr = error;
    }

    @Override
    public String toString() {
        return "AutoBalance";
    }
    
}
