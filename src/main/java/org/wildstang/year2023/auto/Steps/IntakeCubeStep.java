package org.wildstang.year2023.auto.Steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.CubeIntake;
import org.wildstang.year2023.subsystems.RollerClaw;

public class IntakeCubeStep extends AutoStep {

    private CubeIntake intake;
    private RollerClaw claw;
    private boolean on;

    public IntakeCubeStep (boolean on){
        this.on = on;
    }

    @Override
    public void initialize() {
        intake = (CubeIntake) Core.getSubsystemManager().getSubsystem(WSSubsystems.CUBE_INTAKE.getName());
        claw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW.getName());
        
    }

    @Override
    public void update() {
        if(on){
            claw.setGripper(true);
            claw.setSpeed(.8);
            intake.setDeploy(true);
            intake.setSpeed(1.0);
        } else {
            claw.setGripper(false);
            claw.setSpeed(0);
            intake.setDeploy(false);
            intake.setSpeed(0);
        }
        this.setFinished(true);
    }

    @Override
    public String toString() {
        
        return "Intake cube";
    }
    
}
