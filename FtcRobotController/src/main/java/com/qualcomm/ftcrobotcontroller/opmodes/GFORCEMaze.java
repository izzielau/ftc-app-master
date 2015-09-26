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


import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.*;

/**
 * Template Mode
 */
public class GFORCEMaze extends GFORCEHardware {

	// A list of system States.
	private enum State {
		STATE_BEGIN,
		STATE_END
	}

	private State   mCurrentState = State.STATE_BEGIN;
	private double mThrottle = -gamepad1.left_stick_y;
	private double mDirection = gamepad1.right_stick_x;
	private double mLeftSpeed;
	private double mRightSpeed;


	/**
	 * Constructor
	 */
	public GFORCEMaze() {
	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {
		super.init();

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		final double MAX_SPEED = 0.65 ;

		mThrottle = -gamepad1.left_stick_y * MAX_SPEED ;
		mDirection = gamepad1.right_stick_x * MAX_SPEED / 2;

		// clip the right/left values so that the values never exceed +/- 1
		mLeftSpeed = mThrottle + mDirection;
		mRightSpeed = mThrottle - mDirection;

		// Don't just clip motor speeds, scale down so fastest motor is <= full power.
		double Extra = abs(max(mLeftSpeed, mRightSpeed));
		if (Extra > MAX_SPEED)
		{
			double Scale = MAX_SPEED / Extra ;
			mLeftSpeed *= Scale ;
			mRightSpeed *= Scale ;
		}

		// Set constant Speed mode
		setDriveSpeed(mLeftSpeed, mRightSpeed);

		showDiagnostics();
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
	public void showDiagnostics () 	{
		/*
		 * Send telemetry data back to driver station.
		 */
		telemetry.addData("JS",  "JS (T:D) (" + String.format("%.2f", mThrottle) + ":" + String.format("%.2f", mDirection) + ")" );
		telemetry.addData("SP",  "SP (L:R) (" + String.format("%.2f", mLeftSpeed) + ":" + String.format("%.2f", mRightSpeed) + ")" );
	}
}
