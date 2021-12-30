package com.example.myfristgame;

import static com.example.myfristgame.GameView.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public class BackGround {

    int x = 0,y = 0;
    Bitmap backGround, bgGameOver;
    Matrix matrix = new Matrix();


    //set background for the game.
    BackGround(int screenX, int screenY, Resources res){
        matrix.postRotate(90);
        backGround = BitmapFactory.decodeResource(res, R.drawable.background);
        backGround = Bitmap.createScaledBitmap(backGround,screenX, screenY, false);

        bgGameOver = BitmapFactory.decodeResource(res, R.drawable.backgroundgameover);
        bgGameOver = Bitmap.createScaledBitmap(bgGameOver,screenX, screenY, false);
    }

//    Rect getCollisionShape(){
//        return new Rect(0,-2000, screenX, screenY);
//    }

}
