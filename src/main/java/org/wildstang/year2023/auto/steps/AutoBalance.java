package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoBalance extends AutoStep {

    private SwerveDrive swerve;
    // private double heading;
    // private boolean isBlue;
    private static final double kP = 0.02;
    private static final double kD = 0.2;
    private double error, prevErr;

    public AutoBalance (){
        // this.isBlue = isBlue;
        // this.heading = heading;
    }

    @Override
    public void initialize() {
        swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        
    }

    @Override
    public void update() {
        error = swerve.getGyroPitch();
        swerve.setAutoValues(kP * error, 0);
        //dOut = kD * (error - prevErr);
        
        if (Math.abs(error) < 3 || DriverStation.getMatchTime() <= 0.1) {
            swerve.setCross();
            setFinished();
        }

        prevErr = error;
    }

    @Override
    public String toString() {
        return "AutoBalance";
    }
    
}
