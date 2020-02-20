package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Depot Sample Bridge", group ="A_Top")
//@Disabled
public class AutoBlueDepotSampleBridge extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Blue","DepotSampleBridge",false);
    }

}
