package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Right Strafe New Arm", group ="A_Top")

//@Disabled

public class TeleRightStrafeNewArm extends TeleMTZ_Drive_Controls {


    public void runOpMode() throws InterruptedException {
        String controlPadMap = "SkyStone Right Strafe";
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Controls Map:"+controlPadMap+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(controlPadMap,spurGearArm,driveSpeed);
    }

}
