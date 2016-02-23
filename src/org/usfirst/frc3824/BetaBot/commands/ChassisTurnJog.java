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

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc3824.BetaBot.Robot;
import org.usfirst.frc3824.BetaBot.Constants;

/**
 *
 */
public class ChassisTurnJog extends Command
{
	private Timer m_WatchdogTimer;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_DirectionAngle;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public ChassisTurnJog(double DirectionAngle)
	{
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_DirectionAngle = DirectionAngle;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.chassis);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		
		// Create an instance of the timer
        m_WatchdogTimer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize()
	{
		double rightWheel = 0.0;
		double leftWheel  = 0.0;
		
		// Reset the wheel encoder values
		Robot.chassis.resetEncoders();
		
		// Determine if the jog if clockwise
		if (m_DirectionAngle > 0.0)
		{
			leftWheel   =  Constants.JOG_TURN_WHEEL_POWER;
			rightWheel  = -Constants.JOG_TURN_WHEEL_POWER;
		}
		else if (m_DirectionAngle < 0.0)  // counter clockwise
		{
			rightWheel =  Constants.JOG_TURN_WHEEL_POWER;
			leftWheel  = -Constants.JOG_TURN_WHEEL_POWER;
		}
				
		// Set the power based on the direction
		Robot.chassis.setWheelOutput(rightWheel, leftWheel);
		
		// Reset and start the on watch dog timer
		m_WatchdogTimer.reset();
		m_WatchdogTimer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished()
	{
		// Check the watch dog timer
		if (m_WatchdogTimer.get() > Constants.JOG_TURN_WATCHDOG_TIME)
			return(true);
		
		// determine if the encoder value is reached
		if (m_DirectionAngle > 0.0)
		{
			// Get the Left chassis encoder values		
			if (Robot.chassis.getLeftEncoder() > Constants.JOG_TURN_ENCODER_TURN_VALUE)
				return(true);
		}
		else if (m_DirectionAngle < 0.0)
		{
			// Get the Right chassis encoder values
			if (Robot.chassis.getRightEncoder() > Constants.JOG_TURN_ENCODER_TURN_VALUE)
				return(true);
		}
		else  // No turn direction given
			return(true);
		
		// Not completed the move
		return false;
	}

	// Called once after isFinished returns true
	protected void end()
	{
		// Stop the robot and disable the watchdog timer
		Robot.chassis.stop();
		m_WatchdogTimer.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{
		// call the end method
		this.end();
	}
}
