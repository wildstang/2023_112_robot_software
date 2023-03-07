package org.wildstang.year2023.auto.steps;

import org.wildstang.framework.auto.AutoStep;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;

public class WaitForHeading extends AutoStep{

    SwerveDrive swerve;
    double goalHeading;

    public WaitForHeading(double goalAngle, SwerveDrive drive){
        this.swerve = drive;
        this.goalHeading = goalAngle;
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        if(Math.abs(swerve.getGyroAngle() -goalHeading)<10 ){
            setFinished();
        }
        
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "WaitForHeading";
    }
    
}
