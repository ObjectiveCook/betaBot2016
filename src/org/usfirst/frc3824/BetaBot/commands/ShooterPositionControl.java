// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3824.BetaBot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.Robot;

/**
 *
 */
public class ShooterPositionControl extends Command
{
	private double m_ActualShooterSetPoint;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_ShooterSetPoint;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public ShooterPositionControl(double ShooterSetPoint)
	{
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_ShooterSetPoint = ShooterSetPoint;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.shooter);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	protected void initialize()
	{
		System.out.println("Shooter Position initialize");
		
		// Get the present shooter elevator set point and wheel speed
		m_ActualShooterSetPoint = Robot.shooter.GetShooterAngleSetPoint();
		double wheelSpeed = 0.0;

		// Determine if the command was called from a button
		if (m_ShooterSetPoint == Constants.SHOOTER_JOG_UP_BUTTON)
		{ 
			m_ActualShooterSetPoint += 2.0;  // TODO: Change back to 5
			wheelSpeed = Robot.shooter.GetShooterWheelSpeed(); // Keep current Wheel Speed when Jogging
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_JOG_DOWN_BUTTON)
		{ 
			m_ActualShooterSetPoint -= 2.0;  // TODO: Change back to 5
			wheelSpeed = Robot.shooter.GetShooterWheelSpeed();
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_HOME_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter Home", Constants.SHOOTER_ELEVATION_HOME);
		} 		
		else if (m_ShooterSetPoint == Constants.SHOOTER_BOULDER_INTAKE_BUTTON)
		{ 
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter Intake", Constants.SHOOTER_ELEVATION_BOULDER_INTAKE);
			
			// Ball in speed
			wheelSpeed = -1.0;
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_SHOOT_1_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter 1", Constants.SHOOTER_ELEVATION_POSITION1);
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_SHOOT_2_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter 2", Constants.SHOOTER_ELEVATION_POSITION2);
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_SHOOT_3_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter 3", Constants.SHOOTER_ELEVATION_POSITION3);
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_SHOOT_4_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter 4", Constants.SHOOTER_ELEVATION_POSITION4);
		} 
		else if (m_ShooterSetPoint == Constants.SHOOTER_SHOOT_5_BUTTON)
		{
			m_ActualShooterSetPoint = Preferences.getInstance().getDouble("Shooter 5", Constants.SHOOTER_ELEVATION_POSITION5);
		} 
		else
		{
			m_ActualShooterSetPoint = m_ShooterSetPoint;
		}

		// Set the shooter PID set point
		Robot.shooter.setShooterElevationSetpoint(m_ActualShooterSetPoint);

		// Set Wheel Speed
		Robot.shooter.ShooterWheelControl(wheelSpeed);
		
		// Enable the shooter elevation PID
		Robot.shooter.setShooterElevationEnabled(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		SmartDashboard.putNumber("Shooter Angle", Robot.shooter.GetShooterElevatorAngle());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished()
	{
		return true;
	}

	// Called once after isFinished returns true
	protected void end()
	{
		System.out.println("Shooter Position end");
		
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{

	}
}
