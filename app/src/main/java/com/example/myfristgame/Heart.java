package com.example.myfristgame;

import static com.example.myfristgame.GameView.screenRatioX;
import static com.example.myfristgame.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Heart {
    public int width, height;
    int x,y;
    Bitmap heart,deathHeart;


    //create a Bitmap of heart
    Heart(Resources res){
        heart  = BitmapFactory.decodeResource(res, R.drawable.heart);
        deathHeart  = BitmapFactory.decodeResource(res, R.drawable.death_heart);

        //resize heart
        width = heart.getWidth();
        height = heart.getHeight();

        width = (int) (width * screenRatioX / 2);
        height = (int) (height * screenRatioY / 2);

        heart = Bitmap.createScaledBitmap(heart,width,height,false);
        deathHeart = Bitmap.createScaledBitmap(deathHeart,width,height,false);
    }

    Bitmap getHeart(){return heart;}
    Bitmap getDeathHeartHeart(){return deathHeart;}

}
