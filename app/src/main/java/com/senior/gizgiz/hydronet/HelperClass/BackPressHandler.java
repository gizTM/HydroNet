package com.senior.gizgiz.hydronet.HelperClass;

import android.app.Fragment;
import android.app.FragmentManager;

import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;

/**
 * Created by Admins on 009 09/02/2018.
 */

public class BackPressHandler implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressHandler(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPressed() {
        if (parentFragment == null) return false;
        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();
        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;
        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);
                // propagate onBackPressed method call to the child Fragment
                if (!childFragment.onBackPressed()) {
                    // child Fragment was unable to handle the task
                    // It could happen when the child Fragment is last last leaf of a chain
                    // removing the child Fragment from stack
                    childFragmentManager.popBackStackImmediate();
                }
            }
            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }
    }
}
