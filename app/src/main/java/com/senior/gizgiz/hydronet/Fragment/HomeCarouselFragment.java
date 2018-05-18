package com.senior.gizgiz.hydronet.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.HomeDetailFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

public class HomeCarouselFragment extends Fragment {
    protected TabLayout tabLayout;
    protected ViewPager tabPager;
    private SlidingTabAdapter tabAdapter;
    private View rootView,submenu;

    private Fragment dailyFragment, weeklyFragment, homeDetailFragment,newWeeklyFragment;
    private List<Fragment> pageFragments = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();
    private CustomTextView btnFirst,btnSecond,btnThird,btnFourth;

    private float water,pH,ec;
    private String timeStamp;
    public HomeCarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_fragment_submenu, container, false);
        tabLayout = rootView.findViewById(R.id.sliding_tab);
        tabPager = rootView.findViewById(R.id.fragment_viewpager);
        if(getArguments() != null) {
            water = getArguments().getFloat("WATER");
            pH = getArguments().getFloat("PH");
            ec = getArguments().getFloat("EC");
            timeStamp = getArguments().getString("TIMESTAMP");
        }
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

//    void setupTabLayout() {
//        submenu = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                .inflate(R.layout.custom_submenu, null, false);
//        tabAdapter = new SlidingTabAdapter(getChildFragmentManager(),pageFragments);
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getChildFragmentManager(), FragmentPagerItems.with(getContext())
//                .add("Today", DailyStatFragment.class)
//                .add("Weekly", MonthlyStatFragment.class)
//                .add("Monthly", MonthlyStatFragment.class)
//                .add("Yearly",MonthlyStatFragment.class)
//                .create());
//        tabPager.set_max_pages(3);
//        tabPager.setBackgroundAsset(R.raw.background_white);
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new DailyStatFragment()); fragments.add(new MonthlyStatFragment());
//        fragments.add(new MonthlyStatFragment());
//        SlidingTabAdapter adapter = new SlidingTabAdapter(getChildFragmentManager(),fragments);
//        tabPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(tabPager);
//        ((CustomTextView)submenu.findViewById(R.id.btn_first_label)).setText(R.string.menu_sub_plant);
//        ((CustomTextView)submenu.findViewById(R.id.btn_second_label)).setText(R.string.menu_sub_part);
//        ((CustomTextView)submenu.findViewById(R.id.btn_third_label)).setText(R.string.menu_sub_material);
//        for(int i=0; i<tabAdapter.getCount()-1; i++) {
//            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0, 0, 0, 0);
//            tab.requestLayout();
//        }
//    }

    void setupFragmentList() {
        if(pageFragments.size() < 4) {
            Log.e("HomeCarouselFragment","setupFragmentLIst");
            Bundle bundle = new Bundle();
            bundle.putFloat("WATER",water);
            bundle.putFloat("PH",pH);
            bundle.putFloat("EC",ec);
            bundle.putString("TIMESTAMP",timeStamp);
            dailyFragment = new DailyStatFragment();
            dailyFragment.setArguments(bundle);
            pageFragments.add(dailyFragment);
            newWeeklyFragment = new WeeklyStatFragment();
            pageFragments.add(newWeeklyFragment);
            weeklyFragment = new MonthlyStatFragment();
            pageFragments.add(weeklyFragment);
            homeDetailFragment = new HomeDetailFragment();
            pageFragments.add(homeDetailFragment);
        }
    }
    void setupTabLayout() {
        submenu = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_submenu, null, false);
        //initial tabAdapter layout
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabAdapter = new SlidingTabAdapter(getChildFragmentManager(),pageFragments);
        tabPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(tabPager);
        btnFirst = submenu.findViewById(R.id.btn_first_label);
        btnSecond = submenu.findViewById(R.id.btn_second_label);
        btnThird = submenu.findViewById(R.id.btn_third_label);
        btnFourth = submenu.findViewById(R.id.btn_fourth_label);
        btnFirst.setText(R.string.menu_sub_daily);
        btnSecond.setText(R.string.menu_sub_weekly);
        btnThird.setText(R.string.menu_sub_monthly);
        btnFourth.setText(R.string.menu_sub_harvested);
        tabLayout.getTabAt(0).setCustomView(submenu.findViewById(R.id.btn_first));
        tabLayout.getTabAt(1).setCustomView(submenu.findViewById(R.id.btn_second));
        tabLayout.getTabAt(2).setCustomView(submenu.findViewById(R.id.btn_third));
        tabLayout.getTabAt(3).setCustomView(submenu.findViewById(R.id.btn_fourth));
//        btnFirst.setTextSize(20);
//        btnSecond.setTextSize(14);
//        btnThird.setTextSize(14);
//        btnFourth.setTextSize(14);
        btnFirst.setStyle("Bold");
        btnSecond.setStyle("Regular");
        btnThird.setStyle("Regular");
        btnFourth.setStyle("Regular");
        for(int i=0; i<tabLayout.getChildCount(); i++) {
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
//        layoutParams.weight = 0.5f; // e.g. 0.5f
//        layout.setLayoutParams(layoutParams);
            layoutParams.weight = 0f;
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layout.setLayoutParams(layoutParams);
        }
    }
    void handleTabChange() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = pageFragments.get(tab.getPosition());
                if(selectedFragment instanceof Refreshable) ((Refreshable) selectedFragment).fetchData();
                switch (tabLayout.getSelectedTabPosition()) {
                    case 1 :
//                        btnFirst.setTextSize(14);
//                        btnSecond.setTextSize(20);
//                        btnThird.setTextSize(14);
//                        btnFourth.setTextSize(14);
                        btnFirst.setStyle("Regular");
                        btnSecond.setStyle("Bold");
                        btnThird.setStyle("Regular");
                        btnFourth.setStyle("Regular");
                        break;
                    case 2 :
//                        btnFirst.setTextSize(14);
//                        btnSecond.setTextSize(14);
//                        btnThird.setTextSize(20);
//                        btnFourth.setTextSize(14);
                        btnFirst.setStyle("Regular");
                        btnSecond.setStyle("Regular");
                        btnThird.setStyle("Bold");
                        btnFourth.setStyle("Regular");
                        break;
                    case 3 :
//                        btnFirst.setTextSize(14);
//                        btnSecond.setTextSize(14);
//                        btnThird.setTextSize(14);
//                        btnFourth.setTextSize(20);
                        btnFirst.setStyle("Regular");
                        btnSecond.setStyle("Regular");
                        btnThird.setStyle("Regular");
                        btnFourth.setStyle("Bold");
                        break;
                    default :
//                        btnFirst.setTextSize(20);
//                        btnSecond.setTextSize(14);
//                        btnThird.setTextSize(14);
//                        btnFourth.setTextSize(14);
                        btnFirst.setStyle("Bold");
                        btnSecond.setStyle("Regular");
                        btnThird.setStyle("Regular");
                        btnFourth.setStyle("Regular");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
