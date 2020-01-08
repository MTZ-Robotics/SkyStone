package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class z_test_MecanumTest2 extends LinearOpMode
{
    private DcMotor fr;
    private DcMotor br;
    private DcMotor fl;
    private DcMotor bl;
    private DcMotor arm;
    Servo servo;
    double servoPosition = 0.0;
    @Override
    public void runOpMode(){
        fl=hardwareMap.dcMotor.get("frontLeft");
        fr=hardwareMap.dcMotor.get("frontRight");
        bl=hardwareMap.dcMotor.get("backLeft");
        br=hardwareMap.dcMotor.get("backRight");
        arm=hardwareMap.dcMotor.get("arm");
        servo=hardwareMap.servo.get("claw");
        servo.setPosition(servoPosition);
        fl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        double halfspeed=1;
        double reverse=1;
        boolean apress=false;
        boolean bpress=false;
        stat("HardwareMap Complete");
        waitForStart();
        while(!gamepad1.dpad_up&&opModeIsActive()){

            if(gamepad1.a){
                apress=true;
            }
            else if(apress){
                apress=false;
                if(halfspeed==0.5){
                    halfspeed=1;
                }
                else{
                    halfspeed=0.5;
                }
            }

            if(gamepad1.b){
                bpress=true;
            }
            else if(bpress){
                bpress=false;
                if(reverse==-1){
                    reverse=1;
                }
                else{
                    reverse=-1;
                }
            }
            stat(new String[]{"Halfspeed (A): "+halfspeed,"Reverse (B): "+reverse,"Running TeleOp","DPAD-Up To Exit OpMode","DPAD-Down to run Motor Test"});
            bl.setPower(halfspeed*(reverse*(gamepad1.right_stick_y+gamepad1.left_stick_x)-gamepad1.right_stick_x));
            br.setPower(halfspeed*(reverse*(gamepad1.right_stick_y-gamepad1.left_stick_x)+gamepad1.right_stick_x));
            fl.setPower(halfspeed*(reverse*(-gamepad1.right_stick_y+gamepad1.left_stick_x)+gamepad1.right_stick_x));
            fr.setPower(halfspeed*(reverse*(-gamepad1.right_stick_y-gamepad1.left_stick_x)-gamepad1.right_stick_x));
            arm.setPower(gamepad2.left_stick_y);
            servo.setPosition(gamepad2.right_stick_y);
        }
    }



    public void stat(String[] in){
        for(String a : in){
            telemetry.addData("Status",a);
        }
        telemetry.update();
    }

    public void stat(String in){
        telemetry.addData("Status",in);
        telemetry.update();
    }
    public void motorstop(){
        for(DcMotor M : new DcMotor[]{fl,bl,fr,br,arm}){
            M.setPower(0);
        }
    }
}

