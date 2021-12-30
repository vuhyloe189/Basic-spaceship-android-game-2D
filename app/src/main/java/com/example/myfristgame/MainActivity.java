package com.example.myfristgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView play;
    TextView highScoreText;
    SharedPreferences sharedPreferences;
    private boolean isMute = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Scale full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setUIReference();
        //Click button to play game
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameAnimationActivity.class);
                startActivity(intent);
            }
        });

        highScoreText = findViewById(R.id.highScore);
        sharedPreferences = getSharedPreferences("game", MODE_PRIVATE);
        highScoreText.setText("High Score: " + sharedPreferences.getInt("highScore",0));

        isMute = sharedPreferences.getBoolean("isMute", false);

        ImageView muteControl = findViewById(R.id.volume);

        muteControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if(isMute){
                    muteControl.setImageResource(R.drawable.ic_baseline_volume_up_24);
                } else{
                    muteControl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();


            }
        });
    }

    // set UI for the component.
    private void setUIReference(){
        play = (TextView) findViewById(R.id.playButton);
    }
}