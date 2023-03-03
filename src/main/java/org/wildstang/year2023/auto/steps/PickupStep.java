package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.arm.Arm;
import org.wildstang.year2023.subsystems.arm.Arm.HEIGHT;
import org.wildstang.year2023.subsystems.RollerClaw;
import org.wildstang.year2023.subsystems.CubeIntake;


public class PickupStep extends AutoStep {

private boolean isStow;
private String objectType;
private Arm arm;
private RollerClaw rollerClaw;
private CubeIntake cubeIntake;
    
    public PickupStep(String gamePiece, boolean stow){
        this.objectType = gamePiece;
        this.isStow = stow;
    }

    @Override 
    public void initialize(){
        arm = (Arm) Core.getSubsystemManager().getSubsystem(WSSubsystems.ARM);
        rollerClaw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW);
        cubeIntake = (CubeIntake) Core.getSubsystemManager().getSubsystem(WSSubsystems.CUBE_INTAKE);
    }

    @Override
    public void update(){

        if(objectType == "Cube"){

            if(isStow == false){
                cubeIntake.autoIntake(true);
            }
            else if(isStow == true){
                cubeIntake.autoIntake(false);
            }

        }
        else if (objectType == "Cone"){
            
            if(isStow == false){
                arm.autoPosition(HEIGHT.LOW);
                if(arm.isAtTarget()){
                    rollerClaw.autoScore("Hold");
                }
            }
            else if (isStow == true){
                arm.autoPosition(HEIGHT.STOW);
                
            }
        }
        if(arm.isAtTarget()){
            this.setFinished();
        }

    }

    @Override
    public String toString() {
        return "PickupStep";
    }

}
