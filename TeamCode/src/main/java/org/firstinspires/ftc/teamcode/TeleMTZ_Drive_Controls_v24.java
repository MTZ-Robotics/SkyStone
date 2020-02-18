package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleMTZ_Drive_Controls_v24 Turmeric", group ="A_Top")

//@Disabled

public class TeleMTZ_Drive_Controls_v24 extends LinearOpMode {

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


    /*******
     * Add Button Variables & Objects
     ********/

    /*************           MTZ Controls Variables            **************/
// Assign Variables & Objects for Control Pads
    double chassisSpeedSlow;                             //Slow Speed
    mtzButtonBehavior chassisBumpLeftTurnStatus = new mtzButtonBehavior();         //Bump Left Turn




    mtzButtonBehavior chassisBumpForwardStatus = new mtzButtonBehavior();         //Bump Forward
    mtzButtonBehavior chassisBumpLeftStrafeStatus = new mtzButtonBehavior();         //Bump Left Strafe
    mtzButtonBehavior chassisBumpRightStrafeStatus = new mtzButtonBehavior();         //Bump Right Strafe
    mtzButtonBehavior chassisBumpBackStatus = new mtzButtonBehavior();         //Bump Backwards

    mtzButtonBehavior robotExtenderInButtonStatus = new mtzButtonBehavior();         //Park Extender In
    double driveStick1;                             //Drive 1
    double turnStick;                             //Turn
    double chassisSpeedFast;                             //High Speed
    mtzButtonBehavior chassisBumpRightTurnStatus = new mtzButtonBehavior();         //Bump Right Turn

    mtzButtonBehavior startButton1Status = new mtzButtonBehavior();         //Pad Select (A & B)


    mtzButtonBehavior hooksDownButtonStatus = new mtzButtonBehavior();         //Hooks Down

    mtzButtonBehavior blockThrowerButtonStatus = new mtzButtonBehavior();         //Block Thrower
    mtzButtonBehavior hooksUpButtonStatus = new mtzButtonBehavior();         //Hooks Up

    mtzButtonBehavior robotExtenderOutButtonStatus = new mtzButtonBehavior();         //Park Extender Out
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
    /*************           End     MTZ Control Pad Variables            **************/


    @Override

    //This is the opMode call for generically running the opMode in this super class
    public void runOpMode() {


        String controlPadMap = "SkyStone Hunchamuncha Left Strafe";
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Controls Map:"+controlPadMap+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(controlPadMap,spurGearArm,driveSpeed);
    }

