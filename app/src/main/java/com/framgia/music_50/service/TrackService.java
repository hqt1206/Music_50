package com.framgia.music_50.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import com.framgia.music_50.data.model.Track;
import com.framgia.music_50.utils.Constant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private final static String EXTRA_TRACK_LIST = "EXTRA_TRACK_LIST";
    private final static String EXTRA_TRACK_POSITION = "EXTRA_TRACK_POSITION";
    private final int DEFAULT_POSITION = 0;
    private List<Track> mTracks;
    private int mPosition;
    private MediaPlayer mMediaPlayer;
    private ServiceContract.OnMediaPlayerChange mOnMediaPlayerChange;
    private ServiceContract.OnMiniControllerChange mOnMiniControllerChange;
    private TrackBinder mTrackBinder;

    public static Intent getServiceIntent(Context context, List<Track> tracks, int position) {
        Intent intent = new Intent(context, TrackService.class);
        intent.putParcelableArrayListExtra(EXTRA_TRACK_LIST,
                (ArrayList<? extends Parcelable>) tracks);
        intent.putExtra(EXTRA_TRACK_POSITION, position);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTrackBinder = new TrackBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mTracks = intent.getParcelableArrayListExtra(EXTRA_TRACK_LIST);
            mPosition = intent.getIntExtra(EXTRA_TRACK_POSITION, DEFAULT_POSITION);
            if (mTracks != null) {
                play();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mTrackBinder;
    }

    public void setOnMediaChangeListener(ServiceContract.OnMediaPlayerChange mediaChangeListener) {
        mOnMediaPlayerChange = mediaChangeListener;
    }

    public void setOnMiniControllerChange(
            ServiceContract.OnMiniControllerChange miniControllerChange) {
        mOnMiniControllerChange = miniControllerChange;
    }

    public void play() {
        Track track = mTracks.get(mPosition);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(Constant.STREAM_BASE_URL + track.getId() + Constant.STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(this);
        if (mOnMediaPlayerChange != null) {
            mOnMediaPlayerChange.onTrackChange(track);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.setOnCompletionListener(this);
        if (mOnMediaPlayerChange != null) {
            mOnMediaPlayerChange.onMediaPlayerStart();
            mOnMediaPlayerChange.onMediaPlayerStateChange(true);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mOnMediaPlayerChange != null) {
            mOnMediaPlayerChange.onMediaPlayerStateChange(false);
        }
    }

    public int getCurrentDuration() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void playPauseTrack() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (mOnMediaPlayerChange != null) {
                mOnMediaPlayerChange.onMediaPlayerStateChange(false);
            }
        } else {
            mMediaPlayer.start();
            if (mOnMediaPlayerChange != null) {
                mOnMediaPlayerChange.onMediaPlayerStateChange(true);
            }
        }
    }

    public void seekTo(int duration) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(duration);
        }
    }

    public class TrackBinder extends Binder {
        public TrackService getService() {
            return TrackService.this;
        }
    }
}
