/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class GFORCEResetTest extends GFORCEHardware {

	// A list of system States.
	private enum State {
		STATE_BEGIN,
		STATE_RESET,
		STATE_RESET_WAIT,
		STATE_MODE_CHANGE,
		STATE_BUSY_CHANGE,
		STATE_DRIVE_WAIT
	}

	private State   mCurrentState = State.STATE_BEGIN;
	private int		mResetLoops;

	/**
	 * Constructor
	 */
	public GFORCEResetTest() {
	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {
		super.init();

		mResetLoops = 0;
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		showDiagnostics();

		// Execute the current state
		switch (mCurrentState) {

			case STATE_BEGIN:
			default:
				setDrivePower(0, 0);
				mCurrentState = State.STATE_RESET;
				break;

			case STATE_RESET:
				resetDriveEncoders();
				mCurrentState = State.STATE_RESET_WAIT;
				mResetLoops = 0;
				break;

			case STATE_RESET_WAIT:
				if (encodersAtZero())
				{
					mCurrentState = State.STATE_MODE_CHANGE;
				}
				else
					mResetLoops++;
				break;

			case STATE_MODE_CHANGE:
				setDriveSpeed(0.25, 0.25);
				setEncoderTarget(4000, 4000);
				mCurrentState = State.STATE_DRIVE_WAIT;
				break;

			case STATE_DRIVE_WAIT:
				break;
		}
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {
		super.stop();
	}

	//--------------------------------------------------------------------------
	// showDiagnostics ()
	//--------------------------------------------------------------------------
	public void showDiagnostics ()
	{
		String L1 = "L: " + mLeftMotor.getCurrentPosition() + ",\t" + mLeftMotor.getChannelMode().toString() ;
		String L2 = "R: " + mRightMotor.getCurrentPosition() + ",\t" + mRightMotor.getChannelMode().toString() ;
		String L3 = "T: " + mRuntime.toString() + ",\t" + mResetLoops + " Ls, " + mLeftMotor.isBusy() 	+ " " + mRightMotor.isBusy() ;
		try
		{
			// Creates a trace file in the primary external storage space of the
			// current application.
			// If the file does not exists, it is created.
			File traceFile = new File(mMyContext.getContext().getExternalFilesDir(null), "TraceFile.txt");
			if (!traceFile.exists())
				traceFile.createNewFile();
			// Adds a line to the trace file
			BufferedWriter writer = new BufferedWriter(new FileWriter(traceFile, true /*append*/));
			writer.write(L1 + "\n" + L2 + "\n" + L3 + "\n\n" );
			writer.close();
		}
		catch (IOException e)
		{
		}

		telemetry.addData
				( "01", L1 );
		telemetry.addData
				( "02", L2 );
		telemetry.addData
				("03", L3);
	}
}
/*
			case STATE_BUSY_CHANGE:
				if (mBusyLoops++ > 10)
					mCurrentState = State.STATE_DRIVE_WAIT;
				break;
*/
