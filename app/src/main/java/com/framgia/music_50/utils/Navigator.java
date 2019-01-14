package com.framgia.music_50.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.framgia.music_50.screen.playlist.PlaylistFragment;

public class Navigator {
    private Activity mActivity;
    private Fragment mFragment;

    public Navigator(Activity activity) {
        mActivity = activity;
    }

    public Navigator(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
    }

    public void goNextChildFragment(FragmentManager fragmentManager, int containerViewId,
            Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentByTag(tag);
        if (currentFragment == null || tag.equals(PlaylistFragment.TAG)) {
            currentFragment = fragment;
            transaction.add(containerViewId, fragment, tag);
        }
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        showFragment(fragmentManager, transaction, currentFragment);
    }

    private void showFragment(FragmentManager fragmentManager, FragmentTransaction transaction,
            Fragment fragment) {
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            transaction.hide(fragmentManager.getFragments().get(i));
        }
        transaction.show(fragment);
        transaction.commit();
    }

    public void removeFragment(FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
