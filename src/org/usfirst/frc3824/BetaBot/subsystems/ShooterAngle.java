// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3824.BetaBot.subsystems;

import org.usfirst.frc3824.BetaBot.RobotMap;
import org.usfirst.frc3824.BetaBot.commands.*;
import org.usfirst.frc3824.BetaBot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedController;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterAngle extends PIDSubsystem
{
	double m_PresentShooterAngle;
	double m_MinShooterHeight;
	double m_MaxShooterHeight;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AnalogPotentiometer analogPotentiometer = RobotMap.shooterAngleAnalogPotentiometer;
    private final SpeedController speedController = RobotMap.shooterAngleSpeedController;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// Initialize your subsystem here
	public ShooterAngle()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("ShooterAngle", 10.0, 0.0, 0.0);
        setAbsoluteTolerance(0.02);
        getPIDController().setContinuous(false);
        LiveWindow.addActuator("Shooter Angle", "PIDSubsystem Controller", getPIDController());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID

		// Use these to get going:
		// setSetpoint() - Sets where the PID controller should move the system
		// to
		// enable() - Enables the PID controller.

		// Read the minimum and maximum shooter parameters
		m_MinShooterHeight = Preferences.getInstance().getDouble("Min Shooter Height", Constants.SHOOTER_ELEVATION_SETPOINT_MIN);
		m_MaxShooterHeight = Preferences.getInstance().getDouble("Max Shooter Height", Constants.SHOOTER_ELEVATION_SETPOINT_MAX);
	}

	/**
	 * 
	 */
	public void initDefaultCommand()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	}

	/**
	 * 
	 */
	protected double returnPIDInput()
	{
		// Return your input value for the PID loop
		// e.g. a sensor, like a potentiometer:
		// yourPot.getAverageVoltage() / kYourMaxVoltage;

		SmartDashboard.putNumber("Pot Position", analogPotentiometer.get());
		SmartDashboard.putNumber("Pot Setpoint", getSetpoint());

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return analogPotentiometer.get();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
	}

	/**
	 * 
	 */
	protected void usePIDOutput(double output)
	{
		// Use output to drive your system, like a motor
		// e.g. yourMotor.set(output);

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        speedController.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
	}

	/**
	 * Routine to enable or disable the shooter elevation PID
	 */
	public void setShooterElevationEnabled(boolean state)
	{
		// Determine if the shooter elevation PID should be enabled or disabled
		if (state == true)
		{
			enable();
		}
		else
		{
			disable();
		}
	}

	/**
	 * Routine to set the shooter elevation setpoint in degrees
	 */
	public void setShooterElevationSetpoint(double setpointDegrees)
	{
		// Ensure the set point is not below the specified lowest level
		if (setpointDegrees < m_MinShooterHeight)
		{
			setpointDegrees = m_MinShooterHeight;
		}
		
		// Ensure the set point is not above the specified highest level
		if (setpointDegrees > m_MaxShooterHeight)
		{
			setpointDegrees = m_MaxShooterHeight;
		}
		
		// Remember the setpoint
		m_PresentShooterAngle = setpointDegrees;
				
		// Convert degrees to Potentiometer value
		// Potentiometer Set Point = -0.00004(X^2) +0.011X+0.1956
		double setPoint = -0.00004* (setpointDegrees * setpointDegrees) +0.011 * setpointDegrees +0.1956;
		
		SmartDashboard.putNumber("Angle Setpoint", setpointDegrees);
		
		// Set the shooter elevation set point
		setSetpoint(setPoint);
	}
	
	/**
	 * 
	 */
	public double GetShooterElevatorAngle()
	{
		double angle;
		
		// Get the shooter elevator potentiometer position
		angle = analogPotentiometer.get();
		
		// Angle = 58.01 X^2 + 64.95 X - 15.5
		angle = 58.01 * (angle * angle) + (64.95 * angle) - 15.5;
		
		// Return the shooter elevator angle in degrees
		return(angle);
	}
	
	/**
	 * Method to get the present shooter angle set point
	 */
	public double GetShooterAngleSetPoint()
	{
	   return(m_PresentShooterAngle);
	}	
}
