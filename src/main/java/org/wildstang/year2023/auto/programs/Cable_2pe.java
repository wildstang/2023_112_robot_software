package org.wildstang.year2023.auto.programs;

import java.util.List;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.SwervePathFollowerStep;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.steps.PickupStep;
import org.wildstang.year2023.auto.steps.ScoringStep;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

public class Cable_2pe extends AutoProgram{

    private boolean color = false;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("Cable_2pe", new PathConstraints(4.0, 3.0));

        //score preload
        addStep(new ScoringStep(HEIGHT.MID, "Cone Mid"));

        //drive to pickup, move arm to stow, and after 2 seconds deploy cube intake
        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new SwervePathFollowerStep(pathGroup.get(0), swerve, color));
        AutoSerialStepGroup subgroup1_1 = new AutoSerialStepGroup();
        AutoParallelStepGroup subsubgroup1_1_1 = new AutoParallelStepGroup();
        subsubgroup1_1_1.addStep(new ScoringStep(HEIGHT.STOW, "Hold")); // move arm to stow
        subsubgroup1_1_1.addStep(new AutoStepDelay(2000)); // make sure we wait at least 2 seconds before deploying the intake
        subgroup1_1.addStep(subsubgroup1_1_1);
        subgroup1_1.addStep(new PickupStep("Cube", false));
        group1.addStep(subgroup1_1);
        addStep(group1);
        
        //stow intake and drive to cube node
        AutoParallelStepGroup group2 = new AutoParallelStepGroup();
        group2.addStep(new SwervePathFollowerStep(pathGroup.get(1), swerve, color));
        group2.addStep(new PickupStep("Cube", true)); // stow intake
        addStep(group2);

        addStep(new ScoringStep(HEIGHT.MID, "Cube"));

        AutoParallelStepGroup group3 = new AutoParallelStepGroup();
        group3.addStep(new SwervePathFollowerStep(pathGroup.get(1), swerve, color));
        group3.addStep(new ScoringStep(HEIGHT.STOW, "Hold"));

    }

    @Override
    public String toString() {
        return "Cable_2pe";
    }
    
}
