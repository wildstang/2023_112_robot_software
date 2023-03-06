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
// import org.wildstang.year2023.auto.steps.PickupStep;
// import org.wildstang.year2023.auto.steps.ScoringStep;
import org.wildstang.year2023.robot.WSSubsystems;
// import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;

public class Cable_1p1 extends AutoProgram{

    private boolean color = true;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        //score preload
        // addStep(new ScoringStep(HEIGHT.MID, "Cone Mid"));
        addStep(new SetGyroStep(180, swerve));
        addStep(new PathHeadingStep(180, swerve));
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(false));
        addStep(new AutoStepDelay(500));
        // AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        // group1.addStep(new MoveArm("STOW"));
        // group1.addStep(new ClawRelease(true));
        // addStep(group1);

        // addStep(new IntakeCube(true));
        // addStep(new AutoStepDelay(2000));
        // addStep(new IntakeCube(false));
        //drive to pickup, move arm to stow, and after 2 seconds deploy cube intake
        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new SwervePathFollowerStep(PathPlanner.loadPath("Cable_1p1", new PathConstraints(4.0, 3.0)), swerve, color));
        AutoSerialStepGroup subgroup1_1 = new AutoSerialStepGroup();
        AutoParallelStepGroup subsubgroup1_1_1 = new AutoParallelStepGroup();
        subsubgroup1_1_1.addStep(new ClawRelease(true));
        subsubgroup1_1_1.addStep(new MoveArm("STOW")); // move arm to stow
        subsubgroup1_1_1.addStep(new AutoStepDelay(2000)); // make sure we wait at least 2 seconds before deploying the intake
        subgroup1_1.addStep(subsubgroup1_1_1);
        subgroup1_1.addStep(new IntakeCube(true));
        group1.addStep(subgroup1_1);
        AutoSerialStepGroup subGroup1_2 = new AutoSerialStepGroup();
        subGroup1_2.addStep(new AutoStepDelay(1500));
        subGroup1_2.addStep(new PathHeadingStep(0, swerve));
        group1.addStep(subGroup1_2);
        addStep(group1);

        addStep(new AutoStepDelay(1000));
        
        addStep(new IntakeCube(false));
    }

    @Override
    public String toString() {
        return "Cable_1p1";
    }
    
}
