package org.wildstang.year2023.auto.programs;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.year2023.auto.steps.AutoLightShow;


public class AutoLightTest extends AutoProgram {

    @Override
    protected void defineSteps() {

        addStep(new AutoLightShow(true));
        // TODO Auto-generated method stub
        
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "AutoLightTest";
    }
    
}
