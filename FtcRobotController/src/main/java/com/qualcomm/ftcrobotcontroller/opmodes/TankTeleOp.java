/**
 * Created by izzie on 9/19/2015.
 */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

public class TankTeleOp extends OpMode {
    public DcMotor leftWheel;
    public DcMotor rightWheel;
    public DcMotor leftSlave;
    public DcMotor rightSlave;

    public DcMotorController motorController;
    public DcMotorController slaveController;

    @Override
    public void init()
    {
        leftWheel  = hardwareMap.dcMotor.get("motor_1");
        rightWheel = hardwareMap.dcMotor.get("motor_2");
        leftSlave = hardwareMap.dcMotor.get ("motor_3");
        rightSlave = hardwareMap.dcMotor.get ("motor_4");
        motorController = hardwareMap.dcMotorController.get("drive");
        slaveController = hardwareMap.dcMotorController.get("drive_two");
    }

    @Override
    public void loop()
    {
        float right = gamepad1.right_stick_y;
        float left  = gamepad1.left_stick_y;

        ThrottleMap.applyPower(leftWheel, -left);
        ThrottleMap.applyPower(rightWheel, -right);
        ThrottleMap.applyPower(leftSlave, -left);
        ThrottleMap.applyPower(rightSlave, -right);

        telemetry.addData("Power (left): ", ThrottleMap.returnPower(left));
        telemetry.addData("Power (right): ", ThrottleMap.returnPower(right));
    }
}
