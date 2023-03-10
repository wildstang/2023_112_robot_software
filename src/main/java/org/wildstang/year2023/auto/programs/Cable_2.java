package org.wildstang.year2023.auto.programs;

import java.util.List;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.PathHeadingStep;
import org.wildstang.framework.auto.steps.SetGyroStep;
import org.wildstang.framework.auto.steps.SwervePathFollowerStep;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.steps.ClawRelease;
import org.wildstang.year2023.auto.steps.IntakeCube;
import org.wildstang.year2023.auto.steps.MoveArm;
import org.wildstang.year2023.auto.steps.SetAutoDriveStep;
import org.wildstang.year2023.auto.steps.WaitForHeading;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Cable_2 extends AutoProgram{

    private boolean color = true;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("Cable_2", new PathConstraints(3.0, 3.0));

        color = (DriverStation.getAlliance() == Alliance.Blue);
        
        //score preload
        addStep(new SetAutoDriveStep());
        addStep(new SetGyroStep(180, swerve));
        addStep(new PathHeadingStep(180, swerve));
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(false));
        addStep(new AutoStepDelay(500));

        //drive to pickup, move arm to stow, and after 2 seconds deploy cube intake
        AutoParallelStepGroup group0 = new AutoParallelStepGroup();
        group0.addStep(new PathHeadingStep(-2, swerve));
        group0.addStep(new WaitForHeading(-2, swerve));
        group0.addStep(new MoveArm("STOW"));
        group0.addStep(new ClawRelease(true));
        addStep(group0);

        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new SwervePathFollowerStep(pathGroup.get(0), swerve, color));
        AutoSerialStepGroup subgroup1_1 = new AutoSerialStepGroup();
        subgroup1_1.addStep(new AutoStepDelay(1500));
        subgroup1_1.addStep(new IntakeCube(true));
        group1.addStep(subgroup1_1);
        addStep(group1);

        addStep(new AutoStepDelay(800));
        
        //stow intake and drive to cube node
        AutoParallelStepGroup group2 = new AutoParallelStepGroup();
        group2.addStep(new PathHeadingStep(180, swerve));
        group2.addStep(new WaitForHeading(189, swerve));
        group2.addStep(new ClawRelease(true));
        group2.addStep(new IntakeCube(false)); // stow intake
        addStep(group2);

        AutoParallelStepGroup group3 = new AutoParallelStepGroup();
        group3.addStep(new SwervePathFollowerStep(pathGroup.get(1), swerve, color));
        AutoSerialStepGroup subgroup3_1 = new AutoSerialStepGroup();
        subgroup3_1.addStep(new AutoStepDelay(1500));
        subgroup3_1.addStep(new MoveArm("MID"));
        group3.addStep(subgroup3_1);
        addStep(group3);

        // score cube
        addStep(new ClawRelease(false));
        addStep(new AutoStepDelay(500));

        //move arm to stow
        AutoParallelStepGroup group4 = new AutoParallelStepGroup();
        group4.addStep(new MoveArm("STOW"));
        group4.addStep(new ClawRelease(true));
        addStep(group4);

    }

    @Override
    public String toString() {
        return "Cable_2";
    }
    
}
