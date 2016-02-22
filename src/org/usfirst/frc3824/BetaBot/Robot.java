// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3824.BetaBot;

import org.usfirst.frc3824.BetaBot.Robot;
import org.usfirst.frc3824.BetaBot.RobotMap;
import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.commands.*;
import org.usfirst.frc3824.BetaBot.subsystems.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
	Command autonomousCommand;

	public static OI oi;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Chassis chassis;
    public static Power power;
    public static TargetCam targetCam;
    public static Shooter shooter;
    public static BoulderIntake boulderIntake;
    public static Targets targets;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	public static SendableChooser defenseChooser;
	public static SendableChooser startingLocationChooser;
	public static AutoParameters autoParameters;
	public class AutoParameters
	{
		public int m_turn;
		public int m_goal;
		
		AutoParameters(int turn, int goal)
		{
			m_turn = turn;
			m_goal = goal;
		}
		
		public int getTurn()
		{
			return m_turn;
		}
		
		public int getGoal()
		{
			return m_goal;
		}
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
		// Initialize the robot constants
		Constants.InitConstants();

		// Initialize the robot components
		RobotMap.init();

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        chassis = new Chassis();
        power = new Power();
        targetCam = new TargetCam();
        shooter = new Shooter();
        boulderIntake = new BoulderIntake();
        targets = new Targets();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

		// instantiate the command used for the autonomous period
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		// set up the chooser for the defense class 
		// - this tells us what we need to do to get past the defense
		defenseChooser = new SendableChooser();
		defenseChooser.addDefault("1) Do Nothing", new AutonomousDoNothing());
		defenseChooser.addDefault("2) Drive Over Defense", new AutonomousDoNothing());
		defenseChooser.addDefault("3) Cheval de Frise", new AutonomousDoNothing());
		defenseChooser.addDefault("4) Portcullis", new AutonomousDoNothing());
		defenseChooser.addDefault("5) Drawbridge", new AutonomousDoNothing());
		defenseChooser.addDefault("6) Sally Port", new AutonomousDoNothing());
		defenseChooser.addDefault("7) Turn To Image Target", new ChassisTurnToImageTarget());
		SmartDashboard.putData("Defense to cross", defenseChooser);
		
		// set up the chooser for the starting location and high goal vs low goal shot
		// - this tells us what direction we need to turn to get the goal in sight and
		//   whether we're shooting high or low.
		startingLocationChooser = new SendableChooser();
		startingLocationChooser.addDefault("1/2 Low", new AutoParameters(Constants.TURN_RIGHT, Constants.LOW_GOAL));
		startingLocationChooser.addObject("1/2 High", new AutoParameters(Constants.TURN_RIGHT, Constants.HIGH_GOAL));
		startingLocationChooser.addObject("3/4 (High)", new AutoParameters(Constants.TURN_NONE, Constants.HIGH_GOAL));
		startingLocationChooser.addObject("5 Low", new AutoParameters(Constants.TURN_LEFT, Constants.LOW_GOAL));
		startingLocationChooser.addObject("5 High", new AutoParameters(Constants.TURN_LEFT, Constants.HIGH_GOAL));
		SmartDashboard.putData("Starting Location and Shot", startingLocationChooser );

		RobotMap.chassisCompressor.setClosedLoopControl(true);
		
		// Add a USB camera
		CameraServer.getInstance().startAutomaticCapture("cam0");
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit()
	{

	}

	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();

		Robot.targets.getTargetOffsetFromCenterNormalized(Constants.TARGET_CENTER);
		Robot.targets.updateSmartDashboard();
		
		SmartDashboard.putNumber("getX", Robot.oi.getControllerJoystick().getX());
	}

	public void autonomousInit()
	{
		// Determine the autonomous command
		if (defenseChooser.getSelected() != null)
		{
			// Reset the gyro before the start of autonomous
			Robot.chassis.resetGyro();

			// Get the autonomous command
			autonomousCommand = (edu.wpi.first.wpilibj.command.Command) defenseChooser.getSelected();

			autoParameters = (AutoParameters) startingLocationChooser.getSelected();
						
			// schedule the autonomous command
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();

		// Add current gyro angle to smart dashboard
		SmartDashboard.putNumber("Gyro Angle", Robot.chassis.getGyro().getAngle());
		
		SmartDashboard.putNumber("Lidar Range (cm)", Robot.chassis.getLidarDistanceCentimeters());

		Robot.targets.getTargetOffsetFromCenterNormalized(Constants.TARGET_CENTER);
		Robot.targets.updateSmartDashboard();
}

	public void teleopInit()
	{
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();
		SmartDashboard.putBoolean("Compressor Enabled", RobotMap.chassisCompressor.enabled());
		SmartDashboard.putBoolean("Pressure Switch", RobotMap.chassisCompressor.getPressureSwitchValue());
		SmartDashboard.putNumber("Compressor Current", RobotMap.chassisCompressor.getCompressorCurrent());
		
		SmartDashboard.putNumber("Lidar Range (cm)", Robot.chassis.getLidarDistanceCentimeters());

		Robot.targets.getTargetOffsetFromCenterNormalized(Constants.TARGET_CENTER);
		Robot.targets.updateSmartDashboard();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic()
	{
		LiveWindow.run();
	}
}
