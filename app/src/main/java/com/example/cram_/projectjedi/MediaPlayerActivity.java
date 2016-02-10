package com.example.cram_.projectjedi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MediaPlayerActivity extends BaseActivity implements View.OnClickListener{

    SharedPreferences settings;
    public static final String PREFS_NAME = "prefs";
    boolean playing = false;

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

    ImageView iPlay, iStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Intent intent = new Intent(MediaPlayerActivity.this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        iPlay = (ImageView) findViewById(R.id.imageViewPlay);
        iPlay.setOnClickListener(MediaPlayerActivity.this);
        iStop = (ImageView) findViewById(R.id.imageViewStop);
        iStop.setOnClickListener(MediaPlayerActivity.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();

                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewPlay:
                if(!playing) {
                    mService.playMusic();
                    playing = true;
                    iPlay.setImageResource(R.drawable.pause_icon);
                }
                else {
                    mService.pauseMusic();
                    playing = false;
                    iPlay.setImageResource(R.drawable.play_icon);
                }
                break;
            case R.id.imageViewStop:
                mService.stopMusic();
                playing = false;
                iPlay.setImageResource(R.drawable.play_icon);
                break;
        }
    }
}
