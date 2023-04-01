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
import org.wildstang.year2023.auto.steps.AutoLightShow;
import org.wildstang.year2023.auto.steps.ClawRelease;
import org.wildstang.year2023.auto.steps.InitializeOdometry;
import org.wildstang.year2023.auto.steps.IntakeCube;
import org.wildstang.year2023.auto.steps.MoveArm;
import org.wildstang.year2023.auto.steps.SetAutoDriveStep;
import org.wildstang.year2023.auto.steps.WaitForHeading;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Cable_2 extends AutoProgram{

    private boolean color = true;

    @Override
    protected void defineSteps() {
        SwerveDrive swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);

        List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("Cable_2", new PathConstraints(3.5, 3.0));

        color = (DriverStation.getAlliance() == Alliance.Blue);
        
        //score preload
        addStep(new SetGyroStep(180, swerve));
        addStep(new InitializeOdometry(new Translation2d(14.8,0.47), Math.PI));
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(false));
        addStep(new AutoStepDelay(500));

        //drive to pickup, move arm to stow, and after 2 seconds deploy cube intake
        AutoParallelStepGroup group0 = new AutoParallelStepGroup("Realign and stow");
        group0.addStep(new PathHeadingStep(-2, swerve));
        group0.addStep(new SetAutoDriveStep());
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
        group2.addStep(new WaitForHeading(180, swerve));
        addStep(group2);

        AutoParallelStepGroup group3 = new AutoParallelStepGroup();
        group3.addStep(new SwervePathFollowerStep(pathGroup.get(1), swerve, color));
        // AutoSerialStepGroup subgroup3_1 = new AutoSerialStepGroup();
        // subgroup3_1.addStep(new AutoStepDelay(1500));
        // group3.addStep(subgroup3_1);
        AutoSerialStepGroup subgroup3_2 = new AutoSerialStepGroup();
        //subgroup3_2.addStep(new AutoStepDelay(250));
        subgroup3_2.addStep(new IntakeCube(false)); // stow intake
        // NOTE: moved this down 1
        subgroup3_2.addStep(new AutoStepDelay(750));
        subgroup3_2.addStep(new ClawRelease(true));
        subgroup3_2.addStep(new AutoStepDelay(750));
        subgroup3_2.addStep(new MoveArm("MID"));
        addStep(group3);
 
        // score cube
        addStep(new ClawRelease(false, -0.3));
        addStep(new AutoStepDelay(500));

        //move arm to stow
        AutoParallelStepGroup group4 = new AutoParallelStepGroup();
        group4.addStep(new MoveArm("STOW"));
        AutoSerialStepGroup subgroup4_1 = new AutoSerialStepGroup();
        subgroup4_1.addStep(new AutoStepDelay(1200));
        subgroup4_1.addStep(new ClawRelease(true, 0));
        // subgroup1_1.addStep(new PathHeadingStep(0, swerve));
        addStep(group4);

        addStep(new AutoLightShow(true));

    }

    @Override
    public String toString() {
        return "Cable_2";
    }
    
}
