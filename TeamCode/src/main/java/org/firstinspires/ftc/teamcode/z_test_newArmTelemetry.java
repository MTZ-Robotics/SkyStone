package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="z_test_newArmTelemetry", group ="z_test")

//@Disabled

public class z_test_newArmTelemetry extends LinearOpMode {

    // Motor and Servo variables
    private DcMotor arm = null;
    private DcMotor armExtension = null;
    private Servo claw = null;
    private Servo wrist = null;

    @Override

    public void runOpMode() {

        //Hardware Map
        hardwareMap.dcMotor.get("arm");
        hardwareMap.dcMotor.get("armExtension");
        hardwareMap.servo.get("claw");
        hardwareMap.servo.get("wrist");

        //Set Arm Motor Directions
        arm.setDirection(DcMotor.Direction.REVERSE);
        armExtension.setDirection(DcMotor.Direction.REVERSE);

        //Tell Motors to Collect Position Data
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            //Display Data about Motors and Servos on Driver Station
            displayTelemetry();

            //Set Arm Power to Left Joystick Vertical Values
            arm.setPower(gamepad1.left_stick_y);

            //Set Arm Extender Power to Left Joystick Horizontal Values
            armExtension.setPower(gamepad1.left_stick_x);

            //Set Claw Position to Right Joystick Vertical Values
            claw.setPosition(gamepad1.right_stick_y);

            //Set Wrist Position to Bumpers
            if (gamepad2.right_bumper) {
                wrist.setPosition(wrist.getPosition() + 0.05);
            } else if (gamepad2.left_bumper) {
                wrist.setPosition(wrist.getPortNumber() -0.05);
            }

        }
    }

    //This is the code for the telemetry above
    public void displayTelemetry() {
        telemetry.addLine()
                .addData("Arm Power: ", arm.getPower());
        telemetry.addLine()
                .addData("Arm Extender Power: ", armExtension.getPower());
        telemetry.addLine()
                .addData("Arm Position: ", arm.getCurrentPosition());
        telemetry.addLine()
                .addData("Arm Extender Position: ", armExtension.getCurrentPosition());
        telemetry.addLine()
                .addData("Claw Position: ", claw.getPosition());
        telemetry.addLine()
                .addData("Wrist Position: ", wrist.getPosition());
        telemetry.update();
    }
}
