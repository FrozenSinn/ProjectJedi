package com.example.cram_.projectjedi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;
    Boolean prepared = false;

    private final IBinder binder = new MusicBinder();

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public void playMusic() {
        if (!prepared){
            mediaPlayer = MediaPlayer.create(MusicService.this, R.raw.the_cold);
            try{
                mediaPlayer.prepare();
            }
            catch (Exception e) {}
            prepared = true;
        }
        mediaPlayer.start();
    }

    public void pauseMusic() {
        if(prepared && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void stopMusic() {
        if(prepared) {
            mediaPlayer.seekTo(0);
            mediaPlayer.release();
            mediaPlayer = null;
            prepared = false;
        }
    }

}
