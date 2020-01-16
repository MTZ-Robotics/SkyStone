package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Test Timer", group ="z_test")
//@Disabled
public class z_Test_Timer extends LinearOpMode {

    private ElapsedTime endGameTimer;

    double greenWarningTime;
    double yellowWarningTime;
    double redWarningTime;
    boolean greenTimerElapsed;
    boolean yellowTimerElapsed;
    boolean redTimerElapsed;

    @Override

    public void runOpMode() {

        greenWarningTime = 60;
        yellowWarningTime = 70;
        redWarningTime = 80;
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;

        telemetry.log().add("Initialized. Go MTZ! Timer Set for ");
        telemetry.log().add(Double.toString(greenWarningTime));

        waitForStart();

        /*****
         * Start timer here since play was just pressed
         */
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {

            displayTelemetry();

            if (endGameTimer.seconds()>greenWarningTime){
                greenTimerElapsed = true;
            }
            if (endGameTimer.seconds()>yellowWarningTime){
                yellowTimerElapsed = true;
            }
            if (endGameTimer.seconds()>redWarningTime){
                redTimerElapsed = true;
            }

        }
    }


    public void displayTelemetry() {
        telemetry.clearAll();
         telemetry.addLine()
                .addData("Timer: ", endGameTimer.toString());
        telemetry.addLine()
                .addData("Green Timer Elapsed: ", greenTimerElapsed);
        telemetry.addLine()
                .addData("Yellow Timer Elapsed: ", yellowTimerElapsed);
        telemetry.addLine()
                .addData("Red Timer Elapsed: ", redTimerElapsed);
        telemetry.update();
    }

}
