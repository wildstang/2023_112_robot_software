package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.robot.WSSubsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

public class SetAutoDriveStep extends AutoStep{

    private SwerveDrive swerve;

    @Override
    public void initialize() {
        swerve = (SwerveDrive) Core.getSubsystemManager().getSubsystem(WSSubsystems.SWERVE_DRIVE);
        swerve.setToAuto();
    }

    @Override
    public void update() {
        setFinished();
        
    }

    @Override
    public String toString() {
        return "SetAutoDriveStep";
    }
    
}
