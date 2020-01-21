package org.firstinspires.ftc.teamcode;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@TeleOp(name="Test Timer Alert", group ="z_test")
@Disabled
public class z_test_timer_alert extends LinearOpMode {

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

/*
    private static final String VUFORIA_KEY =
            "\"AVqjUTL/////AAABmVnL+WsLRESmq47kooHwhcJo8agx+2Neapzf8VeCj/x+/y9bqF44lkQ1eOLU27J34UG8/9iN72gzW5VvpKwWCR1Cyy1IJ5QeGbgTsz9cZK8QllKDQfZOLJCMjF8om2XkeQMmxIn0ubjUfvzwM1ssaWOorEZYz0EmixrWeCJuoCt2yGWsm547w1By5sLacPmLnQf/s489lw29ibFtG8I7QkGJtUVH8T8LD+efsS/ZEKDIeaX/E+uZz5Zr0vI9EFDrC9bMRGPWHeN7TDBAFwyDDFzVe9hIo9PKaUYFe8zIMElRIsKcWyfCyAhg6+ZJ8F4qgfd+z2seDb/zMesVuWbnd0byStsK5w00TjK8/pPqJmiz";


    private VuforiaLocalizer vuforia = null;
*/
    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need something analogous when you
     * use a color sensor on your robot */
    View relativeLayout;

    /*******************************
     * Sound Variable Declaration
     *******************************/
    String  sounds[] =  {"ss_alarm", "ss_bb8_down", "ss_bb8_up", "ss_darth_vader", "ss_fly_by",
            "ss_mf_fail", "ss_laser", "ss_laser_burst", "ss_light_saber", "ss_light_saber_long", "ss_light_saber_short",
            "ss_light_speed", "ss_mine", "ss_power_up", "ss_r2d2_up", "ss_roger_roger", "ss_siren", "ss_wookie" };
    boolean soundPlaying = false;

    @Override

    public void runOpMode() {
       /* parameters.vuforiaLicenseKey = VUFORIA_KEY;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        */

        /***************
         * Set Timer Variables
         ***************/
        endTime = 20;
        greenWarningTime = 3;
        yellowWarningTime = 8;
        redWarningTime = 13;
        greenTimerElapsed = false;
        yellowTimerElapsed = false;
        redTimerElapsed = false;
        endTimerElapsed = false;

        telemetry.log().add("Initialized. Go MTZ! Timer Set for ");
        telemetry.log().add(Double.toString(greenWarningTime)+" s, " +
                        Double.toString(yellowWarningTime)+" s, " +
                        Double.toString(redWarningTime)+" s, " +
                        Double.toString(endTime)+" s, "
                );
        // Get a reference to the RelativeLayout so we can later change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        Context myApp = hardwareMap.appContext;

        /************************
         * Sound Player Variables & Options
         ***********************/
        // Variables for choosing from the available sounds
        int     soundIndex      = 0;
        int     soundID         = -1;
        // create a sound parameter that holds the desired player parameters.
        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;


        waitForStart();

        /*****
         * Start timer here since play was just pressed
         */
        endGameTimer = new ElapsedTime();
        endGameTimer.reset();

        while (opModeIsActive()) {

            displayTelemetry();


            if (endGameTimer.seconds()>endTime){
                endTimerElapsed = true;
                /*******
                 * Select & Play Sound
                 */
                //Select Sound
                soundIndex = 16;
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
            } else if (endGameTimer.seconds()>redWarningTime){
                redTimerElapsed = true;
                /*******
                 * Select & Play Sound
                 */
                //Select Sound
                soundIndex = 7;
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
            } else if (endGameTimer.seconds()>yellowWarningTime){
                yellowTimerElapsed = true;
                /*******
                 * Select & Play Sound
                 */
                //Select Sound
                soundIndex = 9;
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
                alertYellow();
            } else if (endGameTimer.seconds()>greenWarningTime){
                greenTimerElapsed = true;
                /*******
                 * Select & Play Sound
                 */
                //Select Sound
                soundIndex = 0;
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
                alertGreen();
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

    public void alertGreen(){
        //driver station background green
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.GREEN);
            }
        });

        //turn on torch
        //com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }
    public void alertYellow(){
        //driver station background yellow
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.YELLOW);
            }
        });
        //turn on torch
        //com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }
    public void alertRed(){
        //driver station background red
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.RED);
            }
        });
        //turn on torch
        //com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }
    public void alertWhite(){
        //driver station background red
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
        //turn on torch
        //com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }

}
