package com.example.administrator.ch12activityplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MobileProgramming", "onCreate()");

        setContentView(R.layout.activity_main);

        // MediaPlayer 媛앹껜 �깮�꽦
        mediaPlayer = MediaPlayer.create(this, )

        //****************************

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MobileProgramming", "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MobileProgramming", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MobileProgramming", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MobileProgramming", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MobileProgramming", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MobileProgramming", "onDestory()");
    }
}

