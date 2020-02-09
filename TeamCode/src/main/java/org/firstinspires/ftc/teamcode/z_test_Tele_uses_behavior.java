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

@TeleOp(name="Tele Using Behaviors", group ="z_test")

//@Disabled

public class z_test_Tele_uses_behavior extends LinearOpMode {

    /********************************
     * Timer Variables
     ********************************/
    private ElapsedTime endGameTimer;

    double greenWarningTime;
    double yellowWarningTime;
    double redWarningTime;
    double endGameStart;
    double endGameOver;
    double endGameWarning;
    boolean greenTimerElapsed;
    boolean yellowTimerElapsed;
    boolean redTimerElapsed;
    boolean endGameStartElapsed;

    /***********
     * Lights Control Declarations
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;


    /*************************
     * Motor & Servo Variables
     *************************/
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private DcMotor armExtension;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;
    private Servo wrist;

    double drivePower;
    double armPower;
    double defaultArmPower;

    //boolean rightStrafe;

    @Override

    //This is the opMode call for generically running the opMode in this super class
    public void runOpMode() {

        boolean rightStrafer = false;
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Strafe Right:"+rightStrafer+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(rightStrafer,spurGearArm,driveSpeed);
    }

    //This is the method that handles the controls
    public void controlRobot(Boolean strafeWithRight, Boolean supportArm, Double defaultDrivePower){

        boolean rightStrafe = strafeWithRight;
        boolean accountForArmDrift = supportArm;
        String controlPadMap = "SkyStone Hunchamuncha Left Strafe";

        /***********************
         * Modifiable variables
         **********************/
        endGameStart = 90;
        endGameWarning = endGameStart + 15;
        endGameOver = endGameStart + 30;
        greenWarningTime = 60;
        yellowWarningTime = 70;
        redWarningTime = 80;
        defaultArmPower = 0.75;
        double wristPositionDesired = 0.3;

        /***************
         * Set Timer Variables
         ***************/
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;
        endGameStartElapsed = false;

        /*************
         * Set Lights Variables
         *************/
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        pattern = RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_RED;
        blinkinLedDriver.setPattern(pattern);

        /*******************************
         * Set Motor & Servo Variables
         ******************************/

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        armExtension = hardwareMap.dcMotor.get("armExtension");
        claw = hardwareMap.servo.get("claw");
        wrist = hardwareMap.servo.get("wrist");
        rightHook = hardwareMap.servo.get("rightHook");
        leftHook = hardwareMap.servo.get("leftHook");
        blockThrower = hardwareMap.servo.get("blockThrower");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        armExtension.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);

        /*******
         * Add Button Variables & Objects
         ********/
 //      if (controlPadMap=="SkyStone Hunchamuncha Left Strafe") {

/*************           SkyStone Hunchamuncha Left Strafe     Control Pad Map            **************/
// Assign Variables & Objects for Control Pads
            double chassisSpeedSlow = gamepad1.left_trigger;                              //Slow Speed
            boolean robotExtenderInButton = gamepad1.left_bumper; mtzButtonBehavior robotExtenderInButtonStatus = new mtzButtonBehavior();         //Park Extender In




            boolean chassisBumpForward = gamepad1.dpad_up;mtzButtonBehavior chassisBumpForwardStatus = new mtzButtonBehavior();         //Bump Forward
            boolean chassisBumpLeft = gamepad1.dpad_left; mtzButtonBehavior chassisBumpLeftStatus = new mtzButtonBehavior();         //Bump Left
            boolean chassisBumpRight = gamepad1.dpad_right;mtzButtonBehavior chassisBumpRightStatus = new mtzButtonBehavior();         //Bump Right
            boolean chassisBumpBack = gamepad1.dpad_down; mtzButtonBehavior chassisBumpBackStatus = new mtzButtonBehavior();         //Bump Backwards


            double driveStick1 = gamepad1.left_stick_y;                              //Drive 1
            double turnStick = gamepad1.left_stick_x;                              //Turn
            double chassisSpeedFast = gamepad1.right_trigger;                              //High Speed
            boolean robotExtenderOutButton = gamepad1.right_bumper; mtzButtonBehavior robotExtenderOutButtonStatus = new mtzButtonBehavior();         //Park Extender Out

            boolean startButton1 = gamepad1.start; mtzButtonBehavior startButton1Status = new mtzButtonBehavior();         //Pad Select (A & B)


            boolean hooksDownButton = gamepad1.y;mtzButtonBehavior hooksDownButtonStatus = new mtzButtonBehavior();         //Hooks Down

            boolean blockThrowerButton = gamepad1.b;mtzButtonBehavior blockThrowerButtonStatus = new mtzButtonBehavior();         //Block Thrower
            boolean hooksUpButton = gamepad1.a; mtzButtonBehavior hooksUpButtonStatus = new mtzButtonBehavior();         //Hooks Up


