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

import org.usfirst.frc3824.BetaBot.Constants;
import org.usfirst.frc3824.BetaBot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Targets extends Subsystem
{
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	NetworkTable m_contoursReport;
	NetworkTable m_imageReport;
	NetworkTable m_frameRateReport;
	
	// Used to clear the target arrays before reading target information
	double[] m_defaultValue = new double[0];
	double   m_targetCenterX;
	
	private double m_positionFromOnTargetX;
	private double m_positionFromOnTargetXNormalized;
	
	// Allows off setting the target to compensate for camera angle
	private int  m_onTargetX;  // "Center of Image" on the X-axis

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	}

	public Targets()
	{
		m_contoursReport  = NetworkTable.getTable("GRIP/cameraTargets");
		m_frameRateReport = NetworkTable.getTable("GRIP");
		m_onTargetX       = Constants.IMAGE_ON_TARGET_X_POSITION;
	}

	/*
	 * ***********************************************************************
	 * Return the position of the target, offset from our desired center point
	 * The return value will be in the range of -2 to 2, where -2 a full image
	 * width to the left of "onTarget" and 2 is a full image width to the right
	 * of "onTarget". In the ideal case of the "onTarget" position being exactly
	 * in the center of the image, then the left edge would be -1 and the right
	 * edge would be 1.
	 ***/
	public double getTargetOffsetFromCenterNormalized(int whichTarget)
	// if the image is positioned to the right, the robot is too far left.
	// so this return value is flipped
	{
		// get the center of the largest target in view. We are assuming that the
		// largest target is the one we are facing most directly
		m_targetCenterX = getCenterXOfWhichTarget(whichTarget);

		// calculate offset from "OnTarget" in pixels
		m_positionFromOnTargetX = m_targetCenterX - m_onTargetX;

		// convert the offset in pixels to a normalized range where -1 is one half an
		// image width to the left and 1 is one half an image width to the right.
		m_positionFromOnTargetXNormalized = m_positionFromOnTargetX / (Constants.IMAGE_WIDTH / 2.0);

		return m_positionFromOnTargetXNormalized;
	}

	/*
	 * ***********************************************************************
	 * Convert the normalized value into an angle based on the camera's FOV
	 * Technically, this should be a trigonometric function, but we are using a
	 * linear approximation, which is good enough for our purposes. To use the
	 * trig function, we would have to know our distance as well
	 ***/
	public double getTargetOffsetFromCenterAngle(int whichTarget)
	{
		return getTargetOffsetFromCenterNormalized(whichTarget) * (Constants.CAM_FOV / 2.0);
	}

	/*
	 * ***********************************************************************
	 * Determine if the GRIP image processing pipeline is running on the
	 * RaspberryPi. We assume that if the frame rate is non-zero, then the
	 * pipeline is running. If the frame rate is 0, it is not running
	 ***/
	public boolean isImageProcessingRunning()
	{
		return (m_frameRateReport.getNumber("cameraFrameRate", 0.0) > 0);
	}

	/*
	 * ***********************************************************************
	 * Display data values on the smart dashboard
	 ***/
	public void updateSmartDashboard()
	{
		Robot.targets.getTargetOffsetFromCenterNormalized(Constants.TARGET_CENTER);
		
		SmartDashboard.putNumber("Targets FrameRate", m_frameRateReport.getNumber("cameraFrameRate", 0.0));
		SmartDashboard.putNumber("Targets X offset from image center", m_positionFromOnTargetX);
		SmartDashboard.putNumber("Targets Normalized X offset from image center", m_positionFromOnTargetXNormalized);
		SmartDashboard.putNumber("Targets center X", m_targetCenterX);
		SmartDashboard.putNumber("Targets Image Width", Constants.IMAGE_WIDTH);
		SmartDashboard.putBoolean("Image Processing Running", isImageProcessingRunning());
	}

	/*
	 * ***********************************************************************
	 * Calculate the center of the largest target in the list of targets NOTE: I
	 * THINK this will always be the first object in the array, but until this
	 * can be confirmed, need to do this calculation
	 ***/
	private double getCenterXOfLargestTarget()
	{
		double[] centerXs = m_contoursReport.getNumberArray("centerX", m_defaultValue);
		double[] areas    = m_contoursReport.getNumberArray("area",    m_defaultValue);
		double maxArea    = 0.0;
		int maxAreaIndex  = -1;
		
		// Loop through all targets
		for (int areaIndex = 0; areaIndex < areas.length; areaIndex++)
		{
			// Determine if the area is the largest
			if (areas[areaIndex] > maxArea)
			{
				// Remember the maximum area index
				maxAreaIndex = areaIndex;
				
				// Update the new maximum area
				maxArea = areas[areaIndex];	
			}
		}

		// Determine if a target area was found
		if (maxAreaIndex >= 0)
		{
			// remember the X position of the maximum area target
			m_targetCenterX = centerXs[maxAreaIndex];
		}
		else
		{
			// No target found so return center
			// TODO: Need to indicate not target found
			m_targetCenterX = Constants.IMAGE_WIDTH / 2.0;
		}

		// return the X position of the maximum area target
		return m_targetCenterX;
	}
	/**
	 * Method to select the specified target when there are multiple targets 
	 */
	private double getCenterXOfWhichTarget(int whichTarget)
	{
		double[] centerXs = m_contoursReport.getNumberArray("centerX", m_defaultValue);
		double[] areas    = m_contoursReport.getNumberArray("area",    m_defaultValue);

		// Determine the number of found targets
		if (areas.length == 1)
		{
			// Only one target so return X center
			m_targetCenterX = centerXs[0];  // TODO: EXCEPTION
		}
		else if (areas.length == 2)
		{
			// ----- TWO TARGETS DETECTED -------
			switch (whichTarget)
			{
			case 0: // LEFT - take the left target
				m_targetCenterX = Math.min(centerXs[0], centerXs[1]);
				break;

			case 1: // CENTER - take the LARGEST target
				if (areas[0] > areas[1])
				{
					m_targetCenterX = centerXs[0];
				}
				else
				{
					m_targetCenterX = centerXs[1];
				}
				break;

			case 2: // RIGHT - take the right target
				m_targetCenterX = Math.max(centerXs[0], centerXs[1]);
				break;
			}
		}
		else if (areas.length == 3)
		{
			// ----- THREE TARGETS DETECTED -------
			switch (whichTarget)
			{
			case 0: // LEFT - take the left target
				m_targetCenterX = Math.min(Math.min(centerXs[0], centerXs[1]), centerXs[2]);
				break;

			case 1: // CENTER - take the LARGEST target
				m_targetCenterX = getCenterXOfLargestTarget();
				break;

			case 2: // RIGHT - take the right target
				m_targetCenterX = Math.max(Math.max(centerXs[0], centerXs[1]), centerXs[2]);
				break;
			}
		}
		else
		{
			// No target found so return center
			// TODO: Need to indicate not target found
			m_targetCenterX = Constants.IMAGE_WIDTH / 2.0;
		}

		return m_targetCenterX;
	}
}
