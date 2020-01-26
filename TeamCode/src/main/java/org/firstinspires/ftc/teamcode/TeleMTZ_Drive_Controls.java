package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleMTZ_Drive_Controls", group ="A_Top")

//@Disabled

public class TeleMTZ_Drive_Controls extends LinearOpMode {

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
    View relativeLayout;

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
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;
    //private Servo blinkin;

    double drivePower;
    double armPower;
    double defaultArmPower;

    //boolean rightStrafe;

    @Override

    //This is the opMode call for generically running the opMode in this super class
    public void runOpMode() {

        boolean rightStrafer = true;
        boolean spurGearArm = true;
        double driveSpeed = 0.5;
        telemetry.log().add("Strafe Right:"+rightStrafer+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(rightStrafer,spurGearArm,driveSpeed);
    }

    //This is the method that handles the controls
    public void controlRobot(Boolean strafeWithRight, Boolean supportArm, Double defaultDrivePower){

        boolean rightStrafe = strafeWithRight;
        boolean accountForArmDrift = supportArm;

        /***********************
         * Modifiable variables
         **********************/
        //rightStrafe = false;
        endGameStart = 15;
        endGameWarning = endGameStart + 15;
        endGameOver = endGameStart + 30;
        greenWarningTime = 3;
        yellowWarningTime = 6;
        redWarningTime = 9;
        //defaultDrivePower = 0.5;
        defaultArmPower = 0.35;



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

        pattern = RevBlinkinLedDriver.BlinkinPattern.DARK_RED;
        blinkinLedDriver.setPattern(pattern);


        /*******************************
         * Set Motor & Servo Variables
         ******************************/


        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        claw = hardwareMap.servo.get("claw");
        rightHook = hardwareMap.servo.get("rightHook");
        leftHook = hardwareMap.servo.get("leftHook");
        blockThrower = hardwareMap.servo.get("blockThrower");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);

        /**********************************
         * Set positions on initialize
         **********************************/

        claw.setPosition(0);
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        blockThrower.setPosition(1);

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

        //Start timer here since play was just pressed
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {  //Loop often to see if controls are still the same

            displayTelemetry();

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


            /*************
             * Claw Controls
             *************/

            claw.setPosition(gamepad2.right_stick_y);



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
            // Another variable time is included in OpMode
            if (endGameTimer.seconds()>endGameOver){
                endGameStartElapsed = true;

                pattern = RevBlinkinLedDriver.BlinkinPattern.TWINKLES_RAINBOW_PALETTE;
                blinkinLedDriver.setPattern(pattern);
                alertWhite();
            } else if (endGameTimer.seconds()>endGameWarning){
                endGameStartElapsed = true;

                pattern = RevBlinkinLedDriver.BlinkinPattern.TWINKLES_RAINBOW_PALETTE;
                blinkinLedDriver.setPattern(pattern);
                alertWhite();
            } else if (endGameTimer.seconds()>endGameStart){
                endGameStartElapsed = true;

                pattern = RevBlinkinLedDriver.BlinkinPattern.TWINKLES_RAINBOW_PALETTE;
                blinkinLedDriver.setPattern(pattern);
                alertWhite();
            } else if (endGameTimer.seconds()>redWarningTime){ //Then check for red
                redTimerElapsed = true;

                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                blinkinLedDriver.setPattern(pattern);
                alertRed();
            } else if (endGameTimer.seconds()>yellowWarningTime){ //Then check for yellow
                yellowTimerElapsed = true;

                pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                blinkinLedDriver.setPattern(pattern);
                alertYellow();
            } else if (endGameTimer.seconds()>greenWarningTime){ //Then check for green
                greenTimerElapsed = true;

                alertGreen();
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
                .addData("Claw Position: ", claw.getPosition());
        telemetry.addLine()
                .addData("Left Hook Position: ", leftHook.getPosition());
        telemetry.addLine()
                .addData("Right Hook Position: ", rightHook.getPosition());
        telemetry.addLine()
                .addData("Block Thrower Position: ", blockThrower.getPosition());
        telemetry.update();
    }

    public void alertGreen(){
        //driver station background green
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.GREEN);
            }
        });

    }
    public void alertYellow(){
        //driver station background yellow
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.YELLOW);
            }
        });
    }
    public void alertRed(){
        //driver station background red
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.RED);
            }
        });
    }
    public void alertWhite(){
        //driver station background White
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
    }


}
