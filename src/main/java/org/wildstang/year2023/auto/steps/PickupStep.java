package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.PathFollowerStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import frc.paths.*;
import org.wildstang.year2023.subsystems.arm.Arm;
import org.wildstang.year2023.subsystems.RollerClaw;
import org.wildstang.year2023.subsystems.CubeIntake;


public class PickupStep extends AutoStep {

private boolean isStow;
private String objectType;
private Arm Arm;
private RollerClaw RollerClaw;
private CubeIntake CubeIntake;
    
    public PickupStep(String type, boolean stow){
        this.objectType = type;
        this.isStow = stow;
    }

    @Override 
    public void initialize(){
        Arm = (Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM);
        RollerClaw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW);
        CubeIntake = (CubeIntake) Core.getSubsystemManager().getSubsystem(WSSubsystems.CUBE_INTAKE);
    }

    @Override
    public void update(){

        if(objectType == "Cube"){

            if(isStow == false){
                CubeIntake.AutoIntake(true);
            }
            else if(isStow == true){
                CubeIntake.AutoIntake(false);
            }

        }
        else if (objectType == "Cone"){
            
            if(isStow == false){
                Arm.AutoPosition("Low");
                if(Arm.isAtTarget()){
                    RollerClaw.AutoScore("Hold");
                }
            }
            else if (isStow == true){
                Arm.AutoPosition("Stow");
            }
        }
        this.setFinished();

    }

    @Override
    public String toString() {
        return "Pickup Step";
    }

}
