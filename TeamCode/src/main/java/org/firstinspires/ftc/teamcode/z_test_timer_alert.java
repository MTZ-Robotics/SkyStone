package org.firstinspires.ftc.teamcode;


import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@TeleOp(name="Test Timer Alert", group ="z_test")
//@Disabled
public class z_test_timer_alert extends LinearOpMode {

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

    @Override

    public void runOpMode() {
       /* parameters.vuforiaLicenseKey = VUFORIA_KEY;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        */

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
                alertGreen();
            }
            if (endGameTimer.seconds()>yellowWarningTime){
                yellowTimerElapsed = true;
                alertYellow();
            }
            if (endGameTimer.seconds()>redWarningTime){
                redTimerElapsed = true;
                alertRed();
            }
            if (endGameTimer.seconds()>endTime){
                endTimerElapsed = true;
                alertWhite();
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
