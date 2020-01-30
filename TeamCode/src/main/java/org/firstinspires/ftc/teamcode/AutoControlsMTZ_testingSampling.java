package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name ="Auto Controls testing Sampling", group = "z_test")

//@Disabled

public class AutoControlsMTZ_testingSampling extends LinearOpMode {


    /**************
     *
     * Modify these speeds to help with diagnosing drive errors
     *
     **************/
    private static final double defaultDriveSpeed = 0.2; //Old code wad 0.2
    private static final double defaultTurnSpeed = 0.2; //old code had 0.2
    private static final int defaultPauseTime = 200; //Typically 200 //debug at 1000-2000


    /**********************
     * These variables are the constants in path commands
     **********************/
    private static final double ticksPerRevolution = 145.6;
    private static final double gearReduction = 2.0;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);
    private static final double experimentalInchesPerTurn = 91.8;
    private static final double armTicksPerRevolution = 145.6; //Need verification
    private static final double armGearReduction = 2.0; //Need Verification
    private static final double conversionTicksToDegrees = (armTicksPerRevolution * armGearReduction) / 360;
    private int allianceReverser = 1;


    /*
    ****************
     * Declare motor & servo objects
     ****************
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private Servo claw;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;


    /***********
     * Lights Control Declarations
     ***********

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;


     */

    /********
     * Vuforia Objects
     ********/
    //x= closer and farther away (farther is more negative; closer is less negative (more positive); but it will never be in the positive range)
    //y= right and left (right is negative; left is positive)
    //z= up and down (down is negative; up is positive)
    final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    final boolean PHONE_IS_PORTRAIT = false  ;
    final String VUFORIA_KEY =
            "\"AVqjUTL/////AAABmVnL+WsLRESmq47kooHwhcJo8agx+2Neapzf8VeCj/x+/y9bqF44lkQ1eOLU27J34UG8/9iN72gzW5VvpKwWCR1Cyy1IJ5QeGbgTsz9cZK8QllKDQfZOLJCMjF8om2XkeQMmxIn0ubjUfvzwM1ssaWOorEZYz0EmixrWeCJuoCt2yGWsm547w1By5sLacPmLnQf/s489lw29ibFtG8I7QkGJtUVH8T8LD+efsS/ZEKDIeaX/E+uZz5Zr0vI9EFDrC9bMRGPWHeN7TDBAFwyDDFzVe9hIo9PKaUYFe8zIMElRIsKcWyfCyAhg6+ZJ8F4qgfd+z2seDb/zMesVuWbnd0byStsK5w00TjK8/pPqJmiz";
    final float mmPerInch        = 25.4f;
    final float stoneZ = 2.00f * mmPerInch;
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia = null;
    boolean targetVisible = false;
    float phoneXRotate    = 0;
    float phoneYRotate    = 0;
    float phoneZRotate    = 0;
    /*** End Vuforia Objects ***/

    @Override

    /*****************************************************************************************
     ******************* Default opMode Settings   *******************************************
     ****************************************************************************************/
    public void runOpMode() throws InterruptedException {
        autoPaths("Blue","DepotSampleAudienceWall",false);

    }

    public void autoPaths(String alliance,String pathToRun,Boolean supportArm) throws InterruptedException {

        boolean oldArm = supportArm;

        /*
        *************
         *
         * Declare motors and servos
         *
         *
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
        /**********
         * Commenting out arm reverse to see if it will keep the arm in the correct direction
         *
        //arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);


        /*************
         * Set Lights Variables to the color for the alliance
         *************
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        if (alliance=="Blue") {

            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE;
        } else if (alliance=="Red") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_RED;
        }
        blinkinLedDriver.setPattern(pattern);


        /********
         * Movement starts here on initialize
         *
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        StopAndResetAllEncoders();

        /************
         * Raise the arm to fit within 18" long dimension
         ************
        if (oldArm) {
           RaiseArm(14,defaultPauseTime/4);
        }
         */
        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add(pathToRun+" Initialized. Go "+alliance+" alliance");


        //Paths written for Blue alliance and reverse turns if on Red alliance
        allianceReverser=1;
        if (alliance=="Red") {
            allianceReverser=-1;
        }



        /************************************************************
         * Paths            Paths            Paths          Paths   *
         ************************************************************/
        //Write paths for Blue alliance and apply reverser on turns and strafes


            if (pathToRun=="DepotSampleAudienceWall"){

                /************************************
                 * Path set up -- Add to each path
                 ***********************************/

                //Robot Setup Notes
                telemetry.log().add("Robot starts facing quarry next to other alliance depot.");

                waitForStart();
/*
                //Turn lights off
                if (alliance=="Blue") {
                    pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                } else if (alliance=="Red") {
                    pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                }
                blinkinLedDriver.setPattern(pattern);

                /************
                 * Path Start
                 ************/

                //Debug Timer
                sleep(1000);

                alignToSkyStone();

            } else {

                /************************************
                 * Path set up -- Add to each path
                 ***********************************/

                //Robot Setup Notes
                telemetry.log().add("Error in Path Selection");

                waitForStart();

                /************
                 * Path Start
                 ************/
                sleep(30000);

            }
        }


    /**************************************
     * End of Paths
     **************************************/

    //Path Methods


    public void alignToSkyStone(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.add(stoneTarget);
        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }
        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90 ;
        }
        /*****************
         * Phone Placement
         *****************/
        final float CAMERA_FORWARD_DISPLACEMENT  = 5.0f * mmPerInch; //4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
        final float CAMERA_VERTICAL_DISPLACEMENT = -8.0f * mmPerInch; //Right 8.0f * mmPerInch;   // eg: Camera is 8 Inches right of center
        final float CAMERA_LEFT_DISPLACEMENT     = -6.0f * mmPerInch;     // -Height eg: Camera is above the ground
        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }
        sleep(5000);

        targetsSkyStone.activate();

        //waitForStart();

        while (!isStopRequested()) {
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() && trackable.getName()=="Stone Target") {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    break;
                }
            }

            // Provide feedback as to where the robot is located (if we know).
            if (targetVisible) {
                //express position (translation) of robot in inches.
                VectorF translation = lastLocation.getTranslation();
                telemetry.addData("Pos (Front, Left, Height)(in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                        translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

                // express the rotation of the robot in degrees.
                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
                telemetry.addData("Strafe Robot Right: ", translation.get(1) / mmPerInch);
                telemetry.addData("Turn Robot CCW: ", rotation.firstAngle-90);
                telemetry.addData("Drive Robot Forward: ", translation.get(0) / mmPerInch);


                //Debug Timer
                sleep(10000);
            }
            else {
                telemetry.addData("Visible Target", "none");

                //Debug Timer
                sleep(10000);
            }
            telemetry.update();
        }

        // Disable Tracking when we are done;
        targetsSkyStone.deactivate();

    }

    //Motion Methods





}