package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name ="MoveForwardSlideLeft")
//@Disabled

public class MoveForwardSlideLeft extends LinearOpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor arm;
    Servo claw;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        claw = hardwareMap.servo.get("claw");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        claw.setPosition(0);

        arm.setPower(0.4);
        sleep(1000);
        arm.setPower(0.2);

        waitForStart();

        sleep(15000);

        frontLeft.setPower(0.4);
        frontRight.setPower(0.4);
        backLeft.setPower(0.4);
        backRight.setPower(0.4);

        sleep(200);

        frontLeft.setPower(-0.4);
        frontRight.setPower(0.4);
        backLeft.setPower(0.4);
        backRight.setPower(-0.4);

        sleep(1000);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        arm.setPower(0);

    }
}