package com.senior.gizgiz.hydronet.Activity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.senior.gizgiz.hydronet.HelperClass.CustomTextView;
import com.senior.gizgiz.hydronet.R;

public class MaterialDrawerActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private CustomTextView page_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_drawer);

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/AdventPro-Bold.ttf");

        // Always cast your custom Toolbar here, and set it as the ActionBar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem drawer_home = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_home).withTypeface(font)
                .withIcon(R.drawable.ic_action_home);
        PrimaryDrawerItem drawer_my_plant = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_my_profile).withTypeface(font)
                .withIcon(R.drawable.ic_action_my_plant);
        PrimaryDrawerItem drawer_community = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_community).withTypeface(font)
                .withIcon(R.drawable.ic_action_community).withIsExpanded(true);
        SecondaryDrawerItem drawer_feed = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.menu_feed).withTypeface(font)
                .withIcon(R.drawable.ic_action_feed);
        SecondaryDrawerItem drawer_trading = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.menu_trading).withTypeface(font)
                .withIcon(R.drawable.ic_action_trading);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_bg_green)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_user))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        drawer_home,
                        drawer_my_plant,
                        drawer_community,
                        drawer_feed,
                        drawer_trading,
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();
    }

}
