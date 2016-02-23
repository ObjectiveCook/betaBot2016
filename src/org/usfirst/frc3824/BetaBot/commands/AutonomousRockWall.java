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
public class AutonomousRockWall extends CommandGroup
{
	public double shooterHeight      = 48.0;
	public double position2TurnAngle = 80.0;
	public double positionTurnAngle  = 20.0;
	public double LiDarDistance      = 340.0;
	
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public AutonomousRockWall(int StartingLocation, int ShotChoice) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

		// Drive over the Rock Wall
		// Raise the shooter allowing time for the shooter to raise 
		addParallel(new ShooterPositionControl(30.0));
		addSequential(new Delay(0.2));

		// Determine the starting position
		if (StartingLocation == Constants.STARTING_POSITION_2)
		{
			// Drive over the Rock Wall
			addSequential(new ChassisDriveStraightDistance(380.0, 1.0));
			
			// Raise the shooter while turning towards the goal
			addParallel(new ShooterPositionControl(shooterHeight));

			// Turn right and drive to the goal
			addSequential(new ChassisTurnAngle(position2TurnAngle, 0.0));
			addSequential(new ChassisDriveStraightDistance(200.0, 1.0));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(-position2TurnAngle, 0.0));
		}
		else if (StartingLocation == Constants.STARTING_POSITION_3)
		{
			// Drive over the Rock Wall
			addSequential(new ChassisDriveStraightDistance(380.0, 1.0));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(positionTurnAngle, 0.0));
		}
		else if (StartingLocation == Constants.STARTING_POSITION_4)
		{
			// Drive over the Rock Wall
			addSequential(new ChassisDriveStraightDistance(380.0, 1.0));

			// Should be facing the goal
		}
		else if (StartingLocation == Constants.STARTING_POSITION_5)
		{
			// Drive over the Rock Wall
			addSequential(new ChassisDriveStraightDistance(380.0, 1.0));

			// Turn towards the goal
			addSequential(new ChassisTurnAngle(-positionTurnAngle, 0.0));
		}

		// Determine if the boulder should to shot
		if (ShotChoice == Constants.HIGH_GOAL)
		{
			// Set the shooter height and allow time for the shooter to raise
			addParallel(new ShooterPositionControl(shooterHeight));
			addSequential(new Delay(0.5));

			// Line up based on camera
			addSequential(new ChassisTurnToImageTarget(Constants.TARGET_CENTER));

			// Drive to shoot distance
			addSequential(new ChassisDriveTargetLIDAR(LiDarDistance));

			// Line up based on camera
			addSequential(new ChassisTurnToImageTarget(Constants.TARGET_CENTER));

			// Shoot into the goal
			addSequential(new ShootBoulderInGoal(shooterHeight, 1.0));
			addSequential(new ShooterSetWheelSpeed(0.0));
		}
	}
}