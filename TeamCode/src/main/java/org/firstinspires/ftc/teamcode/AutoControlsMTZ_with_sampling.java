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

@Autonomous(name ="Auto Controls Sampling", group = "z_test")

//@Disabled

public class AutoControlsMTZ_with_sampling extends LinearOpMode {


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

    /*****************
     * Declare motor & servo objects
     ****************/
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
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

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

        /**************
         *
         * Declare motors and servos
         *
         */
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
         */
        //arm.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);


        /*************
         * Set Lights Variables to the color for the alliance
         *************/
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        if (alliance=="Blue") {

            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE;
        } else if (alliance=="Red") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_RED;
        }
        blinkinLedDriver.setPattern(pattern);


        /********
         * Movement starts here on initialize
         */
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        StopAndResetAllEncoders();

        /************
         * Raise the arm to fit within 18" long dimension
         ************/
        if (oldArm) {
           RaiseArm(14,defaultPauseTime/4);
        }
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

        if (pathToRun=="FoundationBridge" || pathToRun=="FoundationWall" ) {

            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Robot should face towards wall centered in tile next to bridge.");

            waitForStart();

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

            goToFoundationfromWall(allianceReverser);
            moveFoundation(allianceReverser);

            //Align to Park
            if (pathToRun=="FoundationBridge") {
                Strafe(allianceReverser * -8, defaultDriveSpeed, defaultPauseTime);
            } else {
                Strafe(allianceReverser * 12,0.1,200);
            }
            //Forward to bridge area
            Drive(24+12, defaultDriveSpeed, 0);

            //Lower arm gracefully
            //LowerArm(10, defaultPauseTime*2);

            //Raise arm a little
            //RaiseArm(3, defaultPauseTime);

            //Park
            //Drive(14, defaultDriveSpeed/2, 0);
        } else if (pathToRun=="DepotWall" || pathToRun=="DepotBridge") {
            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log().add("Robot starts facing quarry on intersection between tiles one tile away from bridge line.");

            waitForStart();

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

            //Wait 15 seconds
            sleep(15000);

            if (pathToRun=="DepotWall"){
                //Move forward slightly
                Drive(1,defaultDriveSpeed,defaultPauseTime);
            } else {
                //Move forward more
                Drive(12,defaultDriveSpeed,defaultPauseTime);
            }

            //Lower arm
            LowerArm(10,defaultPauseTime*2);
            RaiseArm(3,defaultPauseTime);

            //Park
            Strafe(allianceReverser * -24,defaultDriveSpeed,0);

        } else if (pathToRun=="DepotSampleWall" || pathToRun=="DepotSampleBridge") {
            /************************************
             * Path set up -- Add to each path
             ***********************************/
            //Robot Setup Notes
            telemetry.log()
                    .add("Robot starts facing quarry on intersection between tiles one tile away from bridge line.");
            telemetry.log()
                    .add("Robot is using Depot Wall v0.1");
            waitForStart();

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

            if (pathToRun=="DepotSampleWall") {
                //Move forward slightly
                /***********************************
                 * This code has not yet been tested
                 ***********************************/
                Drive(1, defaultDriveSpeed, defaultPauseTime);

                //Strafe right towards first block in line
                Strafe(allianceReverser * -8,defaultDriveSpeed,defaultPauseTime);

                //Add Sampling Here                 Add Sampling Here


                //determineSkyStone();

                //Move toward first block in line
                Drive(20, defaultDriveSpeed, defaultPauseTime);

                //Lower arm
                LowerArm(7 ,defaultPauseTime);

                //Close claw to grab block
                claw.setPosition(1);

                //Raise arm slightly
                RaiseArm(2,defaultPauseTime);

                //Reverse
                Drive(-24,defaultDriveSpeed,defaultPauseTime);

                //Turn towards line
                Turn(allianceReverser * -90,0.1,defaultPauseTime);

                //Drive past line with block
                Drive(48,defaultDriveSpeed,defaultPauseTime);

            }
            else if (pathToRun=="DepotSampleAudienceWall"){

                /************************************
                 * Path set up -- Add to each path
                 ***********************************/

                //Robot Setup Notes
                telemetry.log().add("Robot starts facing quarry next to other alliance depot.");

                waitForStart();

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

                determineSkyStone();

            } else {

                /************************************
                 * Path set up -- Add to each path
                 ***********************************/

                //Robot Setup Notes
                telemetry.log().add("Error in Path Selection");

                waitForStart();

                //Turn lights off
                if (alliance=="Blue") {
                    pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_BLUE;
                } else if (alliance=="Red") {
                    pattern = RevBlinkinLedDriver.BlinkinPattern.SHOT_RED;
                }
                blinkinLedDriver.setPattern(pattern);

                /************
                 * Path Start
                 ************/
                sleep(30000);

            }
        }
    }

    /**************************************
     * End of Paths
     **************************************/

    //Path Methods
    public void goToFoundationfromWall(int allianceReverser) throws InterruptedException{

        //Align Hooks With Foundation
        Drive(-24, defaultDriveSpeed, defaultPauseTime);
        Strafe(allianceReverser*12, defaultDriveSpeed, defaultPauseTime);
        Drive(-5, defaultDriveSpeed, defaultPauseTime);
    }

    public void moveFoundation (int allianceReverser) throws InterruptedException{

        //Hook Foundation
        HooksDown();

        //Move Foundation to Build Zone
        Drive(20, 0.2, defaultPauseTime);
        Turn(allianceReverser * -40, 0.2, defaultPauseTime);
        Drive(5, 0.2, defaultPauseTime);
        Turn(allianceReverser * -80, 0.2, defaultPauseTime);
        Strafe(allianceReverser * -5, 0.2, defaultPauseTime);
        Drive(-12, 0.1, defaultPauseTime);

        //Unhook Foundation
        HooksUp();
    }

    public string determineSkyStone(){
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

        targetsSkyStone.activate();

        waitForStart();

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
                sleep(20000);
            }
            else {
                telemetry.addData("Visible Target", "none");

                //Debug Timer
                sleep(500);
            }
            telemetry.update();
        }

        // Disable Tracking when we are done;
        targetsSkyStone.deactivate();

    }

    //Motion Methods

    public void Drive(int distance, double motorPower, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            DriveByInches(distance);
            RunDriveToPosition();
            DrivePower(motorPower);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void Strafe(int distance, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            StrafeByInches(distance);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void Turn(int degrees, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            TurnByAngle(degrees);
            RunDriveToPosition();
            DrivePower(power);
            while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                DisplayDriveTelemetry();
            }
            DrivePower(0);
            Thread.sleep(pause);
        }
    }
    public void RaiseArm(int distance, int pause) throws InterruptedException {

        //Use encoder for arm
        RaiseByInches(distance);
        RunArmToPosition();
        ArmPower(1);

        while (arm.isBusy()) {
            DisplayArmTelemetry();
        }

        /*

        //Use time based arm controls since the arm falls when the target distance is reached

        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(0.4);
        //1000 ms = 12 inches
        sleep(distance * 1000/12);
        //sleep(1000);
        arm.setPower(0.2);
        Thread.sleep(pause);

         */
    }

    public void LowerArm(int distance, int pause) throws InterruptedException {
        //Encoder controls
        RaiseByInches(-1*distance);
        RunArmToPosition();
        ArmPower(.5);

        while (arm.isBusy()) {
            DisplayArmTelemetry();
        }

        /* Time based controls
        arm.setPower(0.1);
        sleep(distance * 100);
        arm.setPower(0);
        Thread.sleep(pause);

         */
    }
    public void HooksDown() throws InterruptedException{
        //Light Reverse Power On
        lightReverse();
        sleep(500);

        leftHook.setPosition(0);
        rightHook.setPosition(0);
        sleep(1500);

        //Reverse Power Off
        StopAndResetDriveEncoders();
    }
    public void HooksUp() {
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);
        sleep(1500);
    }
    public void lightReverse() throws InterruptedException{

        //This is not making all of the wheels turn in the same direction and so it is commented out
        //frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //DrivePower(-0.1);

        //Substituting this instead
        Drive(-1,0.1,defaultPauseTime);
    }

    //Telemetry Methods

    public void DisplayDriveTelemetry() {
        double frontLeftInches = frontLeft.getCurrentPosition() / conversionTicksToInches;
        double frontRightInches = frontRight.getCurrentPosition() / conversionTicksToInches;
        double backLeftInches = backLeft.getCurrentPosition() / conversionTicksToInches;
        double backRightInches = backRight.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Front Left Inches ", (int) frontLeftInches + "   Power: " + "%.1f", frontLeft.getPower());
        telemetry.addLine()
                .addData("Front Right Inches: ", (int) frontRightInches + "   Power: " + "%.1f", frontRight.getPower());
        telemetry.addLine()
                .addData("Back Left Inches: ", (int) backLeftInches + "   Power: " + "%.1f", backLeft.getPower());
        telemetry.addLine()
                .addData("Back Right Inches: ", (int) backRightInches + "   Power: " + "%.1f", backRight.getPower());
        telemetry.update();
    }
    public void DisplayArmTelemetry() {
        double armInches = arm.getCurrentPosition() / conversionTicksToInches;
        telemetry.clear();
        telemetry.addLine()
                .addData("Arm Inches ", (int) armInches + "  Power: " + "%.1f", arm.getPower());
        telemetry.update();
    }

