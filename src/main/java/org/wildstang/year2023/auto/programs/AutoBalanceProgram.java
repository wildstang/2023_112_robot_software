package org.wildstang.year2023.auto.programs;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.year2023.auto.steps.AutoBalance;

public class AutoBalanceProgram extends AutoProgram{

    @Override
    protected void defineSteps() {
        addStep(new AutoBalance());
        
    }

    @Override
    public String toString() {
        return "AutoBalanceProgram";
    }
    
}
