package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.RollerClaw;

public class ClawRelease extends AutoStep {

    private RollerClaw claw;
    boolean release;
    double speed;

    public ClawRelease (boolean release){
        this.release = release;
        this.speed = 0;
    }

    public ClawRelease (boolean release, double speed) {
        this.release = release;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        claw = (RollerClaw) Core.getSubsystemManager().getSubsystem(WSSubsystems.ROLLER_CLAW.getName());
        
    }

    @Override
    public void update() {
        if (release){
            claw.setGripper(false);
        } else {
            claw.setGripper(true);
        }
        claw.setSpeed(speed);
        this.setFinished(true);
        
    }

    @Override
    public String toString() {
        
        return "Claw Release";
    }
    
}