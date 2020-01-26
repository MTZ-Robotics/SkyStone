package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Foundation Bridge", group ="A_Top")
//@Disabled
public class AutoBlueFoundationBridge extends AutoControlsMTZ {
    public void runOpMode() throws InterruptedException {

        super.autoPaths("Blue","FoundationBridge",false);
    }

}
