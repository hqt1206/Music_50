package com.framgia.music_50.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import com.framgia.music_50.R;
import com.framgia.music_50.data.model.Track;
import com.framgia.music_50.data.source.remote.OnFetchDataListener;
import com.framgia.music_50.screen.BaseActivity;
import com.framgia.music_50.screen.audio.AudioFragment;
import com.framgia.music_50.screen.home.HomeFragment;
import com.framgia.music_50.screen.library.LibraryFragment;
import com.framgia.music_50.screen.search.SearchFragment;
import com.framgia.music_50.utils.Navigator;
import java.util.List;

public class MainActivity extends BaseActivity
        implements MainContract.View, OnFetchDataListener<Track>,
        BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mBottomNavigationView;
    private Navigator mNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void initData() {
        mNavigator = new Navigator(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContainer, new HomeFragment());
        transaction.commit();
    }

    @Override
    public void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onSuccess(List<Track> data) {
    }

    @Override
    public void onError(Exception e) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.actionHome:
                mNavigator.goNextChildFragment(getSupportFragmentManager(), R.id.layoutContainer,
                        HomeFragment.newInstance(), false, HomeFragment.TAG);
                return true;
            case R.id.actionAudio:
                mNavigator.goNextChildFragment(getSupportFragmentManager(), R.id.layoutContainer,
                        AudioFragment.newInstance(), false, AudioFragment.TAG);
                return true;
            case R.id.actionSearch:
                mNavigator.goNextChildFragment(getSupportFragmentManager(), R.id.layoutContainer,
                        SearchFragment.newInstance(), false, SearchFragment.TAG);
                return true;
            case R.id.actionLibrary:
                mNavigator.goNextChildFragment(getSupportFragmentManager(), R.id.layoutContainer,
                        LibraryFragment.newInstance(), false, LibraryFragment.TAG);
                return true;
        }
        return false;
    }
}
