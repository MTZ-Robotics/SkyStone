package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name ="Auto Controls w Gyro", group = "z_test")

//@Disabled

public class AutoControlsMTZ_withGyro extends LinearOpMode {


    /**************
     *
     * Modify these speeds to help with diagnosing drive errors
     *
     **************/
    private static final double defaultDriveSpeed = 0.2;
    private static final double defaultTurnSpeed = 0.4;
    private static final int defaultPauseTime = 200;

    /**********************
     * These variables are the constants in path commands
     **********************/
    private static final double ticksPerRevolution = 145.6;
    private static final double gearReduction = 2.0;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);
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
    private BNO055IMU imu;


    /***********
     * Lights Control Declarations
     ***********/

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    @Override

    public void runOpMode() throws InterruptedException {
        autoPaths("Blue","FoundationWall",false);

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

        /*********
         * IMU Parameters and hardware Map
         */
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled      = true;
        imuParameters.loggingTag          = "IMU";
        imuParameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);

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

            telemetry.addData("Gyro Status", "calibrating...");
            telemetry.update();

            while (!isStopRequested() && !imu.isGyroCalibrated()) {
                idle();
            }

            telemetry.addData("Gyro Status", "Ready!");
            telemetry.update();

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
                Strafe(allianceReverser * -8, defaultDriveSpeed, 90,defaultPauseTime);
            } else {
                Strafe(allianceReverser * 12,0.1,90,200);
            }
            //Forward to bridge area
            Drive(24, defaultDriveSpeed, 0, defaultPauseTime);

           /* //Lower arm gracefully
            LowerArm(10, defaultPauseTime*2);

            //Raise arm a little
            RaiseArm(3, defaultPauseTime);
            */

            //Park
            Drive(14, defaultDriveSpeed/2,0, 0);
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
                Drive(1,defaultDriveSpeed,0,defaultPauseTime);
            } else {
                //Move forward more
                Drive(12,defaultDriveSpeed,0,defaultPauseTime);
            }

            //Lower arm
            LowerArm(10,defaultPauseTime*2);
            RaiseArm(3,defaultPauseTime);

            //Park
            Strafe(allianceReverser * -24,defaultDriveSpeed,90,0);

        }
    }

    //Path Methods
    public void goToFoundationfromWall(int allianceReverser) throws InterruptedException{

        //Align Hooks With Foundation
        Drive(-24, defaultDriveSpeed, 0,defaultPauseTime);
        Strafe(allianceReverser*12, defaultDriveSpeed, 90,defaultPauseTime);
        Drive(-5, defaultDriveSpeed, 0,defaultPauseTime);
    }

    public void moveFoundation (int allianceReverser) throws InterruptedException{

        //Hook Foundation
        HooksDown();

        //Move Foundation to Build Zone
        Drive(20, 0.2,0, defaultPauseTime);
        if (allianceReverser == 1) {
            LeftTurn(40,0.2,defaultPauseTime);
        }  else {
            RightTurn(40, 0.2, defaultPauseTime);
        }
        Drive(5, 0.2, 0,defaultPauseTime);
        if (allianceReverser == 1) {
            LeftTurn(80,0.2,defaultPauseTime);
        }  else {
            RightTurn(80, 0.2, defaultPauseTime);
        }
        Strafe(allianceReverser * -5, 0.2,90, defaultPauseTime);
        Drive(-12, 0.1, 0,defaultPauseTime);

        //Unhook Foundation
        HooksUp();
    }

    //Motion Methods

    public void Drive(int distance, double power, double angle, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            DriveByInches(distance);
            RunDriveToPosition();
            while (opModeIsActive()) {
                double gyroHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                double errorMultiple = 0.1;
                double error = (errorMultiple * (-gyroHeading - angle) / 100);
                if (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                    frontLeft.setPower(power - error);          /*(power - error); */
                    frontRight.setPower(power + error);         /*(power + error);*/
                    backLeft.setPower(power - error);       /*(power - error);*/
                    backRight.setPower(power + error);          /*(power + error);*/
                    DisplayDriveTelemetry();
                } else {
                    DrivePower(0);
                    Thread.sleep(pause);
                    break;
                }
            }
        }
    }
    public void Strafe(int distance, double power, double angle, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            StrafeByInches(distance);
            RunDriveToPosition();
            while (opModeIsActive()) {
                double gyroHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                double errorMultiple = 1.0;
                double error = (errorMultiple * (-gyroHeading - angle) / 100);
                if (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {
                    frontLeft.setPower(power - error);
                    frontRight.setPower(-power + error);
                    backLeft.setPower(-power - error);
                    backRight.setPower(power + error);
                    DisplayDriveTelemetry();
                } else {
                    DrivePower(0);
                    Thread.sleep(pause);
                    break;
                }
            }
        }
    }
    public void LeftTurn(int degrees, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            while (opModeIsActive()) {
                double gyroHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                if (gyroHeading < degrees) {
                    frontLeft.setPower(-power);
                    frontRight.setPower(power);
                    backLeft.setPower(-power);
                    backRight.setPower(power);
                    DisplayDriveTelemetry();
                } else {
                    DrivePower(0);
                    Thread.sleep(pause);
                    break;
                }
            }
        }
    }
    public void RightTurn(int degrees, double power, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            StopAndResetDriveEncoders();
            while (opModeIsActive()) {
                double gyroHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                double invertGyroHeading = gyroHeading * -1;
                if (invertGyroHeading > degrees) {
                    frontLeft.setPower(power);
                    frontRight.setPower(-power);
                    backLeft.setPower(power);
                    backRight.setPower(-power);
                    DisplayDriveTelemetry();
                }else{
                    DrivePower(0);
                    Thread.sleep(pause);
                    break;
                }
            }
        }
    }
    public void RaiseArm(int distance, int pause) throws InterruptedException {
        if (opModeIsActive()) {
            RaiseByInches (distance);
            ArmPower(.75);
            }
            ArmPower(0);
            Thread.sleep(pause);


       /* //Use time based arm controls since the arm falls when the target distance is reached

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
        /*arm.setPower(0.1);
        sleep(distance * 100);
        arm.setPower(0);
        Thread.sleep(pause);

         */
    }
    public void HooksDown()throws InterruptedException {
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
        Drive(-1,0.1,0,defaultPauseTime);
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
        arm.setTargetPosition(distance * 45);
    }
    public void ArmPower(double power) {
        arm.setPower(power);
    }

}