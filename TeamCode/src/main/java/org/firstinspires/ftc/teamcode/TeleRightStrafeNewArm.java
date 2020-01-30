package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Right Strafe New Arm", group ="A_Top")

//@Disabled

public class TeleRightStrafeNewArm extends TeleMTZ_Drive_Controls {


    public void runOpMode() {

        boolean rightStickStrafe = true;
        boolean armSupport = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Strafe Right:"+rightStickStrafe+", Arm Support:"+armSupport+", Drive Power:"+driveSpeed);
        super.controlRobot(rightStickStrafe,armSupport,driveSpeed);
    }

}
