package com.framgia.music_50.data.source.remote;

import com.framgia.music_50.data.model.Track;
import com.framgia.music_50.data.source.TrackDataSource;
import com.framgia.music_50.utils.Constant;

public class TrackRemoteDataSource implements TrackDataSource.RemoteDataSource {
    private static TrackRemoteDataSource sInstance;

    public static TrackRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getTrendingTracks(OnFetchDataListener<Track> listener) {
        new GetTracksAsyncTask(listener).execute(Constant.TRENDING_MUSIC_URL);
    }
}
