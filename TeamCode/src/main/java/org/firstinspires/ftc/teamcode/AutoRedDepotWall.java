package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Red Depot Wall", group ="A_Top")
//@Disabled
public class AutoRedDepotWall extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Red","DepotWall",false);
    }

}
