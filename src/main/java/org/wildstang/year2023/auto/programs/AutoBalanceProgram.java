package org.wildstang.year2023.auto.programs;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.year2023.auto.steps.AutoBalance;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoBalanceProgram extends AutoProgram{

    @Override
    protected void defineSteps() {
        // TODO Auto-generated method stub
        addStep(new AutoBalance());
        
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "AutoBalanceProgram";
    }
    
}
