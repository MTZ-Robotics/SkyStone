package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name ="Red Build Zone Autonomous Op", group = "A_Top")

//@Disabled

public class RedBuildZoneAutonomousOp extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;

    private static final double ticksPerRevolution = 1440;
    private static final double gearReduction = 10.4;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);

    @Override

    public void runOpMode() throws InterruptedException {

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

        claw.setPosition(0);
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        blockThrower.setPosition(1);

        StopAndResetAllEncoders();

        RaiseArm(3,0.35,100);

        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add("Initialized. Go MTZ!!!");

        waitForStart();

        //Align Hooks With Foundation
        Drive(24,0.25, 100);
        telemetry.log().add("Test");
        /*Strafe(12,0.25,100);
        Turn(5,0.25,100);
        Drive(-4,0.25,100);

        //Hook Foundation
        HooksDown();

        //Move Foundation to Build Zone
        Drive(24,0.5,100);
        Turn(2,0.5,100);

        //Unhook Foundation
        HooksUp();

        //Correct Angle and Park
        Turn(-2,0.25,100);
        Strafe(48,0.25,100);*/

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

    public void StopAndResetArmEncoder() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void RunDriveToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void RunArmToPosition() {
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

//Distance Calculation Functions

    public void DriveByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(distance * (int) conversionTicksToInches);
    }

    public void StrafeByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(-distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(-distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(distance * (int) conversionTicksToInches);
    }

    public void TurnByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(-distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(-distance * (int) conversionTicksToInches);
    }

    public void RaiseByInches(int distance) {
    arm.setTargetPosition(distance * (int) conversionTicksToInches);
    }

//Power Functions

    public void DrivePower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    public void ArmPower(double power) {
        arm.setPower(power);
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

    public void Drive(int distance, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            RunDriveToPosition();
            DriveByInches(distance);
            DrivePower(power);
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
            RunDriveToPosition();
            StrafeByInches(distance);
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }

    public void Turn(int distance, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            RunDriveToPosition();
            TurnByInches(distance);
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }

    public void RaiseArm(int distance, double power, int pause) throws InterruptedException {
        if(opModeIsActive()) {
            StopAndResetArmEncoder();
            RunArmToPosition();
            RaiseByInches(distance);
            ArmPower(power);
            while (arm.isBusy()) {
                DisplayArmTelemetry();
            }
            ArmPower(0.2);
            Thread.sleep(pause);
        }
    }

    public void HooksDown() {
        leftHook.setPosition(0);
        rightHook.setPosition(0);
    }

    public void HooksUp() {
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
    }

}