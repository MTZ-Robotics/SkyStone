package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Depot Sample Wall", group ="A_Top")
//@Disabled
public class AutoRedDepotSampleWall extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Red","DepotSampleWall",false);
    }

}