            double driveStick2 = gamepad1.right_stick_y;                              //Drive 2
            double strafeStick = gamepad1.right_stick_x;                              //Strafe
            double clawOpen = gamepad2.left_trigger;                              //Claw Open (Sticky)
            boolean wristAdjustLess = gamepad2.left_bumper; mtzButtonBehavior wristAdjustLessStatus = new mtzButtonBehavior();         //Wrist Adjust -

            boolean resetAdjustments = gamepad2.guide; mtzButtonBehavior resetAdjustmentsStatus = new mtzButtonBehavior();         //Reset Adjustments
            boolean stoneReleaseButton = gamepad2.back;mtzButtonBehavior stoneReleaseButtonStatus = new mtzButtonBehavior();         //Release Stone

            boolean stackLevelUp = gamepad2.dpad_up;mtzButtonBehavior stackLevelUpStatus = new mtzButtonBehavior();         //Stack Level Up
            boolean stackDistanceLess = gamepad2.dpad_left; mtzButtonBehavior stackDistanceLessStatus = new mtzButtonBehavior();         //Stack Distance -
            boolean stackDistanceMore = gamepad2.dpad_right;mtzButtonBehavior stackDistanceMoreStatus = new mtzButtonBehavior();         //Stack Distance +
            boolean stackHalfLevelDown = gamepad2.dpad_down; mtzButtonBehavior stackHalfLevelDownStatus = new mtzButtonBehavior();         //Stack Half Level Down


            double handVerticalStick = gamepad2.left_stick_y;                              //Hand Vertical Move
            double handHorizontalStick = gamepad2.left_stick_x;                              //Hand Horizontal Move
            double clawClose = gamepad2.right_trigger;                              //Claw Close (Sticky)
            boolean wristAdjustMore = gamepad2.right_bumper; mtzButtonBehavior wristAdjustMoreStatus = new mtzButtonBehavior();         //Wrist Adjust +

            boolean startButton2 = gamepad2.start; mtzButtonBehavior startButton2Status = new mtzButtonBehavior();         //Pad Select (A & B)


            boolean handAdjustHigher = gamepad2.y;mtzButtonBehavior handAdjustHigherStatus = new mtzButtonBehavior();         //Hand Adjust Higher
            boolean handAdjustIn = gamepad2.x; mtzButtonBehavior handAdjustInStatus = new mtzButtonBehavior();         //Hand Adjust In
            boolean handAdjustOut = gamepad2.b;mtzButtonBehavior handAdjustOutStatus = new mtzButtonBehavior();         //Hand Adjust Out
            boolean handAdjustLower = gamepad2.a; mtzButtonBehavior handAdjustLowerStatus = new mtzButtonBehavior();         //Hand Adjust Lower


            double handAssist = gamepad2.right_stick_y;                              //Ride Height/Drop to 0
            double stoneReleaseStick = gamepad2.right_stick_x;                              //Release Stone (RL Flick)
// End of Assignment Mapping
/*************           End     SkyStone Hunchamuncha Left Strafe     Control Pad Map            **************/
//        }




        /**********************************
         * Set positions on initialize
         **********************************/

        claw.setPosition(1);
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        blockThrower.setPosition(1);
        wrist.setPosition(0);

        /***********************************************
         * Tell driver station that initialization complete
         **********************************************/
        telemetry.log().add("Initialized. Go MTZ! Timer Set for ");

        telemetry.log().add(Double.toString(greenWarningTime)+" s, " +
                Double.toString(yellowWarningTime)+" s, " +
                Double.toString(redWarningTime)+" s, " +
                Double.toString(endGameStart)+" s, "
        );

        /************* Press Play Button ***********************/

        waitForStart();

        pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        blinkinLedDriver.setPattern(pattern);

