package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
//@Disabled

public class DriverControlOpMTZ_3 extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;

    @Override

    public void runOpMode() {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        claw = hardwareMap.servo.get("claw");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        claw.setPosition(0);

        double drivePower = 1;
        double driveDirection = 1;
        boolean aButtonPressed = false;
        boolean bButtonPressed = false;

        stat("HardwareMap Complete");

        waitForStart();

        while (opModeIsActive()) {

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


            stat(new String[]{"Drive Power (A): " + drivePower, "Drive Direction (B): " + driveDirection, "Arm Power: " + (gamepad2.left_stick_y - 0.2) * -1, "Claw Position" + claw.getPosition()});
            backLeft.setPower(drivePower * (driveDirection * (gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
            backRight.setPower(drivePower * (driveDirection * (gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontLeft.setPower(drivePower * (driveDirection * (-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));
            frontRight.setPower(drivePower * (driveDirection * (-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));
//<<<<<<< HEAD
            arm.setPower((0.5*(gamepad2.left_stick_y)-0.2)* -1);
//=======
            if(gamepad2.left_stick_y>=0) {
                arm.setPower((gamepad2.left_stick_y * -0.2) - 0.2);
            } else {
                arm.setPower((gamepad2.left_stick_y * -0.1) - 0.2);
            }
//>>>>>>> b806f0a0feeb76ad7e1b224d1fafd412450e15b0
            //servo.setPosition(gamepad2.right_stick_y);
        if(gamepad2.b){
            claw.setPosition(0);
        }
        if(gamepad2.a){
            claw.setPosition(1);
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
