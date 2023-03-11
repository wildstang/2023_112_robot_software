package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.LedController;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoLightShow extends AutoStep{

    private boolean run;
    private LedController board;
    

    public AutoLightShow(boolean done){
        run = done;
    }

    @Override
    public void initialize() {

        if (run){
            board = (LedController) Core.getSubsystemManager().getSubsystem(WSSubsystems.LED);
            board.setAuto(true);
        }
    }

    @Override
    public void update() {
        if (DriverStation.getMatchTime() <= 0){
            setFinished();
            board.setAuto(false);
        } 
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "AutoLightShow";
    }
    
}
