package com.senior.gizgiz.hydronet.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.senior.gizgiz.hydronet.Adapter.SlidingTabAdapter;
import com.senior.gizgiz.hydronet.Fragment.PlantCarouselFragment;
import com.senior.gizgiz.hydronet.Fragment.FlipperFragment;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.HelperClass.NavigationManager;
import com.senior.gizgiz.hydronet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 015 15/1/2018.
 */

public class MyProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View contentPage, myProfileContent,submenu;

    private Fragment plantFragment,partFragment,materialFragment;
    private TabLayout tabLayout;
    private ViewPager tabPager;
    private SlidingTabAdapter tabAdapter;

    private PlantCarouselFragment carouselFragment;

    private List<Fragment> pageFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar drawer
        setup();

        // define basic recyclable element
        contentPage = findViewById(R.id.page_content);
        ViewStub contentStub = contentPage.findViewById(R.id.layout_stub);
        contentStub.setLayoutResource(R.layout.activity_submenu);
        myProfileContent = contentStub.inflate();

//        // define content element
//        submenu = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                .inflate(R.layout.custom_submenu, null, false);
//
//        //initial fragment
////        plantFragment = MyPlantFragment.init(plantIdList);
//        plantFragment = new PlantOverviewFragment();
//        pageFragments.add(plantFragment);
////        partFragment = PartFragment.init(partIdList);
//        partFragment = new PartOverviewFragment();
//        pageFragments.add(partFragment);
////        materialFragment = MaterialFragment.init(materialIdList);
//        materialFragment = new MaterialOverviewFragment();
//        pageFragments.add(materialFragment);
//
//        setupTabLayout();
//        handleTabChange();

        if (savedInstanceState == null)
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserve the fragment stack inside each tab
            initScreen();

        else
            // restoring the previously created fragment
            // and getting the reference
            carouselFragment = (PlantCarouselFragment) getSupportFragmentManager().getFragments().get(0);

        FabActivity.initAddFAB(getBaseContext(),contentPage.findViewById(R.id.fab_layout));
    }

    void setup() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((CustomTextView)toolbar.findViewById(R.id.page_title)).setText(R.string.menu_my_profile);
        toolbar.findViewById(R.id.action_quick_user).setVisibility(View.GONE);
        ImageButton notiBTN = toolbar.findViewById(R.id.action_quick_notification);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)notiBTN.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        notiBTN.setLayoutParams(params);
        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    void setupTabLayout() {
        //initial tab layout
        tabLayout = findViewById(R.id.sliding_tab);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabPager = findViewById(R.id.fragment_viewpager);
        tabAdapter = new SlidingTabAdapter(getSupportFragmentManager(),pageFragments);
        tabPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(tabPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ((CustomTextView)submenu.findViewById(R.id.btn_first_label)).setText(R.string.menu_sub_plant);
        ((CustomTextView)submenu.findViewById(R.id.btn_second_label)).setText(R.string.menu_sub_part);
        ((CustomTextView)submenu.findViewById(R.id.btn_third_label)).setText(R.string.menu_sub_material);
        tabLayout.getTabAt(0).setCustomView(submenu.findViewById(R.id.btn_first));
        tabLayout.getTabAt(1).setCustomView(submenu.findViewById(R.id.btn_second));
        tabLayout.getTabAt(2).setCustomView(submenu.findViewById(R.id.btn_third));
        // add tab margin
        for(int i=0; i<tabLayout.getTabCount()-1; i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0,0,50,0);
//            p.setMargins(0, 0, ResourceManager.getDim(getApplicationContext(),R.dimen.tab_margin), 0);
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
                if(selectedFrag instanceof FlipperFragment)
                    ((FlipperFragment) selectedFrag).setViewFirstPage();
            }
        });
    }
    private void initScreen() {
        // Creating the ViewPager container fragment once
        carouselFragment = new PlantCarouselFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, carouselFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return NavigationManager.navigateTo(this,id);
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else if (!carouselFragment.onBackPressed()) {
            super.onBackPressed();
        } else {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
