package org.wildstang.year2023.auto.programs;

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
// import org.wildstang.year2023.auto.steps.PickupStep;
// import org.wildstang.year2023.auto.steps.ScoringStep;
import org.wildstang.year2023.robot.WSSubsystems;
// import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Top_1p1 extends AutoProgram{

    private boolean color = true;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        color = (DriverStation.getAlliance() == Alliance.Blue);
        
        //score preload
        addStep(new SetAutoDriveStep());
        addStep(new SetGyroStep(180, swerve));
        addStep(new PathHeadingStep(180, swerve));
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(false));
        addStep(new AutoStepDelay(500));

        //rotate to heading, stow arm
        AutoParallelStepGroup group0 = new AutoParallelStepGroup();
        group0.addStep(new PathHeadingStep(0, swerve));
        group0.addStep(new WaitForHeading(0,swerve));
        group0.addStep(new MoveArm("STOW"));
        group0.addStep(new ClawRelease(true));
        addStep(group0);

        //drive to pickup, and after 1.5 seconds deploy cube intake
        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new SwervePathFollowerStep(PathPlanner.loadPath("Top_1p1", new PathConstraints(4.0, 3.0)), swerve, color));
        AutoSerialStepGroup subgroup1_1 = new AutoSerialStepGroup();
        subgroup1_1.addStep(new AutoStepDelay(1500));
        subgroup1_1.addStep(new IntakeCube(true));
        group1.addStep(subgroup1_1);
        addStep(group1);

        addStep(new AutoStepDelay(800));
        
        addStep(new IntakeCube(false));
    }

    @Override
    public String toString() {
        return "Top_1p1";
    }
    
}
