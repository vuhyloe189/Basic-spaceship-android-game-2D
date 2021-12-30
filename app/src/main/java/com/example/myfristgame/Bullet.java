package com.example.myfristgame;

import static com.example.myfristgame.GameView.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {

    public int width, height,width1, height1, width2, height2;
    int x,y;
    Bitmap bullet,iconBullet, doubleBullet;

    // create a bullet
    Bullet(Resources resources){
        bullet = BitmapFactory.decodeResource(resources, R.drawable.bullet);
        iconBullet = BitmapFactory.decodeResource(resources, R.drawable.iconbullet);
        doubleBullet = BitmapFactory.decodeResource(resources, R.drawable.doublebullet);

        // resize a bullet
        width = bullet.getWidth();
        height = bullet.getHeight();

        width /= 3;
        height /= 3;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        //resize icon of bullet
        width1 = iconBullet.getWidth();
        height1 = iconBullet.getHeight();

        width1 /= 2;
        height1 /= 2;

        width1 = (int) (width1 * screenRatioX);
        height1 = (int) (height1 * screenRatioY);

        width2 = (int) (doubleBullet.getWidth() * screenRatioX / 3);
        height2 = (int) (doubleBullet.getHeight() * screenRatioY / 3);

        //final create a bullet and scale size
        bullet = Bitmap.createScaledBitmap(bullet,width,height,false);
        iconBullet = Bitmap.createScaledBitmap(iconBullet,width1,height1,false);
        doubleBullet = Bitmap.createScaledBitmap(doubleBullet,width2,height2,false);


    }

    Bitmap getIconBullet(){return iconBullet;}

    Rect getCollisionShape(){
        return new Rect(x,y, x + width, y + height);
    }
}
