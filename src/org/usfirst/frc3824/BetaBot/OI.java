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

import org.usfirst.frc3824.BetaBot.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton joystickShiftReleased;
    public JoystickButton joystickShiftPressed;
    public JoystickButton shooterExtend;
    public JoystickButton shooterRetract;
    public Joystick driveJoystick;
    public JoystickButton button5Up;
    public JoystickButton button3Down;
    public JoystickButton button7Intake;
    public JoystickButton button7IntakeBoulder;
    public JoystickButton button8Rest;
    public JoystickButton button8RestBoulder;
    public JoystickButton button9Shoot1;
    public JoystickButton button10Shoot2;
    public JoystickButton button11Shoot3;
    public JoystickButton button12Shoot4;
    public JoystickButton boulderIntakeLow;
    public JoystickButton boulderIntakeExtend;
    public JoystickButton shooterWheelsOut;
    public JoystickButton shooterWheelsIn;
    public JoystickButton shooterButton2Off;
    public JoystickButton shooterButton1Off;
    public Joystick controllerJoystick;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	public OI()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        controllerJoystick = new Joystick(1);
        
        shooterButton1Off = new JoystickButton(controllerJoystick, 1);
        shooterButton1Off.whenReleased(new ShooterSetWheelSpeed(0));
        shooterButton2Off = new JoystickButton(controllerJoystick, 2);
        shooterButton2Off.whenReleased(new ShooterSetWheelSpeed(0));
        shooterWheelsIn = new JoystickButton(controllerJoystick, 2);
        shooterWheelsIn.whenPressed(new ShooterSetWheelSpeed(-1));
        shooterWheelsOut = new JoystickButton(controllerJoystick, 1);
        shooterWheelsOut.whenPressed(new ShooterSetWheelSpeed(1));
        boulderIntakeExtend = new JoystickButton(controllerJoystick, 6);
        boulderIntakeExtend.whileHeld(new BoulderIntakeControl(-4, true));
        boulderIntakeLow = new JoystickButton(controllerJoystick, 4);
        boulderIntakeLow.whileHeld(new BoulderIntakeControl(-5, false));
        button12Shoot4 = new JoystickButton(controllerJoystick, 12);
        button12Shoot4.whenPressed(new ShooterPositionControl(-120));
        button11Shoot3 = new JoystickButton(controllerJoystick, 11);
        button11Shoot3.whenPressed(new ShooterPositionControl(-110));
        button10Shoot2 = new JoystickButton(controllerJoystick, 10);
        button10Shoot2.whenPressed(new ShooterPositionControl(-100));
        button9Shoot1 = new JoystickButton(controllerJoystick, 9);
        button9Shoot1.whenPressed(new ShooterPositionControl(-90));
        button8RestBoulder = new JoystickButton(controllerJoystick, 8);
        button8RestBoulder.whenPressed(new BoulderIntakeControl(-1, false));
        button8Rest = new JoystickButton(controllerJoystick, 8);
        button8Rest.whenPressed(new ShooterPositionControl(-80));
        button7IntakeBoulder = new JoystickButton(controllerJoystick, 7);
        button7IntakeBoulder.whenPressed(new BoulderIntakeControl(-4, true));
        button7Intake = new JoystickButton(controllerJoystick, 7);
        button7Intake.whenPressed(new ShooterPositionControl(-70));
        button3Down = new JoystickButton(controllerJoystick, 3);
        button3Down.whenPressed(new ShooterPositionControl(-50));
        button5Up = new JoystickButton(controllerJoystick, 5);
        button5Up.whenPressed(new ShooterPositionControl(-30));
        driveJoystick = new Joystick(0);
        
        shooterRetract = new JoystickButton(driveJoystick, 1);
        shooterRetract.whenReleased(new ShooterShoot(false));
        shooterExtend = new JoystickButton(driveJoystick, 1);
        shooterExtend.whenPressed(new ShooterShoot(true));
        joystickShiftPressed = new JoystickButton(driveJoystick, 2);
        joystickShiftPressed.whenPressed(new ShiftGear(true));
        joystickShiftReleased = new JoystickButton(driveJoystick, 2);
        joystickShiftReleased.whenReleased(new ShiftGear(false));


        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Low Bar Shoot Boulder", new AutonomousLowBarShootBoulder());
        SmartDashboard.putData("Chassis Drive Straight", new ChassisDriveStraight());
        SmartDashboard.putData("Chassis Drive Straight Distance: FourFeet", new ChassisDriveStraightDistance(48.0));
        SmartDashboard.putData("Chassis Drive Target LIDAR: BaseOfTower", new ChassisDriveTargetLIDAR(100.0));
        SmartDashboard.putData("Chassis Turn Angle: Turn90", new ChassisTurnAngle(90.0, 0.6));
        SmartDashboard.putData("ShiftGear: highGear", new ShiftGear(true));
        SmartDashboard.putData("Shooter Shoot: ShootControl", new ShooterShoot(true));
        SmartDashboard.putData("Shoot Boulder In Goal: ShootLowGoal", new ShootBoulderInGoal(0.2, 0.5));
        SmartDashboard.putData("CameraDisconnect", new CameraDisconnect());
        SmartDashboard.putData("CameraRun", new CameraRun());
        SmartDashboard.putData("ChassisTurnToImageTarget", new ChassisTurnToImageTarget());
        SmartDashboard.putData("SetBrightnessFromPrefs", new SetBrightnessFromPrefs());
        SmartDashboard.putData("SetExposureFromPrefs", new SetExposureFromPrefs());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
	}

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getDriveJoystick() {
        return driveJoystick;
    }

    public Joystick getControllerJoystick() {
        return controllerJoystick;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}
