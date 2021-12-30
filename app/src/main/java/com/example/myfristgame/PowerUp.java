package com.example.myfristgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.myfristgame.GameView.*;

public class PowerUp {

    public int x,y;
    public int width, height;
    public int speedRun = 75;
    Bitmap powerUp;
    public int powerTRemaining;
    public boolean wasUsed = true;

    PowerUp(Resources resources){

        powerUp = BitmapFactory.decodeResource(resources, R.drawable.powerup);

        width = (int) (powerUp.getWidth() * screenRatioX / 1.5);
        height = (int) (powerUp.getHeight() * screenRatioY / 1.5);

        powerUp = Bitmap.createScaledBitmap(powerUp,width,height,false);

    }

    Bitmap getPowerUp(){
        return powerUp;
    }

    Rect getCollisionShape(){
        return new Rect(x,y, x + width, y + height);
    }

}
