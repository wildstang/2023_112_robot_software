package org.wildstang.year2023.auto.programs;

import java.util.List;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.SwervePathFollowerStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.steps.PickupStep;
import org.wildstang.year2023.auto.steps.ScoringStep;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

public class Cable_1p1 extends AutoProgram{

    private boolean color = false;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("Cable_1p1", new PathConstraints(4.0, 3.0));

        addStep(new ScoringStep(HEIGHT.MID, "Cone Mid"));

        AutoParallelStepGroup group1 = new AutoParallelStepGroup();
        group1.addStep(new SwervePathFollowerStep(pathGroup.get(0), swerve, color));
        group1.addStep(new PickupStep("Cube", true));
        addStep(group1);

        AutoParallelStepGroup group2 = new AutoParallelStepGroup();
        group2.addStep(new PickupStep("Cube", false));
        group2.addStep(new SwervePathFollowerStep(pathGroup.get(1), swerve, color));
        addStep(group2);
        
    }

    @Override
    public String toString() {
        return "Cable_1p1";
    }
    
}
