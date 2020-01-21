package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Right Strafe Test 2", group ="A_Top")

//@Disabled

public class TeleRightStrafeDriverTest2 extends LinearOpMode {

    /********************************
     * Timer Variables
     ********************************/
    private ElapsedTime endGameTimer;

    double greenWarningTime;
    double yellowWarningTime;
    double redWarningTime;
    double endTime;
    boolean greenTimerElapsed;
    boolean yellowTimerElapsed;
    boolean redTimerElapsed;
    boolean endTimerElapsed;
    View relativeLayout;
    String  sounds[] =  {"ss_alarm", "ss_bb8_down", "ss_bb8_up", "ss_darth_vader", "ss_fly_by",
            "ss_mf_fail", "ss_laser", "ss_laser_burst", "ss_light_saber", "ss_light_saber_long", "ss_light_saber_short",
            "ss_light_speed", "ss_mine", "ss_power_up", "ss_r2d2_up", "ss_roger_roger", "ss_siren", "ss_wookie" };
    boolean soundPlaying = false;

    /*************************
     * Motor & Servo Variables
     *************************/
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;

    double drivePower;
    double armPower;
    double defaultDrivePower;
    double defaultArmPower;
    boolean rightStrafe;

    @Override

    public void runOpMode() {
        /***********************
         * Modifiable variables
         **********************/
        rightStrafe = true;
        endTime = 105;
        greenWarningTime = 60;
        yellowWarningTime = 70;
        redWarningTime = 80;
        defaultDrivePower = 0.5;
        defaultArmPower = 0.35;


        /***************
         * Set Timer Variables
         ***************/
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;
        endTimerElapsed = false;
        // Get a reference to the RelativeLayout so we can later change the background
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
        Context myApp = hardwareMap.appContext;
        // Variables for choosing from the available sounds
        int     soundIndex      = 0;
        int     soundID         = -1;
        // create a sound parameter that holds the desired player parameters.
        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;

        /*******************************
         * Set Motor & Servo Variables
         ******************************/


        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        claw = hardwareMap.servo.get("claw");
        rightHook = hardwareMap.servo.get("rightHook");
        leftHook = hardwareMap.servo.get("leftHook");
        blockThrower = hardwareMap.servo.get("blockThrower");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);



        /**********************************
         * Set positions on initialize
         **********************************/


        claw.setPosition(0);
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        blockThrower.setPosition(1);



        /***********************************************
         * Tell driver station that initialization complete
         **********************************************/
        telemetry.log().add("Initialized. Go MTZ! Timer Set for ");
        telemetry.log().add(Double.toString(greenWarningTime)+" s, " +
                Double.toString(yellowWarningTime)+" s, " +
                Double.toString(redWarningTime)+" s, " +
                Double.toString(endTime)+" s, "
        );

        /************* Press Play Button ***********************/

        waitForStart();

