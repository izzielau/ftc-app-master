/**
 * Created by izzie on 9/4/2015.
 */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;

/*
  - Have a constructor that takes a collection of DcMotors.

  - Have a private method that calculates a logarithmic function
    over 0 to 1 similar to the logarithmic function we used
    last year to dampen small joystick movements in teleop
    for the driver.

  - Have method called applyPower that takes the joystick values,
    maps them using the log function and then calls setPower on
    each of the motors passed to the constructor.
 */

public class ThrottleMap {
    public static double logPower;

    private static double logarithmicPower(double power)
    {
        boolean negative = false;

        // Convert power (decimal) to whole number.
        if (power < 0) {
            negative = true;
        } else if (power == 0) {
            return 0;
        }

        // Assign value of 'e' to a variable.
        double e = Math.exp(1.0);

        // Calculate logarithmic value.
        double joystick = Math.abs(power);
        double returnPower = Math.pow(e, (4.6 * joystick)) / 100;

        // Find charge of power.
        if (negative) {
            return -1 * returnPower;
        } else {
            return returnPower;
        }
    }

    public static void applyPower(DcMotor motor, double joystick)
    {
        // Calculate logarithmic power for joystick value.
        logPower = logarithmicPower(joystick);

        // Set logarithmic power to the motor.
        motor.setPower(logPower);
    }

    public static double returnPower(double joystick)
    {
        // Calculate logarithmic power for joystick value.
        logPower = logarithmicPower(joystick);

        // Send the log power to the motor.
        return logPower;
    }
}
