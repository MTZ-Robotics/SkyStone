package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleMTZ_Lights_Timer_v1 Chili", group ="A_Top")

//@Disabled

public class TeleMTZ_Lights_Timer extends LinearOpMode {

    /********************************
     * Timer Variables
     ********************************/
    private ElapsedTime endGameTimer;

    double greenWarningTime;
    double yellowWarningTime;
    double redWarningTime;
    double endGameStart;
    double endGameOver;
    double endGameWarning;
    boolean greenTimerElapsed;
    boolean yellowTimerElapsed;
    boolean redTimerElapsed;
    boolean endGameStartElapsed;

    /***********
     * Lights Control Declarations
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;



    //This is the opMode call for generically running the opMode in this super class
    public void runOpMode() {

        boolean rightStrafer = false;
        boolean spurGearArm = false;
        double driveSpeed = 0.5;
        telemetry.log().add("Strafe Right:"+rightStrafer+", Arm Support:"+spurGearArm+", Drive Power:"+driveSpeed);
        lightsTimer("Red","Demo");
    }

    //This is the method that handles the controls
    public void lightsTimer(String alliance, String mode){


        /***********************
         * Modifiable variables
         **********************/
        if(mode!="Demo"){
            endGameStart = 90;
            endGameWarning = endGameStart + 15;
            endGameOver = endGameStart + 30;
            greenWarningTime = 60;
            yellowWarningTime = 70;
            redWarningTime = 80;

        } else {
            endGameStart = 90;
            endGameWarning = endGameStart + 15;
            endGameOver = endGameStart + 30;
            greenWarningTime = 60;
            yellowWarningTime = 70;
            redWarningTime = 80;
        }

        /***************
         * Set Timer Variables
         ***************/
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;
        endGameStartElapsed = false;

        /*************
         * Set Lights Variables
         *************/
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        pattern = RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_RED;
        blinkinLedDriver.setPattern(pattern);


        telemetry.log().add(Double.toString(greenWarningTime)+" s, " +
                Double.toString(yellowWarningTime)+" s, " +
                Double.toString(redWarningTime)+" s, " +
                Double.toString(endGameStart)+" s, "
        );

        /************* Press Play Button ***********************/

        waitForStart();

        pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        blinkinLedDriver.setPattern(pattern);

        //Start timer here since play was just pressed
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {  //Loop often to see if controls are still the same

            displayTelemetry();

            /*********************************
             * Check if timer has elapsed
             *********************************/
            //Check for End Timer First
            if (endGameTimer.seconds()>endGameOver){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>endGameWarning){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>endGameStart){
                endGameStartElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>redWarningTime){ //Then check for red
                redTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>yellowWarningTime){ //Then check for yellow
                yellowTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                blinkinLedDriver.setPattern(pattern);
            } else if (endGameTimer.seconds()>greenWarningTime){ //Then check for green
                greenTimerElapsed = true;
                pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                blinkinLedDriver.setPattern(pattern);
            }
        }
    }

    public void displayTelemetry() {
             telemetry.update();
    }
}
