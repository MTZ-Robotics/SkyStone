package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Auto Calibration", group ="z_test")
//@Disabled
public class zAutoCalibrate extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Red","Calibrate",false);
    }

}
