package com.example.myfristgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import java.util.logging.Handler;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.concurrent.*;
import com.ea.async.Async;
//import com.google.android.gms.tasks.Task;



@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {

    private int forBreath = 0, boundBreath = 0;
    private final static int MAX_VOLUME = 100;

    private int boundUp = 70;

    public static int rmHeart = 3;

    private Thread thread;
    private boolean isPlaying;
    private boolean isGameOver = false;
    private final boolean end  = false;
    Random random;
    private int meteorShot = 25;
    private final BackGround backGroundStart, backGroundSecond, backGroundThird, backGroundEnd;
    private final BackGround bgGameOver;
    public static int screenX, screenY;
    private int score = 0;
    public int bulletRange = 20;

    public int doubleBulletRange = 100;
    public int timePR = 150;

    public boolean acceptPower = false;


    private final Paint paint;
    private final Paint paint2;
    private final Unit unit;
    private int countWait = 0;
    private final int countRestart = 0;
    private final List<Bullet> bullets;
    private final List<Bullet> doublebullets;
    private final List<Meteor> meteors;
    private final List<Meteor> lmeteors;
    private final List<Meteor> hmeteors;
    private final PowerUp powerUp;
    private final Heart heart;
    private final Heart deathheart;
    private final Bullet iconBullet;
    private final SoundPool soundPool;
    MediaPlayer soundBum;
    private final int sound;

    public static float screenRatioX, screenRatioY;

    private final SharedPreferences sharedPreferences;

    //set double click
    private long startTime;
    static final int MAX_DURATION = 200;

//    private Async async;
//    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public GameView(Context context, int screenX, int screenY) {
        super ( context );

        //save the info into sharePres
        sharedPreferences = context.getSharedPreferences("game",Context.MODE_PRIVATE);
//        Async.init();

        //create a sound
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);

        }
        sound = soundPool.load(context, R.raw.pewpew,1);

        soundBum = MediaPlayer.create(context,R.raw.bumm);
