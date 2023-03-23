package org.wildstang.year2023.subsystems.targeting;

// ton of imports
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.framework.io.inputs.Input;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AimHelper implements Subsystem {
    
    private NetworkTable table;
    private NetworkTableEntry camPose; // x angle
    private NetworkTableEntry tv;

    private boolean isBlue;

    public double[] botpose;
    public double latency;
    public boolean TargetInView;

    @Override
    public void inputUpdate(Input source) {
        
    }

    @Override
    public void init() {
        table = NetworkTableInstance.getDefault().getTable("limelight-plusone");
        camPose = table.getEntry("botpose");
        tv = table.getEntry("tv");
        resetState();
    }

    @Override
    public void selfTest() {        
    }

    public void calcBotpose() {
        //read values periodically
        TargetInView = (double) tv.getNumber(0.0) == 1.0;
        if (TargetInView) {
            botpose = camPose.getDoubleArray(new double[6]);
            latency = Timer.getFPGATimestamp() - (botpose[6]/1000.0);
            
            SmartDashboard.putNumber("LimelightX", botpose[0]);
            SmartDashboard.putNumber("LimelightY", botpose[1]);
            SmartDashboard.putNumber("LimelightLatency", latency);
        }

        SmartDashboard.putBoolean("limelight target acquired", TargetInView);
    }

    @Override
    public void update() {
        calcBotpose();
    }

    @Override
    public void resetState() {
        isBlue = DriverStation.getAlliance() == Alliance.Blue;
        if (isBlue) {
            camPose = table.getEntry("botpose_wpiblue");
        } else {
            camPose = table.getEntry("botpose_wpired");
        }
        TargetInView = false; //is the target in view? only updated when calcTargetCoords is called.
    }

    @Override
    public String getName() {
        return "Aim Helper";
    }  
}