package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Depot Sample Wall", group ="A_Top")
//@Disabled
public class AutoBlueDepotSampleWall extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Blue","DepotSampleWall",false);
    }

}
