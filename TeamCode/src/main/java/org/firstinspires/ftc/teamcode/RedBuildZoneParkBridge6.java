package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name ="Red Build Zone Park Bridge", group = "A_Top")

//@Disabled

public class RedBuildZoneParkBridge6 extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;

    private static final double ticksPerRevolution = 145.6;
    private static final double gearReduction = 2.0;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);
    private static final double experimentalInchesPerTurn = 91.8;

    /**************
     *
     * Modify these speeds to help with diagnosing drive errors
     *
     */
    private static final double defaultDriveSpeed = 0.1;
    private static final double defaultTurnSpeed = 0.1;
    private static final int defaultPauseTime = 1000;

    @Override

    public void runOpMode() throws InterruptedException {

        /**************
         *
         * Declare motors and servos
         *
         */
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
        /**********
         * Commenting out arm reverse to see if it will keep the arm in the correct direction
         */
        //arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);

        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        StopAndResetAllEncoders();

        /************
         * Raise the arm to fit within 18" long dimension
         */

        RaiseArm(14,100);

        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add("Initialized. Go MTZ!!!");

        waitForStart();

        //Align Hooks With Foundation
        Drive(-24,0.12, 200);
        Strafe(-12,0.12, 200);
        Drive(-5,0.12,200);

        //Hook Foundation
        HooksDown();

        //Move Foundation to Build Zone
        Drive(20,0.2,200);
        Turn(40,0.2,200);
        Drive (5,0.2,200);
        Turn(80,0.2,200);
        Strafe(5,0.2,200);
        Drive(-12,0.1,200);

        //Unhook Foundation
        HooksUp();

        //Align to Park
        Strafe(8,0.1,200);
        Drive(24,0.1,200);

        //Lower arm gracefully
        LowerArm(10,1000);

        //Raise arm a little bit
        RaiseArm(3,200);

        //Park
        Drive(14,0.1,0);
    }

//Encoder Functions

    public void StopAndResetAllEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void StopAndResetDriveEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public void RunDriveToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


//Distance Calculation Functions

    public void DriveByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(-1 * distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(-1 * distance * (int) conversionTicksToInches);
    }

    public void StrafeByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(-distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(-distance * (int) conversionTicksToInches);
    }

    public void TurnByAngle(int degrees) {
        frontLeft.setTargetPosition(degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        frontRight.setTargetPosition(-degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        backLeft.setTargetPosition(-degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        backRight.setTargetPosition(degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
    }


//Power Functions

    public void DrivePower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }


 //Telemetry Functions

    public void DisplayDriveTelemetry() {
        double frontLeftInches = frontLeft.getCurrentPosition() / conversionTicksToInches;
        double frontRightInches = frontRight.getCurrentPosition() / conversionTicksToInches;
        double backLeftInches = backLeft.getCurrentPosition() / conversionTicksToInches;
        double backRightInches = backRight.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Front Left Inches ", (int) frontLeftInches + "   Power: " + "%.1f", frontLeft.getPower());
        telemetry.addLine()
                .addData("Front Right Inches: ", (int) frontRightInches + "   Power: " + "%.1f", frontRight.getPower());
        telemetry.addLine()
                .addData("Back Left Inches: ", (int) backLeftInches + "   Power: " + "%.1f", backLeft.getPower());
        telemetry.addLine()
                .addData("Back Right Inches: ", (int) backRightInches + "   Power: " + "%.1f", backRight.getPower());
        telemetry.update();
    }

    public void DisplayArmTelemetry() {
        double armInches = arm.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Arm Inches ", (int) armInches + "  Power: " + "%.1f", arm.getPower());
        telemetry.update();
    }

//Motion Functions

    public void Drive(int distance, double motorPower, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            DriveByInches(distance);
            RunDriveToPosition();
            DrivePower(motorPower);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }

    public void Strafe(int distance, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            StrafeByInches(distance);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }

    public void Turn(int degrees, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            TurnByAngle(degrees);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }


    public void StopAndResetArmEncoder() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void RunArmToPosition() {
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void RaiseByInches(int distance) {
        arm.setTargetPosition(distance * 45);
    }
    public void ArmPower(double power) {
        arm.setPower(power);
    }

    public void RaiseArm(int distance, int pause) throws InterruptedException {
        /**********
         * Use time based arm controls since the arm falls when the target distance is reached
         */

        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(0.4);
        //1000 ms = 12 inches
        sleep(distance * 1000/12);
        //sleep(1000);
        arm.setPower(0.2);
        Thread.sleep(pause);
    }

    public void LowerArm(int distance, int pause) throws InterruptedException {
        arm.setPower(0.1);
        sleep(distance * 100);
        arm.setPower(0);
        Thread.sleep(pause);
    }
    public void HooksDown() {
        leftHook.setPosition(0);
        rightHook.setPosition(0);
        sleep(1500);
    }

    public void HooksUp() {
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        sleep(1500);
    }

}