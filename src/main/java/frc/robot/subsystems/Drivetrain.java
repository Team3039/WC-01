/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.controllers.PS4Gamepad;

public class Drivetrain extends SubsystemBase {

  public TalonSRX leftFrontDrive = new TalonSRX(RobotMap.frontleftMotor);
  public TalonSRX rightFrontDrive = new TalonSRX(RobotMap.frontrightMotor);
  public TalonSRX leftBackDrive = new TalonSRX(RobotMap.rearleftMotor);
  public TalonSRX rightBackDrive = new TalonSRX(RobotMap.rearrightMotor);

  public Solenoid wheelSwap = new Solenoid(RobotMap.wheelSwap);

  public Drivetrain() {
    setNeutralMode(NeutralMode.Brake);
  }

  public void joystickControl(PS4Gamepad gp) {
    //Tele-Op Driving
    //Each Motor is Set to Brake Mode, the motor speeds are set in an Arcade Drive fashion
    double y = gp.getLeftYAxis()*-Constants.y;
    double rot = gp.getRightXAxis()*Constants.rot;

    //Calculated Outputs (Limits Output to 12V)
    double leftOutput = y + rot;
    double rightOutput = rot - y;

    //Assigns Each Motor's Power
    leftFrontDrive.set(ControlMode.PercentOutput, leftOutput);
    rightFrontDrive.set(ControlMode.PercentOutput, rightOutput);
    rightBackDrive.follow(rightFrontDrive);
    leftBackDrive.follow(leftFrontDrive);
  }

  public void stop() {
    leftFrontDrive.set(ControlMode.PercentOutput, 0);
    rightFrontDrive.set(ControlMode.PercentOutput, 0);
    leftBackDrive.follow(leftBackDrive);
    rightBackDrive.follow(rightBackDrive);   
  }

  public void setNeutralMode(NeutralMode mode) {
    //Set Motor's Neutral/Idle Mode to Brake
    leftFrontDrive.setNeutralMode(mode);
    rightFrontDrive.setNeutralMode(mode);
    rightBackDrive.setNeutralMode(mode);
    leftBackDrive.setNeutralMode(mode);
  }

  public double getLeftPosition() {
    return leftBackDrive.getSelectedSensorPosition();
  }

  public double getRightPosition() {
    return rightBackDrive.getSelectedSensorPosition();
  }

  public double getLeftVelocity() {
    return leftBackDrive.getSelectedSensorVelocity();
  }

  public double getRightVelocity() {
    return rightBackDrive.getSelectedSensorVelocity();
  }

  public void driveSwitch (boolean onColsons) {
    wheelSwap.set(onColsons);
  }

  @Override
  public void periodic() {
    //TODO: test this to see if it works
    joystickControl(RobotContainer.getDriver());
  }
}
