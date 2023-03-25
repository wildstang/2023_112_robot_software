package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class InitializeOdometry extends AutoStep {

    private SwerveDrive swerve;
    private Pose2d initPose;
    private Rotation2d initHeading;

    public InitializeOdometry(Translation2d inPose, double inHeading) {
        initHeading = new Rotation2d(inHeading);
        initPose = new Pose2d(inPose, initHeading);
    }

    @Override
    public void initialize() {
        swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        swerve.resetOdometry(initPose, initHeading);
    }

    @Override
    public void update() {
        setFinished();
    }

    @Override
    public String toString() {
        return "InitializeOdometry";
    }
    
}
