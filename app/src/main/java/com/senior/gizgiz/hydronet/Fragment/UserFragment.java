package com.senior.gizgiz.hydronet.Fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.senior.gizgiz.hydronet.Activity.LoginActivity;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.Activity.SettingActivity;
import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Entity.GrowHistory;
import com.senior.gizgiz.hydronet.Fragment.DetailFragment.PlantDetailFragment;
import com.senior.gizgiz.hydronet.Fragment.OverviewFragment.OverviewFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.RealTimeDBManager;
import com.senior.gizgiz.hydronet.HelperClass.Refreshable;
import com.senior.gizgiz.hydronet.Listener.OnBackPressListener;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 004 04/03/2018.
 */

public class UserFragment extends Fragment implements OnBackPressListener {
    protected TabLayout tabLayout;
    protected ViewPager tabPager;
    private SlidingTabAdapter tabAdapter;
    private View submenu;
    private CustomTextView growingStat,harvestedStat,failedStat,soldStat,boughtStat,storyStat,likedStat;

    String username,email;
    private Fragment plantFragment, soldStoryFragment,boughtStoryFragment, storyFragment,likedStoryFragment;
    private List<Fragment> pageFragments = new ArrayList<>();

    public UserFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        username = getArguments().getString("USERNAME");
        email = getArguments().getString("EMAIL");
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ((CustomTextView)view.findViewById(R.id.username)).setText(MainActivity.username);
        ((CustomTextView)view.findViewById(R.id.email)).setText(email);
        tabPager = view.findViewById(R.id.user_viewpager);
        tabLayout = view.findViewById(R.id.user_sliding_tab);
        growingStat = view.findViewById(R.id.stat_growing);
        harvestedStat = view.findViewById(R.id.stat_harvested);
        failedStat = view.findViewById(R.id.stat_failed);
        soldStat = view.findViewById(R.id.stat_sold);
        boughtStat = view.findViewById(R.id.stat_bought);
        storyStat = view.findViewById(R.id.stat_story);
        likedStat = view.findViewById(R.id.stat_fav_story);
        handleSettingBTN(view);
        setupFragments();
        setupTabLayout();
        handleTabChange();
        setupStat();
    }

    private void setupFragments() {
        if(pageFragments.size() < 3) {
            plantFragment = new PlantDetailFragment();
            pageFragments.add(plantFragment);
//            tradeFragment = new TradeSummaryOverviewFragment();
//            pageFragments.add(tradeFragment);
            storyFragment = new MyStoryFragment();
            pageFragments.add(storyFragment);
            likedStoryFragment = new LikedStoryFragment();
            pageFragments.add(likedStoryFragment);
            soldStoryFragment = new SoldStoryFragment();
            pageFragments.add(soldStoryFragment);
            boughtStoryFragment = new BoughtStoryFragment();
            pageFragments.add(boughtStoryFragment);
        }
    }
    private void setupStat() {
        String uid = MainActivity.currentUser.getUid();
        RealTimeDBManager.getDatabase().child("userPlants/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String upid = childSnapshot.getKey();
                        RealTimeDBManager.getDatabase().child("growHistories/"+upid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long growing=0,harvested=0,failed=0;
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    GrowHistory growHistory = childSnapshot.getValue(GrowHistory.class);
                                    if(!growHistory.isHarvested()) growing+=growHistory.getCount();
                                    else if(growHistory.getFailedList()!=null) failed+=growHistory.getFailedList().size();
                                    else harvested+=growHistory.getHarvestList().size();
                                }
                                growingStat.setText(String.valueOf(growing));
                                harvestedStat.setText(String.valueOf(harvested));
                                failedStat.setText(String.valueOf(failed));
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                } else {
                    growingStat.setText("0");
                    harvestedStat.setText("0");
                    failedStat.setText("0");
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
        RealTimeDBManager.getDatabase().child("postStories/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyStat.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
        RealTimeDBManager.getDatabase().child("likedStories/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likedStat.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
        RealTimeDBManager.getDatabase().child("soldStories/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                soldStat.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
        RealTimeDBManager.getDatabase().child("boughtStories/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boughtStat.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }
            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void setupTabLayout() {
        submenu = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_submenu, null, false);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabAdapter = new SlidingTabAdapter(getChildFragmentManager(),pageFragments);
        tabPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(tabPager);
        ((CustomTextView)submenu.findViewById(R.id.btn_first_label)).setText(R.string.menu_sub_plant);
        ((CustomTextView)submenu.findViewById(R.id.btn_second_label)).setText(R.string.menu_sub_story);
        ((CustomTextView)submenu.findViewById(R.id.btn_third_label)).setText(R.string.menu_sub_liked_story);
        ((CustomTextView)submenu.findViewById(R.id.btn_fourth_label)).setText(R.string.menu_sub_sold);
        ((CustomTextView)submenu.findViewById(R.id.btn_fifth_label)).setText(R.string.menu_sub_bought);
        tabLayout.getTabAt(0).setCustomView(submenu.findViewById(R.id.btn_first));
        tabLayout.getTabAt(1).setCustomView(submenu.findViewById(R.id.btn_second));
        tabLayout.getTabAt(2).setCustomView(submenu.findViewById(R.id.btn_third));
        tabLayout.getTabAt(3).setCustomView(submenu.findViewById(R.id.btn_fourth));
        tabLayout.getTabAt(4).setCustomView(submenu.findViewById(R.id.btn_fifth));
        // add tabAdapter margin
//        for(int i=0; i<tabLayout.getTabCount()-1; i++) {
//            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0, 0, ResourceManager.getDim(getContext(),R.dimen.tab_margin), 0);
//            tab.requestLayout();
//        }
    }
    private void handleTabChange() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFrag = pageFragments.get(tab.getPosition());
                if(selectedFrag instanceof Refreshable) ((Refreshable) selectedFrag).fetchData();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {
                Fragment selectedFrag = pageFragments.get(tab.getPosition());
                if(selectedFrag instanceof Refreshable) ((Refreshable) selectedFrag).removeFirebaseListener();
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Fragment selectedFrag = pageFragments.get(tab.getPosition());
                if(selectedFrag instanceof OverviewFragment) ((OverviewFragment) selectedFrag).setViewOverview();
            }
        });
    }
    private void handleSettingBTN(View view) {
        view.findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
//        view.findViewById(R.id.btn_license).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(),AboutActivity.class));
//            }
//        });
    }
    private void handleTestBTN(View view) {
//        view.findViewById(R.id.btn_control_farm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), SensorManagerActivity.class));
//            }
//        });
//        view.findViewById(R.id.btn_edit_db).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), EditDBActivity.class));
//            }
//        });
    }

    @Override
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
}
