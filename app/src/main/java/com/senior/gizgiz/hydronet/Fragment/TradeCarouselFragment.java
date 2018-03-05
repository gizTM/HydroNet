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

import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.MaterialOverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.OverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.PartOverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.PlantOverviewFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.TradeSummaryOverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 001 01/03/2018.
 */

public class TradeCarouselFragment extends Fragment implements OnBackPressListener {
    protected TabLayout tabLayout;
    protected ViewPager tabPager;
    private SlidingTabAdapter tabAdapter;
    private View rootView,submenu;

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
        setupFragmentList();
        setupTabLayout();
        handleTabChange();
    }

    public boolean onBackPressed() {
        Fragment currentFragment = tabAdapter.getItem(tabPager.getCurrentItem());
        if(currentFragment != null && currentFragment.isVisible()) {
            FragmentManager childFm = currentFragment.getChildFragmentManager();
            if (childFm.getBackStackEntryCount() > 0) {
                return ((OnBackPressListener)currentFragment).onBackPressed();
            }
        }
        return false;
    }

    void setupFragmentList() {
        if(pageFragments.size() < 3) {
            summaryFragment = new TradeSummaryOverviewFragment();
            pageFragments.add(summaryFragment);
            buyFragment = new BuyFragment();
            pageFragments.add(buyFragment);
            sellFragment = new SellFragment();
            pageFragments.add(sellFragment);
        }
    }
    void setupTabLayout() {
        submenu = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_submenu, null, false);
        //initial tabAdapter layout
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        // Note that we are passing childFragmentManager, not FragmentManager
        tabAdapter = new SlidingTabAdapter(getChildFragmentManager(),pageFragments);
        tabPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(tabPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ((CustomTextView)submenu.findViewById(R.id.btn_first_label)).setText(R.string.menu_sub_summary);
        ((CustomTextView)submenu.findViewById(R.id.btn_second_label)).setText(R.string.menu_sub_buy);
        ((CustomTextView)submenu.findViewById(R.id.btn_third_label)).setText(R.string.menu_sub_sell);
        tabLayout.getTabAt(0).setCustomView(submenu.findViewById(R.id.btn_first));
        tabLayout.getTabAt(1).setCustomView(submenu.findViewById(R.id.btn_second));
        tabLayout.getTabAt(2).setCustomView(submenu.findViewById(R.id.btn_third));
        // add tabAdapter margin
        for(int i=0; i<tabLayout.getTabCount()-1; i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0,0,50,0);
            p.setMargins(0, 0, ResourceManager.getDim(getContext(),R.dimen.tab_margin), 0);
            tab.requestLayout();
        }
    }
    void handleTabChange() {
        // when tab changed
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Fragment selectedFrag = pageFragments.get(tab.getPosition());
//                if(selectedFrag instanceof FlipperFragment)
//                    ((FlipperFragment) selectedFrag).resetFlipper();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Fragment selectedFrag = pageFragments.get(tab.getPosition());
                if(selectedFrag instanceof OverviewFragment) {
//                    Toast.makeText(getContext(),"tab reselcted : #"+tab.getPosition(),Toast.LENGTH_SHORT).show();
                    ((OverviewFragment) selectedFrag).setViewOverview();
                }
            }
        });
    }
}
