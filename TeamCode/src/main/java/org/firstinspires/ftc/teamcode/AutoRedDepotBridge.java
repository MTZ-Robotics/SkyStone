package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Depot Bridge", group ="A_Top")
//@Disabled
public class AutoRedDepotBridge extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Red","DepotBridge",false);
    }

}