        //Start timer here since play was just pressed
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {  //Loop often to see if controls are still the same

            displayTelemetry();

            /*********************
             * Speed adjust with triggers
             ********************/
            if (gamepad1.right_trigger > 0) {
                drivePower = defaultDrivePower*2;
            } else if (gamepad1.left_trigger > 0) {
                drivePower = defaultDrivePower/2;
                } else {
                    drivePower = defaultDrivePower;
            }

            /*****************
             * Hook Controls
             ****************/


            if (gamepad2.dpad_down) {
                leftHook.setPosition(0);
                rightHook.setPosition(0);
                //drivePower = defaultDrivePower/2;
                drivePower = .1;
            }
            if (gamepad2.dpad_up) {
                leftHook.setPosition(0.5);
                rightHook.setPosition(0.5);
            }


            /*************************
             * Chassis drive controls
             *************************/

            if(rightStrafe) {
                backLeft.setPower(drivePower * ((gamepad1.left_stick_y + gamepad1.right_stick_x) - gamepad1.left_stick_x));
                backRight.setPower(drivePower * ((gamepad1.left_stick_y - gamepad1.right_stick_x) + gamepad1.left_stick_x));
                frontLeft.setPower(drivePower * ((-gamepad1.left_stick_y + gamepad1.right_stick_x) + gamepad1.left_stick_x));
                frontRight.setPower(drivePower * ((-gamepad1.left_stick_y - gamepad1.right_stick_x) - gamepad1.left_stick_x));
            } else {
                backLeft.setPower(drivePower * ((gamepad1.right_stick_y + gamepad1.left_stick_x) - gamepad1.right_stick_x));
                backRight.setPower(drivePower * ((gamepad1.right_stick_y - gamepad1.left_stick_x) + gamepad1.right_stick_x));
                frontLeft.setPower(drivePower * ((-gamepad1.right_stick_y + gamepad1.left_stick_x) + gamepad1.right_stick_x));
                frontRight.setPower(drivePower * ((-gamepad1.right_stick_y - gamepad1.left_stick_x) - gamepad1.right_stick_x));
            }
            
            if (gamepad2.right_trigger > 0) {
                armPower = 0.5;
            } else if (gamepad2.left_trigger > 0) {
                armPower = 0.2;
            } else {
                armPower = 0.35;
            }


            armPower = defaultArmPower;

            /*************
             * Arm Controls
             *************/

            arm.setPower(armPower * (gamepad2.right_stick_y) - 0.2);


            /*************
             * Claw Controls
             *************/

            claw.setPosition(gamepad2.left_stick_y);



            /************************
             * Cap Stone thrower controls
             ***********************/

            if(gamepad2.a){
                blockThrower.setPosition(0.45);
            } else {
                blockThrower.setPosition(1);
            }


            /*********************************
             * Check if timer has elapsed
             *********************************/
            //Check for End Timer First
            if (endGameTimer.seconds()>endTime){
                endTimerElapsed = true;
                /******** Select & Play Sound**************/
                soundIndex = 16; //Select Sound
                //Play sound if a sound has finished playing
                if (!soundPlaying) {
                    // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                    if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0){
                        // Signal that the sound is now playing.
                        soundPlaying = true;
                        // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                        SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                                new Runnable() {
                                    public void run() {
                                        soundPlaying = false;
                                    }} );
                    }
                }
                alertWhite();
            } else if (endGameTimer.seconds()>redWarningTime){ //Then check for red
                redTimerElapsed = true;
                /******** Select & Play Sound**************/
                soundIndex = 7; //Select Sound
                //Play sound if a sound has finished playing
                if (!soundPlaying) {
                    // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                    if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0){
                        // Signal that the sound is now playing.
                        soundPlaying = true;
                        // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                        SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                                new Runnable() {
                                    public void run() {
                                        soundPlaying = false;
                                    }} );
                    }
                }
                alertRed();
            } else if (endGameTimer.seconds()>yellowWarningTime){ //Then check for yellow
                yellowTimerElapsed = true;
                /******** Select & Play Sound**************/
                soundIndex = 9; //Select Sound
                //Play sound if a sound has finished playing
                if (!soundPlaying) {
                    // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                    if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0){
                        soundPlaying = true; // Signal that the sound is now playing.
                        // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                        SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                                new Runnable() {
                                    public void run() {
                                        soundPlaying = false;
                                    }} );
                    }
                }
                alertYellow();
            } else if (endGameTimer.seconds()>greenWarningTime){ //Then check for green
                greenTimerElapsed = true;
                /******** Select & Play Sound**************/
                soundIndex = 0; //Select Sound
                //Play sound if a sound has finished playing
                if (!soundPlaying) {
                    // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                    if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0){
                        soundPlaying = true; // Signal that the sound is now playing.
                        // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                        SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                                new Runnable() {
                                    public void run() {
                                        soundPlaying = false;
                                    }} );
                    }
                }
                alertGreen();
            }

        }
    }

    public void displayTelemetry() {
        telemetry.clearAll();
        telemetry.addLine()
                .addData("Timer: ", endGameTimer.toString());
        telemetry.addLine()
                .addData("Front Left Power: ", frontLeft.getPower());
        telemetry.addLine()
                .addData("Front Right Power: ", frontRight.getPower());
        telemetry.addLine()
                .addData("Back Left Power: ", backLeft.getPower());
        telemetry.addLine()
                .addData("Back Right Power: ", backRight.getPower());
        telemetry.addLine()
                .addData("Arm Power: ", arm.getPower());
        telemetry.addLine()
                .addData("Claw Position: ", claw.getPosition());
        telemetry.addLine()
                .addData("Left Hook Position: ", leftHook.getPosition());
        telemetry.addLine()
                .addData("Right Hook Position: ", rightHook.getPosition());
        telemetry.addLine()
                .addData("Block Thrower Position: ", blockThrower.getPosition());
        telemetry.update();
    }

    public void alertGreen(){
        //driver station background green
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.GREEN);
            }
        });
    }
    public void alertYellow(){
        //driver station background yellow
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.YELLOW);
            }
        });
    }
    public void alertRed(){
        //driver station background red
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.RED);
            }
        });
    }
    public void alertWhite(){
        //driver station background White
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
    }


}
