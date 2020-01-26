package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Right Strafe Old Arm", group ="A_Top")

//@Disabled

public class TeleRightStrafeOldArm extends TeleMTZ_Drive_Controls {


    public void runOpMode() {

        boolean rightStickStrafe = true;
        boolean armSupport = true;
        double driveSpeed = 0.5;
        telemetry.log().add("Strafe Right:"+rightStickStrafe+", Arm Support:"+armSupport+", Drive Power:"+driveSpeed);
        super.controlRobot(rightStickStrafe,armSupport,driveSpeed);
    }

}
