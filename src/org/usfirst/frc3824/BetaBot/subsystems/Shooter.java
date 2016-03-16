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
import org.usfirst.frc3824.BetaBot.Constants;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem
{
	private boolean m_enabled = true;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AnalogPotentiometer analogPotentiometer = RobotMap.shooterAnalogPotentiometer;
    private final SpeedController actuator = RobotMap.shooterActuator;
    private final CANTalon wheelRightA = RobotMap.shooterWheelRightA;
    private final CANTalon wheelRightB = RobotMap.shooterWheelRightB;
    private final CANTalon wheelLeftA = RobotMap.shooterWheelLeftA;
    private final CANTalon wheelLeftB = RobotMap.shooterWheelLeftB;
    private final Solenoid ballShooterPiston = RobotMap.shooterBallShooterPiston;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	private PIDController shooterAngleController;

	double m_PresentShooterAngle;
	double m_PresentWheelSpeed;
	double m_MinShooterHeight;
	double m_MaxShooterHeight;

	public Shooter()
	{
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		shooterAngleController = new PIDController(20.0, 0, 0, analogPotentiometer, actuator);
		shooterAngleController.setAbsoluteTolerance(0.001);
		shooterAngleController.setContinuous(false);
		shooterAngleController.setSetpoint(analogPotentiometer.get());
		
		LiveWindow.addActuator("Shooter Angle", "Angle PID", shooterAngleController);

		m_MinShooterHeight = Preferences.getInstance().getDouble("Min Shooter Height", Constants.SHOOTER_ELEVATION_SETPOINT_MIN);
		m_MaxShooterHeight = Preferences.getInstance().getDouble("Max Shooter Height", Constants.SHOOTER_ELEVATION_SETPOINT_MAX);
		
		shooterAngleController.enable();
	}
	
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	}

	/**
	 * Method to enable or disable the shooter wheels. Note: The actual control
	 * of the shooter wheels is in the default ShooterControl command.
	 */
	public void ShooterWheelEnableDisable(boolean enable)
	{
		// Remember the requested state of the shooter wheels
		m_enabled = enable;
	}

	/**
	 * Method to determine if the shooter wheels are enabled
	 */
	public boolean IsShooterWheelEnabled()
	{
		// Return the state of the shooter wheels
		return (m_enabled);
	}

	/**
	 * Method to enable the shooter with the specified speed. The method can
	 * also disable the shooter with a speed of zero.
	 */
	public void ShooterWheelControl(double speed)
	{
		// Record Current Wheel Speed
		m_PresentWheelSpeed = speed;

		// Set the shooter wheel motor speeds
		// Note: The multiplier is to ensure maximum speed is reached
		// The multiplier also reduces the minimum speed
		wheelRightA.set(speed);
		wheelRightB.set(speed);
		wheelLeftA.set(speed);
		wheelLeftB.set(speed);
	}

	/**
	 * Method to shoot the ball
	 */
	public void ShooterShootBallControl(boolean state)
	{
		// Control the gear shift piston
		ballShooterPiston.set(state);
	}

	/**
	 * Routine to enable or disable the shooter elevation PID
	 */
	public void setShooterElevationEnabled(boolean state)
	{
		// Determine if the shooter elevation PID should be enabled or disabled
		if (state == true)
		{
			shooterAngleController.enable();
		}
		else
		{
			shooterAngleController.disable();
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

		double A = Constants.SHOOTER_ELEVATION_POT_A;
		double B = Constants.SHOOTER_ELEVATION_POT_B;
		double C = Constants.SHOOTER_ELEVATION_POT_C;

		// Convert degrees to Potentiometer value
		// Potentiometer Set Point = -0.00004(X^2) +0.011X+0.1956
		double setPoint = A * (setpointDegrees * setpointDegrees) + (B * setpointDegrees) + C;

		SmartDashboard.putNumber("Shooter Angle Setpoint", setpointDegrees);

		// Set the shooter elevation set point
		shooterAngleController.setSetpoint(setPoint);
	}

	/**
	 * Method to get teh shooter elecation angel
	 */
	public double GetShooterElevatorAngle()
	{
		double angle;

		// Get the shooter elevator potentiometer position
		angle = analogPotentiometer.get();

		double A = Constants.SHOOTER_ELEVATION_ANGLE_A;
		double B = Constants.SHOOTER_ELEVATION_ANGLE_B;
		double C = Constants.SHOOTER_ELEVATION_ANGLE_C;

		// Angle = 58.01 X^2 + 64.95 X - 15.5
		angle = A * (angle * angle) + (B * angle) + C;

		// Return the shooter elevator angle in degrees
		return (angle);
	}

	/**
	 * Method to get the present shooter angle set point
	 */
	public double GetShooterAngleSetPoint()
	{
		return (m_PresentShooterAngle);
	}

	/**
	 * Method to get the present shooter wheel speed
	 */
	public double GetShooterWheelSpeed()
	{
		return (m_PresentWheelSpeed);
	}
}
