package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.mtzButtonBehavior;

@TeleOp(name="Tele Using Behaviors", group ="z_test")

//@Disabled

public class z_test_Tele_uses_behavior extends LinearOpMode {


    @Override

    //This is the opMode call for generically running the opMode in this super class
    public void runOpMode() {

        String controlPadMap = "SkyStone Hunchamuncha Left Strafe";

        /*******
         * Add Button Variables & Objects
         ********/
/*************           SkyStone Hunchamuncha Left Strafe     Control Pad Map            **************/
// Assign Variables & Objects for Control Pads
        double chassisSpeedSlow;                             //Slow Speed
        mtzButtonBehavior robotExtenderInButtonStatus = new mtzButtonBehavior();         //Park Extender In




        mtzButtonBehavior chassisBumpForwardStatus = new mtzButtonBehavior();         //Bump Forward
        mtzButtonBehavior chassisBumpLeftStatus = new mtzButtonBehavior();         //Bump Left
        mtzButtonBehavior chassisBumpRightStatus = new mtzButtonBehavior();         //Bump Right
        mtzButtonBehavior chassisBumpBackStatus = new mtzButtonBehavior();         //Bump Backwards


        double driveStick1;                             //Drive 1
        double turnStick;                             //Turn
        double chassisSpeedFast;                             //High Speed
        mtzButtonBehavior robotExtenderOutButtonStatus = new mtzButtonBehavior();         //Park Extender Out

        mtzButtonBehavior startButton1Status = new mtzButtonBehavior();         //Pad Select (A & B)


        mtzButtonBehavior hooksDownButtonStatus = new mtzButtonBehavior();         //Hooks Down

        mtzButtonBehavior blockThrowerButtonStatus = new mtzButtonBehavior();         //Block Thrower
        mtzButtonBehavior hooksUpButtonStatus = new mtzButtonBehavior();         //Hooks Up


        double driveStick2;                             //Drive 2
        double strafeStick;                             //Strafe
        double clawOpen;                             //Claw Open (Sticky)
        mtzButtonBehavior wristAdjustLessStatus = new mtzButtonBehavior();         //Wrist Adjust -

        mtzButtonBehavior resetAdjustmentsStatus = new mtzButtonBehavior();         //Reset Adjustments
        mtzButtonBehavior stoneReleaseButtonStatus = new mtzButtonBehavior();         //Release Stone

        mtzButtonBehavior stackLevelUpStatus = new mtzButtonBehavior();         //Stack Level Up
        mtzButtonBehavior stackDistanceLessStatus = new mtzButtonBehavior();         //Stack Distance -
        mtzButtonBehavior stackDistanceMoreStatus = new mtzButtonBehavior();         //Stack Distance +
        mtzButtonBehavior stackHalfLevelDownStatus = new mtzButtonBehavior();         //Stack Half Level Down


        double handVerticalStick;                             //Hand Vertical Move
        double handHorizontalStick;                             //Hand Horizontal Move
        double clawClose;                             //Claw Close (Sticky)
        mtzButtonBehavior wristAdjustMoreStatus = new mtzButtonBehavior();         //Wrist Adjust +

        mtzButtonBehavior startButton2Status = new mtzButtonBehavior();         //Pad Select (A & B)


        mtzButtonBehavior handAdjustHigherStatus = new mtzButtonBehavior();         //Hand Adjust Higher
        mtzButtonBehavior handAdjustInStatus = new mtzButtonBehavior();         //Hand Adjust In
        mtzButtonBehavior handAdjustOutStatus = new mtzButtonBehavior();         //Hand Adjust Out
        mtzButtonBehavior handAdjustLowerStatus = new mtzButtonBehavior();         //Hand Adjust Lower


        double handAssist;                             //Ride Height/Drop to 0
        double stoneReleaseStick;                             //Release Stone (RL Flick)
// End of Assignment Mapping
/*************           End     SkyStone Hunchamuncha Left Strafe     Control Pad Map            **************/



        int i=0,j=0;

        telemetry.log().add("Initialized. Go MTZ!");
        /************* Press Play Button ***********************/

        waitForStart();

        while (opModeIsActive()) {  //Loop often to see if controls are still the same


            /***********
             * Gather Button Input
             */
            if (controlPadMap=="SkyStone Hunchamuncha Left Strafe") {

/*************           SkyStone Hunchamuncha Left Strafe     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed
                robotExtenderInButtonStatus.update(gamepad1.left_bumper);             //Park Extender In




                chassisBumpForwardStatus.update(gamepad1.dpad_up);             //Bump Forward
                chassisBumpLeftStatus.update(gamepad1.dpad_left);             //Bump Left
                chassisBumpRightStatus.update(gamepad1.dpad_right);             //Bump Right
                chassisBumpBackStatus.update(gamepad1.dpad_down);             //Bump Backwards


                driveStick1 = gamepad1.left_stick_y;             //Drive 1
                turnStick = gamepad1.left_stick_x;             //Turn
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed
                robotExtenderOutButtonStatus.update(gamepad1.right_bumper);             //Park Extender Out

                startButton1Status.update(gamepad1.start);             //Pad Select (A & B)


                hooksDownButtonStatus.update(gamepad1.y);             //Hooks Down

                blockThrowerButtonStatus.update(gamepad1.b);             //Block Thrower
                hooksUpButtonStatus.update(gamepad1.a);             //Hooks Up


                driveStick2 = gamepad1.right_stick_y;             //Drive 2
                strafeStick = gamepad1.right_stick_x;             //Strafe
                clawOpen = gamepad2.left_trigger;             //Claw Open (Sticky)
                wristAdjustLessStatus.update(gamepad2.left_bumper);             //Wrist Adjust -

                resetAdjustmentsStatus.update(gamepad2.guide);             //Reset Adjustments
                stoneReleaseButtonStatus.update(gamepad2.back);             //Release Stone

                stackLevelUpStatus.update(gamepad2.dpad_up);             //Stack Level Up
                stackDistanceLessStatus.update(gamepad2.dpad_left);             //Stack Distance -
                stackDistanceMoreStatus.update(gamepad2.dpad_right);             //Stack Distance +
                stackHalfLevelDownStatus.update(gamepad2.dpad_down);             //Stack Half Level Down


                handVerticalStick = gamepad2.left_stick_y;             //Hand Vertical Move
                handHorizontalStick = gamepad2.left_stick_x;             //Hand Horizontal Move
                clawClose = gamepad2.right_trigger;             //Claw Close (Sticky)
                wristAdjustMoreStatus.update(gamepad2.right_bumper);             //Wrist Adjust +

                startButton2Status.update(gamepad2.start);             //Pad Select (A & B)


                handAdjustHigherStatus.update(gamepad2.y);             //Hand Adjust Higher
                handAdjustInStatus.update(gamepad2.x);             //Hand Adjust In
                handAdjustOutStatus.update(gamepad2.b);             //Hand Adjust Out
                handAdjustLowerStatus.update(gamepad2.a);             //Hand Adjust Lower


                handAssist = gamepad2.right_stick_y;             //Ride Height/Drop to 0
                stoneReleaseStick = gamepad2.right_stick_x;             //Release Stone (RL Flick)
/*************           End     SkyStone Hunchamuncha Left Strafe     Updates            **************/
            }



            if (wristAdjustLessStatus.clickedDown){
                i=i+1;
            }
            if (wristAdjustLessStatus.isDown){
                j=j+1;
            }
            telemetry.clearAll();
            telemetry.addLine("wristLess D:" + wristAdjustLessStatus.isDown + " U:" + wristAdjustLessStatus.isUp + " cU:"+ wristAdjustLessStatus.clickedUp+ " cD:"+ wristAdjustLessStatus.clickedDown);
            telemetry.addLine("i:"+ i);
            telemetry.addLine("j:"+ j);
            telemetry.addLine("Left Bumper:"+ gamepad2.left_bumper);

            telemetry.update();
        }
    }
}