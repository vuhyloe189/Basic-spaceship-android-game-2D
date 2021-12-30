package com.example.myfristgame;

import static com.example.myfristgame.GameView.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;

public class Unit {

    public boolean isGoing = false;
    public int x, y, width, height, moveCounter = 0, deadStep = 0;
    Bitmap unitStart, unitEnd, unitMiddle, unitNothing, deadFirst, deadSecond, deadThird, deadFinal;
    private GameView gameView;
    public int waitToBullet = 0;
    public int waitToDoubleBullet = 0;

    // create a unit component and some active with this.
    // route with GameVIew to create a bullet shooting.
    Unit(GameView gameView, int screenX, int screenY, Resources res){

        this.gameView = gameView;

        // get image unit on BITMAP
        unitStart = BitmapFactory.decodeResource(res, R.drawable.units);
        unitEnd = BitmapFactory.decodeResource(res, R.drawable.unite);
        unitMiddle = BitmapFactory.decodeResource(res, R.drawable.unitm);
        unitNothing = BitmapFactory.decodeResource(res, R.drawable.nothing);
        deadFirst = BitmapFactory.decodeResource(res, R.drawable.deads);
        deadSecond = BitmapFactory.decodeResource(res, R.drawable.deadse);
        deadThird = BitmapFactory.decodeResource(res, R.drawable.deadthi);
        deadFinal = BitmapFactory.decodeResource(res, R.drawable.deade);

        width = unitStart.getWidth();
        height = unitStart.getHeight();

        width /= 3  ;
        height /= 3 ;


        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);


        // delay for game to update information
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() { }
//        }, 10000);

        // scale the unit in Bitmap
        // in realtime it cant catch height.
        unitStart = Bitmap.createScaledBitmap(unitStart, width,height, false);
        unitEnd = Bitmap.createScaledBitmap(unitEnd, width,height, false);
        unitMiddle = Bitmap.createScaledBitmap(unitMiddle, width,height, false);
        unitNothing = Bitmap.createScaledBitmap(unitNothing,1,1,false);

        //when the unit dead
        deadFirst = Bitmap.createScaledBitmap(deadFirst, width,height, false);
        deadSecond = Bitmap.createScaledBitmap(deadSecond, width,height, false);
        deadThird = Bitmap.createScaledBitmap(deadThird, width,height, false);
        deadFinal = Bitmap.createScaledBitmap(deadFinal, width,height, false);

        // set point
        x = (int) ((screenX - width) * screenRatioX / 2);
        y = (int) (screenY * screenRatioY - height);

        gameView.bulletRange();
    }

    // Change between 2 unit each count.
    // If count == 0 will show unitStart, else will show unitEnd



    Bitmap getMove() {

        if(!isGoing){
            return unitMiddle;
        } else {
            //when start game a bullet will auto create new
            // if only shoot when touch the screen
            if(gameView.acceptPower){
                gameView.newDoubleBullet(gameView.doubleBulletRange());
            } else {
                gameView.newBullet(gameView.bulletRange());
                gameView.doubleBulletRange = 100;
            }
            if(moveCounter == 0){
                moveCounter++;
                return unitStart;
            }else {
                moveCounter --;
                return unitEnd;
            }
        }
//        if(moveCounter <=  70){
//            moveCounter++;
//            return unitMiddle;
//        } else {
//            if(moveCounter == 71){
//                moveCounter++;
//                return unitStart;
//            } else {
//                moveCounter --;
//                return unitEnd;
//            }
//        }
    }

    Bitmap getDead(){
        if(deadStep == 0){
            deadStep++;
            return deadFirst;
        } else if(deadStep == 1){
            deadStep++;
            return  deadSecond;
        } else if(deadStep == 2){
            deadStep++;
            return  deadThird;
        } else if(deadStep == 3){
            deadStep++;
            return deadFinal;
        }
        deadStep = 0;
        return unitNothing;
    }

    Rect getCollisionShape(){
        return new Rect(x,y, x + width, y + height);
    }

    // get position
    public int resizeWidth(int width){
        return width * (int) screenRatioX;
    }
    public int resizeHeight(int height){
        return height * (int) screenRatioY;
    }

}
