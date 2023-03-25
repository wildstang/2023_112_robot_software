package org.wildstang.year2023.auto.programs;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.PathHeadingStep;
import org.wildstang.framework.auto.steps.SetGyroStep;
import org.wildstang.framework.auto.steps.SwervePathFollowerStep;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.steps.AutoBalance;
import org.wildstang.year2023.auto.steps.AutoLightShow;
import org.wildstang.year2023.auto.steps.ClawRelease;
import org.wildstang.year2023.auto.steps.MoveArm;
import org.wildstang.year2023.auto.steps.SetAutoDriveStep;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Center_1pe extends AutoProgram{

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        boolean color = DriverStation.getAlliance() == Alliance.Blue;

        //score preload
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(false));
        addStep(new SetGyroStep(180, swerve));
        addStep(new PathHeadingStep(180, swerve));
        addStep(new AutoStepDelay(500));

        //rotate to heading, stow arm, drive onto charging station
        AutoParallelStepGroup group0 = new AutoParallelStepGroup();
        group0.addStep(new SetAutoDriveStep());
        group0.addStep(new SwervePathFollowerStep(PathPlanner.loadPath("Center_1pe", new PathConstraints(3.0, 3.0)), swerve, color));
        group0.addStep(new MoveArm("STOW"));
        AutoSerialStepGroup subgroup0_1 = new AutoSerialStepGroup();
        subgroup0_1.addStep(new AutoStepDelay(300));
        subgroup0_1.addStep(new ClawRelease(true));
        group0.addStep(subgroup0_1);
        addStep(group0);
        
        addStep(new AutoBalance());

        addStep(new AutoLightShow(true));
    }

    @Override
    public String toString() {
        return "Center_1pe";
    }
    
}
