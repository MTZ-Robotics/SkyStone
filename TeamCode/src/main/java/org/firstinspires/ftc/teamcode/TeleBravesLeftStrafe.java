package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Hunchamuncha Left Strafe", group ="A_Top")

//@Disabled

public class TeleBravesLeftStrafe extends TeleMTZ_Drive_Controls {


    public void runOpMode() throws InterruptedException {

        String controlPadMap = "SkyStone Braves Left Strafe";
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Controls Map:"+controlPadMap+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        controlRobot(controlPadMap,spurGearArm,driveSpeed);
    }

}
