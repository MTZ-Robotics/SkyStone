package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*************************************
 *
 * Op Mode Name below must be changed to show up in OpMode List on driver station
 *
 * Name should be short and descriptive
 *
 * Group should be one of the following:
 * Test - If it is dedicated to testing functionality of network or hardware or systems
 * Concept - If the opMode has not been validated
 * A_Top - If the OpMode is ready to use in competition
 *
 **************************************/

@TeleOp(name="Concept Driver 11", group ="Concept")

/**********************************************
 *
 * Driver 11
 *
 * Meccanum chassis with 0.5 motor speed factor with boost available
 * Arm uses a constant power to keep the arm in position while gravity tries to pull it down
 * Drive by Time and Power is used to estimate displacement
 * Standard servo claw from 0-1 positions
 * Standard servo hooks on the back of the bot from 0 to 0.5 positions
 * Standard servo blockThrower
 *
 * Future versions should add
 *  drive by encoder
 *  Camera assistance
 *  Bump chassis forward
 *
 * Revisions
 *  Driver 11 Changes from Driver 10
 *   Added block thrower servo controls
 *
 *  Driver 10 Changes from Driver 9
 *   Added Boost to chassis power - Lindsey
 *   Set default chassis power to 0.5 - Lindsey
 *
 *
 *  Driver 9 Changes from previous version, Version 5:
 *   Added Comments in a number of places - KWS
 *   Removed reverse and halfpower controls similar to version 7
 *   Changed claw control to use Right Stick same as version 7
 *
 *************************************************/

/***
 * Comment out @Disabled below if you want it to show in the list of OpModes in the phone
 */
//@Disabled


public class DriverControlOpMTZ_11 extends LinearOpMode {

    //Determine variables for hardware to map to
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;

    @Override

    public void runOpMode() {

        //Map hardware to variables here

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        claw = hardwareMap.servo.get("claw");
        rightHook = hardwareMap.servo.get("rightHook");
        leftHook = hardwareMap.servo.get("leftHook");
        blockThrower = hardwareMap.servo.get("blockThrower");

        //Set default values for mapped hardware directions and positions

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);
        /*********
         Commented out below reverse for Arm:
         It would be nice to fix the formula and use this
         to have the arm functions make sense someday
        ****************/
        /*
        arm.setDirection(DcMotor.Direction.REVERSE);
        ************************* End of Comment Out */

        //Claw Open
        claw.setPosition(0);
        //Hooks Up
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        /*******************************
         * Setting the default drive power to 0.5
         * so that the chassis is slow
         * enough to control easily
         ******************************/

        double defaultDrivePower = 0.5;
        double drivePower = defaultDrivePower;

        /**************************
         * Set block thrower servo values
         **************************/

        double blockThrowerUp = 0.33;
        double blockThrowerDown = 1.0;
        //Default Block Thrower Up
        blockThrower.setPosition(blockThrowerUp);

        /************************************
         * Setting drive direction to 1
         * so that the chassis goes forward
         * when the stick is pressed forward
         ***********************************/

        double driveDirection = 1;

        /* Commenting out below since this is not needed right now

        //Used for storing a state of the buttons for changing power and reverse

        boolean aButtonPressed = false;
        boolean bButtonPressed = false;

        *************** End of commenting out */

        /**************************************
         * Output that the values are loaded and the bot is initialized
         **************************************/

        stat("HardwareMap Complete: Go MTZ!!! ");

        /**************
         * Play Button
         **************/
        waitForStart();

        while (opModeIsActive()) {


            // Commenting out the speed changing feature and the reversing feature for the chassis
            // since they were confusing to the drivers

            /*

            //Speed Changing Feature
            if (gamepad1.a) {
                aButtonPressed = true;
            } else if (aButtonPressed) {
                aButtonPressed = false;
                if (drivePower == 0.5) {
                    drivePower = 1;
                } else {
                    drivePower = 0.5;
                }
            }

            //Chassis Drive direction changing feature
            if (gamepad1.b) {
                bButtonPressed = true;
            } else if (bButtonPressed) {
                bButtonPressed = false;
                if (driveDirection == -1) {
                    driveDirection = 1;
                } else {
                    driveDirection = -1;
                }
            }

            ***************************** End of power and reverse comment out  */

            /**********************
             * Adjust DrivePower with Right Trigger
             */
            drivePower = drivePower + gamepad1.right_trigger * (1 - defaultDrivePower);

            /**********************************************
             * Output power values to driver station screen
             **********************************************/

            stat(new String[]{"Drive Power (A): " + drivePower, "Drive Direction (B): " + driveDirection, "Arm Power: " + (gamepad2.left_stick_y - 0.2) * -1, "Claw Position" + claw.getPosition()});


            /**************************************************
             * Controls
             *
             * Controller 1: Left Stick:  Forward, Backwards, Right and Left Rotate
             * Controller 1: Right Stick: Strafe Right and Left
             * Controller 1: Right Trigger: Boost Chassis Speed
             * Controller 1: A, X, Y, B: Block Thrower
             *
             * Controller 2: Left Stick:  Arm Up & Down
             * Controller 2: Bumpers:     Arm bump up and down
             *
             * Controller 2: Right Stick: Up - Claw Close, Center - Claw Open
             *
             *
             **************************************************/

            //Old Controls
            /*
             * Controller 2: A Button:    Claw Close
             * Controller 2: B Button:    Claw Open
             */

            //Arm raise
            arm.setPower((0.5*(gamepad2.left_stick_y)-0.2)* -1);

            //Arm Bump
            if (gamepad2.left_bumper) {
                arm.setPower(0);
                sleep(100);
                arm.setPower(0.2);
            }
            if (gamepad2.right_bumper) {
                arm.setPower(0.4);
                sleep(100);
                arm.setPower(0.2);
            }

            //Chassis Motor Powers
            backLeft.setPower(drivePower * (driveDirection * (gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
            backRight.setPower(drivePower * (driveDirection * (gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontLeft.setPower(drivePower * (driveDirection * (-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontRight.setPower(drivePower * (driveDirection * (-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));

            //Claw close and open on release
            claw.setPosition(gamepad2.right_stick_y);

            /* Commenting out old claw control code

            if(gamepad2.b){
                claw.setPosition(0);
            }
            if(gamepad2.a){
                claw.setPosition(1);
            }

            */

            //Block Thrower
            if(gamepad1.a){
                blockThrower.setPosition(blockThrowerDown);
            }
            if(gamepad1.b){
                blockThrower.setPosition(0);
            }
            if(gamepad1.y){
                blockThrower.setPosition(blockThrowerUp);
            }
            if(gamepad1.a){
                blockThrower.setPosition(0.75);
            }
            //Hooks
            if (gamepad2.dpad_down) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
            }
            if (gamepad2.dpad_up) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }
        }
    }

    public void stat(String[] in){
        for(String a : in){
            telemetry.addData("Status",a);
        }
        telemetry.update();
    }

    public void stat(String in){
        telemetry.addData("Status",in);
        telemetry.update();
    }

    public void motorstop(){
        for(DcMotor M : new DcMotor[]{frontLeft,backLeft,frontRight,backRight,arm}){
            M.setPower(0);
        }
    }
}
