package org.wildstang.year2023.auto;

import java.util.ArrayList;
import java.util.List;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.PathFollowerStep;
import org.wildstang.framework.auto.steps.SetGyroStep;
import org.wildstang.framework.auto.steps.SwervePathFollowerStep;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.Steps.ClawRelease;
import org.wildstang.year2023.auto.Steps.IntakeCubeStep;
import org.wildstang.year2023.auto.Steps.MoveArm;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import frc.paths.*;


public class ConeCubeAuto extends AutoProgram {

    @Override
    protected void defineSteps() {

        List<PathPlannerTrajectory> pathGroup1 = PathPlanner.loadPathGroup("RightConeCube", new PathConstraints(4, 3));

        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE.getName());

        addStep(new SetGyroStep(180, swerve));
        addStep(new MoveArm("HIGH"));
        addStep(new ClawRelease(true));
        addStep(new AutoStepDelay(250));
        
        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new MoveArm("STOW"));
        group1.addStep(new ClawRelease(false));
        group1.addStep(new SwervePathFollowerStep(pathGroup1.get(0), swerve));
        addStep(group1);
        
        AutoParallelStepGroup group2 = new AutoParallelStepGroup();
        group2.addStep(new IntakeCubeStep(true));
        group2.addStep(new SwervePathFollowerStep(pathGroup1.get(1), swerve));
        addStep(group2);

        AutoParallelStepGroup group3 = new AutoParallelStepGroup();
        group3.addStep(new IntakeCubeStep(false));
        group3.addStep(new SwervePathFollowerStep(pathGroup1.get(2), swerve));
        addStep(group3);

        addStep(new MoveArm("HIGH"));
        addStep(new ClawRelease(true));
        addStep(new AutoStepDelay(250));


    }

    @Override
    public String toString() {
        return "One Cone One Cube";
    }
    
}