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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.Robot;

/**
 *
 */
public class ChassisTurnToImageTarget extends Command 
{
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private int m_whichTarget;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public ChassisTurnToImageTarget(int whichTarget) 
    {
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_whichTarget = whichTarget;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        m_Timer = new Timer();
    }
    
    // Declare constants for the desired target
    public static final int kTargetLeft   = 0;
    public static final int kTargetCenter = 1;
    public static final int kTargetRight  = 2;
    
    private int   m_state;
    private Timer m_Timer;  

    /**
     * Class default constructor
     */
	public ChassisTurnToImageTarget()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		
		m_whichTarget = kTargetCenter;
        m_Timer       = new Timer();
	}

	/**
	 * Called just before this Command runs the first time
	 */
	protected void initialize()
	{
		double heading      = Robot.chassis.getCurrentHeading();
		double targetOffset = Robot.targets.getTargetOffsetFromCenterAngle(m_whichTarget);
		
		// Set the PID up for driving straight
		Robot.chassis.configurePIDs(Preferences.getInstance().getDouble("ImageTurn_P", Constants.DRIVETRAIN_DRIVE_STRAIGHT_P), 
									Preferences.getInstance().getDouble("ImageTurn_I", Constants.DRIVETRAIN_DRIVE_STRAIGHT_I), 
									Preferences.getInstance().getDouble("ImageTurn_D", Constants.DRIVETRAIN_DRIVE_STRAIGHT_D), 
									heading + targetOffset, 0.0, 0.0);	

		SmartDashboard.putNumber("ImageTurn Angle SetPoint", Robot.chassis.getHeadingSetpoint());
		SmartDashboard.putNumber("ImageTurn Target Offset", targetOffset);

		// Initialize the state machine state
		m_state = 1;

		// Reset and start the on target timer
		m_Timer.reset();
		m_Timer.start();
	}

	/**
	 * Called repeatedly when this Command is scheduled to run
	 * 
	 * State 1: Angle tolerance < 7.0
	 * State 2: Angle tolerance < 2.5
	 * State 3: Complete
	 */
	protected void execute()
	{
		double offsetAngle = 0;
		double error       = Math.abs(Robot.chassis.getPID_Error());
		
		// Determine the image offset angle
		offsetAngle = Robot.targets.getTargetOffsetFromCenterAngle(m_whichTarget);

		// The first state uses a large acceptance angle
		if ((m_state == 1) && (error < 7.0))
		{
			// Ensure on target error holds for the specified time
			if (m_Timer.get() > 1.0)
			{
				// Stop is error is less than 2.5
				if (error < 2.5)
				{
					// Complete so set the state to three
					m_state = 3;
				}
				else
				{
					// Go to the next state
					m_state++;
					
					// Update the heading PID with the new gyro heading and target offset angle
					Robot.chassis.setPID_Heading(Robot.chassis.getCurrentHeading() + offsetAngle);
					
					// Reset the timer to hold the next error tolerance
					m_Timer.reset();
				}
			}
		}
		
		// State 2 is for an error less than 2.5 degrees
		else if ((m_state == 2) && (error < 2.5))
		{
			// Ensure on target error holds for the specified time
			if (m_Timer.get() > 1.0)
			{
				// Complete so set the state to three
				m_state = 3;
			}
		}
		else  // Error is greater than any state acceptance
		{
			m_Timer.reset();
		}
				
		SmartDashboard.putNumber("ImageTurn Current Angle", Robot.chassis.getCurrentHeading());
		SmartDashboard.putNumber("ImageTurn PID Error", Robot.chassis.getPID_Error());
		SmartDashboard.putNumber("ImageTurn OffsetFromCenterAngle", offsetAngle);
		SmartDashboard.putNumber("ImageTurn State", m_state);
		SmartDashboard.putNumber("ImageTurn Timer", m_Timer.get());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished()
	{
		if (m_state == 3)
			return(true);
		else
			return(false);
	}

	// Called once after isFinished returns true
	protected void end()
	{
		// disable the PID and stop the robot
		Robot.chassis.disablePIDs();
		Robot.chassis.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{
		// call the end method
		this.end();
	}
}
