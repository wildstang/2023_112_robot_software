package org.wildstang.year2023.auto;

import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.AutoStep;
import org.wildstang.framework.auto.steps.PathFollowerStep;
import org.wildstang.framework.core.Core;
import org.wildstang.year2023.auto.Steps.ClawRelease;
import org.wildstang.year2023.auto.Steps.IntakeCubeStep;
import org.wildstang.year2023.auto.Steps.MoveArm;
import org.wildstang.year2023.auto.Steps.OuttakeCubeStep;
import org.wildstang.year2023.robot.WSSubsystems;
import frc.paths.*;

/**
 * Sample auto program that just waits 10 seconds before finishing.
 * @author Liam
 */
public class SampleAutoProgram extends AutoProgram {

    

    @Override
    protected void defineSteps() {
        //Scores cone Preload
        addStep(new MoveArm("MID"));
        addStep(new ClawRelease(true));
        addStep(new ClawRelease(false));
        addStep(new MoveArm("STOW"));

        //Pickup cube from field
        addStep(new IntakeCubeStep(true));
        addStep(new IntakeCubeStep(false));
        
        //score cube
        addStep(new OuttakeCubeStep(true));
        addStep(new OuttakeCubeStep(false));
    }

    @Override
    public String toString() {
        return "Sample";
    }
    
}