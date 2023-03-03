package org.wildstang.year2023.robot;

import org.wildstang.framework.core.Subsystems;
import org.wildstang.year2023.subsystems.swerve.SwerveDrive;
import org.wildstang.year2023.subsystems.RollerClaw;
import org.wildstang.year2023.subsystems.arm.Arm;
// import org.wildstang.year2023.subsystems.targeting.AimHelper;
import org.wildstang.year2023.subsystems.CubeIntake;
import org.wildstang.year2023.subsystems.LedController;

/**
 * All subsystems are enumerated here.
 * It is used in Robot.java to initialize all subsystems.
 */
public enum WSSubsystems implements Subsystems {

    // enumerate subsystems
    SWERVE_DRIVE("Swerve Drive", SwerveDrive.class),
    ARM("Arm", Arm.class),
    ROLLER_CLAW("Roller Claw", RollerClaw.class),
    CUBE_INTAKE("Cube Intake", CubeIntake.class),
    LED("leds", LedController.class)
    //AIM_HELPER("Aim Helper", AimHelper.class),
    //SAMPLE("Sample", SampleSubsystem.class)
    ;

    /**
     * Do not modify below code, provides template for enumerations.
     * We would like to have a super class for this structure, however,
     * Java does not support enums extending classes.
     */
    
    private String name;
    private Class<?> subsystemClass;

    /**
     * Initialize name and Subsystem map.
     * @param name Name, must match that in class to prevent errors.
     * @param subsystemClass Class containing Subsystem
     */
    WSSubsystems(String name, Class<?> subsystemClass) {
        this.name = name;
        this.subsystemClass = subsystemClass;
    }

    /**
     * Returns the name mapped to the subsystem.
     * @return Name mapped to the subsystem.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns subsystem's class.
     * @return Subsystem's class.
     */
    @Override
    public Class<?> getSubsystemClass() {
        return subsystemClass;
    }
}