package org.firstinspires.ftc.teamcode;

public class mtzConstants {
// Adjustments for efficiency
    public static final double driveEfficiency = 1.0;
    public static final double strafeEfficiency = 1.0;
    public static final double turnEfficiency = 91.8;
    public static final double armRotationEfficiency = 1.0;
    public static final double armExtensionEfficiency = 1.0;
// Debug Delay
    public static final int defaultPauseTime = 200;  //Milliseconds after a command


// Timer
    public static double endGameStart = 90;
    public static double endGameWarning = endGameStart + 15;
    public static double endGameWarning2 = endGameStart + 25;
    public static double endGameOver = endGameStart + 30;
    public static double greenWarningTime = 60;
    public static double yellowWarningTime = 70;
    public static double redWarningTime = 80;
// Powers & Speeds
    public static double defaultArmPower = 0.75;
    public static double defaultArmLowerPower = 0.2;
    public static double defaultArmExtensionPower = 1.0;


    public static double driveBump = 1; // inches
    public static double strafeBump = 1; // inches
    public static double turnBump = 3; // degrees
    public static double wristBump = 0.03; // servo rotations

    public static double defaultDriveSpeed = 0.5;
    public static double driveSlowRatio = 0.7;
    public static double driveFastRatio = 2.0;


// Positions
    public static final double clawOpenPosition = 1;
    public static final double clawClosedPosition = 0;
    public static final double blockThrowerDownPosition = 0.55;
    public static final double blockThrowerUpPosition = 1.0;
    public static final double leftHookUpPosition = 0.5;
    public static final double rightHookUpPosition = 0.5;
    public static final double leftHookDownPosition = 0;
    public static final double rightHookDownPosition = 0;
    public static final double leftHookInPosition = 1.0;
    public static final double rightHookInPosition = 1.0;
    public static final int handAssistRideHeightLevel = 1;
    public static final int handAssistRideHeightDistance = 1;
    public static final boolean handAssistRideHeightAboveLevel = true;

    public static int skyStonePosition = 2;


// Adjustments for where home is for the hand
    public static final double armRotationDegreesAtHome = -36.011;
    public static final double armExtensionInchesAtHome = 3.22;
    public static final int stackDistanceAtHome = 0;
    public static final int stackLevelAtHome = 0;
    public static final double armExtensionCollapsedLength = 16.125;
    public static final double armPivotHeight = 11.375;
    public static final double armPivotDistance = 15.65;

// Adjustments for hand position
    public static final double handHorizontalAdjustment = 0;
    public static final double handVerticalAdjustment = 0;

//Max Ranges
    public static final double minArmExtensionInches = 0;
    public static final double maxArmExtensionInches = 200/25.4; //200mm stroke
    public static final double minArmDegrees = -60;
    public static final double maxArmDegrees = 70;
    public static final double minWristPosition = 0.4;
    public static final double maxWristPosition = 1.0;

// Stack Arrays
    public static final double[] stackHeightOnLevelArray = {0,1,5,9,13,17,21,25,29,33};
    public static final double[] stackHeightAboveLevelArray = {3,4,8,12,16,20,24,28,32,36};
    public static final double[] stackDistanceArray = {0,1.2,5.2};

// Conversions
    public static final double ticksPerRevolution1150 = 145.6;
    public static final double ticksPerRevolution435 = 383.6;

    private static final double gearReductionArm = 24.0;
    private static final double gearReductionExtensionSpur = 1.0;
    private static final double translationInchPerRotationExtensionScrew = 4 * 2.0 / 25.4; //4 Starts, 2mm pitch, 25.4 mm/inch
    private static final double gearReductionWheel = 2.0;
    private static final double wheelDiameterInches = 4.0;

    public static final double ticksPerInchWheelDrive = driveEfficiency * (ticksPerRevolution1150 * gearReductionWheel) / (Math.PI * wheelDiameterInches);
    public static final double ticksPerInchWheelStrafe = strafeEfficiency * (ticksPerRevolution1150 * gearReductionWheel) / (Math.PI * wheelDiameterInches);
    public static final double ticksPerDegreeTurnChassis = turnEfficiency * ((ticksPerRevolution1150 * gearReductionWheel) / (Math.PI * wheelDiameterInches))/360;

    public static final double ticksPerDegreeArm = armRotationEfficiency * (ticksPerRevolution435 /360 ) * gearReductionArm;
    public static final double ticksPerInchExtension = armExtensionEfficiency * ticksPerRevolution435 * gearReductionExtensionSpur / translationInchPerRotationExtensionScrew;

    //conversion methods
    public static double armLengthDesired(double horDesired, double vertDesired){
        double horArmLengthDesired = horDesired + armPivotDistance;
        double vertArmLengthDesired = vertDesired - armPivotHeight;
        return Math.sqrt(Math.pow(horArmLengthDesired,2) + Math.pow(vertArmLengthDesired,2));
    }

    public static double wristConversionToServo(double angle){
        double servoPosition = 0.5;
        double wristAngles[] = {40.00, 90.0, 140};
        double wristNumbers[] = {minWristPosition, 0.69, maxWristPosition};
        for(int i=0;i <= wristAngles.length - 1;i++ ){
            if(wristAngles[i]>=angle && i==0){
                servoPosition = wristNumbers[i];
                break;
            } else
                if(wristAngles[i]>=angle && i>0){
                //i is higher: then we use it as the upper bound and prorate down to i-1 angles & numbers
                    servoPosition = prorate(angle,wristAngles[i-1],wristAngles[i],wristNumbers[i-1],wristNumbers[i]);
                break;
            } else {
                    servoPosition = wristNumbers[i];
                break;
            }

        }
        return servoPosition;
    }
    public static double prorate(double givenNumber, double givenRangeLow, double givenRangeHigh, double findRangeLow, double findRangeHigh){
        double findNumber;
        findNumber= findRangeHigh-((givenRangeHigh-givenNumber)/(givenRangeHigh-givenRangeLow)*(findRangeHigh-findRangeLow));
        return findNumber;
    }
    public static int findStackLevel(){
        int level = 0;
        return level;
    }
    public static int findStackDistance(){
        int level = 0;
        return level;
    }

}
