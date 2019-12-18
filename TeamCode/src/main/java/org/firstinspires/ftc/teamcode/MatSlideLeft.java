package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name ="MatSlideLeft", group ="AA_Top")
//@Disabled

public class MatSlideLeft extends LinearOpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor arm;
    Servo claw;
    Servo rightHook;
    Servo leftHook;

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

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);

        claw.setPosition(0);
        rightHook.setPosition(0.5);
        leftHook.setPosition(0.5);

        arm.setPower(0.4);
        sleep(1000);
        arm.setPower(0.2);

        waitForStart();

        frontLeft.setPower(-0.3);
        frontRight.setPower(-0.3);
        backLeft.setPower(-0.3);
        backRight.setPower(-0.3);

        sleep(1000);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        leftHook.setPosition(0);
        rightHook.setPosition(0);

        sleep(2000);

        frontLeft.setPower(0.3);
        frontRight.setPower(0.3);
        backLeft.setPower(0.3);
        backRight.setPower(0.3);

        sleep(2000);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        arm.setPower(0.1);

        sleep(200);

        arm.setPower(0.2);

        frontLeft.setPower(0.3);
        frontRight.setPower(-0.3);
        backLeft.setPower(-0.3);
        backRight.setPower(0.3);

        sleep(1000);

    }
}