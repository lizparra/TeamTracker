package com.drunk.mode.Callbacks;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.drunk.mode.Fragments.RootFragment;


public class BackPressImpl implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressImpl(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPressed() {

        if (parentFragment == null) return false;

        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            return false;

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
                childFragmentManager.popBackStackImmediate();
            }

            return true;
        }
    }

    @Override
    public RootFragment getFragment() {
        return (RootFragment) parentFragment;
    }
}
