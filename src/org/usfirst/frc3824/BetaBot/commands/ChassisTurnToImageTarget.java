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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.Robot;
import org.usfirst.frc3824.BetaBot.subsystems.Targets.Target;
import org.usfirst.frc3824.BetaBot.subsystems.Targets.TargetInfo;

/**
 *
 */
public class ChassisTurnToImageTarget extends Command
{
	private Timer   m_shooterTimer;
	private boolean m_finished = false;
	private boolean m_shouldSearchUp     = true;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public ChassisTurnToImageTarget()
	{
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.chassis);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// Initialize the timer
		m_shooterTimer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize()
	{
		// Initialize the encoder based turn PID with the present encoder value of zero
		Robot.chassis.configureEncoderPIDs(Constants.IMAGE_ANGLE_ENCODER_P,
		                                   Constants.IMAGE_ANGLE_ENCODER_I,
		                                   Constants.IMAGE_ANGLE_ENCODER_D,
		                                   Constants.IMAGE_ANGLE_MINIMUM_OUTPUT,
		                                   Constants.IMAGE_ANGLE_MAXIMUM_OUTPUT,
		                                   0, 0.0);

		// reset and start the timer
		m_shooterTimer.reset();
		m_shooterTimer.start();
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		// find target
		TargetInfo foundTarget = Robot.targets.getTargetingInfo();
		
		// Determine if the robot is properly aligned
		if (robotAlignedToTarget(foundTarget) == true)
			m_finished = true;
			
		// Determine if a new image should be processed
		if (m_shooterTimer.get() > 0.12)
		{
			// Adjust the robot angle and shooter height
			determine_shooter_height(foundTarget);
			determine_robot_turn_angle(foundTarget);
				
			// Reset the timer for the next image processing
			m_shooterTimer.reset();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished()
	{
		return m_finished;
	}

	// Called once after isFinished returns true
	protected void end()
	{
		// Disable both gyro and the two encoder drive PIDs 
		Robot.chassis.disableAllPIDs();
		
		// Stop the timers
		m_shooterTimer.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{
		this.end();
	}

	/**
	 * Method to track the Y position of the target and set the shooter
	 * position angle to the target
	 */
	private void determine_shooter_height(TargetInfo foundTarget)
	{
		// Get present shooter angle
		double shooterAngle = Robot.shooter.GetShooterAngleSetPoint();

		// Ensure target has been detected
		if (foundTarget != null)
		{
			// Calculate desired shooter angle
			// Note: The image (0,0) pixel is top left corner
			// If pixelYOffset is positive, then target is too low
			int pixelYOffset = foundTarget.offsetFromTargetY;
			
			SmartDashboard.putNumber("pixelYOffset", pixelYOffset);
			
			// Assume the offset is positive
			int isPixelYOffsetPositive = 1;
			
			// Determine if the offset is negative
			if (pixelYOffset < 0)
			{
				// Make the offset positive and set the is positive variable to -1
				isPixelYOffsetPositive = -1;
				pixelYOffset *= -1;
			}
			
			// Adjust shooter angle based on distance from target
			if (pixelYOffset > Constants.IMAGE_LARGE_PIXEL_OFFSET_Y)
				shooterAngle += isPixelYOffsetPositive * Constants.IMAGE_LARGE_STEP_ANGLE_Y;

			else if (pixelYOffset > Constants.IMAGE_MEDIUM_PIXEL_OFFSET_Y)
				shooterAngle += isPixelYOffsetPositive * Constants.IMAGE_MEDIUM_STEP_ANGLE_Y;

			else if (pixelYOffset > Constants.IMAGE_SMALL_PIXEL_OFFSET_Y)
				shooterAngle += isPixelYOffsetPositive * Constants.IMAGE_SMALL_STEP_ANGLE_Y;

			// Update the shooter set point
			Robot.shooter.setShooterElevationSetpoint(shooterAngle);
		}
		else
		{
			// Can't see target, search
			if (m_shouldSearchUp)
			{
				// If we're searching up, continue up as long as we are below max
				if (shooterAngle < Constants.IMAGE_SEARCH_MAX_SHOOTER_POSITION)
				{
					shooterAngle += Constants.IMAGE_LARGE_STEP_ANGLE_Y;
				}
				else
				{
					// above max, change direction
					m_shouldSearchUp = false;
				}			
			}
			else
			{
				// If we're searching down, continue down as long as we are above min
				if (shooterAngle > Constants.IMAGE_SEARCH_MIN_SHOOTER_POSITION)
				{
					shooterAngle -= Constants.IMAGE_LARGE_STEP_ANGLE_Y;
				}
				else
				{
					// below min, change direction
					m_shouldSearchUp = true;
				}
			}	
		}
	}

	/**
	 * Method to track the X position of the target and set the desired encoder
	 * positions to turn the robot towards the target
	 */
	private void determine_robot_turn_angle(TargetInfo foundTarget)
	{
		// Get present encoder position
		// Note: Both encoders have the same set point except the right is the negative
		// of the left encoder 
		// Note: Since the left encoder is being used, which is negative, then the value must be reversed
		double encoderPosition = -Robot.chassis.getEncoderSetpoint();

		// Ensure target has been detected
		if (foundTarget != null)
		{
			// Calculate the delta pixels from the target
			int pixelXOffset = foundTarget.offsetFromTargetX;
			
			SmartDashboard.putNumber("pixelXOffset", pixelXOffset);
			
			// Assume the offset is positive
			int isPixelXOffsetPositive = 1;
			
			// Determine if the offset is negative
			if (pixelXOffset < 0)
			{
				// Make the offset positive and set the is positive variable to -1
				isPixelXOffsetPositive = -1;
				pixelXOffset *= -1;
			}

			// Adjust wheel encoders based on distance from target
			if (pixelXOffset > Constants.IMAGE_LARGE_PIXEL_OFFSET_X)
				encoderPosition += isPixelXOffsetPositive * Constants.IMAGE_LARGE_STEP_ANGLE_X;

			else if (pixelXOffset > Constants.IMAGE_MEDIUM_PIXEL_OFFSET_X)
				encoderPosition += isPixelXOffsetPositive * Constants.IMAGE_MEDIUM_STEP_ANGLE_X;

			else if (pixelXOffset > Constants.IMAGE_SMALL_PIXEL_OFFSET_X)
				encoderPosition += isPixelXOffsetPositive * Constants.IMAGE_SMALL_STEP_ANGLE_X;

			// Update the chassis encoder set points
			Robot.chassis.setEncoderPID_Setpoint(encoderPosition);
		}
	}

	/**
	 * Method to determine when the robot chassis and shooter are aligned to 
	 * the target
	 */
	private boolean robotAlignedToTarget(TargetInfo foundTarget)
	{
		// Ensure found target
		if (foundTarget == null)
			return false;
		
		// Determine if the robot is lined to the target
		if ((Math.abs(foundTarget.offsetFromTargetX) <= Constants.IMAGE_TURN_TO_TARGET_X) &&
			(Math.abs(foundTarget.offsetFromTargetY) <= Constants.IMAGE_TURN_TO_TARGET_Y))
			return true;

		// Not on target
		return false;
	}
}
