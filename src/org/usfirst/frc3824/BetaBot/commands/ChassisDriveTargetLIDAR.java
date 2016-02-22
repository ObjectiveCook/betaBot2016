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

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.Robot;

/**
 *
 */
public class ChassisDriveTargetLIDAR extends Command
{
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_TargetDistance;
	
	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public ChassisDriveTargetLIDAR(double TargetDistance)
	{
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_TargetDistance = TargetDistance;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.chassis);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}
	
	//-------------------------------------------------------------------------
	// NOTE: This currently drives forward (or backward) until the robot is at
	// the distance from the target specified by 'TargetDistance'.  It does NOT
	// drive 'TargetDistance' forward.  
	// IDEAS: 
	//   - use PID to keep it driving straight - done
	//   - use PID to control the speed 
	//-------------------------------------------------------------------------
	protected void initialize()
	{
		// Determine the PID direction and power
		boolean driveForward = (Robot.chassis.getLidarDistanceCentimeters() - m_TargetDistance > 0.0);

		// Set the PID up for driving straight
		Robot.chassis.configurePIDs(Constants.DRIVETRAIN_DRIVE_STRAIGHT_P, 
		                            Constants.DRIVETRAIN_DRIVE_STRAIGHT_I, 
		                            Constants.DRIVETRAIN_DRIVE_STRAIGHT_D, 
		                            Robot.chassis.getCurrentHeading(), 0.0, 
		                            0.8 * (driveForward?1.0:-1.0));
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		// make sure we're driving in the correct direction
		double distanceDelta = (Robot.chassis.getLidarDistanceCentimeters() - m_TargetDistance);
		boolean driveForward = (distanceDelta > 0.0);
		
		if (distanceDelta < 50.0)
		{
			Robot.chassis.setMagnitude( 0.4 * (driveForward?1.0:-1.0) );
		}
		else
		{
			Robot.chassis.setMagnitude( 0.8 * (driveForward?1.0:-1.0) );
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished()
	{
		// might overshoot here
		return (Math.abs(m_TargetDistance - Robot.chassis.getLidarDistanceCentimeters()) < 5.0); 
	}

	// Called once after isFinished returns true
	protected void end()
	{
		Robot.chassis.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{
		end();
	}
}
