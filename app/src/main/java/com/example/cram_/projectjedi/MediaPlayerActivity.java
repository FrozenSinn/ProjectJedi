package com.example.cram_.projectjedi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MediaPlayerActivity extends BaseActivity implements View.OnClickListener{

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;

            mService = binder.getService();

            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    MusicService mService;
    boolean bound = false;

    //MediaPlayer mediaPlayer;
    Button bPlay, bPause, bStop;
    //Boolean prepared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        currentView = R.id.activityMPlayer;


        Intent intent = new Intent(MediaPlayerActivity.this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        bPlay = (Button) findViewById(R.id.buttonPlay);
        bPlay.setOnClickListener(MediaPlayerActivity.this);
        bPause = (Button) findViewById(R.id.buttonPause);
        bPause.setOnClickListener(MediaPlayerActivity.this);
        bStop = (Button) findViewById(R.id.buttonStop);
        bStop.setOnClickListener(MediaPlayerActivity.this);
        //prepared = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                /*if (!prepared){
                    mediaPlayer = MediaPlayer.create(MediaPlayerActivity.this, R.raw.zombie_walk);
                    try{
                        mediaPlayer.prepare();
                    }
                    catch (Exception e) {}
                    prepared = true;
                }
                mediaPlayer.start();*/
                mService.playMusic();
                break;
            case R.id.buttonPause:
                //if(prepared && mediaPlayer.isPlaying()) mediaPlayer.pause();
                mService.pauseMusic();
                break;
            case R.id.buttonStop:
                /*if(prepared) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    prepared = false;
                }*/
                mService.stopMusic();
                break;
        }
    }
}
