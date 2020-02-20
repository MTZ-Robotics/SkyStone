package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name ="Auto with color sensor v1 Dark Chocolate", group = "b_concept")

//@Disabled

public class AutoControlsMTZ_ColorSensorSample extends LinearOpMode {

    /**********************************************************
     * Modify these speeds to help with diagnosing drive errors
     **********************************************************/
    private static final double defaultDriveSpeed = 0.2;
    private static final double defaultTurnSpeed = 0.4;
    private static final int defaultPauseTime = 200;

    /****************************************************
     * These variables are the constants in path commands
     ****************************************************/
    private static final double ticksPerRevolution = 145.6;
    private static final double gearReduction = 2.0;
    private static final double wheelDiameterInches = 4.0;
    private static final double pi = 3.1415;
    private static final double conversionTicksToInches = (ticksPerRevolution * gearReduction) / (pi * wheelDiameterInches);
    private static final double experimentalInchesPerTurn = 91.8;
    private int allianceReverser = 1;

    /*******************************
     * Declare motor & servo objects
     *******************************/
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor arm;
    private DcMotor armExtension;
    private Servo claw;
    private Servo wrist;
    private Servo leftHook;
    private Servo rightHook;
    private Servo blockThrower;
    private Servo leftStoneHook;
    private Servo rightStoneHook;
    private ColorSensor leftColorSensor;
    private ColorSensor rightColorSensor;

    /*****************************
     * Lights Control Declarations
     *****************************/
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    @Override
    /***********************************************
     * RUN PATH HERE   RUN PATH HERE   RUN PATH HERE
     ***********************************************/
    public void runOpMode() throws InterruptedException {
        autoPaths("Blue","DepotWall",false);
    }

    public void autoPaths(String alliance,String pathToRun,Boolean supportArm) throws InterruptedException {

        boolean oldArm = supportArm;

        /***********************
         * Map Motors and Servos
         ***********************/
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        arm = hardwareMap.dcMotor.get("arm");
        armExtension = hardwareMap.dcMotor.get("armExtension");
        claw = hardwareMap.servo.get("claw");
        wrist = hardwareMap.servo.get("wrist");
        rightHook = hardwareMap.servo.get("rightHook");
        leftHook = hardwareMap.servo.get("leftHook");
        blockThrower = hardwareMap.servo.get("blockThrower");

        //leftColorSensor = hardwareMap.colorSensor.get("color_sensor");
        //rightColorSensor = hardwareMap.colorSensor.get("color_sensor2");

        leftColorSensor = hardwareMap.colorSensor.get("sensor_color");
        rightColorSensor = hardwareMap.colorSensor.get("sensor_color2");


        /********************************
         * Set Motor and Servo Directions
         ********************************/
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
        armExtension.setDirection(DcMotor.Direction.REVERSE);
        leftHook.setDirection(Servo.Direction.REVERSE);

        /****************************************************
         * Set Lights Variables to the color for the alliance
         ****************************************************/
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        if (alliance=="Blue") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_BLUE;
        } else if (alliance=="Red") {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BREATH_RED;
        }
        blinkinLedDriver.setPattern(pattern);

        /***************************
         * Initialization Procedures
         ***************************/
        //Servo Movements
        leftHook.setPosition(0.5);
        rightHook.setPosition(0.5);

        StopAndResetAllEncoders();

        //Motor Movements
        if (oldArm) {
           RaiseArm(14,defaultPauseTime/4);
        }

        //Telemetry Update
        telemetry.log().clear();
        telemetry.update();
        telemetry.log().add(pathToRun+" Initialized. Go "+alliance+" alliance");

        //Set alliance reverser value based on blue or red
        allianceReverser = 1;
        if (alliance=="Red") {
            allianceReverser = -1;
        }

        /*******************************************************
         * Paths   Paths   Paths   Paths   Paths   Paths   Paths
         *******************************************************/
        //Write paths for Blue alliance and apply reverser on turns and strafes

        if (pathToRun=="FoundationBridge" || pathToRun=="FoundationWall" ) {

            /*********************************
             * Path set up -- Add to each path
             *********************************/
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
            Drive(24, defaultDriveSpeed, defaultPauseTime);

           /* //Lower arm gracefully
            LowerArm(10, defaultPauseTime*2);

            //Raise arm a little
            RaiseArm(3, defaultPauseTime);
            */

            //Park
            Drive(14, defaultDriveSpeed/2, 0);
        } else if (pathToRun=="DepotWall" || pathToRun=="DepotBridge") {

            /*********************************
             * Path set up -- Add to each path
             *********************************/
            //Robot Setup Notes
            telemetry.log().add("Robot starts facing quarry in the middle of the center tile on the depot side.");

            waitForStart();

            //Turn lights off
            if (alliance=="Blue") {
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            } else if (alliance=="Red") {
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            }
            blinkinLedDriver.setPattern(pattern);

            //Store color sensor values
            float leftHsvValues[] = {0F,0F,0F};
                final float leftValues[] = leftHsvValues;
            float rightHsvValues[] = {0F,0F,0F};
                final float rightValues [] = rightHsvValues;

            //Enable color sensor LED
            leftColorSensor.enableLed(true);
            rightColorSensor.enableLed(true);

            /************
             * Path Start
             ************/
            //Drive to stones
            Drive(12,0.25,0);
            Drive(4,0.05,100);
            //If color sensor needs time to read values, increase pause above

            if (rightHsvValues[0] > 60) {
                telemetry.addLine()
                        .addData("Skystone Location: ", "right");
            } else if (leftHsvValues[0] > 60) {
                telemetry.addLine()
                        .addData("Skystone Location: ", "left");
            } else {
                telemetry.addLine()
                        .addData("Skystone Location: ", "center");
            }

            //If right, slide right, grab, deliver, return to wall (adjusting 8" right)
            //If left, slide left, grab, deliver, return to wall (adjusting 8" left)
            //If center, grab, deliver, return to wall (no translation)

            //Align to sample second time

            //If right, slide right, grab, deliver, park (adjusting 8" right)
            //If left, push middle stone away, rotate left, grab, realign, deliver, park
            //If center, grab, deliver, park (no translation)

            //If objective completed early, consider returning for more stones
        }
    }

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

    public void StoneHooksUp() {
        leftStoneHook.setPosition(0);
        rightStoneHook.setPosition(0);
    }

    public void LeftStoneHookDown() {
        leftStoneHook.setPosition(1);
    }

    public void RightStoneHookDown() {
        rightStoneHook.setPosition(1);
    }
    public void CloseClaw() {
        claw.setPosition(-1);
    }

    public void OpenClaw() {
        claw.setPosition(0);
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
        arm.setTargetPosition(distance * 45);
    }
    public void ArmPower(double power) {
        arm.setPower(power);
    }

}