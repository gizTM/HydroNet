package com.senior.gizgiz.hydronet.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.OverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.TradeSummaryOverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.HelperClass.ViewPagerParallax;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 001 01/03/2018.
 */

public class TradeCarouselFragment extends Fragment implements OnBackPressListener {
    protected SmartTabLayout tabLayout;
    protected ViewPagerParallax tabPager;
    private SlidingTabAdapter tabAdapter;
    private View rootView,submenu;
    FragmentPagerItemAdapter adapter;

    private Fragment summaryFragment, buyFragment, sellFragment;
    private List<Fragment> pageFragments = new ArrayList<>();

    public TradeCarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_fragment_submenu, container, false);
        tabLayout = rootView.findViewById(R.id.sliding_tab);
        tabPager = rootView.findViewById(R.id.fragment_viewpager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        this.rootView = view;
        setupTabLayout();
        handleTabChange();
        view.findViewById(R.id.sub_page_title).setVisibility(View.GONE);
    }

    public boolean onBackPressed() {
        Fragment currentFragment = adapter.getItem(tabPager.getCurrentItem());
        if(currentFragment != null && currentFragment.isVisible()) {
            FragmentManager childFm = currentFragment.getChildFragmentManager();
            if (childFm.getBackStackEntryCount() > 0) {
                return ((OnBackPressListener)currentFragment).onBackPressed();
            }
        }
        return false;
    }

    void setupTabLayout() {
        submenu = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_submenu, null, false);
//        tabAdapter = new SlidingTabAdapter(getChildFragmentManager(),pageFragments);
//        adapter = new FragmentPagerItemAdapter(
//                getChildFragmentManager(), FragmentPagerItems.with(getContext())
//                .add("Summary", TradeSummaryOverviewFragment.class)
//                .add("Sell", SellFragment.class)
//                .add("Buy", BuyFragment.class)
//                .create());
//        tabPager.set_max_pages(3);
        tabPager.setBackgroundAsset(R.raw.background_white);

        tabPager.setAdapter(adapter);
        tabLayout.setViewPager(tabPager);
    }
    void handleTabChange() {
//        // when tab changed
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                Fragment selectedFrag = pageFragments.get(tab.getPosition());
////                if(selectedFrag instanceof FlipperFragment)
////                    ((FlipperFragment) selectedFrag).resetFlipper();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {}
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                Fragment selectedFrag = pageFragments.get(tab.getPosition());
//                if(selectedFrag instanceof OverviewFragment) {
////                    Toast.makeText(getContext(),"tab reselcted : #"+tab.getPosition(),Toast.LENGTH_SHORT).show();
//                    ((OverviewFragment) selectedFrag).setViewOverview();
//                }
//            }
//        });
    }
}
