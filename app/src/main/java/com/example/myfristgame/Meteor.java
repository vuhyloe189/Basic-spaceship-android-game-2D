package com.example.myfristgame;

import static com.example.myfristgame.GameView.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.sql.Struct;
import java.util.Random;

public class Meteor {

    public int x, y;
//    public int shotCount = 0;
    public int width, height, width1, height1, width3, height3;
    public int speedRun = 30;
    public boolean wasExplode = true;
    Bitmap meteor, lengthMeteor, HugeMeteor;
    private Random random;
    private int appear;

    //create a meteor
    Meteor(Resources resources){

        random = new Random();

        meteor = BitmapFactory.decodeResource(resources, R.drawable.stone);
        lengthMeteor = BitmapFactory.decodeResource(resources, R.drawable.lengstone);
        HugeMeteor = BitmapFactory.decodeResource(resources, R.drawable.hugemeteor);

        width = meteor.getWidth();
        height = meteor.getHeight();
        width = (int) (width * screenRatioX / 3);
        height = (int) (height * screenRatioY / 3);

        width1 = lengthMeteor.getWidth();
        height1 = lengthMeteor.getHeight();
        width1 = (int) (width1 * screenRatioX / 3);
        height1 = (int) (height1 * screenRatioY / 3);

        width3 = HugeMeteor.getWidth();
        height3 = HugeMeteor.getHeight();
        width3 = (int) (width3 * screenRatioX / 3);
        height3 = (int) (height3 * screenRatioY / 3);

        meteor = Bitmap.createScaledBitmap( meteor, width, height,false);
        lengthMeteor = Bitmap.createScaledBitmap( lengthMeteor, width1, height1,false);
        HugeMeteor = Bitmap.createScaledBitmap( HugeMeteor, width3, height3,false);


    }

    // get meteor on Game view
    Bitmap getMeteor(){
        return meteor;
    }

    Bitmap getLengthMeteorMeteor(){
        return lengthMeteor;
    }

    Bitmap getBigMeteor(){
        return HugeMeteor;
    }

//    Bitmap ComparisonMeteor(){
//
//    }



    // count bullet to delete meteor
    public int countMeteor(int shot){
        int sumShot = shot;
        if (0 < sumShot){
            sumShot--;
        }
        return sumShot;
    }

    // when collision
    Rect getCollisionShape(){
        return new Rect(x,y, x + width, y + height);
    }
    Rect getCollisionShape1(){
        return new Rect(x,y, x + width1, y + height1);
    }
    Rect getCollisionShape3(){
        return new Rect(x,y, x + width3, y + height3);
    }
}