    //This is the method that handles the controls
    public void controlRobot(String controlPadMap, Boolean supportArm, Double defaultDrivePower){

        boolean accountForArmDrift = supportArm;

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
        double wristPositionDesired = 0.5;

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

        /**********************************
         * Set positions on initialize
         **********************************/

        /*claw.setPosition(1);
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        blockThrower.setPosition(1);
        wrist.setPosition(.5);*/

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



            /***********
             * Gather Button Input
             */

            if (controlPadMap=="SkyStone Hunchamuncha Left Strafe") {

/*************           SkyStone Hunchamuncha Left Strafe     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed
                chassisBumpLeftTurnStatus.update(gamepad1.left_bumper);             //Bump Left Turn




                chassisBumpForwardStatus.update(gamepad1.dpad_up);             //Bump Forward
                chassisBumpLeftStrafeStatus.update(gamepad1.dpad_left);             //Bump Left Strafe
                chassisBumpRightStrafeStatus.update(gamepad1.dpad_right);             //Bump Right Strafe
                chassisBumpBackStatus.update(gamepad1.dpad_down);             //Bump Backwards

                robotExtenderInButtonStatus.update(gamepad1.left_stick_button);             //Park Extender In
                driveStick1 = gamepad1.left_stick_y;             //Drive 1
                turnStick = gamepad1.left_stick_x;             //Turn
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed
                chassisBumpRightTurnStatus.update(gamepad1.right_bumper);             //Bump Right Turn

                startButton1Status.update(gamepad1.start);             //Pad Select (A & B)


                hooksDownButtonStatus.update(gamepad1.y);             //Hooks Down

                blockThrowerButtonStatus.update(gamepad1.b);             //Block Thrower
                hooksUpButtonStatus.update(gamepad1.a);             //Hooks Up

                robotExtenderOutButtonStatus.update(gamepad1.left_stick_button);             //Park Extender Out
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
            } else if (controlPadMap=="SkyStone Hand Turkey Right Strafe") {
/*************           SkyStone Hand Turkey Right Strafe     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed
                chassisBumpLeftTurnStatus.update(gamepad1.left_bumper);             //Bump Left Turn




                chassisBumpForwardStatus.update(gamepad1.dpad_up);             //Bump Forward
                chassisBumpLeftStrafeStatus.update(gamepad1.dpad_left);             //Bump Left Strafe
                chassisBumpRightStrafeStatus.update(gamepad1.dpad_right);             //Bump Right Strafe
                chassisBumpBackStatus.update(gamepad1.dpad_down);             //Bump Backwards

                robotExtenderInButtonStatus.update(gamepad1.left_stick_button);             //Park Extender In
                driveStick1 = gamepad1.left_stick_y;             //Drive 1
                strafeStick = gamepad1.left_stick_x;             //Strafe
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed
                chassisBumpRightTurnStatus.update(gamepad1.right_bumper);             //Bump Right Turn

                startButton1Status.update(gamepad1.start);             //Pad Select (A & B)


                hooksDownButtonStatus.update(gamepad1.y);             //Hooks Down

                blockThrowerButtonStatus.update(gamepad1.b);             //Block Thrower
                hooksUpButtonStatus.update(gamepad1.a);             //Hooks Up

                robotExtenderOutButtonStatus.update(gamepad1.left_stick_button);             //Park Extender Out
                driveStick2 = gamepad1.right_stick_y;             //Drive 2
                turnStick = gamepad1.right_stick_x;             //Turn
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
/*************           End     SkyStone Hand Turkey Right Strafe     Updates            **************/
            } else if (controlPadMap=="SkyStone Right Strafe") {

/*************           SkyStone Right Strafe     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed












                strafeStick = gamepad1.left_stick_x;             //Strafe
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed











                driveStick1 = gamepad1.right_stick_y;             //Drive
                turnStick = gamepad1.right_stick_x;             //Turn

                wristAdjustLessStatus.update(gamepad2.left_bumper);             //Wrist Adjust +




                hooksDownButtonStatus.update(gamepad2.dpad_up);             //Hooks Up


                hooksUpButtonStatus.update(gamepad2.dpad_down);             //Hooks Down


                handVerticalStick = gamepad2.left_stick_y;             //Arm Angle
                handHorizontalStick = gamepad2.left_stick_x;             //Arm Extension

                mtzButtonBehavior wristAdjustMoreStatus = new mtzButtonBehavior();         //Wrist Adjust -







                blockThrowerButtonStatus.update(gamepad2.a);             //Block Thrower


                clawClose = gamepad2.right_stick_y;             //Claw

/*************           End     SkyStone Right Strafe     Updates            **************/
            } else if (controlPadMap=="SkyStone Left Strafe") {

/*************           SkyStone Left Strafe     Controls Update Status           **************/
                chassisSpeedSlow = gamepad1.left_trigger;             //Slow Speed











                driveStick1 = gamepad1.left_stick_y;             //Drive
                turnStick = gamepad1.left_stick_x;             //Turn
                chassisSpeedFast = gamepad1.right_trigger;             //High Speed












                strafeStick = gamepad1.right_stick_x;             //Strafe

                wristAdjustLessStatus.update(gamepad2.left_bumper);             //Wrist Adjust +




                hooksDownButtonStatus.update(gamepad2.dpad_up);             //Hooks Up


                hooksUpButtonStatus.update(gamepad2.dpad_down);             //Hooks Down


                handVerticalStick = gamepad2.left_stick_y;             //Arm Angle
                handHorizontalStick = gamepad2.left_stick_x;             //Arm Extension

                wristAdjustMoreStatus.update(gamepad2.right_bumper);             //Wrist Adjust -







                blockThrowerButtonStatus.update(gamepad2.a);             //Block Thrower


                clawClose = gamepad2.right_stick_y;             //Claw

/*************           End     SkyStone Left Strafe     Updates            **************/
            }




            displayTelemetry();

            /*********************
             * Speed adjust with triggers
             ********************/
            if (chassisSpeedFast > 0) {
                drivePower = defaultDrivePower*2;
            } else if (chassisSpeedSlow > 0) {
                drivePower = defaultDrivePower * 0.7;
                } else {
                    drivePower = defaultDrivePower;
            }

            /*****************
             * Hook Controls
             ****************/

            if (hooksDownButtonStatus.isDown) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
                //drivePower = defaultDrivePower/2;
                drivePower = .1;
            }
            if (hooksUpButtonStatus.isDown) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }

            /*************************
             * Chassis drive controls
             *************************/

            backLeft.setPower(drivePower * ((driveStick1 + strafeStick) - turnStick));
            backRight.setPower(drivePower * ((driveStick1 - strafeStick) + turnStick));
            frontLeft.setPower(drivePower * ((-driveStick1 + strafeStick) + turnStick));
            frontRight.setPower(drivePower * ((-driveStick1 - strafeStick) - turnStick));




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

                arm.setPower( armPower * (handVerticalStick) - 0.2 );
            } else {

               if(handVerticalStick < 0) {
                   arm.setPower(armPower * (handVerticalStick));
               } else {
                   arm.setPower(0.75 * armPower * (handVerticalStick));
               }
            }

            armExtension.setPower((handHorizontalStick));

            /*************
             * Claw Controls
             *************/

            claw.setPosition(1-clawClose);

            /*************
             * Wrist Controls
             *************/

            if (wristAdjustLessStatus.clickedDown) {
                wristPositionDesired = wristPositionDesired - 0.01;
            } else if (wristAdjustMoreStatus.clickedDown) {
                wristPositionDesired = wristPositionDesired + 0.01;
            }

            wrist.setPosition(wristPositionDesired);

            /************************
             * Cap Stone thrower controls
             ***********************/

            if(blockThrowerButtonStatus.isDown){
                blockThrower.setPosition(0.55);
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