//Encoder Methods

    public void StopAndResetAllEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void StopAndResetDriveEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void RunDriveToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

//Distance Calculation Methods

    public void DriveByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(-1 * distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(-1 * distance * (int) conversionTicksToInches);
    }

    public void StrafeByInches(int distance) {
        frontLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        frontRight.setTargetPosition(-distance * (int) conversionTicksToInches);
        backLeft.setTargetPosition(distance * (int) conversionTicksToInches);
        backRight.setTargetPosition(-distance * (int) conversionTicksToInches);
    }

    public void TurnByAngle(int degrees) {
        frontLeft.setTargetPosition(degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        frontRight.setTargetPosition(-degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        backLeft.setTargetPosition(-degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
        backRight.setTargetPosition(degrees * (int) conversionTicksToInches * (int) experimentalInchesPerTurn / 360);
    }

//Power Methods

    public void DrivePower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }
    public void StopAndResetArmEncoder() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void RunArmToPosition() {
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void RaiseByInches(int distance) {
        //New Arm may need a new constant
        arm.setTargetPosition(distance * 450);
    }

    public void RaiseByDegrees(int angle) {
        double tickCalc= angle / conversionTicksToDegrees;
        int intTicks = (int)tickCalc;
        arm.setTargetPosition(intTicks);
    }
    public void ArmPower(double power) {
        arm.setPower(power);
    }




}