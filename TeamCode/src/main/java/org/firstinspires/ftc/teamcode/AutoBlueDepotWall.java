package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Blue Depot Wall", group ="A_Top")
//@Disabled
public class AutoBlueDepotWall extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Blue","DepotWall",false);
    }

}
