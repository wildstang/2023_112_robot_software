package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.PathFollowerStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import frc.paths.*;
import org.wildstang.year2023.subsystems.arm.Arm;
import org.wildstang.year2023.subsystems.RollerClaw;


public class ScoringStep extends AutoStep {
    
private String armHeight;
private String scoringMode;
private Arm Arm;
private RollerClaw RollerClaw;

    public ScoringStep(String height, String mode){
        this.armHeight = height;
        this.scoringMode = mode;
    }

    @Override 
    public void initialize(){
        Arm = (Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM);
        RollerClaw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW);
    }

    @Override
    public void update(){
        Arm.AutoPosition(armHeight);
        if (Arm.isAtTarget()){
            RollerClaw.AutoScore(scoringMode);
        }
        this.setFinished();
    }   

    @Override
    public String toString() {
        return "Scoring Step";
    }





}
