package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;

public class InitializeOdometry extends AutoStep {

    private SwerveDrive swerve;
    private Pose2d initPose;

    public InitializeOdometry(Pose2d inPose) {
        initPose = inPose;
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        swerve.resetDriveEncoders();
        swerve.resetOdometry(initPose);
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
