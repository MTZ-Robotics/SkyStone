package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Depot Sample Bridge", group ="A_Top")
//@Disabled
public class AutoRedDepotSampleBridge extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Red","DepotSampleBridge",false);
    }

}