//        float volume = (float) (1 - (Math.log(MAX_VOLUME) / Math.log(MAX_VOLUME)));
        soundBum.setVolume(1, 1);


        GameView.screenX = screenX;
        GameView.screenY = screenY;

        //random
        random = new Random();

        //scale the ratio for every android pixel.
        screenRatioX =  screenX / 1080f;
        screenRatioY =  screenY / 1920f;

        //create a unit moving
        unit = new Unit(this, screenX,screenY, getResources());

        // create icon of the bullet
        iconBullet = new Bullet(getResources());

        // create a list of bullets when shooting.
        bullets = new ArrayList<>();
        doublebullets = new ArrayList<>();

        //create a array of bird.
        meteors = new ArrayList<>();
        lmeteors = new ArrayList<>();
        hmeteors = new ArrayList<>();

        //create a heart
        heart = new Heart(getResources());
        deathheart = new Heart(getResources());

        //create a power up items
        powerUp = new PowerUp(getResources());
        powerUp.x = random.nextInt(screenX - powerUp.width);

        // set background for 2 screen
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setTextSize(64);
        paint2.setColor(Color.YELLOW);


        backGroundStart = new BackGround(screenX, screenY, getResources());
        backGroundEnd = new BackGround(screenX, screenY, getResources());
        backGroundSecond = new BackGround(screenX, screenY, getResources());
        backGroundThird = new BackGround(screenX, screenY, getResources());

        bgGameOver = new BackGround(screenX,screenY,getResources());

        backGroundStart.y = screenY / 2;
        backGroundSecond.y = 0;
        backGroundThird.y = -screenY / 2;
        backGroundEnd.y = -screenY;


    }

    // run, pause, sleep and resume game each time
    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            sleep();
        }
    }

    private void update(){

        int remainingHeart = 3;


        // move screen x pixels (with same speed for different screen types)
        backGroundStart.y += 10 * screenRatioY;
        backGroundEnd.y += 10 * screenRatioY;
        backGroundSecond.y += 10 * screenRatioY;
        backGroundThird.y += 10 * screenRatioY;
//        Log.d("bgsY1", String.valueOf(backGroundStart.y));

        // if the frame was out of the screen, set the height ratio = - screen height.
        if( backGroundStart.y >= screenY){
            backGroundStart.y = -screenY;

        }
        // the same in the second
        if( backGroundEnd.y  >=  screenY){
            backGroundEnd.y = -screenY;
        }

        if( backGroundSecond.y >= screenY){
            backGroundSecond.y = -screenY;

        }
        // the same in the third
        if( backGroundThird.y  >=  screenY){
            backGroundThird.y = -screenY;
        }

//        if(unit.isGoing){
//            unit.x -= 20 * screenRatioX;
//        } else{
//            unit.x += 20 * screenRatioX;
//        }

        // set moving of unit, if it was being touched (isGoing = true).
        if(unit.isGoing){

        }
        if(unit.x <= 0){
            unit.x = 0;
        }
        if(unit.x >= screenX - unit.width){
            unit.x = screenX - unit.width;
        }
        if(unit.y <= 0){
            unit.y = 0;
        }
        if(unit.y >= screenY - unit.height ){
            unit.y = screenY - unit.height;
        }

        //create Meteor
//        for(int i = 0; i <= random.nextInt(6); i++){
//            Meteor meteor = new Meteor(getResources());
//            meteors[i] = meteor;
//            meteor.x = random.nextInt(screenX - meteor.width);
////            plus++;
//        }

        // check condition
        // create a list trash to remove which bullet out screen.
        List<Bullet> trash = new ArrayList<>();
        //trash for meteor
        List<Meteor> exploreMeteor = new ArrayList<>();

        //count meteor which ...
        List<Meteor> countMeteor = new ArrayList<>();

        for (Bullet bullet : bullets){
            if(bullet.y < 0){
                trash.add(bullet);
            }
            // shoot each 150 pixel after scale
            bullet.y -= 50 * screenRatioY;

            //used when meteorite was hit
            for (Meteor meteor : meteors){

                if(Rect.intersects(meteor.getCollisionShape(), bullet.getCollisionShape())){
                    score++;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }
            for (Meteor meteor : lmeteors){
                if(Rect.intersects(meteor.getCollisionShape1(), bullet.getCollisionShape())){
                    score++;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }

            for (Meteor meteor : hmeteors){
                if(Rect.intersects(meteor.getCollisionShape3(), bullet.getCollisionShape())){
//                    if(meteor.countMeteor(meteorShot) > 0){
//                        meteorShot = meteor.countMeteor(meteorShot);
//                        return;
//                    }
                    score++;
                    meteorShot = 25;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }
        }

        for (Bullet bullet : doublebullets){
            if(bullet.y < 0){
                trash.add(bullet);
            }
            // shoot each 150 pixel after scale
            bullet.y -= 50 * screenRatioY;

            //used when meteorite was hit
            for (Meteor meteor : meteors){

                if(Rect.intersects(meteor.getCollisionShape(), bullet.getCollisionShape())){
                    score++;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }
            for (Meteor meteor : lmeteors){
                if(Rect.intersects(meteor.getCollisionShape1(), bullet.getCollisionShape())){
                    score++;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }

            for (Meteor meteor : hmeteors){
                if(Rect.intersects(meteor.getCollisionShape3(), bullet.getCollisionShape())){
//                    if(meteor.countMeteor(meteorShot) > 0){
//                        meteorShot = meteor.countMeteor(meteorShot);
//                        return;
//                    }
                    score++;
                    meteorShot = 25;
                    exploreMeteor.add(meteor);
                    trash.add(bullet);
                    meteor.wasExplode = true;
                }
            }
        }

        //delete meteor which was shot.
        for(Meteor meteor: exploreMeteor){
            meteors.remove(meteor);
        }
        for(Meteor meteor: exploreMeteor){
            lmeteors.remove(meteor);
        }
        for(Meteor meteor: exploreMeteor){
            hmeteors.remove(meteor);
        }


        // remove out bullet
        for (Bullet bullet: trash){
            bullets.remove(bullet);
        }

        //check valid of meteor.
        for (Meteor meteor: meteors){

            meteor.y += meteor.speedRun;

            // move left right for the meteor
            if(meteor.x >= 0 && meteor.x + meteor.width <= screenX * screenRatioX){
                int leftRight = 0;
                leftRight = random.nextInt(2);
                if(leftRight == 0){
                    meteor.x += 5;
                } else {
                    meteor.x -= 5;
                }
            } else if( meteor.x < 0) {
                meteor.x += 5;
            } else{
                meteor.x -= 5;
            }

            //set was explode = false.
            //if not one meteor explore after moving out of frame, game over
            meteor.wasExplode = false;
            if(meteor.y  + meteor.height > screenY){
                remainingHeart--;
            }
            if(Rect.intersects(meteor.getCollisionShape(), unit.getCollisionShape())){
                meteor.y = screenY + 10;
                remainingHeart--;
            }
        }

        //check valid of length meteor.
        for (Meteor meteor: lmeteors){

            meteor.y += meteor.speedRun;


            // move left right for the meteor
            if(meteor.x >= 0 && meteor.x + meteor.width1 <= screenX * screenRatioX){
                int leftRight = 0;
                leftRight = random.nextInt(2);
                if(leftRight == 0){
                    meteor.x += 3;
                } else {
                    meteor.x -= 3;
                }
            } else if( meteor.x < 0) {
                meteor.x += 3;
            } else{
                meteor.x -= 3;
            }

            //set was explode = false.
            //if not one meteor explore after moving out of frame, game over
            meteor.wasExplode = false;
            if(meteor.y  + meteor.height > screenY){
                remainingHeart--;
            }

            if(Rect.intersects(meteor.getCollisionShape1(), unit.getCollisionShape())){
                remainingHeart--;
                meteor.y = screenY + 10;
            }
        }

        //check valid of huge meteor.
        for (Meteor meteor: hmeteors){

            meteor.y += meteor.speedRun;
            // move left right for the meteor
            if(meteor.x >= 0 && meteor.x + meteor.width3 <= screenX * screenRatioX){
                int leftRight = 0;
                leftRight = random.nextInt(2);
                if(leftRight == 0){
                    meteor.x += 1;
                } else {
                    meteor.x -= 1;
                }
            } else if( meteor.x < 0) {
                meteor.x += 1;
            } else{
                meteor.x -= 1;
            }

            //set was explode = false.
            //if not one meteor explore after moving out of frame, game over
            meteor.wasExplode = false;
            if(meteor.y  + meteor.height > screenY){
                remainingHeart--;

            }
            if(Rect.intersects(meteor.getCollisionShape3(), unit.getCollisionShape())){
                remainingHeart--;
                meteor.y = screenY + 10;
            }
        }

        if(timePR == 0) {
            powerUp.y += powerUp.speedRun;
            if(Rect.intersects(powerUp.getCollisionShape(), unit.getCollisionShape())){
                powerUp.wasUsed = true;
                acceptPower = true;
                timePR = 5000;
            }
            if(powerUp.y + powerUp.height > screenY){
                powerUp.wasUsed = false;
                acceptPower = false;
                timePR = 2000;
            }
        }

        rmHeart = remainingHeart;

        //create new meteor
        newMeteor();

        //recall bullet after 32 thread.sleep()
        bulletRange();
    }

    //time each reaptions.
    private void sleep(){
        // every launch just after 17/1000 seconds. The larger the number, the lower the screen's rate of change.
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   }

   // resume
    public void resume(){

        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    //pause
    public void pause(){

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // draw the game background with Canvas and Paint
    private void draw(){
        if(getHolder().getSurface().isValid()){
            //draw a surface area(background) to write to
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGroundStart.backGround, backGroundStart.x, backGroundStart.y, paint);
            canvas.drawBitmap(backGroundEnd.backGround, backGroundEnd.x, backGroundEnd.y, paint);
            canvas.drawBitmap(backGroundSecond.backGround, backGroundSecond.x, backGroundSecond.y, paint);
            canvas.drawBitmap(backGroundThird.backGround, backGroundThird.x, backGroundThird.y, paint);

            canvas.drawText(String.valueOf(score), (screenX - paint.getTextSize())/ 2f, 150 * screenRatioY, paint);

            //if game over, no need Canvas anything else
            if(isGameOver){

                if(sharedPreferences.getBoolean("isMute", false)){
                    soundBum.start();
                }

//                @Deprecated
//                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... voids) {
//                        getHolder().unlockCanvasAndPost(canvas);
//
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//
//                    }
//                };
//                task.execute();

//                try{
//
//                    Thread.sleep(5000);
//
//                }catch (InterruptedException e){return;}

                saveHighScoreEver();

                canvas.drawBitmap(unit.getDead(), unit.x, unit.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                countWait++;
                //count for wait before shutdown the screen
                if(countWait == 5){
                    canvas = getHolder().lockCanvas();
                    canvas.drawBitmap(bgGameOver.bgGameOver, bgGameOver.x, bgGameOver.y, paint);
                    getHolder().unlockCanvasAndPost(canvas);
                    isPlaying = false;

//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    getContext().startActivity(intent);

                }
                return;
            }


            //draw remaining of heart
            switch (rmHeart){
                case 3:
                    canvas.drawBitmap(heart.getHeart(),0, 20 * screenRatioX, paint);
                    canvas.drawBitmap(heart.getHeart(),heart.width + 10 * screenRatioY, 20 * screenRatioX, paint);
                    canvas.drawBitmap(heart.getHeart(),2 * heart.width + 20 * screenRatioY, 20 * screenRatioX, paint);
                    break;
                case 2:
                    canvas.drawBitmap(heart.getHeart(),0, 20 * screenRatioX, paint);
                    canvas.drawBitmap(heart.getHeart(),heart.width + 10 * screenRatioY, 20 * screenRatioX, paint);
                    canvas.drawBitmap(deathheart.getDeathHeartHeart(),2 * deathheart.width + 20 * screenRatioY, 20 * screenRatioX, paint);
                    break;
                case 1:
                    canvas.drawBitmap(heart.getHeart(),0, 20 * screenRatioX, paint);
                    canvas.drawBitmap(deathheart.getDeathHeartHeart(),deathheart.width + 10 * screenRatioY, 20 * screenRatioX, paint);
                    canvas.drawBitmap(deathheart.getDeathHeartHeart(),2 * deathheart.width + 20 * screenRatioY, 20 * screenRatioX, paint);
                    break;
                default:
                    canvas.drawBitmap(deathheart.getHeart(),0, 20 * screenRatioX, paint);
                    canvas.drawBitmap(deathheart.getHeart(),deathheart.width + 10 * screenRatioY, 20 * screenRatioX, paint);
                    canvas.drawBitmap(deathheart.getHeart(),2 * deathheart.width + 20 * screenRatioY, 20 * screenRatioX, paint);
                    isGameOver = true;
                    break;
            }


            if(checkPowerUpValid()){
                canvas.drawBitmap(powerUp.getPowerUp(), powerUp.x, powerUp.y, paint);
            }

            if(acceptPower){
                canvas.drawText(String.valueOf(doubleBulletRange), ((iconBullet.width1) + 20 ) , (screenY  - (iconBullet.height1 - paint2.getTextSize()) / 2) ,paint2);
            } else{
                // draw a unit place.
                // need draw it after draw a background
                canvas.drawText(String.valueOf(bulletRange), ((iconBullet.width1) + 20 ) , (screenY  - (iconBullet.height1 - paint2.getTextSize()) / 2) ,paint2);

            }

            // draw uit
            canvas.drawBitmap(unit.getMove(), unit.x, unit.y, paint);

            // draw the remaining of bullet
            canvas.drawBitmap(iconBullet.getIconBullet(),0,screenY - iconBullet.height1, paint2);

            // draw list bullet
            for(Bullet bullet: bullets){
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            }

            //draw list double bullet
            for(Bullet bullet: doublebullets){
                canvas.drawBitmap(bullet.doubleBullet, bullet.x, bullet.y, paint);
            }

            // draw 3 type of meteor
            for(Meteor meteor: meteors){
                canvas.drawBitmap(meteor.getMeteor(), meteor.x, meteor.y, paint);
            }

            for(Meteor meteor: lmeteors){
                canvas.drawBitmap(meteor.getLengthMeteorMeteor(), meteor.x, meteor.y, paint);
            }

            for(Meteor meteor: hmeteors){
                canvas.drawBitmap(meteor.getBigMeteor(), meteor.x, meteor.y, paint);
            }

            //write to the surface, with no this cmd, no other code can call lockCanvas()
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void saveHighScoreEver() {
        if(sharedPreferences.getInt("highScore", 0) < score){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }
    }


    //On touch go Left Right
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                if(event.getY() < screenY )
//                    unit.isGoing = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                unit.isGoing = false;
//                break;
//        }
//
//        return true;
//    }

    // create each new bullet.
    public void newBullet(boolean canShoot){
        if(canShoot){
            if(sharedPreferences.getBoolean("isMute", false)){
                soundPool.play(sound,1,1,0,0,1);
            }
            Bullet bullet = new Bullet(getResources());
            bullet.x = (int) ((unit.x  ) + (unit.width / 2) - (bullet.width / 2));
            bullet.y = (int) ((unit.y ) + 20 * screenRatioY) ;
            bullets.add(bullet);
            bulletRange--;
        }
    }

    public void newDoubleBullet(boolean canShoot){
        if(canShoot){
            if(sharedPreferences.getBoolean("isMute", false)){
                soundPool.play(sound,1,1,0,0,1);
            }
            Bullet bullet = new Bullet(getResources());
            bullet.x = (int) ((unit.x  ) + (unit.width / 2) - (bullet.width1 / 2));
            bullet.y = (int) ((unit.y  ) + 20 * screenRatioY) ;
            doublebullets.add(bullet);
            doubleBulletRange--;
        }
    }

    // create each new meteor.
    public void newMeteor(){

        int typeMedium = random.nextInt(50);

        if(boundBreath == 0){
            boundBreath = random.nextInt(25);
        }

        Meteor meteor = new Meteor(getResources());
        meteor.x = random.nextInt(screenX - meteor.width);
        meteor.y = 0;

        if(forBreath < boundBreath){
            forBreath++;
        }else{
            if(typeMedium < 23)
                meteors.add(meteor);
            else if(typeMedium < 46 && typeMedium >= 23)
                lmeteors.add(meteor);
            else
                hmeteors.add(meteor);
            forBreath = 0;
            boundBreath = 0;
        }
    }

    //set on Touch on screen
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionEvent = event.getAction();
        float X = event.getX();
        float Y = event.getY();

        switch (actionEvent & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                unit.isGoing = true;
                break;
            case MotionEvent.ACTION_UP:
                // set unit off fire when not touching
                unit.isGoing = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                //
                break;
            case MotionEvent.ACTION_MOVE:
                // when move the unit, function is start to calculate the coordinates.
                if(event.getY() < screenY * screenRatioY && event.getX() < screenX * screenRatioX){
                    unit.isGoing = true;
                }
                unit.x = (int) X;
                unit.y = (int) Y;
                break;
            default:
                //
                break;
        }


        if(!isPlaying){

            if (event.getAction() == MotionEvent.ACTION_UP) {

                startTime = System.currentTimeMillis();
            }
            else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(System.currentTimeMillis() - startTime <= MAX_DURATION)
                {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
            }
        }
        return true;
    }


    public boolean bulletRange(){
        if(bulletRange > 0){
            return true;
        } else if(unit.waitToBullet < 45){
            unit.waitToBullet++;
            return false;
        } else if(unit.waitToBullet == 45){
            bulletRange = 12;
            unit.waitToBullet = 0;
            return true;
        }
        return false;
    }

    public boolean doubleBulletRange(){
        if(doubleBulletRange > 0){
            return true;
        } else
            acceptPower = false;
            return false;
    }

    public boolean checkPowerUpValid(){
        if(timePR > 0){
            timePR--;
            return false;
        } else{
            return true;
        }
    }
}
