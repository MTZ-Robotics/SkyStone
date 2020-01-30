package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Blue Depot Bridge", group ="A_Top")
//@Disabled
public class AutoBlueDepotBridge extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Blue","DepotBridge",false);
    }

}
