package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.arm.Arm;
import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.RollerClaw;


public class ScoringStep extends AutoStep {
    
private HEIGHT armHeight;
private String scoringMode;
private Arm arm;
private RollerClaw rollerClaw;

    public ScoringStep(HEIGHT height, String mode){
        this.armHeight = height;
        this.scoringMode = mode;
    }

    @Override 
    public void initialize(){
        arm = (Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM);
        rollerClaw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW);
    }

    @Override
    public void update(){
        arm.autoPosition(armHeight);
        if (arm.isAtTarget()){
            rollerClaw.autoScore(scoringMode);
            this.setFinished();
        }
    }   

    @Override
    public String toString() {
        return "ScoringStep";
    }





}