        //Start timer here since play was just pressed
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {  //Loop often to see if controls are still the same

            displayTelemetry();


            /***********
             * Gather Button Input
             */
            if (controlPadMap=="SkyStone Hunchamuncha Left Strafe") {

/*************           SkyStone Hunchamuncha Left Strafe     Controls Update Status           **************/

                robotExtenderInButtonStatus.update(robotExtenderInButton);




                chassisBumpForwardStatus.update(chassisBumpForward);
                chassisBumpLeftStatus.update(chassisBumpLeft);
                chassisBumpRightStatus.update(chassisBumpRight);
                chassisBumpBackStatus.update(chassisBumpBack);





                robotExtenderOutButtonStatus.update(robotExtenderOutButton);

                startButton1Status.update(startButton1);


                hooksDownButtonStatus.update(hooksDownButton);

                blockThrowerButtonStatus.update(blockThrowerButton);
                hooksUpButtonStatus.update(hooksUpButton);





                wristAdjustLessStatus.update(wristAdjustLess);

                resetAdjustmentsStatus.update(resetAdjustments);
                stoneReleaseButtonStatus.update(stoneReleaseButton);

                stackLevelUpStatus.update(stackLevelUp);
                stackDistanceLessStatus.update(stackDistanceLess);
                stackDistanceMoreStatus.update(stackDistanceMore);
                stackHalfLevelDownStatus.update(stackHalfLevelDown);





                wristAdjustMoreStatus.update(wristAdjustMore);

                startButton2Status.update(startButton2);


                handAdjustHigherStatus.update(handAdjustHigher);
                handAdjustInStatus.update(handAdjustIn);
                handAdjustOutStatus.update(handAdjustOut);
                handAdjustLowerStatus.update(handAdjustLower);




/*************           End     SkyStone Hunchamuncha Left Strafe     Updates            **************/
            }


//            wristAdjustLessStatus.update(wristAdjustLess);
//            wristAdjustMoreStatus.update(wristAdjustMore);
            /*********************
             * Speed adjust with triggers
             ********************/
            if (gamepad1.right_trigger > 0) {
                drivePower = defaultDrivePower*2;
            } else if (gamepad1.left_trigger > 0) {
                drivePower = defaultDrivePower/2;
                } else {
                    drivePower = defaultDrivePower;
            }

            /*****************
             * Hook Controls
             ****************/

            if (gamepad2.dpad_down) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
                //drivePower = defaultDrivePower/2;
                drivePower = .1;
            }
            if (gamepad2.dpad_up) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }

            /*************************
             * Chassis drive controls
             *************************/

            if(rightStrafe) {
                backLeft.setPower(drivePower * ((gamepad1.left_stick_y + gamepad1.right_stick_x) - gamepad1.left_stick_x));
                backRight.setPower(drivePower * ((gamepad1.left_stick_y - gamepad1.right_stick_x) + gamepad1.left_stick_x));
                frontLeft.setPower(drivePower * ((-gamepad1.left_stick_y + gamepad1.right_stick_x) + gamepad1.left_stick_x));
                frontRight.setPower(drivePower * ((-gamepad1.left_stick_y - gamepad1.right_stick_x) - gamepad1.left_stick_x));
            } else {
                backLeft.setPower(drivePower * ((gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
                backRight.setPower(drivePower * ((gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
                frontLeft.setPower(drivePower * ((-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));
                frontRight.setPower(drivePower * ((-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));
            }

            /*************
             * Arm Controls
             *************/
            /*

            if (gamepad2.right_trigger > 0) {

                armPower = 0.5;
            } else if (gamepad2.left_trigger > 0) {
                armPower = 0.2;
            } else {
                armPower = 0.35;
            }
             */

            armPower = defaultArmPower;


            if (accountForArmDrift) {

                arm.setPower( armPower * (gamepad2.left_stick_y) - 0.2 );
            } else {

                arm.setPower( armPower * (gamepad2.left_stick_y) );
            }

            armExtension.setPower((gamepad2.left_stick_x));

            /*************
             * Claw Controls
             *************/

            claw.setPosition(1-gamepad2.right_stick_y);

            /*************
             * Wrist Controls
             *************/

            //wrist.setPosition(gamepad2.right_stick_x);

            if (wristAdjustLessStatus.clickedUp){
                wristPositionDesired = wristPositionDesired - 0.05;
            } else if (wristAdjustMoreStatus.clickedUp) {
                wristPositionDesired = wristPositionDesired + 0.05;
            }

            wrist.setPosition(wristPositionDesired);

            /************************
             * Cap Stone thrower controls
             ***********************/

            if(gamepad2.a){
                blockThrower.setPosition(0.45);
            } else {
                blockThrower.setPosition(1);
            }

            /*********************************
             * Check if timer has elapsed
             *********************************/
            //Check for End Timer First
            if (endGameTimer.seconds()>endGameOver){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>endGameWarning){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>endGameStart){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>redWarningTime){ //Then check for red
                redTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>yellowWarningTime){ //Then check for yellow
                yellowTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>greenWarningTime){ //Then check for green
                greenTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                blinkinLedDriver.setPattern(pattern);
            }
        }
    }

    public void displayTelemetry() {
        telemetry.clearAll();
        telemetry.addLine()
                .addData("Timer: ", endGameTimer.toString());
        telemetry.addLine()
                .addData("Front Left Power: ", frontLeft.getPower());
        telemetry.addLine()
                .addData("Front Right Power: ", frontRight.getPower());
        telemetry.addLine()
                .addData("Back Left Power: ", backLeft.getPower());
        telemetry.addLine()
                .addData("Back Right Power: ", backRight.getPower());
        telemetry.addLine()
                .addData("Arm Power: ", arm.getPower());
        telemetry.addLine()
                .addData("Arm Extender Power: ", armExtension.getPower());
        telemetry.addLine()
                .addData("Claw Position: ", claw.getPosition());
        telemetry.addLine()
                .addData("Wrist Position: ", wrist.getPosition());
        telemetry.addLine()
                .addData("Left Hook Position: ", leftHook.getPosition());
        telemetry.addLine()
                .addData("Right Hook Position: ", rightHook.getPosition());
        telemetry.addLine()
                .addData("Block Thrower Position: ", blockThrower.getPosition());
        telemetry.update();
    }
}
