//package com.senior.gizgiz.hydronet.Fragment;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.senior.gizgiz.hydronet.HelperClass.CustomFlipperLayout;
//
///**
// * Created by Admins on 017 17/1/2018.
// */
//
//public class FlipperFragment extends Fragment {
//    private CustomFlipperLayout flipperLayout;
//    private int inflatedLayoutId;
//    private int flipperId;
//    private int firstPageLayout,secondPageLayout;
//
//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(this.inflatedLayoutId,container,false);
////        flipperLayout = new TwoPageFlipperLayout(getContext());
//        flipperLayout = view.findViewById(this.flipperId);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view,savedInstanceState);
//    }
//
//    public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }
//
//    public void setInflatedLayoutId(int inflatedLayoutId) { this.inflatedLayoutId = inflatedLayoutId; }
//    public void setFlipperId(int flipperId) { this.flipperId = flipperId; }
//}

package com.senior.gizgiz.hydronet.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.senior.gizgiz.hydronet.HelperClass.CustomFlipperLayout;

/**
 * Created by Admins on 007 07/02/2018.
 */

public abstract class FlipperFragment extends Fragment {
    private static final String LAYOUT = "inflatedLayoutId";
    private static final String FLIPPER = "flipperId";
    private static final String SECOND_PAGE_LIST = "list";
    protected CustomFlipperLayout flipperLayout;
    protected int flipperId,inflatedLayoutId,secondPageListId;

    public FlipperFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            inflatedLayoutId = getArguments().getInt(LAYOUT);
//            flipperId = getArguments().getInt(FLIPPER);
//            secondPageListId = getArguments().getInt(SECOND_PAGE_LIST);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(inflatedLayoutId, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flipperLayout = view.findViewById(flipperId);
    }

    public void setFlipperId(int flipperId) { this.flipperId = flipperId; }
    public void setInflatedLayoutId(int inflatedLayoutId) { this.inflatedLayoutId = inflatedLayoutId; }


    public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }
    public void setViewSecondPage() { flipperLayout.setViewSecondPage(); }
    public void resetFlipper() { flipperLayout.resetFlipper(); }

//    public void setViewFirstPage() { flipperLayout.setViewFirstPage(); }
//    public void setViewSecondPage() { flipperLayout.setViewSecondPage(); }

//    public static FlipperFragment init(List<Integer> idList) {
//        FlipperFragment flipperFrag = new FlipperFragment();
//        Bundle args = new Bundle();
//        args.putInt(LAYOUT,idList.get(0));
//        args.putInt(FLIPPER,idList.get(1));
//        args.putInt(SECOND_PAGE_LIST,idList.get(2));
//        flipperFrag.setArguments(args);
//        return flipperFrag;
//    }
}
