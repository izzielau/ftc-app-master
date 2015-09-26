package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.MyContext;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//------------------------------------------------------------------------------
// TestBotHardware
//------------------------------------------------------------------------------
// Extends the OpMode class to provide a single hardware access point for the
// GFORCE Robot.
//------------------------------------------------------------------------------
public class GFORCEHardware extends OpMode
{
    //--------------------------------------------------------------------------
    // These class members manage the aspects of the drive motors.
    //--------------------------------------------------------------------------
    private DcMotorController   mDriveController;
    public  DcMotor             mLeftMotor;
    public  DcMotor             mRightMotor;
    public  ElapsedTime         mRuntime = new ElapsedTime();

    public  MyContext           mMyContext = new MyContext(null);


    //--------------------------------------------------------------------------
    // TestBotHardware
    //--------------------------------------------------------------------------
    public GFORCEHardware()
    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    }

    //--------------------------------------------------------------------------
    // init
    //--------------------------------------------------------------------------
    @Override public void init ()
    {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        mDriveController = hardwareMap.dcMotorController.get("drive");
        mLeftMotor = hardwareMap.dcMotor.get ("Left wheels");
        mRightMotor = hardwareMap.dcMotor.get ("Right wheels");

        // Reverse RIGHT motor if direct or chain  drive
        // Reverse LEFT motor if geared drive
        mRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set Runtime to zero
        setDrivePower(0, 0);
        mRuntime.reset();
    }

    //--------------------------------------------------------------------------
    // start
    //--------------------------------------------------------------------------
    @Override public void start ()
    {
        //
        // Only actions that are common to all Op-Modes (i.e. both automatic and
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //
    }

    //--------------------------------------------------------------------------
    // loop
    //--------------------------------------------------------------------------
    @Override public void loop ()
    {
        //
        // Only actions that are common to all OpModes (i.e. both auto and\
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    }

    //--------------------------------------------------------------------------
    // stop
    //--------------------------------------------------------------------------
    @Override public void stop ()
    {
        // Ensure that the motors are turned off.
        setDrivePower(0, 0);
    }


    //--------------------------------------------------------------------------
    // setEncoderTarget( LeftEncoder, RightEncoder);
    // Ensure correct encoder targets
    //--------------------------------------------------------------------------
    void setEncoderTarget (int leftEncoder, int rightEncoder)
    {
        mLeftMotor.setTargetPosition(leftEncoder);
        mRightMotor.setTargetPosition(rightEncoder);
        walkToPosition();
    }

    //--------------------------------------------------------------------------
    // setDrivePower( LeftPower, RightPower);
    // Ensure correct drive mode and set power level
    //--------------------------------------------------------------------------
    void setDrivePower (double leftPower, double rightPower)
    {
        useConstantPower();
        mLeftMotor.setPower(Range.clip(leftPower, -1, 1));
        mRightMotor.setPower(Range.clip(rightPower, -1, 1));
    }

    //--------------------------------------------------------------------------
    // setDriveSpeed( LeftSpeed, RightSpeed);
    // Ensure correct drive mode and set speed 
    //--------------------------------------------------------------------------
    void setDriveSpeed (double leftSpeed, double rightSpeed)
    {
        useConstantSpeed();
        mLeftMotor.setPower(Range.clip(leftSpeed, -1, 1));
        mRightMotor.setPower(Range.clip(rightSpeed, -1, 1));
    }

    //--------------------------------------------------------------------------
    // walkToPosition ()
    // Set both drive motors to servo mode
    //--------------------------------------------------------------------------
    public void walkToPosition ()
    {
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    //--------------------------------------------------------------------------
    // useConstantSpeed ()
    // Set both drive motors to contant speed
    //--------------------------------------------------------------------------
    public void useConstantSpeed ()
    {
        setDriveMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // useConstantPower ()
    // Set both drive motors to contant power
    //--------------------------------------------------------------------------
    public void useConstantPower ()
    {
        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // resetDriveEncoders()
    // Reset both drive motor encoders
    //--------------------------------------------------------------------------
    public void resetDriveEncoders()
    {
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // setDriveMode ()
    // Set new mode if it needs changing.
    //--------------------------------------------------------------------------
    public void setDriveMode(DcMotorController.RunMode mode)
    {
        // Reset the motor encoders on the drive wheels.
        if (mLeftMotor.getChannelMode() != mode)
            mLeftMotor.setChannelMode(mode);

        if (mRightMotor.getChannelMode() != mode)
            mRightMotor.setChannelMode(mode);
    }

    //--------------------------------------------------------------------------
    // getLeftPosition ()
    // Return Left Encoder count
    //--------------------------------------------------------------------------
    int getLeftPosition ()
    {
        return mLeftMotor.getCurrentPosition ();
    }

    //--------------------------------------------------------------------------
    // getRightPosition ()
    // Return Right Encoder count
    //--------------------------------------------------------------------------
    int getRightPosition ()
    {
        return mRightMotor.getCurrentPosition ();
    }

    //--------------------------------------------------------------------------
    // moveComplete()
    // Return true if no busy flag is set
    //--------------------------------------------------------------------------
    boolean moveComplete()
    {
        return (!mLeftMotor.isBusy() && !mRightMotor.isBusy());
    }

    //--------------------------------------------------------------------------
    // encodersAtZero()
    // Return tre if both encoders read zero
    //--------------------------------------------------------------------------
    boolean encodersAtZero ()
    {
        return ((getLeftPosition() == 0) && (getRightPosition() == 0));
    }
}
