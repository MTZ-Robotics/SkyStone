package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Left Strafe Driver Control Op", group = "AB_Top")

//@Disabled

public class LeftStrafeDriverControlOp extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;

    double drivePower;
    double armPower;

    @Override

    public void runOpMode() {

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

        telemetry.log().add("Initialized. Go MTZ!!!");

        waitForStart();

        while (opModeIsActive()) {

            displayTelemetry();


            if (gamepad1.right_trigger > 0) {
                drivePower = 1;
            } else if (gamepad1.left_trigger > 0) {
                drivePower = 0.25;
                } else {
                    drivePower = 0.5;
            }

            backLeft.setPower(drivePower * ((gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
            backRight.setPower(drivePower * ((gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontLeft.setPower(drivePower * ((-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontRight.setPower(drivePower * ((-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));

            if (gamepad2.right_trigger > 0) {
                armPower = 0.5;
            } else if (gamepad2.left_trigger > 0) {
                armPower = 0.2;
            } else {
                armPower = 0.35;
            }

            arm.setPower(armPower * (gamepad2.left_stick_y) - 0.2);

            claw.setPosition(gamepad2.right_stick_y);

            if (gamepad2.dpad_down) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
            }
            if (gamepad2.dpad_up) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }

            if(gamepad2.a){
                blockThrower.setPosition(0.45);
            } else {
                blockThrower.setPosition(1);
            }

        }
    }

    public void displayTelemetry() {
        telemetry.clear();
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

}
