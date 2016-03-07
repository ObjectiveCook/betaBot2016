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
import org.usfirst.frc3824.BetaBot.Robot;

/**
 *
 */
public class AutonomousLowBar extends CommandGroup
{
	public double driveDistance = 510.0;
	public double shooterHeight = 37.0;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public AutonomousLowBar(int ShotChoice) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

		// Drive over the low bar with the shooter at 0 degrees
		addParallel(new ShooterPositionControl(0.0));
		
    	// Lower the boulder pickup
		addSequential(new AxeControl(true, true));
		addSequential(new Delay(0.4));
		
		addSequential(new ChassisDriveStraightDistance(driveDistance, 1.0));

		// Raise the shooter while turning towards the goal
		addSequential(new ShooterPositionControl(shooterHeight));
		addSequential(new ChassisTurnAngle(55.0, 0.2));

//		// Determine if the Robot should shoot the boulder
//		if (ShotChoice == Constants.HIGH_GOAL)
//		{
		// Line up and shoot based on camera
		addSequential(new VisionAutomatedAimAndShoot());
//    }
    
//		// Line up based on camera
//		addSequential(new ChassisTurnToImageTarget(Constants.TARGET_LEFT));
//
//		// Determine if the Robot should shoot the boulder
//		if (ShotChoice == Constants.HIGH_GOAL)
//		{
//			// Shoot into the goal
//			addSequential(new ShootBoulderInGoal(shooterHeight, 1.0));
//			addSequential(new ShooterSetWheelSpeed(0.0));
//		}
	}
}
