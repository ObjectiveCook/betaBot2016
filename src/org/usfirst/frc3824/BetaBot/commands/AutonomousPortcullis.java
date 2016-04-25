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

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.subsystems.*;

/**
 *
 */
public class AutonomousPortcullis extends CommandGroup
{
	public double driveDistance      = 380.0;
	
	public double position2TurnAngle = 80.0; // Position 2
	public double position2Drive     = 200.0;
	
	public double positionTurnAngle  = 20.0;  // Position 3 and 5
	public double positionDrive      = 90.0;
	
	public double LiDarDistance      = Constants.AUTONOMOUS_LIDAR_DISTANCE_TO_TARGET;
	public double shooterHeight      = Constants.AUTONOMOUS_LIDAR_RANGE_SHOOTER_ANGLE;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public AutonomousPortcullis(int StartingPosition, int ShotChoice) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    	// Ensure the shooter is in neutral position
		addParallel(new ShooterPositionControl(10.0));

    	// Lower the arms
		addParallel(new AxeControlDown());
		addSequential(new Delay(1.5));
		
		// Drive through the Portculis
		addSequential(new ChassisDriveStraightDistance(driveDistance, 0.8));
		
		// Raise the boulder pickup
		addParallel(new AxeControlUp());
		
		// Determine the starting position
		if (StartingPosition == Constants.STARTING_POSITION_2)
		{
			// Raise the shooter while turning towards the goal
			addParallel(new ShooterPositionControl(shooterHeight));

			// Turn right and drive to the goal
			addSequential(new ChassisTurnAngle(position2TurnAngle, 0.0));
			addSequential(new ChassisDriveStraightDistance(position2Drive, 1.0));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(0.0, 0.0));
		}
		else if (StartingPosition == Constants.STARTING_POSITION_3)
		{
			// Raise the shooter while turning towards the goal
			addParallel(new ShooterPositionControl(shooterHeight));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(positionTurnAngle, 0.0));
		}
		else if (StartingPosition == Constants.STARTING_POSITION_4)
		{
			// Raise the shooter while turning towards the goal
			addParallel(new ShooterPositionControl(shooterHeight));

			// Should be facing the goal, but wait for shooter to reach position
			addSequential(new Delay(0.5));
		}
		else if (StartingPosition == Constants.STARTING_POSITION_5)
		{
			// Raise the shooter while turning towards the goal
			addParallel(new ShooterPositionControl(shooterHeight));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(-positionTurnAngle, 0.0));
		}

		// Line up based on camera
		addSequential(new ChassisTurnToImageTarget());

		// Drive to shoot distance
		addSequential(new ChassisDriveTargetLIDAR(LiDarDistance));

		// Determine if the Robot should shoot the boulder
		if (ShotChoice == Constants.HIGH_GOAL)
		{
			// Line up and shoot based on camera
			addSequential(new VisionAutomatedAimAndShoot());
	
			// Turn around
			addSequential(new ChassisTurnAngle(180.0, 0.0));
			
			// Drive away from goal
			addSequential(new ChassisDriveStraightDistance(90.0, 0.8));
		}
	}
}
