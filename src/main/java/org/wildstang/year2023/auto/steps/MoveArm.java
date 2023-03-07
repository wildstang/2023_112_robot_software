package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.arm.*;

public class MoveArm extends AutoStep {

    private double targetPos;
    private Arm arm;

    public MoveArm(double targetPos){
        this.targetPos = targetPos;
    }

    public MoveArm(String target) {
        switch (target) {
            case "LOW":
                this.targetPos = ArmConstants.LOW_POS;
                break;
            case "MID":
                this.targetPos = ArmConstants.MID_POS;
                break;
            case "HIGH":
                this.targetPos = ArmConstants.HIGH_POS;
                break;
            case "STOW":
                this.targetPos = ArmConstants.STOW_POS;
                break;
        }
    }

    @Override
    public void initialize() {
        arm = (Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM.getName());
        
    }

    @Override
    public void update() {
        arm.setPosTarget(targetPos);
        if (arm.isAtTarget()){
            setFinished();
        }
    }

    @Override
    public String toString() {
        return "MoveArm";
    }
    
}