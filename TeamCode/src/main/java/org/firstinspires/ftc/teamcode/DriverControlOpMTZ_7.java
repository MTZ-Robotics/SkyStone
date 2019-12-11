package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MTZ Driver 7", group ="Concept")
//@Disabled

//This code was made last second and probably sucks.  You are welcome.

public class DriverControlOpMTZ_7 extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo rightHook;
    private Servo leftHook;

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
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);
        claw.setPosition(0);
        rightHook.setPosition(0.5);
        leftHook.setPosition(0.5);

        stat("HardwareMap Complete");

        waitForStart();

        while (opModeIsActive()) {
            stat(new String[]{"Arm Power: " + (gamepad2.left_stick_y-0.2), "Claw Position" + claw.getPosition()});
            backLeft.setPower((gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x);
            backRight.setPower((gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x);
            frontLeft.setPower((-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x);
            frontRight.setPower((-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x);
            arm.setPower((0.4*(gamepad2.left_stick_y))-0.2);
            claw.setPosition(gamepad2.right_stick_y);
            if(gamepad2.a) {
                claw.setPosition(1);
            }
            if(gamepad2.b) {
                claw.setPosition(0);
            }
            if (gamepad2.dpad_up) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }
            if (gamepad2.dpad_down) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
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
