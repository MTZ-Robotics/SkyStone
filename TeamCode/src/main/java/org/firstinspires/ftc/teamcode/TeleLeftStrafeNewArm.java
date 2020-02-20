package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Left Strafe New Arm", group ="A_Top")

//@Disabled

public class TeleLeftStrafeNewArm extends TeleMTZ_Drive_Controls {


    public void runOpMode() throws InterruptedException {
        String controlPadMap = "SkyStone Left Strafe";
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Controls Map:"+controlPadMap+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(controlPadMap,spurGearArm,driveSpeed);
    }

}
