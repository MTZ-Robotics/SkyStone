package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Hand Turkey Right Strafe", group ="A_Top")

//@Disabled

public class TeleHandTurkeyRightStrafe extends TeleMTZ_Drive_Controls {


    public void runOpMode() {
        String controlPadMap = "SkyStone Hand Turkey Right Strafe";
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Controls Map:"+controlPadMap+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(controlPadMap,spurGearArm,driveSpeed);
    }

}
