package org.wildstang.year2023.robot;

import org.wildstang.framework.core.AutoPrograms;
import org.wildstang.year2023.auto.programs.AutoBalanceProgram;
import org.wildstang.year2023.auto.programs.Cable_1p1;
import org.wildstang.year2023.auto.programs.Cable_1p1e;
import org.wildstang.year2023.auto.programs.Cable_2;
import org.wildstang.year2023.auto.programs.Cable_2pe;
import org.wildstang.year2023.auto.programs.Center_1pe;
import org.wildstang.year2023.auto.programs.Center_1pme;
import org.wildstang.year2023.auto.programs.Top_1p1;
import org.wildstang.year2023.auto.programs.Top_1p1e;
import org.wildstang.year2023.auto.programs.Top_2;
import org.wildstang.year2023.auto.programs.Top_2pe;

/**
 * All active AutoPrograms are enumerated here.
 * It is used in Robot.java to initialize all programs.
 */
public enum WSAutoPrograms implements AutoPrograms {

    // enumerate programs
    // TEST_PROGRAM("Sample", SampleAutoProgram.class),
    CABLE_1P1("Cable_1p1", Cable_1p1.class),
    CABLE_1P1E("Cable_1p1e", Cable_1p1e.class),
    CABLE_2("Cable_2", Cable_2.class),
    CABLE_2PE("Cable_2pe", Cable_2pe.class),
    CENTER_1PE("Center_1pe",Center_1pe.class),
    CENTER_1PME("Center_1pme",Center_1pme.class),
    TOP_1P1("Top_1p1",Top_1p1.class),
    TOP_1P1E("Top_1p1e",Top_1p1e.class),
    TOP_2("Top_2",Top_2.class),
    TOP_2PE("Top_2pe",Top_2pe.class),
    AUTOBALANCEPROGRAM("AutoBalanceProgram", AutoBalanceProgram.class),
    ;

    /**
     * Do not modify below code, provides template for enumerations.
     * We would like to have a super class for this structure, however,
     * Java does not support enums extending classes.
     */
    
    private String name;
    private Class<?> programClass;

    /**
     * Initialize name and AutoProgram map.
     * @param name Name, must match that in class to prevent errors.
     * @param programClass Class containing AutoProgram
     */
    WSAutoPrograms(String name, Class<?> programClass) {
        this.name = name;
        this.programClass = programClass;
    }

    /**
     * Returns the name mapped to the AutoProgram.
     * @return Name mapped to the AutoProgram.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns AutoProgram's class.
     * @return AutoProgram's class.
     */
    @Override
    public Class<?> getProgramClass() {
        return programClass;
    }
}