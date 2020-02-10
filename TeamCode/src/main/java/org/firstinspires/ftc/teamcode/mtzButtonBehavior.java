package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled

public class mtzButtonBehavior {

    public boolean isUp, isDown, clickedUp, clickedDown;

    public void update(boolean buttonDown){
        if(buttonDown){
            if (this.isUp){
                this.clickedDown=true;
            } else {
                this.clickedDown=false;
            }
            this.isDown = true;
            this.isUp = false;
            this.clickedUp = false;
        } else {
            if (this.isDown){
                this.clickedUp=true;
            } else {
                this.clickedUp=false;
            }
            this.isUp = true;
            this.isDown = false;
            this.clickedDown = false;
        }
    }
}
